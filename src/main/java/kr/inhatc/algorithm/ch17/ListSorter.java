package kr.inhatc.algorithm.ch17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ListSorter<T> {

	/**
	 * 삽입 정렬
	 * @param list
	 * @param comparator
	 */
	private void insertionSort(List<T> list, Comparator<T> comparator) {
		for (int i = 0; i < list.size(); i++) {
			T elt_i = list.get(i);	//i번째 위치의 원소 가져오기 
			int j = i;
			while(j>0) {
				T elt_j = list.get(j-1);
				if(comparator.compare(elt_i, elt_j)>= 0) {
					break;
				}
				list.set(j, elt_j);
				j--;
			}
			list.set(j, elt_i);
		}
	}

	/**
	 * 병합 정렬 수행
	 * @param list
	 * @param comparator
	 */
	private void mergeSortInPlace(List<T> list, Comparator<T> comparator) {
		List<T> sorted = mergeSort(list, comparator);
		list.clear();			// 기존 리스트 지우고 
		list.addAll(sorted);	// 정렬된 리스트로 대체 
	}
	
	/**
	 * 병합 정렬
	 * - 리스트를 인자로 받아 오름차순으로 정렬된 
	 * 동일한 요소의 새로운 리스트 반환  
	 * @param list
	 * @param comparator
	 * @return
	 */
	private List<T> mergeSort(List<T> list, Comparator<T> comparator) {
		int size = list.size();		// 전체 리스트의 크기
		if(size <= 1) {				// 크기가 1 이하면 그냥 반환 
			return list;	
		}
		
		// 리스트를 반으로 나누어 정렬한다. 
		List<T> first = mergeSort(new LinkedList<T>(list.subList(0, size/2)), comparator);
		List<T> second = mergeSort(new LinkedList<>(list.subList(size/2, size)), comparator);
		
		return merge(first, second, comparator);
	}

	private List<T> merge(List<T> first, List<T> second, Comparator<T> comparator) {
		List<T> result = new LinkedList<>();
		int total = first.size() + second.size();	// 전체 크기 
		for(int i=0; i<total; i++) {
			List<T> winner = pickWinner(first, second, comparator);
			result.add(winner.remove(0));
		}
		return result;
	}

	/**
	 * 두개의 정렬된 리스트를 하나의 정렬된 리스트로 만들기
	 * @param first
	 * @param second
	 * @param comparator
	 * @return
	 */
	private List<T> pickWinner(List<T> first, List<T> second, Comparator<T> comparator) {
		if(first.size() == 0) {
			return second;
		}
		if(second.size() == 0) {
			return first;
		}
		
		int res = comparator.compare(first.get(0), second.get(0));
		if(res < 0) {
			return first;
		}
		if(res > 0) {
			return second;
		}
		return first;
	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>(Arrays.asList(3, 5, 1, 4, 2));
		
		Comparator<Integer> comparator = new Comparator<Integer>() {

			@Override
			public int compare(Integer elt1, Integer elt2) {				
				return elt1.compareTo(elt2);
			}
		};

		ListSorter<Integer> sorter = new ListSorter<>();
		sorter.insertionSort(list, comparator);
		System.out.println(list);
		
		list = new ArrayList<>(Arrays.asList(3, 5, 1, 4, 2));
		sorter.mergeSortInPlace(list, comparator);
		System.out.println(list);
	}


	
	
}
