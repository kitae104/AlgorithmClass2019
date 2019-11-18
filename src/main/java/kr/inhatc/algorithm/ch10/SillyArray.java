package kr.inhatc.algorithm.ch10;

import java.util.Arrays;
import java.util.Map;

public class SillyArray {

	private final char[] array;
	
	public SillyArray(char[] array) {
		this.array = array;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(array);
	}
	
	@Override
	public int hashCode() {
		int total = 0;
		for (int i = 0; i < array.length; i++) {
			total += array[i];
		}
		System.out.println("해시 " + total);
		return total;
	}

	@Override
	public boolean equals(Object other) {
		return this.toString().equals(other.toString());
	}

	/**
	 * 배열의 i번 째 위치를 제공되는 문자로 대체한다. 
	 * @param i
	 * @param c
	 */
	public void setChar(int i, char c) {
		this.array[i] = c;
	}
	
	public static void main(String[] args) {
		Map<SillyArray, Integer> map = new MyBetterMap<>();
		
		SillyArray array1 = new SillyArray("Word1".toCharArray());
		map.put(array1, 1);
		Integer value = map.get(array1);
		System.out.println("value : " + value);
		
		array1.setChar(0, 'C');
		
		Integer value2 = map.get(array1);
		System.out.println("value : " + value2);
		
		for (SillyArray key : map.keySet()) {
			System.out.println(key + " : " + map.get(key));
		}
	}
}
