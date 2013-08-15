package com.berka.multiplanner.Network;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.berka.multiplanner.Factories.AbstractFactory;
import com.berka.multiplanner.Factories.Interface.IURL;
import com.berka.multiplanner.Models.Interface.IStop;

public class AutoComplete extends AsyncTask<String, Integer, IStop> {
	
	HttpGet request;
	Boolean run = false;
	DefaultHttpClient client;
	@Override
	protected IStop doInBackground(String... arg0) {
	
		try {
			
			run = true;
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
	protected void onCancelled(IStop res)
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

