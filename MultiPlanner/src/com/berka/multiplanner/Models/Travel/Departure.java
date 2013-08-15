
package com.berka.multiplanner.Models.Travel;

import org.json.JSONException;
import org.json.JSONObject;

import com.berka.multiplanner.Models.Trips.Location;

public class Departure{
   	private String datetime;
   	private Location location;

 	public Departure(JSONObject jsonObject) throws JSONException {
		location = new Location(jsonObject.getJSONObject("location"));
		datetime = jsonObject.getString("datetime");
	}
	public String getDatetime(){
		return this.datetime;
	}
	public void setDatetime(String datetime){
		this.datetime = datetime;
	}
 	public Location getLocation(){
		return this.location;
	}
	public void setLocation(Location location){
		this.location = location;
	}
}
