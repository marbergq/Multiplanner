
package com.berka.multiplanner.Models.Travel;

import org.json.JSONException;
import org.json.JSONObject;

public class Segment{
   	private Arrival arrival;
   	private Departure departure;
   	private SegmentId segmentid;
   	private String direction;
   	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}




   	
	public Segment(JSONObject json) throws JSONException {

			
			segmentid = new SegmentId(json.getJSONObject("segmentid"));
			try{
			direction = json.getString("direction");
			}catch(Exception e){}
			departure = new Departure(json.getJSONObject("departure"));
			arrival = new Arrival(json.getJSONObject("arrival"));
					
	}
	
	
	public Arrival getArrival(){
		return this.arrival;
	}
	public void setArrival(Arrival arrival){
		this.arrival = arrival;
	}
 	public Departure getDeparture(){
		return this.departure;
	}
	public void setDeparture(Departure departure){
		this.departure = departure;
	}
 	public SegmentId getSegmentid(){
		return this.segmentid;
	}
	public void setSegmentid(SegmentId segmentid){
		this.segmentid = segmentid;
	}
}
