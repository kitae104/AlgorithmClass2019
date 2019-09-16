package kr.inhatc.algorithm.ch01;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class ListClientExampleTest {

	@Test
	public void testListClientExample() {
		ListClientExample lce = new ListClientExample();
		//@SuppressWarnings("rawtypes")
		List list = lce.getList();
		assertThat(list, instanceOf(LinkedList.class) );
	}
}
