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
    	
    	for(int i = 0; i < state.player.length; i++) {
    		PlayerUs player = state.player[i];
    		if(player.hole_cards != null) {
    			Card[] cards = player.hole_cards;
    			
    			//Paar
    			if(cards[0].rank == cards[1].rank) {
    				return (int) (player.stack * 0.5);
    			}
    		}
    	}
    	
        
    	 
    	return state.current_buy_in;
    }

    public static void showdown(JsonElement game) {
    }
}
