package kr.inhatc.algorithm.ch08;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import kr.inhatc.algorithm.ch06.WikiNodeIterable;
import kr.inhatc.algorithm.ch07.WikiFetcher;

/**
 * 검색어와 검색어가 페이지에서 등장하는 횟수를 매핑하는 클래스  
 * @author kitae
 *
 */
public class TermCounter {
	
	private Map<String, Integer> map;	// 검색어와 등장횟수 매핑 
	private String label;				// 문서식별 - URL 저장 
	//public static int cnt = 0;			// 전체 빈도수를 처리하는 변수 
	
	/**
	 * 생성자
	 * @param label
	 */
	public TermCounter(String label) {
		this.label = label;
		this.map = new HashMap<String, Integer>();
	}
	
	/**
	 * 검색어와 횟수를 기록 
	 * @param term
	 * @param count
	 */
	public void put(String term, int count) {
		map.put(term, count);
	}

	/**
	 * 검색어의 등작 횟수 반환 
	 * @param term
	 * @return
	 */
	public Integer get(String term) {
		Integer count = map.get(term);
		return count == null ? 0 : count;
	}
	
	/**
	 * 검색어를 인자로 받아와서 검색어와 검색어 관련 횟수를 1증가 시킴 
	 * @param term
	 */
	public void incrementTermCount(String term) {
		put(term, get(term) + 1);
		//cnt++;
	}
	
	/**
	 * 구문들을 노드로 분리해서 처리하는 메소드 
	 * 이 메소드는 jsoup 라이브러리의 Element 객체 컬렉션인 Elements 객체를 인자로 받음
	 * 컬렉션에 반복문을 수행하고 Node 객체에 대한 processTree 메소드 호출
	 *  
	 * @param paragraphs
	 */
	public void processElements(Elements paragraphs) {
		for(Node node : paragraphs) {
			processTreeNode(node);
		}
	}

	/**
	 * 노드로 분리된 구문에서 TextNode를 추출해 
	 * 해당 텍스트를 처리하는 메소드를 호출하는 메소드
	 * DOM 트리의 루트를 나타내는 jsoup 라이브러리의 Node 객체를 인자로 받음. 
	 * 이 메소드는 트리에 반복문으 실행하여 텍스트를 포함한 노드를 찾은 다음
	 * 텍스트를 추출하여 processText 메소드로 전달함
	 * 
	 * @param node
	 */
	private void processTreeNode(Node root) {
		for(Node node : new WikiNodeIterable(root)) {
			if(node instanceof TextNode) {
				processText(((TextNode)node).text());
			}
		}		
	}

	/**
	 * 문자열을 정제해서 출현 횟수를 증가시키는 메소드
	 * 단어와 공백, 구두점 등을 포함한 String 객체를 인자로 받음
	 * 구두점은 공백으로 대체하고 나머지 글자는 소문자로 변환한 다음 텍스트를 단어로 
	 * 나눈다. 그리고 각 단어에 반복문을 실행하여 단어별로 incrementTermCount 메소드를 호출 
	 * replaceAll과 split 메소드는 정규표현식을 인자로 받는다. 
	 * @param text
	 */
	private void processText(String text) {
		// 문장 부호를 공백으로 바꾸고 소문자로 변환 한 다음 공백으로 나누십시오.
		String[] array = text.replaceAll("\\pP", " ").toLowerCase().split("\\s+");
		
		for (int i = 0; i < array.length; i++) {
			String term = array[i];
			incrementTermCount(term);		// 단어 횟수 증가시키기 
		}
	}
	
	private void printCounts() {
		for(String key : KeySet()) {
			Integer count = get(key);
			System.out.println(key + " : " + count);
		}
		
		System.out.println("Total of all counts = " + size());
	}

	private Set<String> KeySet() {
		return map.keySet();
	}
	
	public int  size() {
		int total = 0;
		for (Integer value: map.values()) {
			total += value;
		}
		return total;
	}
	
	public static void main(String[] args) throws IOException {
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		
		WikiFetcher wf = new WikiFetcher();
		
		Elements paragraphs = wf.fetchWikipedia(url);
		
		TermCounter counter = new TermCounter(url);
		counter.processElements(paragraphs);
		counter.printCounts();

	}
}
