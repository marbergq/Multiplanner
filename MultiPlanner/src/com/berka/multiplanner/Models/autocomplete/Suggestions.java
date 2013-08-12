package com.berka.multiplanner.Models.autocomplete;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.berka.multiplanner.Models.Location;

public class Suggestions {

	List<Location> stops;
	public List<Location> getStops() {
		return stops;
	}
	public Suggestions(String responseString) throws JSONException
	{
		
	stops = new ArrayList<Location>();
		
		
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
		stops = new ArrayList<Location>();
		
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
