
package com.berka.multiplanner.Models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class To{
   	private List<Location> location;

   	public To(JSONObject object) throws JSONException
   	{
   		location = new ArrayList<Location>();
   		try
   		{
   			JSONArray array = object.getJSONArray("location");
   			
   			for(int i = 0; i < array.length(); i++)
   			{
   				try{
   				location.add(new Location(array.getJSONObject(i)));
   				}catch(Exception e)
   				{
   					
   				}
   			}
   			
   			
   		}catch(JSONException e)
   		{
   			//object
   			location.add(new Location(object.getJSONObject("location")));
   			
   		}
   	}
   	
 	public List<Location> getLocation(){
		return this.location;
	}
	public void setLocation(List<Location> location){
		this.location = location;
	}
}
