package kr.inhatc.algorithm.ch14;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.select.Elements;

import kr.inhatc.algorithm.ch07.WikiFetcher;
import kr.inhatc.algorithm.ch08.TermCounter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class JedisIndex {

	private Jedis jedis;
		
	/**
	 * 생성자	
	 * @param jedis
	 */
	public JedisIndex(Jedis jedis) {
		super();
		this.jedis = jedis;
	}

	/**
	 * 주어진 검색어에 대한 Redis 키를 반환합니다.
	 * URLSet의 키는URLSet:으로 시작. 
	 * the 단어를 포함한 URL을 얻으려면 URLSet:the라는 키로 set에 접근  
	 * @param term
	 * @return
	 */
	private String urlSetKey(String term) {		
		return "URLSet:" + term;
	}
	
	/**
	 * 검색어에 연관된 set에 URL을 추가
	 * @param term
	 * @param tc
	 */
	public void add(String term, TermCounter tc) {
		String urlKey = urlSetKey(term);
		jedis.sadd(urlKey, tc.getLabel());
	}
	

	/**
	 * 검색어를 조회하여 URL 집합을 반환
	 * @param term
	 * @return
	 */
	public Set<String> getURLs(String term){
		Set<String> set = jedis.smembers(urlSetKey(term));
		return set;
	}
	
	/**
	 * 주어진 URL에 있는 검색어가 등장하는 횟수를 반환 
	 * @param url
	 * @param term
	 * @return
	 */
	public Integer getCount(String url, String term) {
		String redisKey = termCounterKey(url);
		String count = jedis.hget(redisKey, term);
		return Integer.valueOf(count);
	}
	
	/**
	 * URL의 TermCounter에 대한 Redis 키를 반환합니다
	 * TermCounter 객체의 키는 TermCounter로 시작하고 조회하려는 URL로 끝난다.  
	 * @param url
	 * @return
	 */
	private String termCounterKey(String url) {
		return "TermCounter:" + url;
	}

	/**
	 * TermCounter의 내용을 Redis로 푸시.
	 * @param tc
	 */
	private List<Object> pushTermCounterToRedis(TermCounter tc) {
		Transaction t = jedis.multi();
		
		String url = tc.getLabel();
		String hashName = termCounterKey(url);
		
		// 이 페이지가 이미 색인화 된 경우; 오래된 해시를 삭제
		t.del(hashName);
		
		// 각 용어에 대해, termCounter에 항목을 추가하고 
		// 색인의 새 구성원을 추가하십시오.
		for(String term : tc.KeySet()) {
			Integer count = tc.get(term);
			t.hset(hashName, term, count.toString());
			t.sadd(urlSetKey(term), url);
		}
		
		// 트랜잭션 수행 
		List<Object> res = t.exec();
		return res;
	}
	
	/**
	 * 데이터베이스에서 모든 URLSet 객체를 삭제하는 메소드
	 */
	private void deleteTermCounters() {
		Set<String> keys = termCounterKeys(); // 색인 된 URL에 대한 TermCounter 키를 리턴
		Transaction t = jedis.multi();
		for(String key : keys) {
			t.del(key);
		}
		t.exec(); 		// 트랜잭션 처리 
	}
	
	/**
	 * 색인 된 URL에 대한 TermCounter 키를 리턴합니다.
	 * @return
	 */
	private Set<String> termCounterKeys() {		
		return jedis.keys("TermCounter:*");
	}

	/**
	 * 데이터베이스에서 모든 URLSet 객체를 삭제하는 메소드
	 */
	private void deleteURLSets() {
		Set<String> keys = urlSetKeys();
		Transaction t = jedis.multi();
		for (String key : keys) {
			t.del(key);
		}
		t.exec();		// 트랜잭션 처리 
	}

	/**
	 * 인덱싱 된 용어에 대한 URLSet 키를 반환합니다.
	 * @return
	 */
	private Set<String> urlSetKeys() {		
		return jedis.keys("URLSet:*");
	}

	/**
	 * DB로부터 모든 키를 지우는 메소드 
	 */
	private void deleteAllKeys() {
		Set<String> keys = jedis.keys("*");
		Transaction t = jedis.multi();
		for (String key : keys) {
			t.del(key);
		}
		t.exec();
	}
		
	/**
	 * term을 찾고 URL에서 count할 맵을 반환합니다.
	 * @param string
	 * @return
	 */
	public Map<String, Integer> getCounts(String term) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Set<String> urls = getURLs(term);
		for (String url: urls) {
			Integer count = getCount(url, term);
			map.put(url, count);
		}
		return map;
	}
	
	/**
	 * term을 찾고 URL에서 count할 맵을 반환합니다.
	 * @param string
	 * @return
	 */
	private Map<String, Integer> getCountsFaster(String term) {
		// 문자열 집합을 목록으로 변환하여 매번 동일한 순회 순서를 얻습니다.
		List<String> urls = new ArrayList<>();
		urls.addAll(getURLs(term));
		
		// 모든 조회를 수행하는 트랜잭션 구성
		Transaction t = jedis.multi();
		for (String url : urls) {
			String redisKey = termCounterKey(url);
			t.hget(redisKey, term);
		}
		
		List<Object> res = t.exec();
		
		// 결과를 반복하고 맵을 만듭니다.
		Map<String, Integer> map = new HashMap<>();
		int i = 0; 
		for (String url : urls) {
			System.out.println(url);
			Integer count = Integer.valueOf((String) res.get(i++));
			map.put(url, count);
		}
		return map;
	}
	
	/**
	 * 색인에 페이지 추가.
	 * @param url
	 * @param paragraphs
	 */
	public void indexPage(String url, Elements paragraphs) {
		System.out.println("Indexing : " + url);
		
		// TermCounter를 만들고 paragraphs의 terms를 세기
		TermCounter tc = new TermCounter(url);
		tc.processElements(paragraphs);
		
		// TermCounter의 내용을 Redis로 푸시
		pushTermCounterToRedis(tc);
	}
	
	/**
	 * 테스트 목적으로 색인에 두 페이지를 저장합니다.
	 * @param index
	 * @throws IOException 
	 */
	private static void loadIndex(JedisIndex index) throws IOException {
		WikiFetcher wf = new WikiFetcher();
		
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
//		String url = "https://ko.wikipedia.org/wiki/%EC%9E%90%EB%B0%94";
		Elements paragraphs = wf.fetchWikipedia(url);
		index.indexPage(url, paragraphs);
		
		url = "https://en.wikipedia.org/wiki/Programming_language";
//		url = "https://ko.wikipedia.org/wiki/%EC%BB%B4%ED%93%A8%ED%84%B0_%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D";
		paragraphs = wf.fetchWikipedia(url);	// readWikipedia(url);
		index.indexPage(url, paragraphs);
	}
	
	public static void main(String[] args) throws IOException {
		Jedis jedis = JedisMaker.make();
		JedisIndex index = new JedisIndex(jedis);
		
		index.deleteTermCounters();
		index.deleteURLSets();
		index.deleteAllKeys();

		loadIndex(index);
		
		Map<String, Integer> map = index.getCounts("java");
//		Map<String, Integer> map = index.getCountsFaster("java");
		for (Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(entry);
		}		
	}

	
}
