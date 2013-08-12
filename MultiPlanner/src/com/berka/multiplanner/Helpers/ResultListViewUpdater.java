package com.berka.multiplanner.Helpers;

import java.text.ParseException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.berka.multiplanner.Helpers.Logic.MultipleResult;
import com.berka.multiplanner.Models.ListViewUpdaterModel;
import com.berka.multiplanner.Models.Trip;

public class ResultListViewUpdater extends AsyncTask<ListViewUpdaterModel, Integer, List<Trip>> {
	private final MultipleResult result;
	
	public ResultListViewUpdater(MultipleResult result)
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
					
					if(tripss.size() != 0)
						Collections.sort(tripss);
					trips.addAll(tripss);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
				}
			};
			
			t.start();
			
			t.join();
			
			
			return trips;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

}
