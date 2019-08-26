package kr.inhatc.algorithm.ch11;

import java.util.Map;

import kr.inhatc.algorithm.ch09.MyLinearMap;

/**
 * MyHashMap을 상속 받아 처리하는 클래스 
 * @author kitae
 *
 * @param <K>
 * @param <V>
 */
public class MyFixedHashMap<K, V> extends MyHashMap<K, V> implements Map<K, V> {

	private int size = 0;	// 엔트리 개수 유지

	@Override
	public void clear() {
		super.clear();
		size = 0;
	}
	
	/**
	 * 하위 맵에서 변경이 있었는지 알수 없기 때문에
	 * 제거와 추가를 번갈아 가면서 수행 
	 */
	@Override
	public V put(K key, V value) {
		// 키에 해당하는 하위 맵 찾기 
		MyLinearMap<K, V> map = chooseMap(key);
		size -= map.size();					// 맵 크기만큼 줄이고
		V oldValue = map.put(key, value);	// 맵에 새로운 내용 추가
		size += map.size();					// 다시 크기를 설정 

		if (size() > maps.size() * FACTOR) {
			size = 0;
			rehash();
		}
		return oldValue;
	}

	/**
	 * 하위 맵에서 변경이 있었는지 알수 없기 때문에
	 * 제거와 추가를 번갈아 가면서 수행 
	 */
	@Override
	public V remove(Object key) {
		// 키에 해당하는 하위 맵 찾기 
		MyLinearMap<K, V> map = chooseMap(key);
		size -= map.size();				// 하위맵 크기 만큼 줄임 
		V oldValue = map.remove(key);	// 키에 해당하는 내용 제거 
		size += map.size();				// 변경 사항을 다시 size에 저장 
		return oldValue;
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Integer> map = new MyFixedHashMap<String, Integer>();
		for (int i=0; i<10; i++) {
			map.put(new Integer(i).toString(), i);
		}
		Integer value = map.get("3"); 
		System.out.println(value);
	}
}