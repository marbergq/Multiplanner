package com.berka.multiplanner.Models.Abstraction;

import org.json.JSONObject;

import com.berka.multiplanner.Models.Interface.ILocation;

public abstract class AbstractStop implements ILocation{

   	public void setX(String x) {
		this.x = x;
	}

	public void setY(String y) {
		this.y = y;
	}

	public void setBestmatch(String bestmatch )
	{
		try{
			setBestmatch(Boolean.parseBoolean(bestmatch));
		}catch(Exception ex){
			setBestmatch(false);
		}
		
	}
	public void setBestmatch(Boolean bestmatch) {
		this.bestmatch = bestmatch;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public void setLocationid(Number locationid) {
		this.locationid = locationid;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTheAllmightyJSON(JSONObject theAllmightyJSON) {
		this.theAllmightyJSON = theAllmightyJSON;
	}

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
   	
	@Override
	public String getx(String x) {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public String gety(String y) {
		// TODO Auto-generated method stub
		return y;
	}

	@Override
	public Boolean getBestmatch() {
		// TODO Auto-generated method stub
		return bestmatch;
	}

	@Override
	public String getDisplayname() {
		// TODO Auto-generated method stub
		return displayname;
	}

	@Override
	public Number getLocationid() {
		// TODO Auto-generated method stub
		return locationid;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}
	
	@Override 
	public String toString()
	{
		return getDisplayname();
	}

}
