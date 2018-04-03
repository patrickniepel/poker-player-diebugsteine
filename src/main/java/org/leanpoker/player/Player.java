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
    	
    	
        
    	 
    	 return state.current_buy_in;
    }

    public static void showdown(JsonElement game) {
    }
}
