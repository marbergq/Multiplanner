package com.berka.multiplanner.Helpers;

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

public class FrequentlySearched implements ISaveTrips {

	private final Context context;
	private Set<ILocation> locations;
	private static FrequentlySearched me = null;
	
	private FrequentlySearched(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	
	
	

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof ILocation)
			saveStop((ILocation)arg1);
		
	}

	@Override
	public void saveStop(final ILocation stop) {
		new Thread(){
			@Override
			public void run()
			{
				Editor p = context.getSharedPreferences("SAVED",Activity.MODE_PRIVATE).edit();
				
				p.putString(stop.getLocationid().toString(), stop.getTheJSONBluePrint().toString());
				p.commit();
			}
		}.start();
		
	}

	@Override
	public Set<ILocation> getStops() {
		if(locations != null && locations.size() != 0) 
			return locations;
		
		locations = new HashSet<ILocation>();
			
			try {
			SharedPreferences  p = context.getSharedPreferences("SAVED", Activity.MODE_PRIVATE);
			Map<String,String> map = (Map<String,String>)p.getAll();
			
			for(String x : map.values())		
					locations.add(new Location(new JSONObject(x)));
			
		} catch (JSONException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
		return locations;
}




	public static ISaveTrips getInstance(Context context) {
		if(me==null)
			me = new FrequentlySearched(context);
		return me;	
	}
	public static ISaveTrips getInstance() throws Exception {
		if(me==null)
			throw new Exception("Not instanciated!");
		return me;	
	}
}

