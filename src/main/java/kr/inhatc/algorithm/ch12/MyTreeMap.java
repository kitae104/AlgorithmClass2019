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
		if(key == null) {
			throw new IllegalArgumentException();
		}
		
		//@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) key;
		
		// 구현 필요!! 
		
		return null;
	}
	
	@Override
	public boolean containsValue(Object value) {		
		return containsValueHelper(root, value);
	}

	private boolean containsValueHelper(MyTreeMap<K, V>.Node root2, Object value) {
		// 구현 필요!! 
		return false;
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
		// 구현하기 
		return null;
	}

	@Override
	public V remove(Object key) {
		// TODO Auto-generated method stub
		return null;
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
		
		// 이부분 채우기 
		
		return set;
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
		// TODO Auto-generated method stub

	}

}
