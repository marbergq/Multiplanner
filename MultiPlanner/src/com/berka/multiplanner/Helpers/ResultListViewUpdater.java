package com.berka.multiplanner.Helpers;

import java.text.ParseException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.berka.multiplanner.Helpers.Interface.IResult;
import com.berka.multiplanner.Helpers.Logic.MultipleResult;
import com.berka.multiplanner.Models.ListViewUpdaterModel;
import com.berka.multiplanner.Models.Trips.Trip;

public class ResultListViewUpdater extends AsyncTask<ListViewUpdaterModel, Integer, List<Trip>> {
	private final IResult result;
	
	public ResultListViewUpdater(IResult result)
	{
		this.result=result;
	}
	
	@Override
	protected List<Trip> doInBackground(ListViewUpdaterModel... params) {
		// TODO Auto-generated method stub
		final ListViewUpdaterModel param = params[0];
		final List<Trip> trips = Collections.synchronizedList(new LinkedList<Trip>());
		try {
			Thread t = new Thread(){
				@Override
				public void run(){
					List<Trip> tripss;
					try {
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
			Thread t2 = new Thread(){
				@Override
				public void run(){
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
						else
							try {
								sleep(50);
							} catch (InterruptedException e) {
								Log.i("FINISH", "INTERRUPTED:(");
								break;
							}
					}
					
				}
			}
			;
			
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

}
