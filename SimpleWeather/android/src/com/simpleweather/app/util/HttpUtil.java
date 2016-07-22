package com.simpleweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtil {
    
	public static void sendHttpRequest(final String httpUrl, final String httpArg,
			final HttpCallbackListener listener) {  
	    final String finalHttpUrl = httpUrl + "?" + httpArg;
	    
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
  				    URL url = new URL(finalHttpUrl);
				    connection = (HttpURLConnection) url.openConnection();
			        connection.setRequestMethod("GET");
			        connection.setConnectTimeout(8000);
			        connection.setReadTimeout(8000);
			        connection.setRequestProperty("apikey",  "f11f28ccc7b3dfc8effca0e411f4e88e");
			        connection.connect();
			        InputStream is = connection.getInputStream();
			        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				    String strRead = null;
				    StringBuffer response = new StringBuffer();
				    while ((strRead = reader.readLine()) != null) {
				    	response.append(strRead);
				    	response.append("\r\n");
				    }
				    reader.close();
				    if (listener != null) {
				    	Log.d("DevDebug", response.toString());
				    	listener.onFinish(response.toString());
				    }
				} catch (Exception e) {
					if (listener != null) {
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
