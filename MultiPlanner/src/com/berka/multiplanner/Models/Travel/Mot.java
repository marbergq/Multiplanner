package com.berka.multiplanner.Models.Travel;

import org.json.JSONException;
import org.json.JSONObject;

public class Mot {

	public Mot(JSONObject jsonObject) throws JSONException {
		displayType = jsonObject.getString("@displaytype");
		type = jsonObject.getString("@type");
		text = jsonObject.getString("#text");
	}
	public String getDisplayType() {
		return displayType;
	}
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	private String displayType;
	private String type;
	private String text;
	
	@Override 
	public int hashCode()
	{
		int hash = 1;
		hash = hash * 17 + (displayType==null?0:displayType.hashCode());
		hash = hash * 31 + (type == null?0:type.hashCode());
		hash = hash * 13 + (text == null?0:text.hashCode());
		return hash;
	}
	
	@Override
	public boolean equals(Object o) {
	
		if(o instanceof Mot)
		{
			
			Mot another = (Mot)o;
			Mot me = this;
			
			if(me==another)
				return true;
			
			if( (another.displayType == null && me.displayType != null ) || (another.displayType != null && me.displayType == null)
					)
				return false;
			else if(!another.displayType.equals(me.displayType))
				return false;
			
			if( (another.text == null && me.text != null ) || (another.text != null && me.text == null)
					)
				return false;
			else if(!another.text.equals(me.text))
				return false;
			
			if( (another.type == null && me.type  != null ) || (another.type  != null && me.type  == null)
					)
				return false;
			else if(!another.type.equals(me.type ))
				return false;
				
			return true;
			
		}
		return false;
	}
}
