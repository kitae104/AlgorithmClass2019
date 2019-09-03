package kr.inhatc.algorithm.ch16;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Card implements Comparable<Card>{

	public static final String[] RANKS = {
	        null, "Ace", "2", "3", "4", "5", "6", "7",
	        "8", "9", "10", "Jack", "Queen", "King"};

	public static final String[] SUITS = {
	        "Clubs", "Diamonds", "Hearts", "Spades"};

	
	private int rank;
	private int suit;
	
	/**
	 * 생성자
	 * @param rank
	 * @param suit
	 */
	public Card(int rank, int suit) {
		super();
		this.rank = rank;
		this.suit = suit;
	}

	public int getRank() {
		return this.rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getSuit() {
		return this.suit;
	}

	public void setSuit(int suit) {
		this.suit = suit;
	}

	@Override
	public String toString() {
		return RANKS[this.rank] + " of " + SUITS[this.suit];
	}

	/**
	 * 랭크와 카드가 동일한지 확인 
	 * @param that
	 * @return
	 */
	public boolean equals(Card that) {
		return this.rank == that.rank && this.suit == that.suit;
	}
	
	@Override
	public int compareTo(Card that) {
		if(this.suit < that.suit) {
			return -1;
		} 
		if(this.suit > that.suit) {
			return 1;
		}
		if(this.rank < that.rank) {
			return -1;
		}
		if(this.rank > that.rank) {
			return 1;
		}
		return 0;
	}

	public static List<Card> makeDeck(){
		List<Card> cards = new ArrayList<>();
		for(int suit = 0; suit <= 3; suit++) {	// 카드 종류 4개 
			for(int rank = 1; rank <= 13; rank++) {	// 숫자 종류 13개
				Card card = new Card(rank, suit);
				cards.add(card);
			}
		}
		return cards;
	}
	
	public static void main(String[] args) {
		
		List<Card> cards = makeDeck();
		Collections.sort(cards);
		System.out.println(cards.get(0));
		System.out.println(cards.get(51));
		
		Comparator<Card> comparator = new Comparator<Card>() {

			@Override
			public int compare(Card card1, Card card2) {
				if(card1.getSuit() < card2.getSuit()) {
					return -1;
				}
				if(card1.getSuit() > card2.getSuit()) {
					return 1;
				}
				
				int rank1 = getRankAceHigh(card1);
				int rank2 = getRankAceHigh(card2);
				
				if(rank1 < rank2) {
					return -1;
				}
				if(rank1 > rank2) {
					return 1;
				}
				return 0;
			}

			private int getRankAceHigh(Card card) {
				int rank = card.getRank();
				if(rank == 1) {
					return 14;
				} 
				else {
					return rank;
				}
			}
		};
		
		// 외부 비교자를 이용해서 카드 정렬 
		Collections.sort(cards, comparator);
		System.out.println(cards.get(0));
		System.out.println(cards.get(51));
	}
}
