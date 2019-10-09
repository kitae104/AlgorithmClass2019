package kr.inhatc.algorithm.ch03;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MyLinkedList<E> implements List<E> {

	/**
	 * Node를 관리하는 클래스  
	 */
	private class Node { 
		public E data;
		public Node next;

		/**
		 * 생성자 
		 * @param data
		 */
		public Node(E data) {
			this.data = data;
			this.next = null;
		}
				
		/**
		 * 생성자 
		 * @param data
		 * @param next
		 */
		public Node(E data, Node next) {
			this.data = data;
			this.next = next;
		}
		
		public String toString() {
			return "Node(" + data.toString() + ")";
		}
	}

	private int size;            // 요소의 개수 관리 - 상수시간 관리 
	private Node head;           // 첫번째 노드 참조

	/**
	 * 생성자
	 */
	public MyLinkedList() {
		head = null;
		size = 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// run a few simple tests
		List<Integer> mll = new MyLinkedList<Integer>();
		mll.add(1);
		mll.add(2);
		mll.add(3);
		System.out.println(Arrays.toString(mll.toArray()) + " size = " + mll.size());

//		mll.remove(new Integer(2));
		mll.remove(Integer.valueOf(2));
		System.out.println(Arrays.toString(mll.toArray()) + " size = " + mll.size());
	}

	/**
	 * 요소 추가하기 - 마지막에 노드 추가
	 */
	@Override
	public boolean add(E element) {
		if (head == null) {
			head = new Node(element);
		} else {
			Node node = head;
			// 마지막 노드까지 반복 
			for ( ; node.next != null; node = node.next) {}
			node.next = new Node(element);	// 마지막에 새로운 노드 추가 
		}
		size++;
		return true;
	}


	@Override
	public boolean addAll(Collection<? extends E> collection) {
		boolean flag = true;
		for (E element: collection) {
			flag &= add(element);
		}
		return flag;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		head = null;
		size = 0;
	}

	@Override
	public boolean contains(Object obj) {
		return indexOf(obj) != -1;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		for (Object obj: collection) {
			if (!contains(obj)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 인덱스에 해당하는 노드의 데이터 반환 
	 */
	@Override
	public E get(int index) {
		Node node = getNode(index);
		return node.data;
	}

	/** 
	 * 인덱스에 해당하는 노드 반환 
	 * @param index
	 * @return
	 */
	private Node getNode(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		Node node = head;
		for (int i=0; i<index; i++) {
			node = node.next;
		}
		return node;
	}

	

	/** Checks whether an element of the array is the target.
	 *
	 * Handles the special case that the target is null.
	 *
	 * @param target
	 * @param object
	 */
	private boolean equals(Object target, Object element) {
		if (target == null) {
			return element == null;
		} else {
			return target.equals(element);
		}
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<E> iterator() {
		E[] array = (E[]) toArray();
		return Arrays.asList(array).iterator();
	}

	@Override
	public int lastIndexOf(Object target) {
		Node node = head;
		int index = -1;
		for (int i=0; i<size; i++) {
			if (equals(target, node.data)) {
				index = i;
			}
			node = node.next;
		}
		return index;
	}

	@Override
	public ListIterator<E> listIterator() {
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return null;
	}

	@Override
	public boolean remove(Object obj) {
		int index = indexOf(obj);
		if (index == -1) {
			return false;
		}
		remove(index);
		return true;
	}

	
	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean flag = true;
		for (Object obj: collection) {
			flag &= remove(obj);
		}
		return flag;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E set(int index, E element) {
		Node node = getNode(index);
		E old = node.data;
		node.data = element;
		return old;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		if (fromIndex < 0 || toIndex >= size || fromIndex > toIndex) {
			throw new IndexOutOfBoundsException();
		}
		// TODO: classify this and improve it.
		int i = 0;
		MyLinkedList<E> list = new MyLinkedList<E>();
		for (Node node=head; node != null; node = node.next) {
			if (i >= fromIndex && i <= toIndex) {
				list.add(node.data);
			}
			i++;
		}
		return list;
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		int i = 0;
		for (Node node=head; node != null; node = node.next) {
			// System.out.println(node);
			array[i] = node.data;
			i++;
		}
		return array;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}
	
	//======================================================
	// 수정이 필요한 부분(2019.10.09) 
	//======================================================
	
	/**
	 * 타겟 데이터와 동등한 데이터를 갖고 있는 노드의 위치 반환 
	 */
	@Override
	public int indexOf(Object target) {
		//TODO: FILL THIS IN!
		//Node node = head;
		for (int i = 0; i < size; i++) {
			if(equals(target, get(i))) {	// 아래 구현된 equals 사용 
				return i;	// 해당 위치 반환 
			}
			//node = node.next;
		}				
		return -1;
	}
	
	/**
	 * 특정한 위치에 노드 추가하기 
	 */
	@Override
	public void add(int index, E element) {
		//TODO: FILL THIS IN!		
		if(index == 0) {
			head = new Node(element, head);
		} else {
			Node node = getNode(index - 1);		// 범위에 대한 처리 수행 
			node.next = new Node(element, node.next);	// 노드를 생성하여 추가
		}
		size++;		// 노드 추가후 크기 변경 
	}
	
	/**
	 * 인덱스에 해당하는 내용물을 반환하고 해당 위치를 제거한다. 
	 */
	@Override
	public E remove(int index) {
		//TODO: FILL THIS IN!
		E element = get(index);		// 인덱스에 해당하는 원소 반환
		if(index == 0) {
			head = head.next;			
		} else {
			Node node = getNode(index - 1);
			node.next = node.next.next; 
		}
		size--;	// 전체 크기를 하나 줄인다. 
		
		return element;		// 제거된 값 반환 
	}

}