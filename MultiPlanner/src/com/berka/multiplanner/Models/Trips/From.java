
package com.berka.multiplanner.Models.Trips;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class From{
   	private List<Location> location;

   	public From(JSONObject object) throws JSONException
   	{
   		location = new ArrayList<Location>();
   		try
   		{
   			JSONArray array = object.getJSONArray("location");
   			
   			for(int i = 0,q=0; i < array.length(); i++)
   			{
   				if(q > 7)
   					break;
   				try{
   					location.add(new Location(array.getJSONObject(i)));
   					q++;
   				}catch(Exception e)
   				{
   					//continue
   				}
   			}
   			
   			
   		}catch(JSONException e)
   		{
   			//object not array
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
