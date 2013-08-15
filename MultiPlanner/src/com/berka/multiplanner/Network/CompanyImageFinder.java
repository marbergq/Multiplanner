package com.berka.multiplanner.Network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.berka.multiplanner.APIKEY;

public class CompanyImageFinder extends AsyncTask<Integer, Integer, Bitmap> {

	private static final String requestUrl ="https://api.trafiklab.se/samtrafiken/resrobot/ProducerList.json?apiVersion=2.1&key="+APIKEY.APIKEY;
	@Override
	protected Bitmap doInBackground(Integer... arg0) {
		// TODO Auto-generated method stub
		try {
		DefaultHttpClient client = new DefaultHttpClient();
		Log.i("Trying to get image for company", "CompanyId: "+ arg0[0]);
		HttpGet request = new HttpGet(requestUrl);
		
		
			JSONObject obj = new JSONObject(EntityUtils.toString(client.execute(request).getEntity()));
			obj = obj.getJSONObject("producerlistresult");
			JSONArray producers = obj.getJSONArray("producer");
			
			JSONObject result = null;
			for(int i = 0; i < producers.length(); i++)
			{
				JSONObject buff = producers.getJSONObject(i);
				if(buff.getString("@id").equals(arg0[0].toString()))
				{
					Log.i("found companyID!", "");
					result = buff;
					break;
				}
			}
			
			if(result != null)
			{
				String url = result.getString("logourl");
				Log.i("found companyUrl!", "");
				HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();  
							
				  InputStream is = connection.getInputStream();  
				  Bitmap bitmap = BitmapFactory.decodeStream(is,null,null);
				
				  return bitmap;
				
				
			}
			
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}



}
