package com.berka.multiplanner.Models.autocomplete;

import org.json.JSONException;
import org.json.JSONObject;

import com.berka.multiplanner.Models.Abstraction.AbstractStop;

public class Stop extends AbstractStop {

	
	public Stop(JSONObject jsonObject) throws JSONException {
		
		if(!jsonObject.getString("type").equals("1"))
			throw new JSONException("not a hlp");
		setY(jsonObject.getString("xcoord"));
		setX(jsonObject.getString("ycoord"));
		setDisplayname( jsonObject.getString("value"));
			if(!displayname.contains(" "))
				throw new JSONException("not a hlp");
		setLocationid( Double.parseDouble(removeStartingZeros(jsonObject
				.getString("extId"))));
		setTheAllmightyJSON(jsonObject);
	}

	private String removeStartingZeros(String string) {
		return string.replaceAll("^[0]+", "");
		
//		
//		StringBuilder builder = new StringBuilder();
//		for (int i = 0; i < string.length(); i++) {
//			if (string.charAt(i) == '0' && i < 3)
//				continue;
//			builder.append(string.charAt(i));
//
//		}
//		return builder.toString();
	}

}
