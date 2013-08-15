
package com.berka.multiplanner.Models.Trips;

import org.json.JSONException;
import org.json.JSONObject;

public class Findlocationresult{
   	private From from;
   	private To to;
   public	Findlocationresult(JSONObject object) throws JSONException {
   		object = object.getJSONObject("findlocationresult");
	   	from = new From(object.getJSONObject("from"));
   		
   		try{
   			to = new To(object.getJSONObject("to"));
   		}catch(Exception e)
   		{
   			to = null;
   		}
   	}
   	
 	public From getFrom(){
		return this.from;
	}
	public void setFrom(From from){
		this.from = from;
	}
 	public To getTo(){
		return this.to;
	}
	public void setTo(To to){
		this.to = to;
	}
}
