package com.berka.multiplanner.Helpers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.berka.multiplanner.Adapters.ListResultViewAdapter;
import com.berka.multiplanner.Helpers.Interface.IResult;
import com.berka.multiplanner.Models.ListViewUpdaterModel;
import com.berka.multiplanner.Models.Trips.Trip;

public class ResultListViewUpdater extends AsyncTask<ListViewUpdaterModel, Integer, List<Trip>> {
	private final IResult result;
	private ListResultViewAdapter adapter;
	private View noResult,loading;
	
	private ResultListViewUpdater(IResult result)
	{
		this.result=result;
	}
	
	
	
	
	public ResultListViewUpdater(IResult result, ListResultViewAdapter adapter,
			ListViewUpdaterModel model, View noResult, View loading) {
		// TODO Auto-generated constructor stub
		this.result=result;this.adapter=adapter;this.noResult=noResult;this.loading=loading;
	}


	private void endListLoad()
	{
		Log.i("endListLoad", "");
		loading.setVisibility(View.INVISIBLE);
		noResult.setVisibility(View.INVISIBLE);
		displayNoResults();
	}
	
	private void displayNoResults()
	{
		Log.i("ADAPTER MSG", "Adapter Count: "+adapter.getCount());
		if(adapter.getCount()==0)
			noResult.setVisibility(View.VISIBLE);
		else
			noResult.setVisibility(View.GONE);
		
	}
	
	@Override
	protected void onPostExecute(List<Trip> resultz){
		if(result != null){
			
			adapter.addAll(resultz);
			adapter.notifyDataSetChanged();
			
			}
		endListLoad();
	}

	private void startListLoad()
	{
		Log.i("startListLoad", "");
		
		noResult.setVisibility(View.INVISIBLE);
		if(loading.getVisibility() != View.VISIBLE)
			loading.setVisibility(View.VISIBLE);
		
	}
	@Override
	protected void onPreExecute()
	{
		startListLoad();
	}
	
	@Override
	protected List<Trip> doInBackground(ListViewUpdaterModel... params) {
		// TODO Auto-generated method stub
		final ListViewUpdaterModel param = params[0];
		final List<Trip> trips = Collections.synchronizedList(new LinkedList<Trip>());
		try {
			Thread t =
					new Thread(){
				@Override
				public void run(){
					List<Trip> tripss;
					try {
						if(param.getPlanner().getFrom().size()==1)
							tripss= result.getSingleTripsResult(param.getPlanner());
						else
							tripss = result.getTrips(param.getPlanner(), param.getOnlySelectedStops(),param.getOnlyFastest());
					if(tripss==null)
						return;
					if(tripss.size() != 0)
						Collections.sort(tripss);
					trips.addAll(tripss);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
				}
			};
			Thread t2 = getCheckerThread(param, trips);
			
			//start the first thread
			t.start();
			//start the isCancelled thread
			t2.start();
			//wait for it 
			t2.join();
			//kill the loading thread
			if(t.isAlive())
				t.interrupt();
			t=null;
			t2=null;
			
			Log.i("trips size: ", trips.size()+"");
			
			//t.join();
			
			
			return trips;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	private Thread getCheckerThread(final ListViewUpdaterModel param,  final List<Trip> trips)
	{
		return new Thread(){
			@Override
			public void run(){
				int maxTests = param.getPlanner().getFrom().size()*30;
				int testNr=0;
				while(true)
				{
					if(trips.size()>0){
						Log.i("FINISH", "ALLDONE");
						break;
					}
					else if(isCancelled()){
						Log.i("FINISH", "cancel");
						break;
					}
					else if(testNr>=maxTests)
					{
						Log.i("ResultListView", "TIMEOUT ");
						break;
					}
					else
						try {
							sleep(50);
							testNr++;
						} catch (InterruptedException e) {
							Log.i("FINISH", "INTERRUPTED:(");
							break;
						}
				}
				
			}
		}
		;
	}

}
