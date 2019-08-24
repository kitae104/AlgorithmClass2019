package kr.inhatc.algorithm.ch09;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyLinearMap<K, V> implements Map<K, V> {

	// Entry 객체들을 담은 ArrayList 
	private List<Entry> entries = new ArrayList<>();
	
	@Override
	public int size() {		
		return entries.size();
	}

	@Override
	public boolean isEmpty() {		
		return entries.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {		
		return findEntry(key) != null;
	}

	/**
	 * 대상키가 포함 된 항목을 반환하거나 없는 경우 null을 반환합니다.
	 * @param key
	 * @return
	 */
	private Entry findEntry(Object key) {
		for (Entry entry : entries) {
			if(equals(key, entry.getKey())) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public boolean containsValue(Object key) {
		for (Map.Entry<K, V> entry : entries) {
			if(equals(key, entry.getValue())) {
				return true;
			}
		}
		return false;
	}

	private boolean equals(Object target, Object obj) {
		if(target == null) {
			return obj == null;
		}
		return target.equals(obj);
	}
	
	@Override
	public V get(Object key) {
		Entry entry = findEntry(key);
		if(entry == null) {
			return null;
		}
		return entry.getValue();
	}

	/**
	 * 키에 대한 값을 입력할 경우 새로운 경우엔 값 입력후 null 반환 
	 * 해당 키가 존재하는 경우엔 새 값을 등록하고 기존 값을 반환 
	 */
	@Override
	public V put(K key, V value) {
		Entry entry = findEntry(key);
		
		if(entry == null) {
			// 새로운 엔트리 추가 
			entries.add(new Entry(key, value));
			return null;
		} else {
			// 기존에 존재하는 경우 
			V oldValue = entry.getValue();
			entry.setValue(value);
			return oldValue;
		}		
	}

	@Override
	public V remove(Object key) {
		Entry entry = findEntry(key);
		if(entry == null) {
			return null;
		} else {
			V value = entry.getValue();
			entries.remove(entry);
			return value;
		}
	}
	

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K,? extends V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		entries.clear();
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new HashSet<>();
		for (Entry entry : entries) {
			set.add(entry.getKey());
		}
		return set;
	}

	@Override
	public Collection<V> values() {
		Set<V> set = new HashSet<>();
		for(Entry entry : entries) {
			set.add(entry.getValue());
		}
		return set;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {		
		throw new UnsupportedOperationException();
	}

	
	/**
	 * 'entries'에 대한 참조를 반환합니다.
	 *
	 *  이것은 Map 인터페이스의 일부가 아닙니다. 
	 *  기능을 제공하기 위해 여기에 있습니다
	 * "entrySet"의 *는 "올바른"방법보다 실질적으로 더 간단한 방법입니다.
	 * @return
	 */
	public Collection<? extends java.util.Map.Entry<K, V>> getEntries() {
		return entries;
	}

	public class Entry implements Map.Entry<K, V> {
		
		private K key;
		private V value;
		
		/**
		 * 생성자 		
		 * @param key
		 * @param value
		 */
		public Entry(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {			
			return key;
		}

		@Override
		public V getValue() {			
			return value;
		}

		@Override
		public V setValue(V value) {
			this.value = value; 
			return this.value;
		}		
	}
	
	public static void main(String[] args) {
		Map<String, Integer> map = new MyLinearMap<>();
		map.put("Word1", 1);
		map.put("Word2", 2);
		map.put("Word3", 3);
		Integer value = map.get("Word1");
		System.out.println(value);
		
		for (String key : map.keySet()) {
			System.out.println(key + " : " + map.get(key));
		}
	}
	
}
