package com.berka.multiplanner.Network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.berka.multiplanner.Factories.AbstractFactory;
import com.berka.multiplanner.Factories.Interface.IURL;
import com.berka.multiplanner.Models.Abstraction.AbstractStop;
import com.berka.multiplanner.Models.Interface.IStops;
import com.berka.multiplanner.Models.Trips.Location;

public class AutoComplete extends AsyncTask<String, Integer, IStops> {
	
	HttpGet request;
	Boolean run = false;
	DefaultHttpClient client;
	ArrayAdapter<AbstractStop> adapter;
	
	public AutoComplete(){}
	public AutoComplete(Adapter adapter)
	{
		this.adapter=(ArrayAdapter)adapter;
	}
	
	
	
	@Override
	protected void onPostExecute(IStops result) {
		if(result!=null&&adapter!=null){
		adapter.clear();
		adapter.addAll(result.getStops());
		adapter.notifyDataSetChanged();
		}
	}


	@Override
	protected IStops doInBackground(String... arg0) {
	
		try {
		
				client = (DefaultHttpClient) AbstractFactory.getIURLFactory().getClient();
				request = (HttpGet) AbstractFactory.getIURLFactory().makeRequest(IURL.AUTOCOMPLETE, arg0);
				
				if(isCancelled())
					return null;
				
				return AbstractFactory.getModelFactory().createStopsFromResponse(client.execute(request).getEntity());
				
			}
			catch(Exception e)
			{	
				return null;
			}
		
	}
	
	@Override
	protected void onCancelled(IStops res)
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

}

