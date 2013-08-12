package com.berka.multiplanner.Models.Travel;

import org.json.JSONException;
import org.json.JSONObject;

public class SegmentId {

	public SegmentId(JSONObject jsonObject) throws JSONException {

		mot = new Mot(jsonObject.getJSONObject("mot"));

		try{
			carrier = new Carrier(jsonObject.getJSONObject("carrier"));
		}catch(Exception e){}
		try{
			distance = jsonObject.getDouble("distance");
		}catch(Exception E){}
	}
	
	public Carrier getCarrier() {
		return carrier;
	}
	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}

	Carrier carrier;
	
	
	public Mot getMot() {
		return mot;
	}
	public void setMot(Mot mot) {
		this.mot = mot;
	}
	public Number getDistance() {
		return distance;
	}
	public void setDistance(Number distance) {
		this.distance = distance;
	}
	private Mot mot;
	private Number distance;

	@Override
	public int hashCode()
	{
		int hash = 1;
		hash = hash*17 + (mot == null? 0:mot.hashCode());
		hash = hash * 31 + distance.hashCode();
		 hash = hash *13 + (carrier == null? 0 : carrier.hashCode());
	return hash;
	}

	@Override
	public boolean equals(Object o) {

		if(o instanceof SegmentId)
		{
			SegmentId another = (SegmentId)o;
			SegmentId me = this;
			if(another == me)
				return true;
			else 
			{
				return me.mot.equals(another.mot);
			}
		}
		return false;
	}
	
	
	
}
