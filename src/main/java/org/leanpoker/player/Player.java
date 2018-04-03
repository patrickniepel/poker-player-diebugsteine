package org.leanpoker.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.Map;

public class Player {

    static final String VERSION = "Default Java folding player";

    public static int betRequest(JsonElement request) {
    	
    	//Gson gson = new Gson(); // Or use new GsonBuilder().create();
    	Gson gson = new Gson();
    	
    	//String json = gson.toJson); // serializes target to Json
    	GameState state = gson.fromJson(request, GameState.class); // deserializes json into target2
    	
    	Card[] cards = null;
    	PlayerUs player = null;
    	
    	for(int i = 0; i < state.player.length; i++) {
    		player = state.player[i];
    		if(player.hole_cards != null) {
    			cards = player.hole_cards;
    			
    		}
    	}
    	
    	if(shouldFold(cards)) {
    		return 0;
    	}
    	
		//Paar
		if(cards[0].rank == cards[1].rank) {
			return (int) (player.stack * 0.5);
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

    public static void showdown(JsonElement game) {
    }
}
