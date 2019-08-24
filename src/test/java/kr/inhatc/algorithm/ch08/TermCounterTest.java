package kr.inhatc.algorithm.ch08;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import kr.inhatc.algorithm.ch07.WikiFetcher;

public class TermCounterTest {

	private TermCounter counter;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		
		WikiFetcher wf = new WikiFetcher();
		Elements paragraphs =  wf.fetchWikipedia(url);  //wf.readWikipedia(url);
		
		counter = new TermCounter(url.toString());
		counter.processElements(paragraphs);
	}

	@Test
	public void testSize() {
		assertThat(counter.size(), is(4440)); // is(4798)
	}

}
