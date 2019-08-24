package kr.inhatc.algorithm.ch06;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.jsoup.nodes.Node;

/**
 * DOM 트리에서 노드를 순회하는데 사용되는 클래스 
 * 
 * 일반적인 Iterable 객체 대신 
 * 노드를 반복적으로 탐색하는 로직과 그 노드를 처리하는 로직을 
 * 쉽고 깔끔하게 분리하기 위해 사용되는 클래스 
 * @author kitae
 *
 */
public class WikiNodeIterable implements Iterable<Node> {

	private Node root;
	
	/**
	 * 생성자 
	 * 1. 루프 노드에 대한 참조를 인자로 받음 
	 * @param root
	 */
	public WikiNodeIterable(Node root) {
		this.root = root;
	}

	/**
	 * Iterator 객체를 생성하여 반환함 
	 */
	@Override
	public Iterator<Node> iterator() {
		return new WikiNodeIterator(root);
	}
	
	/**
	 * 내부 클래스 생성 
	 * - 실제 반복작업 수행 	 *
	 */
	public class WikiNodeIterator implements Iterator<Node> {
		
		Deque<Node> stack;

		/**
		 * 생성자 
		 * 스택을 초기화하고 그 안에 루트 노드를 푸쉬함 
		 * @param node
		 */
		public WikiNodeIterator(Node node) {
			stack = new ArrayDeque<Node>();
			stack.push(root);
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();	// 스택이 비었는지 확인 
		}

		/**
		 * 다음 Node를 팝하고 그 자식 노드들은 역순으로 푸쉬한 후
		 * pop한 Node를 반환한다.  
		 */		
		@Override
		public Node next() {
			// 스택이 빈 경우라면 예외 처리 
			if (stack.isEmpty()) {
				throw new NoSuchElementException();
			}

			// otherwise pop the next Node off the stack
			Node node = stack.pop();
			// System.out.println(node);

			// push the children onto the stack in reverse order
			List<Node> nodes = new ArrayList<Node>(node.childNodes());
			Collections.reverse(nodes);
			for (Node child : nodes) {
				stack.push(child);
			}
			return node;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
