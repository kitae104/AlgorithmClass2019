package kr.inhatc.algorithm.ch10;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.inhatc.algorithm.ch09.MyLinearMap;

/**
 * 
 * @author kitae
 *
 * @param <K>
 * @param <V>
 */
public class MyBetterMap<K, V> implements Map<K, V> {

	protected List<MyLinearMap<K, V>> maps;
	
	/**
	 * 생성자 
	 * 매개변수가 없는 경우 기본 크기 2로 맵 생성 
	 */
	public MyBetterMap() {
		makeMaps(2);
	}
	
	/**
	 * 생성자 
	 * 사용할 맵의 크기를 설정 
	 * @param k
	 */
	public MyBetterMap(int k) {
		makeMaps(k);
	}
	
	/**
	 * k개의 MyLinearMap을 생성함  
	 * @param k
	 */
	protected void makeMaps(int k) {
		maps = new ArrayList<>(k);		// ArrayList<MyLinearMap<K, V>>(k)
		for (int i = 0; i < k; i++) {
			maps.add(new MyLinearMap<>());	// new MyLinearMap<K, V>()
		}
	}

	/**
	 * 전체 크기를 반환한다. 
	 */
	@Override
	public int size() {
		// 서브맵의 크기를 합산
		int total = 0;
		for (MyLinearMap<K, V> map : maps) {
			total += map.size();
		}
		return total;
	}

	/**
	 * 크기가 0인지 확인 
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * 해당 키를 가지고 있는지 확인 
	 */
	@Override
	public boolean containsKey(Object key) {
		MyLinearMap<K, V> map = chooseMap(key);
		return map.containsKey(key);
	}

	/**
	 * 해당 값을 포함하고 있는지 확인 
	 */
	@Override
	public boolean containsValue(Object value) {
		for (MyLinearMap<K, V> map : maps) {
			if(map.containsValue(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * hashCode를 사용하여 주어진 키를 포함해야 할 맵을 찾습니다.
	 * @param key
	 * @return
	 */
	private MyLinearMap<K, V> chooseMap(Object key) {
		//int index = key == null ? 0 : Math.abs(key.hashCode() % maps.size());
		
		int index = 0;
		if(key != null) {
			index = Math.abs(key.hashCode() % maps.size());
		}
		
		return maps.get(index);
	}
	
	/**
	 * 키에 해당하는 맵을 선택하고 그 맵으로부터 값을 가져온다. 
	 */
	@Override
	public V get(Object key) {
		MyLinearMap<K, V> map = chooseMap(key);
		return map.get(key);
	}

	/**
	 * 키에 해당하는 맵을 선택하고 해당 맵의 키에 새로운 값으로 
	 * 저장하고 이전 값을 반환한다. 
	 */
	@Override
	public V put(K key, V value) {
		MyLinearMap<K, V> map = chooseMap(key);
		V oldValue = map.put(key, value); 
		return oldValue;
	}

	@Override
	public V remove(Object key) {
		MyLinearMap<K, V> map = chooseMap(key);
		V removeValue = map.remove(key);
		return removeValue;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}

	}

	@Override
	public void clear() {
		// 모든 서브 맵을 클리어 
		for (int i = 0; i < maps.size(); i++) {
			maps.get(i).clear();			
		}

	}

	/**
	 * 서브맵으로부터 keySets를 추가 
	 */
	@Override
	public Set<K> keySet() {
		Set<K> set = new HashSet<>();
		for (MyLinearMap<K, V> map : maps) {
			set.addAll(map.keySet());	// 한꺼번에 추가 
		}
		return set;
	}

	/**
	 * 서브맵으로부터 valueSets를 추가
	 */
	@Override
	public Collection<V> values() {
		Set<V> set = new HashSet<>();
		for (MyLinearMap<K, V> map : maps) {
			set.addAll(map.values());	// 한꺼번에 추가 
		}
		return set;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	public static void main(String[] args) {
		Map<String, Integer> map = new MyBetterMap<>();
	}
	
}
