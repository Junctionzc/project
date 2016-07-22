package com.simpleweather.app.activity;

import com.simpleweather.app.R;
import com.simpleweather.app.util.ShareHandler;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MenuLayout extends LinearLayout {
    
	public MenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.menu_layout, this);
		Button refresh = (Button) findViewById(R.id.refresh);	
		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message message = new Message();
				message.what = DisplayWeatherActivity.UPDATE_WEATHER;
				ShareHandler.getHandler().sendMessage(message);
			}
		});
		Button more = (Button) findViewById(R.id.more);	
		more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(getContext(), "You clicked more button", Toast.LENGTH_SHORT).show();
				Message message = new Message();
				message.what = DisplayWeatherActivity.MORE;
				ShareHandler.getHandler().sendMessage(message);
			}
		});
	}
}
