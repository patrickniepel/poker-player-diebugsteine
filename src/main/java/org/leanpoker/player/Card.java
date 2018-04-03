package org.leanpoker.player;

public class Card {
	public String rank;
	public String suit;

	public int getValue() {

		int intRank = 0;

		if (rank.equals("J")) {
			intRank = 11;
		}
		else if (rank.equals("Q")) {
			intRank = 12;
		}
		else if (rank.equals("K")) {
			intRank = 13;
		}
		else if (rank.equals("A")) {
			intRank = 14;
		}

		if (suit.equals("clubs")) {
			return intRank;
		}
		if (suit.equals("spades")) {
			return intRank + 100;
		}
		if (suit.equals("hearts")) {
			return intRank + 200;
		}
		if (suit.equals("diamonds")) {
			return intRank + 300;
		}

		return -1;
	}
	
	public boolean isPicture() {
		return this.getValue() >= 11;
	}

}
