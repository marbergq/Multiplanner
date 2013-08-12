 package com.berka.multiplanner.Models.Travel;

import org.json.JSONException;
import org.json.JSONObject;

public class Carrier {

	private String name;
	public Carrier(JSONObject jsonObject) throws JSONException {
		name = jsonObject.getString("name");
		url = jsonObject.getString("url");
		id = jsonObject.getDouble("id");
		number = jsonObject.getDouble("number");
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Number getId() {
		return id;
	}
	public void setId(Number id) {
		this.id = id;
	}
	public Number getNumber() {
		return number;
	}
	public void setNumber(Number number) {
		this.number = number;
	}
	private String url;
	private Number id;
	private Number number;
	
}
