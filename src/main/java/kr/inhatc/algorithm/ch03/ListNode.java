package kr.inhatc.algorithm.ch03;

public class ListNode {

	public Object data;
	public ListNode next;

	/**
	 * 생성자
	 */
	public ListNode() {
		this.data = null;
		this.next = null;
	}

	/**
	 * 생성자
	 * @param data
	 */
	public ListNode(Object data) {
		this.data = data;
		this.next = null;
	}

	/**
	 * 생성자
	 * @param data
	 * @param next
	 */
	public ListNode(Object data, ListNode next) {
		this.data = data;
		this.next = next;
	}

	/**
	 * 내용 출력
	 */
	public String toString() {
		return "ListNode(" + data.toString() + ")";
	}
}