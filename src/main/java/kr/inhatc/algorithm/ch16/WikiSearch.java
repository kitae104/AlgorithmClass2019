package kr.inhatc.algorithm.ch16;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kr.inhatc.algorithm.ch14.JedisIndex;
import kr.inhatc.algorithm.ch14.JedisMaker;
import redis.clients.jedis.Jedis;

public class WikiSearch {

	// 검색어를 포함하는 URL별 관련성 점수의 맵
	private Map<String, Integer> map;

	public WikiSearch(Map<String, Integer> map) {
		this.map = map;
	}

	/**
	 * 주어진 URL의 관련성을 찾습니다.
	 * 
	 * @param url
	 * @return
	 */
	public Integer getRelevance(String url) {
		Integer relevance = map.get(url);
		return relevance == null ? 0 : relevance;
	}

	private static WikiSearch search(String term, JedisIndex index) {
		Map<String, Integer> map = index.getCounts(term);
		return new WikiSearch(map);
	}

	/**
	 * 용어 빈도 순서로 내용을 인쇄합니다.
	 */
	private void print() {
		List<Entry<String, Integer>> entries = sort();
		for (Entry<String, Integer> entry : entries) {
			System.out.println(entry);
		}

	}

	/**
	 * 관련성을 기준으로 결과를 정렬하십시오.
	 * 
	 * @return
	 */
	public List<Entry<String, Integer>> sort() {
		// 항목 목록을 만듭니다.
		List<Entry<String, Integer>> entries = new LinkedList<>(map.entrySet());
		// 정렬을위한 Comparator 객체 만들기
		Comparator<Entry<String, Integer>> comparator = new Comparator<Map.Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
				return e1.getValue().compareTo(e2.getValue());
			}
		};

		// 항목을 정렬하고 반환
		Collections.sort(entries, comparator);

		return entries;
	}

	/**
	 * 두 검색 결과의 교집합을 계산합니다.
	 * 
	 * @param search
	 * @return
	 */
	public WikiSearch and(WikiSearch search) {
		Map<String, Integer> intersection = new HashMap<String, Integer>();
		for (String term : map.keySet()) {
			if (search.map.containsKey(term)) {
				int relevance = totalRelevance(this.map.get(term), search.map.get(term));
				intersection.put(term, relevance);
			}
		}
		return new WikiSearch(intersection);
	}

	/**
	 * 여러 용어로된 검색의 관련성을 계산합니다
	 * 
	 * @param integer
	 * @param integer2
	 * @return
	 */
	private int totalRelevance(Integer rel1, Integer rel2) {
		return rel1 + rel2;
	}

	/**
	 * 두 검색 결과의 합집합을 계산합니다.
	 * 
	 * @param search
	 * @return
	 */
	public WikiSearch or(WikiSearch that) {
		Map<String, Integer> union = new HashMap<String, Integer>(map);
		for (String term: that.map.keySet()) {
			int relevance = totalRelevance(this.getRelevance(term), that.getRelevance(term));
			union.put(term, relevance);
		}
		return new WikiSearch(union);
	}

	/**
	 * 두 검색 결과의 차집합을 계산합니다.
	 * 
	 * @param search
	 * @return
	 */
	public WikiSearch minus(WikiSearch search) {
		Map<String, Integer> difference = new HashMap<String, Integer>(map);
		for (String term : search.map.keySet()) {
			difference.remove(term);
		}
		return new WikiSearch(difference);
	}

	public static void main(String[] args) throws IOException {

		// JedisIndex 만들기
		Jedis jedis = JedisMaker.make();
		JedisIndex index = new JedisIndex(jedis);

		// 첫번째 용어 검색
		String term1 = "java";
		System.out.println("Query : " + term1);
		WikiSearch search1 = search(term1, index);
		search1.print();

		// 두번째 용어 검색
		String term2 = "programming";
		System.out.println("Query : " + term2);
		WikiSearch search2 = search(term2, index);
		search2.print();

		// 공통검색 (교집합)
		System.out.println("Query : " + term1 + " AND " + term2);
		WikiSearch intersection = search1.and(search2);
		intersection.print();

		// 공통검색 (합집합)
		System.out.println("Query : " + term1 + " OR " + term2);
		WikiSearch union = search1.or(search2);
		union.print();

		// 공통검색 (차집합)
		System.out.println("Query : " + term1 + " MINUS " + term2);
		WikiSearch difference = search1.minus(search2);
		difference.print();
	}

}
