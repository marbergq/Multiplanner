package com.berka.multiplanner.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.berka.multiplanner.APIKEY;
import com.berka.multiplanner.Helpers.Logic.FindLogic;
import com.berka.multiplanner.Helpers.Logic.MultipleResult;
import com.berka.multiplanner.Models.Location;
import com.berka.multiplanner.Models.Travel.Segment;
import com.berka.multiplanner.Models.Travel.Timetableresult;
import com.berka.multiplanner.Planner.Planner;

public class PlanSearchHandler extends AsyncTask<Planner, Integer, MultipleResult> {
	
	public static final String searchUrl = "https://api.trafiklab.se/samtrafiken/resrobot/Search.json?" +
	"apiVersion=2.1&coordSys=RT90" +"&key="+APIKEY.APIKEY;
	
	

	    private int index;
	    private int max;
	    final ProgressBar bar;
	    final TextView text;
	    final List<String> currentSearch;
	    public PlanSearchHandler(ProgressBar bar,TextView text)
	    {
	    	this.bar = bar;
	    	this.text = text;
	    	 currentSearch = Collections.synchronizedList(new ArrayList<String>());
	    }
	    
	    
	@Override
	protected MultipleResult doInBackground(Planner... arg0) {
		// TODO Auto-generated method stub
		try {
			final Planner[] planner = arg0;
			max = planner[0].getFrom().size();
			bar.setProgress(0);
			bar.setMax(max);
			index = 0;
			List<Timetableresult> result = new ArrayList<Timetableresult>();
			final List<Timetableresult> saferesult = Collections.synchronizedList(result);
			
			
			List<Thread> threadOfLists = new ArrayList<Thread>();
			for(Location from : planner[0].getFrom())
				currentSearch.add(from.getDisplayname());
			publishProgress(index);
		for(final Location from : planner[0].getFrom()){
		Thread t = new Thread(){
			String search ="";
			@Override
			public void run(){
				try {
					search=from.getDisplayname();
			DefaultHttpClient client = new DefaultHttpClient();
		
			
			//HttpGet request = new HttpGet(searchUrl+"&fromId="+arg0[0].getFrom().get(0)+"&toId="+arg0[0].getTo()+"&searchType=F");
			String requestUrl = searchUrl+"&fromId="+from.getLocationid().intValue()+"&toId="+planner[0].getTo().getLocationid().intValue()+
					"&date="+planner[0].getDateString()+"&time="+planner[0].getTimeString()+"&arrival=true"+
					"&searchType=F";
			Log.d("Query", requestUrl);
			HttpGet request = new HttpGet(requestUrl);
			String content = readFromStream( client.execute(request).getEntity().getContent() );
				JSONObject obj = new JSONObject(content);
				
				saferesult.add(new Timetableresult(obj));
				publishProgress(++index);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
			threadOfLists.add(t);
			t.start();
		}
		//Wait for all the threads to finish
		for(Thread t : threadOfLists){
			
			t.join();
		}
		for(Thread t : threadOfLists){
			t = null;
		}
		HashMap<String, List<List<Segment>>> re=	FindLogic.getResult(saferesult);
		MultipleResult toReturn = new MultipleResult(re);
	//	publishProgress(++index);
		return toReturn;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e("MARTIIN", "ERROR", e);
			return null;
		}
		

	}
	
	 @Override
	    protected void onProgressUpdate(final Integer... values) {
		 Log.w("Progress", "Value : "+ values[0]);
		 try{
			 text.setText(currentSearch.get(values[0]).toUpperCase(Locale.ROOT));
		 }
		 catch(Exception e){}
		 if(bar!=null)
	        bar.setProgress(values[0]);
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
