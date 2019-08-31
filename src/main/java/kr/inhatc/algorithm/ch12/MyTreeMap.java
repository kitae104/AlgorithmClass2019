package kr.inhatc.algorithm.ch12;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author kitae
 *
 * @param <K>
 * @param <V>
 */
public class MyTreeMap<K, V> implements Map<K, V> {

	private int size = 0;
	private Node root = null;
	
	/**
	 * 트리에서 노트 표현  
	 */
	protected class Node{
		public K key;
		public V value;
		public Node left = null;
		public Node right = null;
		
		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {		
		return size == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return findNode(key) != null;
	}

	private Node findNode(Object key) {
		// 일부 구현은 null을 키로 다루기도 하지만 여기서는 아님 
		if(key == null) {
			throw new IllegalArgumentException();
		}
		
		// 컴파일러 경고 무시 
		//@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) key;
		
		// 실제 탐색 
		Node node = root;		// 루트 지정 
		while(node != null) {
			int cmp = k.compareTo(node.key);	// 키와 노드의 키를 비교한다. 
			if(cmp < 0) {				// 키가 현재 노드보다 작은 경우 왼쪽으로 
				node = node.left;
			} else if(cmp > 0) {		// 키가 현재 노드보다 큰 경우 오른쪽으로매늠 
				node = node.right;	
			} else {
				return node;			// 찾은 경우 
			}
		}		
		return null;
	}
	
	@Override
	public boolean containsValue(Object value) {		
		return containsValueHelper(root, value);
	}

	private boolean containsValueHelper(Node node, Object value) {
		// 노드가 null이면 대상을 찾지 못하고 바닥에 이른 상태이기 때문에 false
		if(node == null) {
			return false;
		}
		
		// 원하는 것을 찾은 경우 
		if(equals(value, node.value)) {
			return true;
		}
		
		// 왼쪽 트리에서 해당 값 찾기 
		if(containsValueHelper(node.left, value)) {
			return true;
		}
		
		// 오른쪽 트리에서 해당 값 찾기 
		if(containsValueHelper(node.right, value)) {
			return true;
		}
		
		return false;
	}
	
	private boolean equals(Object value, Object nodeValue) {
		if(value == null) {
			return nodeValue == null;
		}
		return value.equals(nodeValue);
	}

	@Override
	public V get(Object key) {
		Node node = findNode(key);
		if(node == null) {
			return null;
		}
		return node.value;
	}

	@Override
	public V put(K key, V value) {
		if(key == null) {
			throw new NullPointerException();
		}
		if(root == null) {
			root = new Node(key, value);	// 새로운 노드 생성 
			size++;
			return null;
		}
		
		return putHelper(root, key, value);
	}

	private V putHelper(Node node, K key, V value) {
		@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) key;
		
		int cmp = k.compareTo(node.key);
		
		if(cmp < 0) {
			// 찾지 못한 경우라면 새로운 노드를 추가하고 값을 설정 
			if(node.left == null) {	
				node.left = new Node(key, value);
				size++;
				return null;
			} else {	// 이미 노드가 있는 경우라면 왼쪽으로 이동해서 반복 처리 
				return putHelper(node.left, key, value);
			}
		} else if (cmp > 0) {
			if(node.right == null) {
				node.right = new Node(key, value);
				size++;
				return null;
			} else {
				return putHelper(node.right, key, value);
			}
		}
		
		// 노드를 찾은 경우 값을 바꾸고 이전 값을 반환 
		V oldValue = node.value;
		node.value = value;
				
		return oldValue;
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}

	}

	@Override
	public void clear() {
		size = 0;
		root = null;
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new LinkedHashSet<>();		
		addInOrder(root, set);	// 중위 탐색 수행하기		
		return set;
	}

	/**
	 * 중위 탐색 수행하기 
	 * @param node
	 * @param set
	 */
	private void addInOrder(Node node, Set<K> set) {
		if(node == null) {
			return;
		}
		addInOrder(node.left, set);
		set.add(node.key);
		addInOrder(node.right, set);		
	}

	@Override
	public Collection<V> values() {
		Set<V> set = new HashSet<>();
		Deque<Node> stack = new LinkedList<>();
		stack.push(root);
		while(!stack.isEmpty()) {
			Node node = stack.pop();
			if(node == null) {
				continue;
			}
			set.add(node.value);
			stack.push(node.left);
			stack.push(node.right);
		}
		return set;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	public static void main(String[] args) {
		Map<String, Integer> map = new MyTreeMap<>();
		map.put("Word1", 1);
		map.put("Word2", 2);
		map.put("Word3", 3);
		Integer value = map.get("Word1");
		System.out.println(value);
		
		System.out.println(map.containsValue(1));
		
		for (String key : map.keySet()) {
			System.out.println(key + " : " + map.get(key));
		}

	}

	public MyTreeMap<K, V>.Node makeNode(K key, V value) {
		return new Node(key, value);
	}

	public void setTree(Node node, int size ) {
		this.root = node;
		this.size = size;
	}

	public int height() {
		return heightHelper(root);
	}

	private int heightHelper(Node node) {
		if(node == null) {
			return 0;
		} 
		int left = heightHelper(node.left);
		int right = heightHelper(node.right);
		return Math.max(left, right) + 1;
	}	
}
