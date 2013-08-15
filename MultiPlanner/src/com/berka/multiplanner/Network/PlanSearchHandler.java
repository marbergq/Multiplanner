package com.berka.multiplanner.Network;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.berka.multiplanner.Factories.AbstractFactory;
import com.berka.multiplanner.Factories.Interface.IURL;
import com.berka.multiplanner.Helpers.Interface.IResult;
import com.berka.multiplanner.Models.Travel.Timetableresult;
import com.berka.multiplanner.Models.Trips.Location;
import com.berka.multiplanner.Planner.Planner;

public class PlanSearchHandler extends AsyncTask<Planner, Integer, IResult> {
	
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
	protected IResult doInBackground(Planner... arg0) {
		// TODO Auto-generated method stub
		try {
			final Planner[] planner = arg0;
			max = planner[0].getFrom().size();
			bar.setProgress(0);
			bar.setMax(max);
			index = 0;
			List<Timetableresult> result = new ArrayList<Timetableresult>();
			
			final List<Timetableresult> saferesult = Collections
					.synchronizedList(result);

			List<Thread> threadOfLists = new ArrayList<Thread>();
			for (Location from : planner[0].getFrom())
				currentSearch.add(from.getDisplayname());
			publishProgress(index);
			
			for (final Location from : planner[0].getFrom()) {
				Thread t = new Thread() {
					
					@Override
					public void run() {
						try {

							DefaultHttpClient client = (DefaultHttpClient) AbstractFactory.getIURLFactory().getClient();

							// HttpGet request = new
							// HttpGet(searchUrl+"&fromId="+arg0[0].getFrom().get(0)+"&toId="+arg0[0].getTo()+"&searchType=F");
							
							
							HttpGet request = (HttpGet) AbstractFactory.getIURLFactory().makeRequest(IURL.SEARCH, new Object[]{from,planner});
							
							JSONObject obj = new JSONObject(EntityUtils.toString(client.execute(request).getEntity(),HTTP.UTF_8));

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
						} catch (HttpException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				
				threadOfLists.add(t);
				t.start();
			}
			// Wait for all the threads to finish
			for (Thread t : threadOfLists) {
				t.join();
			}
			
			for (Thread t : threadOfLists) {
				t = null;
			}

			// publishProgress(++index);
			return AbstractFactory.getModelFactory().createResultList(saferesult);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
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
	
}
