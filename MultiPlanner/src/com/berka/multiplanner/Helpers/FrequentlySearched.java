package com.berka.multiplanner.Helpers;

import java.io.NotActiveException;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.berka.multiplanner.Helpers.Interface.ISaveTrips;
import com.berka.multiplanner.Models.Interface.ILocation;
import com.berka.multiplanner.Models.Trips.Location;

public class FrequentlySearched implements Observer,ISaveTrips {

	private final Context context;
	public FrequentlySearched(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveStop(final ILocation stop) {
		Thread t = new Thread(){
			@Override
			public void run()
			{
				Editor p = context.getSharedPreferences("SAVED",Activity.MODE_PRIVATE).edit();
				
				p.putString(stop.getLocationid().toString(), stop.getTheJSONBluePrint().toString());
				p.commit();
			}
		};
		
	}

	@Override
	public Set<ILocation> getStops() {
		return null;
//		final Set<ILocation> locations = new HashSet<ILocation>();
//		Thread t = new Thread(){
//			
//		@Override public void run(){
//			try {
//			SharedPreferences  p = context.getSharedPreferences("SAVED", Activity.MODE_PRIVATE);
//			Map<String,String> map = (Map<String,String>)p.getAll();
//			
//			for(String x : map.values())		
//					locations.add(new Location(new JSONObject(x)));
//			
//		} catch (JSONException e) {
//			
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	}
//		return locations;
}
}

