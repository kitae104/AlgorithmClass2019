package kr.inhatc.algorithm.ch14;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Map;

import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import kr.inhatc.algorithm.ch07.WikiFetcher;
import redis.clients.jedis.Jedis;

public class JedisIndexTest {

	private static String url1, url2;
	private Jedis jedis;
	private JedisIndex index;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		jedis = JedisMaker.make();
		index = new JedisIndex(jedis);

		loadIndex(index);
	}

	/**
	 * Loads the index with two pages read from files.
	 *
	 * @return
	 * @throws IOException
	 */
	private static void loadIndex(JedisIndex index) throws IOException {
		WikiFetcher wf = new WikiFetcher();

		url1 = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		Elements paragraphs = wf.fetchWikipedia(url1);
		index.indexPage(url1, paragraphs);

		url2 = "https://en.wikipedia.org/wiki/Programming_language";
		paragraphs = wf.fetchWikipedia(url2);
		index.indexPage(url2, paragraphs);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		jedis.close();
	}

	/**
	 * Test method for {@link JedisIndex#getCounts(java.lang.String)}.
	 */
	@Test
	public void testGetCounts() {
		Map<String, Integer> map = index.getCounts("java");
		assertThat(map.get(url1), is(163));
		assertThat(map.get(url2), is(9));
//		assertThat(map.get(url1), is(339));
//		assertThat(map.get(url2), is(264));
	}
}