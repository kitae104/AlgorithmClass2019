package kr.inhatc.algorithm.ch14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

import redis.clients.jedis.Jedis;

/**
 * 
 * @author kitae
 *
 */
public class JedisMaker {

	/**
	 * Jedis 객체를 생성하는 메소드 
	 * Jedis 객체가 인증되면 데이터베이스와 통신 
	 * @return
	 */
public static Jedis make() throws IOException {
		
		// assemble the directory name
		String slash = File.separator;
		String filename = "redis_url.txt";
		URL fileURL = JedisMaker.class.getClassLoader().getResource(filename);
		String filepath = URLDecoder.decode(fileURL.getFile(), "UTF-8");
		
		// open the file
		StringBuilder sb = new StringBuilder();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e1) {
			System.out.println("File not found: " + filename);
			printInstructions();
			return null;
		}

		// read the file
		while (true) {
			String line = br.readLine();
			if (line == null) break;
			sb.append(line);
		}
		br.close();

		// parse the URL
		URI uri;
		try {
			uri = new URI(sb.toString());
		} catch (URISyntaxException e) {
			System.out.println("Reading file: " + filename);
			System.out.println("It looks like this file does not contain a valid URI.");
			printInstructions();
			return null;
		}
		String host = uri.getHost();
		int port = uri.getPort();

		String[] array = uri.getAuthority().split("[:@]");
		String auth = array[1];
		
		// connect to the server
		Jedis jedis = new Jedis(host, port);

		try {
			jedis.auth(auth);
		} catch (Exception e) {
			System.out.println("Trying to connect to " + host);
			System.out.println("on port " + port);
			System.out.println("with authcode " + auth);
			System.out.println("Got exception " + e);
			printInstructions();
			return null;
		}
		return jedis;
	}

	
	private static void printInstructions() {
		System.out.println("");		
		System.out.println("redis://redistogo:AUTH@HOST:PORT");
		System.out.println("redis_url.txt in the src/resources");		
	}

	public static void main(String[] args) throws IOException {
		Jedis jedis = make();

		// String
		jedis.set("mykey", "myvalue");
		String value = jedis.get("mykey");
	    System.out.println("Got value: " + value);
	    
	    // Set
	    jedis.sadd("myset", "element1", "element2", "element3");
	    System.out.println("element2 is member: " + jedis.sismember("myset", "element2"));
	    
	    // List
	    jedis.rpush("mylist", "element1", "element2", "element3");
	    System.out.println("element at index 1: " + jedis.lindex("mylist", 1));
	    
	    // Hash
	    jedis.hset("myhash", "word1", Integer.toString(2));
	    jedis.hincrBy("myhash", "word2", 1);
	    System.out.println("frequency of word1: " + jedis.hget("myhash", "word1"));
	    System.out.println("frequency of word2: " + jedis.hget("myhash", "word2"));
	}

}
