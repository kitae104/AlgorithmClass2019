package kr.inhatc.algorithm.ch04;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jfree.data.xy.XYSeries;

import kr.inhatc.algorithm.ch04.Profiler.Timeable;

public class ProfileListAdd {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		profileArrayListAddEnd();
		//profileArrayListAddBeginning();
		//profileLinkedListAddBeginning();
		//profileLinkedListAddEnd();
	}

	/**
	 * ArrayList의 끝에 새로운 요소를 추가하는 add 메소드의 실행시간 측정 
	 */
	public static void profileArrayListAddEnd() {
		Timeable timeable = new Timeable() {
			List<String> list;

			// 시간 측정을 수행하기 전에 필요한 일 수행 
			public void setup(int n) {
				list = new ArrayList<String>();
			}

			// 측정 작업 수행 
			public void timeMe(int n) {
				for (int i=0; i<n; i++) {
					list.add("a string");	// 마지막에 추가 
				}
			}
		};
				
		int startN = 4000;  // 54000
		int endMillis = 1000;
		runProfiler("ArrayList add end", timeable, startN, endMillis);
	}
	
	/**
	 * Characterize the run time of adding to the beginning of an ArrayList
	 */
	public static void profileArrayListAddBeginning() {
		// TODO: FILL THIS IN!
		Timeable timeable = new Timeable() {
			List<String> list;

			public void setup(int n) {
				list = new ArrayList<String>();
			}

			public void timeMe(int n) {
				for (int i=0; i<n; i++) {
					list.add(0, "a string");	// 시작 부분에 추가 
				}
			}
		};
				
		int startN = 4000;
		int endMillis = 1000;
		runProfiler("ArrayList add beginning", timeable, startN, endMillis);
	}

	/**
	 * Characterize the run time of adding to the beginning of a LinkedList
	 */
	public static void profileLinkedListAddBeginning() {
		// TODO: FILL THIS IN!
		Timeable timeable = new Timeable() {
			List<String> list;

			public void setup(int n) {
				list = new LinkedList<String>();	// LinkedList로 생성 
			}

			public void timeMe(int n) {
				for (int i=0; i<n; i++) {
					list.add(0, "a string");	// 시작 부분에 추가 
				}
			}
		};
				
		int startN = 128000;
		int endMillis = 2000;
		runProfiler("LinkedList add beginning", timeable, startN, endMillis);
	}

	/**
	 * Characterize the run time of adding to the end of a LinkedList
	 */
	public static void profileLinkedListAddEnd() {
		// TODO: FILL THIS IN!
		Timeable timeable = new Timeable() {
			List<String> list;

			public void setup(int n) {
				list = new LinkedList<String>();	// LinkedList로 생성 
			}

			public void timeMe(int n) {
				for (int i=0; i<n; i++) {
					list.add("a string");	// 마지막 부분에 추가 
				}
			}
		};
				
		int startN = 64000;  // 4000
		int endMillis = 1000;
		runProfiler("LinkedList add end", timeable, startN, endMillis);
	}

	/**
	 * Runs the profiles and displays results.
	 * 
	 * @param timeable
	 * @param startN
	 * @param endMillis
	 */
	private static void runProfiler(String title, Timeable timeable, int startN, int endMillis) {
		Profiler profiler = new Profiler(title, timeable);
		// timeMe()를 범위 안에서 여러번 호출 
		XYSeries series = profiler.timingLoop(startN, endMillis);
		profiler.plotResults(series);
	}
}