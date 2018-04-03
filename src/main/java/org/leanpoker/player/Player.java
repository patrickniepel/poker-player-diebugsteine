package org.leanpoker.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Player {

	static final String VERSION = "Default Java folding player";

	public static int betRequest(JsonElement request) {

		// Gson gson = new Gson(); // Or use new GsonBuilder().create();
		Gson gson = new Gson();

		// String json = gson.toJson); // serializes target to Json
		GameState state = gson.fromJson(request, GameState.class); // deserializes
																	// json into
																	// target2

		Card[] hands = null;
		PlayerUs player = new PlayerUs();
		
		for (int i = 0; i < state.players.length; i++) {
			player = state.players[i];
			if (player.hole_cards != null) {
				hands = player.hole_cards;
			}
		}
		
	

		PokerHands pokerHands = checkCommunityCards(hands, state.community_cards);
		
		if (shouldFold(hands)) {
			return 0;
		}
		
		if (pokerHands == PokerHands.FOUR_OF_A_KIND) {
			return (int) (state.current_buy_in + player.stack);
		}
		if (pokerHands == PokerHands.STRAIGHT) {
			return (int) (state.current_buy_in + player.stack * 0.4);
		}
		if (pokerHands == PokerHands.FLUSH) {
			return (int) (state.current_buy_in + player.stack * 0.5);
		}
		if (pokerHands == PokerHands.THREE_OF_A_KIND) {
			return (int) (state.current_buy_in + player.stack * 0.4);
		}
		if (pokerHands == PokerHands.TWO_PAIR) {
			return (int) (state.current_buy_in + player.stack * 0.3);
		}
		if (pokerHands == PokerHands.PAIR) {
			return (int) (state.current_buy_in);
		}
		if (pokerHands == PokerHands.HIGH_CARD) {
			return (int) (state.current_buy_in);
		}

		return 0;
	}
	

	public static boolean shouldFold(Card[] hand) {
		int difference = 0;
		Card card1 = hand[0];
		Card card2 = hand[1];
		difference = Math.abs(card1.getValue() - card2.getValue());
		boolean hasPicture = (card1.isPicture() && card2.isPicture());
		
		//if(card1.getValue() % 100 < 7 && card2.getValue() % 100 < 7 && card1.getValue() % 100 != card2.getValue() % 100) {
			//return true;
		//}
		if (hasPicture) {
			return false;
		} else {
			return difference >= 6;
		}
	}

	public static PokerHands checkCommunityCards(Card[] hand, Card[] community) {

		Card[] all = new Card[hand.length + community.length];
		int[] values = new int[all.length];

		all[0] = hand[0];
		all[1] = hand[1];

		for (int i = 0; i < community.length; i++) {
			all[i + 2] = community[i];
		}

		for (int i = 0; i < values.length; i++) {
			values[i] = all[i].getValue();
		}

		//Count ranks
		HashMap<String, Integer> dict = new HashMap<>();

		for (Card card : all) {
			Integer rank = dict.get(card.rank);

			if (rank == null) {
				dict.put(card.rank, 1);
			} else {
				dict.put(card.rank, rank + 1);
			}
		}
		
		//Count suits
		HashMap<String, Integer> dictSuits = new HashMap<>();

		for (Card card : all) {
			Integer suitValue = dict.get(card.suit);

			if (suitValue == null) {
				dict.put(card.suit, 1);
			} else {
				dict.put(card.suit, suitValue + 1);
			}
		}

		Set<String> keys = dict.keySet();
		ArrayList list = new ArrayList(keys);
		
		Set<String> keysSuits = dictSuits.keySet();
		ArrayList listSuits = new ArrayList(keysSuits);
		
		if (hasFourOfAKind(list, dict)) {
			return PokerHands.FOUR_OF_A_KIND;
		}
		
		if (hasStraight(all)) {
			return PokerHands.STRAIGHT;
		}
		
		if (hasFlush(listSuits, dictSuits)) {
			return PokerHands.FLUSH;
		}
		
		if (hasThreeOfAKind(list, dict)) {
			return PokerHands.THREE_OF_A_KIND;
		}

		if (hasTwoPair(list, dict)) {
			return PokerHands.TWO_PAIR;
		}
		
		if (hasPair(list, dict)) {
			return PokerHands.PAIR;
		}

		return PokerHands.HIGH_CARD;
	}
	
	public static boolean hasPair(ArrayList list, HashMap<String, Integer> dict) {

		int result = 0;

		for (int i = 0; i < list.size(); i++) {
			String rank = list.get(i).toString();

			int appearanceOfRank = dict.get(rank);

			if (appearanceOfRank >= 2) {
				return true;
			}
		}

		return false;
	}

	public static boolean hasTwoPair(ArrayList list, HashMap<String, Integer> dict) {

		int result = 0;

		for (int i = 0; i < list.size(); i++) {
			String rank = list.get(i).toString();

			int appearanceOfRank = dict.get(rank);

			if (appearanceOfRank >= 2) {
				result += 1;
			}
		}

		return result >= 2;
	}
	
	public static boolean hasThreeOfAKind(ArrayList list, HashMap<String, Integer> dict) {

		int result = 0;

		for (int i = 0; i < list.size(); i++) {
			String rank = list.get(i).toString();

			int appearanceOfRank = dict.get(rank);

			if (appearanceOfRank >= 3) {
				return true;
			}
		}

		return false;
	}
	
	public static boolean hasFourOfAKind(ArrayList list, HashMap<String, Integer> dict) {

		int result = 0;

		for (int i = 0; i < list.size(); i++) {
			String rank = list.get(i).toString();

			int appearanceOfRank = dict.get(rank);

			if (appearanceOfRank >= 4) {
				return true;
			}
		}

		return false;
	}
	
	public static boolean hasFlush(ArrayList list, HashMap<String, Integer> dict) {

		int result = 0;

		for (int i = 0; i < list.size(); i++) {
			String rank = list.get(i).toString();

			int appearanceOfSuit = dict.get(rank);

			if (appearanceOfSuit >= 5) {
				return true;
			}
		}

		return false;
	}
	
	public static boolean hasStraight(Card[] all) {

		boolean hasAce = false;
		int[] values = new int[all.length];
		
		for(int i = 0; i < all.length; i++) {
			values[i] = all[i].getValue();
		}
		
		Arrays.sort(values);
		
		for(Card card : all) {
			if(card.rank.equals("A")) {
				hasAce = true;
			}
		}
		
		boolean isStraight = true;
		int lengthOfStraight = 5;
		
		if(all.length < lengthOfStraight) {
			return false;
		}
		
		if(all.length == 5) {
			for(int i = 0; i < lengthOfStraight - 1; i++) {
				
				int value1 = values[i] % 100;
				int value2 = values[i+1] % 100;
				
				if(value1 + 1 != value2) {
					isStraight = false;
				}
			}
		}
		
		if(all.length == 6) {
			for(int j = 0; j < 2; j++) {
				
				for(int i = j; i < lengthOfStraight - 1 + j; i++) {
					
					int value1 = values[i] % 100;
					int value2 = values[i+1] % 100;
					
					if(value1 + 1 != value2) {
						isStraight = false;
					}
				}
			}
		}
		

		if(all.length == 7) {
			for(int j = 0; j < 3; j++) {
				
				for(int i = j; i < lengthOfStraight - 1 + j; i++) {
					
					int value1 = values[i] % 100;
					int value2 = values[i+1] % 100;
					
					if(value1 + 1 != value2) {
						isStraight = false;
					}
				}
			}
		}
		
		return isStraight;
	}


	public static void showdown(JsonElement game) {
	}
}
