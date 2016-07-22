package com.simpleweather.app.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simpleweather.app.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CitySelectActivity extends Activity {
    
    private String[] cityData;
    private String[] cityId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		Intent intent = getIntent();
		try {
		    JSONArray jsonArray = new JSONObject(intent.getStringExtra("cityJsonData")).getJSONArray("retData");
		    cityData = new String[jsonArray.length()];
		    cityId = new String[jsonArray.length()]; 
		    for (int i = 0; i < jsonArray.length(); i++) {
		    	JSONObject jsonObject = jsonArray.getJSONObject(i);
		    	String province = jsonObject.getString("province_cn");
		    	String district = jsonObject.getString("district_cn");
		    	String name = jsonObject.getString("name_cn");
		    	if (province.equals(district)) {
		    	    if (district.equals(name)) {
		    	        cityData[i] =  district + "市";
		    	    } else {
		    		    cityData[i] =  district + "市" + name + "区/县";
		    	    }
		    	} else {
		    	    if (district.equals(name)) {
		    	        cityData[i] =  province + "省" + district + "市";
		    	    } else {
		    		    cityData[i] =  province + "省" + district + "市" + name + "区/县";
		    	    }
		    	}
		    	cityId[i] = jsonObject.getString("area_id");
//		    	Log.d("DevDebug", cityData[i]);
//		    	Log.d("DevDebug", "cityid=" + cityId[i]);
		    }
	    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(
	    			CitySelectActivity.this, android.R.layout.simple_list_item_1, cityData);
	    	ListView listView = (ListView) findViewById(R.id.list_view);
	    	listView.setAdapter(adapter);
	    	listView.setOnItemClickListener(new OnItemClickListener() {
	    		@Override
	    		public void onItemClick(AdapterView<?> parent, View view,
	    				int position, long id) {
	    			Intent intent = new Intent();
	    			intent.putExtra("cityId", cityId[position]);
	    			setResult(RESULT_OK, intent);
	    			finish();
//	    			Log.d("DevDebug", "position = " + position);
//	    			Log.d("DevDebug", "cityid = " + cityId[position]);
	    		}
	    	});
		} catch (Exception e) {
//			Log.d("DevDebug", "jsonArray read error");
			e.printStackTrace();
		}
	}
}
