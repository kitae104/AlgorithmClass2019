package kr.inhatc.algorithm.ch08;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.select.Elements;

import kr.inhatc.algorithm.ch07.WikiFetcher;

public class Index {
	
	// 검색어별로 TermCounter의 Set 객체와 매핑,
	// 각 TermCounter 객체는 검색어가 등장하는 웹페이지를 의미
	private Map<String, Set<TermCounter>> index = new HashMap<String, Set<TermCounter>>();
	
	/**
	 * term과 관련된 집합에 TermCounter를 추가합니다.
	 * @param term
	 * @param tc
	 */
	public void add(String term, TermCounter tc) {
		Set<TermCounter> set = get(term);
		
		// 처음으로 용어가 보이면 새로운 세트를 만듭니다.
		if(set == null) {
			set = new HashSet<>();
			index.put(term, set);
		}
		// 그렇지 않으면 기존 세트를 수정할 수 있습니다
		set.add(tc);
	}

	/**
	 * 검색어를 찾고 TermCounters 집합을 리턴합니다.
	 * @param term
	 * @return
	 */
	public Set<TermCounter> get(String term) {
		return index.get(term);
	}
	
	public void printIndex() {
		// 검색어에 반복문 실행 
		for(String term: keySet()) {
			System.out.println(term); 	// 단어 출력 
			
			// 단어별 등장하는 페이지와 등장 횟수를 표시 
			Set<TermCounter> tcs = get(term);
			for (TermCounter tc : tcs) {
				Integer count = tc.get(term);
				System.out.println(" " + tc.getLabel() + " " + count);
			}
		}
	}

	/**
	 * 인덱스의 키들의 집합을 반환 
	 * @return
	 */
	private Set<String> keySet() {
		return index.keySet();
	}
	
	/**
	 * 인덱스에 페이지를 추가하십시오.
	 * @param url
	 * @param paragraphs
	 */
	public void indexPage(String url, Elements paragraphs) {
		
		// TermCounter를 만들고 단락의 단어를 계산
		TermCounter tc = new TermCounter(url);
		tc.processElements(paragraphs);
		
        // TermCounter의 각 검색어에 대해 TermCounter 객체를 인덱스에 추가합니다.
		for (String term : tc.KeySet()) {
			add(term, tc);
		}
	}
	
	public static void main(String[] args) throws IOException {
		WikiFetcher wf = new WikiFetcher();
		Index indexer = new Index();
		
//		String url = "https://ko.wikipedia.org/wiki/%EC%9E%90%EB%B0%94_(%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D_%EC%96%B8%EC%96%B4)";
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		Elements paragraphs = wf.fetchWikipedia(url);
		indexer.indexPage(url, paragraphs);
		
		url = "https://en.wikipedia.org/wiki/Programming_language";
//		url = "https://ko.wikipedia.org/wiki/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D_%EC%96%B8%EC%96%B4";
		paragraphs = wf.fetchWikipedia(url);
        indexer.indexPage(url, paragraphs);
        
        indexer.printIndex();

	}
}
