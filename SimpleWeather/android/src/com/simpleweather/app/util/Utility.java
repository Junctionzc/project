package com.simpleweather.app.util;

import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    
	public static void handleWeatherResponse(String response) {
    	try {
    		JSONObject jsonObject = new JSONObject(response).getJSONObject("retData");
    	} catch (JSONException e) {
    		e.printStackTrace();
    	}
    }
}
