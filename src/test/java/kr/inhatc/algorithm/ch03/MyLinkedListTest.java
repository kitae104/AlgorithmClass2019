package kr.inhatc.algorithm.ch03;

import java.util.ArrayList;

import org.junit.Before;

import kr.inhatc.algorithm.ch02.MyArrayListTest;

public class MyLinkedListTest extends MyArrayListTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);

		mylist = new MyLinkedList<Integer>();
		mylist.addAll(list);
	}
}