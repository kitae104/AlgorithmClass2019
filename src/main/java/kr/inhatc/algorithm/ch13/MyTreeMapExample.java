package kr.inhatc.algorithm.ch13;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import kr.inhatc.algorithm.ch12.MyTreeMap;

public class MyTreeMapExample {

	public static void main(String[] args) {
		int n = 10000; //16384;
		System.out.println("\nTesting MyTreeMap with random strings");
		putRandomStrings(n);	// 난수 문자열 생성
		
		System.out.println("\nTesting MyTreeMap with timestamps");
		putTimestamps(n);
	}

	/**
	 * 난수 문자열 생성하기 
	 * @param n
	 */
	private static void putRandomStrings(int n) {
		//TreeMap<String, Integer> map = new TreeMap<String, Integer>();
		MyTreeMap<String, Integer> map = new MyTreeMap<>();
		
		final long startTime = System.currentTimeMillis();
		
		for(int i = 0; i < n; i++) {
			String uuid = UUID.randomUUID().toString();
			map.put(uuid, 0);
		}
		
		final long elapsed = System.currentTimeMillis() - startTime;
		//printResults(map, elapsed, -1);
		printResults(map, elapsed, map.height());
	}

	private static void printResults(Map<String, Integer> map, long elapsed, int height) {
		System.out.println("    Time in milliseconds = " + (elapsed));
		System.out.println("    Final size of MyTreeMap = " + map.size());
		System.out.println("    log base 2 of size of MyTreeMap = " + Math.log(map.size()) / Math.log(2));
		System.out.println("    Final height of MyTreeMap = " + height);
	}

	private static void putTimestamps(int n) {
//		TreeMap<String, Integer> map = new TreeMap<String, Integer>();
		MyTreeMap<String, Integer> map = new MyTreeMap<String, Integer>();
		
		final long startTime = System.currentTimeMillis();		
		for (int i=0; i<n; i++) {
			String timestamp = Long.toString(System.nanoTime());
			map.put(timestamp, 0);
		}
		final long elapsed = System.currentTimeMillis() - startTime;
		
		printResults(map, elapsed, map.height());
		//printResults(map, elapsed, -1);
	}
}
