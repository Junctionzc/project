package com.simpleweather.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simpleweather.app.R;
import com.simpleweather.app.util.DensityUtil;
import com.simpleweather.app.util.HttpCallbackListener;
import com.simpleweather.app.util.HttpUtil;
import com.simpleweather.app.util.ShareContext;
import com.simpleweather.app.util.ShareHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class DisplayWeatherActivity extends Activity {
    
	public static final int UPDATE_WEATHER = 1;
	
	public static final int MORE = 2;
	
	public static final int SELECT_CITY = 3;
	
	public static final int NETWORK_ERROR = 4;
	
	public static final int CITYTEXT_EMPTY = 5;
	
	public static final int UPDATE_SUCCESS = 6;
	
	public String cityId;
	
	private boolean showButtonTips = false;
	
	private LinearLayout backgroundLinearLayout;
	
	private Animation animation;
	
	private ProgressDialog progressDialog;
	
	private TextView cityNameText;
	
	private TextView publishText;
	
	private TextView weatherDespText;
	
	private ImageView weatherDespImage;
	
	private TextView tempText;
	
	private TextView templText;
	
	private TextView temphText;
	
	private Handler handler = new Handler() {	
		
    public void handleMessage(Message msg) {
		switch (msg.what) {
		case UPDATE_WEATHER:
			queryFromServer();
			break;
		case MORE:
			if (showButtonTips) {	
			    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.menu_layout);
			    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
			    linearParams.height = DensityUtil.dip2px(ShareContext.getContext(), 70);
			    linearLayout.setLayoutParams(linearParams);
			    showButtonTips = false;
			} else {
			    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.menu_layout);
			    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
			    linearParams.height = DensityUtil.dip2px(ShareContext.getContext(), 45);
			    linearLayout.setLayoutParams(linearParams);
			    showButtonTips = true;
			}
			break;
		case SELECT_CITY:
//			Log.d("DevDebug", "select city");
			try {
		        JSONObject jsonObject = new JSONObject(msg.getData().getString("cityJsonData"));
		        if (jsonObject.getInt("errNum") == 0) {
			        Intent intent = new Intent(DisplayWeatherActivity.this, CitySelectActivity.class);
			        intent.putExtra("cityJsonData", msg.getData().getString("cityJsonData"));
			        startActivityForResult(intent, 1);
		        } else {
					Toast.makeText(ShareContext.getContext(), "没有找到对应的城市", Toast.LENGTH_SHORT).show();
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}			
			break;
		case NETWORK_ERROR:
		    runOnUiThread(new Runnable() {
		    	@Override
		    	public void run() {
					Toast.makeText(ShareContext.getContext(), "网络异常", Toast.LENGTH_SHORT).show();
		    	}
		    });
			break;
		case CITYTEXT_EMPTY:
		    runOnUiThread(new Runnable() {
		    	@Override
		    	public void run() {
					Toast.makeText(ShareContext.getContext(), "请输入城市名称", Toast.LENGTH_SHORT).show();
		    	}
		    });
			break;
		case UPDATE_SUCCESS:
		    runOnUiThread(new Runnable() {
		    	@Override
		    	public void run() {
					Toast.makeText(ShareContext.getContext(), "更新成功", Toast.LENGTH_SHORT).show();
		    	}
		    });
			break;
		default:
			break;
		    }
		}
	};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		ShareHandler.setHandler(handler);
		backgroundLinearLayout = (LinearLayout) findViewById(R.id.weather_background);
		animation = AnimationUtils.loadAnimation(this, R.layout.animation);
		animation.setFillAfter(true);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_time);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		weatherDespImage = (ImageView) findViewById(R.id.weather_how);
		tempText = (TextView) findViewById(R.id.temp);
		templText = (TextView) findViewById(R.id.temp_l);
		temphText = (TextView) findViewById(R.id.temp_h);
		SharedPreferences cityData = getSharedPreferences("cityData", MODE_PRIVATE);
		cityId = cityData.getString("cityId", "101280601");
		queryFromServer();		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				TextView searchCityNameText = (TextView) findViewById(R.id.search_city_name);
				searchCityNameText.setText("");
				searchCityNameText.clearFocus();
				String cityId = data.getStringExtra("cityId");
				SharedPreferences.Editor cityDataEditor = getSharedPreferences("cityData",
						MODE_PRIVATE).edit();
				cityDataEditor.putString("cityId", cityId);
				cityDataEditor.commit();
				this.cityId = cityId;
				queryFromServer();
			}
			break;
		}
	}
	
	private void queryFromServer() {
		String httpUrl = "http://apis.baidu.com/apistore/weatherservice/cityid";
		String httpArg = "cityid=" + cityId;
		//showProgressDialog();
		HttpUtil.sendHttpRequest(httpUrl, httpArg, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				try {
				    JSONObject weatherInfo = new JSONObject(response).getJSONObject("retData");
		            final String publishTime = weatherInfo.getString("date") + " " + weatherInfo.getString("time");
				    final String cityName = weatherInfo.getString("city");
				    final String tmp = weatherInfo.getString("temp");
		            final String tmpl = weatherInfo.getString("l_tmp");
		            final String tmph = weatherInfo.getString("h_tmp");
		            final String weatherDesp = weatherInfo.getString("weather");
		            //closeProgressDialog();
				    runOnUiThread(new Runnable() {
				    	@Override
				    	public void run() {
				    		if (weatherDesp.indexOf("暴雨") != -1) {
				    			backgroundLinearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.rainstorm));				  
				    			weatherDespImage.setImageDrawable(getResources().getDrawable(R.drawable.weather_rain));
				    		} else if (weatherDesp.indexOf("雨") != -1) {
				    			backgroundLinearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.rain));
				    			weatherDespImage.setImageDrawable(getResources().getDrawable(R.drawable.weather_rain));
				    		} else if (weatherDesp.indexOf("阴") != -1) {
				    			backgroundLinearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.overcast));
				    			weatherDespImage.setImageDrawable(getResources().getDrawable(R.drawable.weather_overcast));
				    		} else if (weatherDesp.indexOf("晴") != -1 || weatherDesp.indexOf("多云") != -1) {
				    			backgroundLinearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.sunshine));
				    			weatherDespImage.setImageDrawable(getResources().getDrawable(R.drawable.weather_sun));
				    		} else if (weatherDesp.indexOf("雪") != -1) {
			    			    backgroundLinearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.snow));
			    			    weatherDespImage.setImageDrawable(getResources().getDrawable(R.drawable.weather_snow));
			    		    } else {
				    			backgroundLinearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.sunshine));
				    			weatherDespImage.setImageDrawable(getResources().getDrawable(R.drawable.weather_sun));
				    		}
				    		backgroundLinearLayout.startAnimation(animation);
				    		publishText.setText("最后更新时间 " + publishTime);
				    		cityNameText.setText(cityName);
				    		tempText.setText(tmp + "℃");
						    templText.setText("最低气温 " + tmpl + "℃");
				            temphText.setText("最高气温 " + tmph + "℃");
				            weatherDespText.setText(weatherDesp);
							Message message = new Message();
							message.what = DisplayWeatherActivity.UPDATE_SUCCESS;
							ShareHandler.getHandler().sendMessage(message);
				    	}
				    });
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onError(Exception e) {
				//closeProgressDialog();
				Message message = new Message();
				message.what = DisplayWeatherActivity.NETWORK_ERROR;
				ShareHandler.getHandler().sendMessage(message);
				e.printStackTrace();
			}
		});
	}
	
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
}
