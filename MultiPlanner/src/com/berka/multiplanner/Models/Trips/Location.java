
package com.berka.multiplanner.Models.Trips;

import org.json.JSONException;
import org.json.JSONObject;

import com.berka.multiplanner.Models.Abstraction.AbstractStop;

public class Location extends AbstractStop{
   	

   	public JSONObject getTheJSONBluePrint()
   	{
   		return theAllmightyJSON;
   	}
   	public Location()
   	{
   		displayname = "Loading...";
   		locationid=-1;
   	}
  
   	
   	public Location(JSONObject object) throws JSONException
   	{
   		setX(object.getString("@x"));
   		setY(object.getString("@y"));
		//setBestmatch(object.getString("bestmatch"));
		//setType(object.getString("type"));
   		setDisplayname(object);
   		setLocationid(object);
   		setTheAllmightyJSON(object);
   	}
   	
   	
   	protected void setDisplayname(JSONObject object) throws JSONException
   	{
   		try{
   			setDisplayname(object.getString("displayname"));
   		}catch(JSONException e)
   		{
   			setDisplayname(object.getString("name"));
   		}
   	}	
   	
   	
   	protected void setLocationid(JSONObject object) throws JSONException
   	{
   		try{
   			setLocationid(object.getDouble("locationid"));
   		}catch(JSONException e)
   		{
   				setLocationid(object.getDouble("@id"));
   		}
   	}
   	
}
