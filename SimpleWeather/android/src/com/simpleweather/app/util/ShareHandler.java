package com.simpleweather.app.util;

import android.os.Handler;

public class ShareHandler {
    private static Handler handler = null;
    
    public static void setHandler(Handler handler) {
    	ShareHandler.handler = handler;
    }
    
    public static Handler getHandler() {
    	return handler;
    }
}
