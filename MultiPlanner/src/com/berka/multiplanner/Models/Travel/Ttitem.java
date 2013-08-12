package com.berka.multiplanner.Models.Travel;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ttitem {
	private List<List<Segment>> segment;

	public Ttitem(JSONArray object) throws JSONException {
		for (int i = 0; i < object.length(); i++) {
			try{
			addASegment(new Segment(object.getJSONObject(i)));
			}catch(JSONException e)
			{
				JSONArray array =object.getJSONArray(i);
				ArrayList<Segment> buff = new ArrayList<Segment>();
				for(int j=0;j<array.length(); j++)
				{
					buff.add(new Segment(object.getJSONObject(i)));
				}
				addArraySegment(buff);
			}
		}
	}

	public Ttitem(JSONObject jsonObject) throws JSONException {
		try{
		JSONArray segments = jsonObject.getJSONArray("segment");
		ArrayList<Segment> buff = new ArrayList<Segment>();
		for (int i = 0; i < segments.length(); i++) {	
			buff.add(new Segment(segments.getJSONObject(i)));
		}
		addArraySegment(buff);
		}catch(JSONException ex)
		{
			JSONObject segment = jsonObject.getJSONObject("segment");
			addASegment(new Segment(segment));
		}
	}
	private Boolean addArraySegment(List<Segment> segment)
	{
		
		if(this.segment == null)
			this.segment = new ArrayList<List<Segment>>();
		return this.segment.add(segment);

	}
	private Boolean addASegment(Segment segment)
	{
		if(this.segment == null)
			this.segment = new ArrayList<List<Segment>>();

		List<Segment> buff = new ArrayList<Segment>();
		buff.add(segment);
		return this.segment.add(buff);
	}

	public List<List<Segment>> getSegment() {
		return this.segment;
	}

	public void setSegment(List<List<Segment>> segment) {
		this.segment = segment;
	}
}
