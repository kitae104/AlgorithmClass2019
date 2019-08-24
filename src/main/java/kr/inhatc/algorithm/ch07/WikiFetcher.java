package kr.inhatc.algorithm.ch07;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 웹 페이지를 다운로드하고 파싱하는데 사용되는 클래스 
 * @author kitae
 *
 */
public class WikiFetcher {
	private long lastRequestTime = -1;
	private long minInterval = 1000;

	public Elements fetchWikipedia(String url) throws IOException {
		sleepIfNeeds();

		Connection conn = Jsoup.connect(url); 
		Document doc = conn.get();
		Element content = doc.getElementById("mw-content-text");
		Elements paras = content.select("p");
		return paras;
	}

	private void sleepIfNeeds() {
		if (lastRequestTime != -1) {
			long currentTime = System.currentTimeMillis();
			long nextRequestTime = lastRequestTime + minInterval;

			if (currentTime < nextRequestTime) {
				try {
					// System.out.println("Sleeping until " + nextRequestTime);
					Thread.sleep(nextRequestTime - currentTime);
				} catch (InterruptedException e) {
					System.err.println("Warning: sleep interrupted in fetchWikipedia.");
				}
			}
		}
		lastRequestTime = System.currentTimeMillis();
	}

	public Elements readWikipedia(String url) throws IOException {
		URL realURL = new URL(url);

		// assemble the file name
		String slash = File.separator;
		String filename = "resources" + slash + realURL.getHost() + realURL.getPath();

		// read the file
		InputStream stream = WikiFetcher.class.getClassLoader().getResourceAsStream(filename);
		Document doc = Jsoup.parse(stream, "UTF-8", filename);

		// parse the contents of the file
		Element content = doc.getElementById("mw-content-text");
		Elements paras = content.select("p");
		return paras;		
	}

	public static void main(String[] args) throws IOException {
		
		WikiFetcher wf = new WikiFetcher();
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
//		Elements paragraphs = wf.readWikipedia(url);
		Elements paragraphs = wf.fetchWikipedia(url);

		for (Element paragraph : paragraphs) {
			System.out.println(paragraph);
		}
	}

}
