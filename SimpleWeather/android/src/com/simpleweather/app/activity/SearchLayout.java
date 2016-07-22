package com.simpleweather.app.activity;

import java.net.URLEncoder;

import org.json.JSONObject;

import com.simpleweather.app.R;
import com.simpleweather.app.util.HttpCallbackListener;
import com.simpleweather.app.util.HttpUtil;
import com.simpleweather.app.util.ShareContext;
import com.simpleweather.app.util.ShareHandler;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SearchLayout extends LinearLayout{
    
	public SearchLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.search_layout, this);
	    Button searchButton = (Button) findViewById(R.id.search_button);
	    
	    searchButton.setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {	    		
	    	    EditText searchCityName = (EditText) findViewById(R.id.search_city_name);
	    	    
	    	    if (searchCityName.getText().toString().equals("")) {
				    Message message = new Message();
				    message.what = DisplayWeatherActivity.CITYTEXT_EMPTY;
				    ShareHandler.getHandler().sendMessage(message);	    	
	    	    } else {
	    		    String httpUrl = "http://apis.baidu.com/apistore/weatherservice/citylist";
	    		    String httpArg = "cityname="; 		
	    		    try {
	    			    httpArg += URLEncoder.encode(searchCityName.getText().toString(), "UTF-8");
	    		    } catch (Exception e) {
	    			    e.printStackTrace();
	    		    }
	    		
	    		    Log.d("DevDebug", httpArg);
	    		    HttpUtil.sendHttpRequest(httpUrl, httpArg, new HttpCallbackListener() {
	    			    @Override
	    			    public void onFinish(String response) {
	    				    try {
//	    				        Log.d("DevDebug", response);
	    					    Message message = new Message();
	    					    message.what = DisplayWeatherActivity.SELECT_CITY;
	    					    Bundle bundle = new Bundle();
	    					    bundle.putString("cityJsonData", response);
	    					    message.setData(bundle);
	    					    ShareHandler.getHandler().sendMessage(message);
	    				    } catch (Exception e) {
	    					    e.printStackTrace();
	    				    }
	    			    }
	    			
	    			    @Override
	    			    public void onError(Exception e) {
    					    Message message = new Message();
    					    message.what = DisplayWeatherActivity.NETWORK_ERROR;
    					    ShareHandler.getHandler().sendMessage(message);
	    				    e.printStackTrace();
	    			    }
	    		    });	
	    	    }
	    	}
	    });
    
	}
}
