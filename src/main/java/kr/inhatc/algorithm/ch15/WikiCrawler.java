package kr.inhatc.algorithm.ch15;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kr.inhatc.algorithm.ch07.WikiFetcher;
import kr.inhatc.algorithm.ch14.JedisIndex;
import kr.inhatc.algorithm.ch14.JedisMaker;
import redis.clients.jedis.Jedis;

public class WikiCrawler {
	
	private String source;		// 크롤링을 시작할 URL	
	private JedisIndex index;	// 결과를 저장하는 JedisIndex 객체 
	private Queue<String> queue = new LinkedList<String>(); // 발견하였으나 아직 인덱싱하지 않은 URL들을 추적하는 LinkedList 객체
	final static WikiFetcher wf = new WikiFetcher();	// 웹 페이지를 읽고 파싱하는데 사용할 WikiFetcher 객체 
	
	/**
	 * 생성자
	 * @param source
	 * @param index
	 */
	public WikiCrawler(String source, JedisIndex index) {
		this.source = source;
		this.index = index;
		queue.offer(source);	// 큐에 처음위치 입력 
	}
	
	// 대기열의 URL 수를 반환합니다.
	public int queueSize() {
		return queue.size();
	}
	
	/**
	 * 단락(paragraphs)을 구문 분석하고 큐에 내부 링크를 추가합니다.
	 * @param paragraphs
	 */
	public void queueInternalLinks(Elements paragraphs) {
		for (Element paragraph : paragraphs) {
			queueInternalLinks(paragraph);
		}
		
	}
	
	/**
	 * 단락(paragraph)을 구문 분석하고 큐에 내부 링크를 추가합니다.
	 * @param paragraph
	 */
	private void queueInternalLinks(Element paragraph) {
		Elements elements = paragraph.select("a[href]");
		for (Element element : elements) {
			String relURL = element.attr("href");
			
			if(relURL.startsWith("/wiki/")) {
				String absURL = "https://en.wikipedia.org" + relURL;
				System.out.println(absURL);
				queue.offer(absURL);
			}
		}
	}

	/**
	 * 대기열에서 URL을 가져 와서 색인을 생성합니다.	 
	 * 
	 * @param b
	 * @return
	 * @throws IOException 
	 */
	public String crawl(boolean testing) throws IOException {
		if(queue.isEmpty()) {
			return null;
		}
		
		String url = queue.poll();
		System.out.println("Crawling " + url);
		
		if(testing == false && index.isIndexed(url)) {
			System.out.println("Already indexed.");
			return null;
		}
		
		Elements paragraphs;
		if(testing) {	// 테스트 모드 : 파일 읽기 
			System.out.println("<<test>>");
			paragraphs = wf.readWikipedia(url);
		} else {		// 실제 모드 : 웹에서 읽기 
			System.out.println("<<real>>");
			paragraphs = wf.fetchWikipedia(url);
		}
		
		index.indexPage(url, paragraphs);
		queueInternalLinks(paragraphs);
		
		return url;
	}
	
	public static void main(String[] args) throws IOException {
		// 위키 크롤러 만들기
		Jedis jedis = JedisMaker.make();
		JedisIndex index = new JedisIndex(jedis);
		String source = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		WikiCrawler wc = new WikiCrawler(source, index);

		// 테스트 목적으로 큐를 로드
		Elements paragraphs = wf.fetchWikipedia(source);
		wc.queueInternalLinks(paragraphs);

		// 새 페이지를 색인 할 때까지 반복
		String res;
		do {
			res = wc.crawl(false);		
		} while(res == null);
		
		Map<String, Integer> map = index.getCounts("java");
		for (Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(entry);
		}
	}
}
