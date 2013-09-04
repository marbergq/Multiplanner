package com.berka.multiplanner.Models.autocomplete;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.berka.multiplanner.Models.Abstraction.AbstractStop;
import com.berka.multiplanner.Models.Interface.IStops;
import com.berka.multiplanner.Models.Trips.Location;

public class Suggestions implements IStops{

	List<AbstractStop> stops;
	public List<AbstractStop> getStops() {
		return stops;
	}
	public Suggestions(String responseString) throws JSONException
	{
		
	stops = new ArrayList<AbstractStop>();
		
		
			responseString=	responseString.replaceAll("SLs.sls=", "");
			responseString=	responseString.replaceAll(";SLs.showSuggestion();", "");
			JSONObject object = new JSONObject(responseString);
			JSONArray array = object.getJSONArray("suggestions");
			
			for(int i = 0; i < array.length(); i++)
			{
				try{
				stops.add(new Stop(array.getJSONObject(i)));
				}catch(JSONException ex){}
			}
			
			
		
	}
	public Suggestions(JSONObject object) {
		stops = new ArrayList<AbstractStop>();
		
		try{
		
			JSONArray array = object.getJSONArray("suggestions");
			
			for(int i = 0; i < array.length(); i++)
			{
				try{
				stops.add(new Stop(array.getJSONObject(i)));
				}catch(JSONException ex){}
			}
			
			
		}catch(JSONException e)
		{
			
		}
		
		
		// TODO Auto-generated constructor stub
	}

}
