package kr.inhatc.algorithm.ch11;

import java.util.List;
import java.util.Map;

import kr.inhatc.algorithm.ch09.MyLinearMap;
import kr.inhatc.algorithm.ch10.MyBetterMap;

public class MyHashMap<K, V> extends MyBetterMap<K, V> implements Map<K, V> {

	// 재해시하기 전 하위 맵당 평균 엔트리 개수
	// 로드 팩터(load factor) 
	protected static final double FACTOR = 1.0;
	
		
	@Override
	public V put(K key, V value) {
		V oldValue = super.put(key, value);
		
		// 하위 맵당 엔트리의 개수가 임계치를 넘지 않는지 확인
		// size()는 엔트리 전체의 개수 반환  
		// maps.size()는 내장된 맵의 개수 반환 
		if(size() > maps.size() * FACTOR) {
			rehash();
		}
		return oldValue;
	}

	/**
	 * 맵 수를 두 배로 늘리고 기존 항목을 다시 해시합니다.
	 */
	private void rehash() {
//		System.out.println("size() " + size());
//		System.out.println("rehash() 발생" + maps.size());
		
		// 기존 항목을 저장
		List<MyLinearMap<K, V>> oldMap = maps;
				
		// 기존 보다 2배 큰 Map 만들기
		int k = maps.size() * 2;
		makeMaps(k);
//		System.out.println("내장된 맵의 크기 " + maps.size());
		
		// 새 Map에 기존 항목을 다시 넣습니다.
		for(MyLinearMap<K, V> map : oldMap) {
			for(Map.Entry<K, V> entry : map.getEntries()) {
				// 새로운 해시 맵으로 이동 
				put(entry.getKey(), entry.getValue());	 
			}
		}
	}

	public static void main(String[] args) {
		Map<String,Integer> map = new MyHashMap<>();
		for (int i = 0; i < 10; i++) {
			//map.put(new Integer(i).toString(), i);
			//map.put(Integer.valueOf(i).toString(), i);
			map.put(""+i, i);
		}

		Integer value = map.get("3");
		System.out.println(value);
	}

}
