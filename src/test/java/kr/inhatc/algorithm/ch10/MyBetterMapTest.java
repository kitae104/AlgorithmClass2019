package kr.inhatc.algorithm.ch10;

import org.junit.Before;

import kr.inhatc.algorithm.ch09.MyLinearMapTest;

public class MyBetterMapTest extends MyLinearMapTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		map = new MyBetterMap<String, Integer>();
		map.put("One", 1);
		map.put("Two", 2);
		map.put("Three", 3);
		map.put(null, 0);
	}
}