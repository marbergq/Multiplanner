
package com.berka.multiplanner.Models.Trips;

import org.json.JSONException;
import org.json.JSONObject;

public class Location{
   	protected String x;
   	protected String y;
   	protected Boolean bestmatch;
   	protected String displayname;
   	protected Number locationid;
   	protected String type;

   	protected JSONObject theAllmightyJSON;
   	
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
   		x = object.getString("@x");
   		y = object.getString("@y");
   		try{
   			bestmatch = Boolean.parseBoolean(object.getString("bestmatch"));
   		}catch(Exception e)
   		{
   			bestmatch = false;
   		}
   		try{
   		displayname = object.getString("displayname");
   		type = object.getString("type");
   		locationid = object.getDouble("locationid");
   		}catch(Exception e)
   		{
   			try{
   				displayname = object.getString("name");
   				locationid = object.getDouble("@id");
   			}catch(Exception ex){}
   		}
   		theAllmightyJSON = object;
   	}
   	
   	public String getx(){
		return this.x;
	}
	public void setx(String x){
		this.x = x;
	}
 	public String gety(){
		return this.y;
	}
	public void sety(String y){
		this.y = y;
	}
 	public Boolean getBestmatch(){
		return this.bestmatch;
	}
	public void setBestmatch(Boolean bestmatch){
		this.bestmatch = bestmatch;
	}
 	public String getDisplayname(){
		return this.displayname;
	}
	public void setDisplayname(String displayname){
		this.displayname = displayname;
	}
 	public Number getLocationid(){
		return this.locationid;
	}
	public void setLocationid(Number locationid){
		this.locationid = locationid;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
	@Override
	public String toString()
	{
			return this.getDisplayname();
	}
}
