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

		System.err.println("ERRRROR: " + state);
		
		for (int i = 0; i < state.player.length; i++) {
			player = state.player[i];
			if (player.hole_cards != null) {
				hands = player.hole_cards;
			}
		}
		
		

		//if (shouldFold(cards)) {
			//return 0;
		//}

		PokerHands pokerHands = checkCommunityCards(hands, state.community_cards);
		
		if (pokerHands == PokerHands.FOUR_OF_A_KIND) {
			return (int) (player.stack);
		}
		if (pokerHands == PokerHands.FLUSH) {
			return (int) (player.stack * 0.5);
		}
		if (pokerHands == PokerHands.THREE_OF_A_KIND) {
			return (int) (player.stack * 0.4);
		}
		if (pokerHands == PokerHands.TWO_PAIR) {
			return (int) (player.stack * 0.3);
		}
		if (pokerHands == PokerHands.PAIR) {
			return (int) (player.stack * 0.25);
		}
		if (pokerHands == PokerHands.HIGH_CARD) {
			return (int) (player.stack * 0.1);
		}

		return state.current_buy_in;
	}

	public static boolean shouldFold(Card[] hand) {
		int difference = 0;
		Card card1 = hand[0];
		Card card2 = hand[1];
		difference = Math.abs(card1.getValue() - card2.getValue());
		boolean hasPicture = (card1.isPicture() || card2.isPicture());
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


	public static void showdown(JsonElement game) {
	}
}
