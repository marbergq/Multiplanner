package com.berka.multiplanner.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

import com.berka.multiplanner.Models.autocomplete.Suggestions;

public class AutoComplete extends AsyncTask<String, Integer, Suggestions> {

	//final String findLocationBaseUrl = "https://api.trafiklab.se/samtrafiken/resrobot/FindLocation.json?apiVersion=2.1&coordSys=WGS84&key="+APIKEY;
	final String findLocationBaseUrl= "http://reseplanerare.resrobot.se/bin/ajax-getstop.exe/sny?L=vs_resrobot&REQ0JourneyStopsS0A=255&REQ0JourneyStopsB=12&S=";
	HttpGet request;
	Boolean run = false;
	DefaultHttpClient client;
	@Override
	protected Suggestions doInBackground(String... arg0) {
	
		try {
			
			try {
				run = true;
				client = new DefaultHttpClient();
				String url = findLocationBaseUrl+URLEncoder.encode(arg0[0], "UTF-8")+"?&js=true&";
				Log.d("autocomp", url);
				request = new HttpGet(url);
				
				InputStream stream = client.execute(request).getEntity().getContent();
				if(isCancelled())
					return null;
				String content = readFromStream( stream );
				Log.d("autocomp", content);
				return new Suggestions(content);
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e("Martinerror", "error",e);
				return null;
			}
			catch(Exception e)
			{	Log.e("Martinerror", "error",e);
			
				return null;
			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		
	}
	
	@Override
	protected void onCancelled(Suggestions res)
	{
		if(request != null){
			Log.d("Cancel", "true");
		request.abort();
		run = false;
		}
	}
	
	public void cancelRequest()
	{
		try{
			if(request != null)
					request.abort();
		}catch(Exception e){}
		Log.d("Cancel", "REQUEST CANCELD");
	}
	
	private String readFromStream(final InputStream stream) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuffer buff = new StringBuffer();
		String buffstring = "";
		while((buffstring = reader.readLine()) != null)
		{
			buff.append(buffstring);
		}
		reader.close();
		
		return buff.toString();
	}

}

