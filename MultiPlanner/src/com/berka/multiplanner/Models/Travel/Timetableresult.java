
package com.berka.multiplanner.Models.Travel;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Timetableresult{
   	private List<Ttitem> ttitem;

 	public List<Ttitem> getTtitem(){
		return this.ttitem;
	}
	public void setTtitem(List<Ttitem> ttitem){
		this.ttitem = ttitem;
	}
	public Timetableresult(JSONObject object) throws JSONException
	{
		ttitem = new ArrayList<Ttitem>();
		object = object.getJSONObject("timetableresult");

			JSONArray array = object.getJSONArray("ttitem");
			for(int i = 0; i < array.length(); i++)
			{
				ttitem.add(new Ttitem(array.getJSONObject(i)));
			}
	}
}
