package kr.inhatc.algorithm.ch10;

import java.util.Map;

public class SillyString {

	private String innerString;
	
	public SillyString(String innerString) {
		this.innerString = innerString;
	}
	
	@Override
	public String toString() {
		return innerString;
	}
	
	@Override
	public boolean equals(Object other) {
		return this.toString().equals(other.toString());
		//return this.hashCode() == other.hashCode();
	}

	@Override
	public int hashCode() {
		int total = 0;
		for (int i=0; i<innerString.length(); i++) {
			total += innerString.charAt(i);
		}
		System.out.println(innerString + " - 해시 : "  + total);
		return total;
	}
	
	public static void main(String[] args) {
		Map<SillyString, Integer> map = new MyBetterMap<>();
		
		map.put(new SillyString("Word1"), 1);
		map.put(new SillyString("Word2"), 2);
		map.put(new SillyString("Worc3"), 3);
		Integer value = map.get(new SillyString("Word1"));
		System.out.println(value);
		
		for (SillyString key: map.keySet()) {
			System.out.println(key + ", " + map.get(key));
		}
		
		System.out.println(new SillyString("Word2").equals(new SillyString("Worc3")));
	}

}
