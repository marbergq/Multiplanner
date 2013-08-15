package com.berka.multiplanner.Models.autocomplete;

import org.json.JSONException;
import org.json.JSONObject;

import com.berka.multiplanner.Models.Trips.Location;

public class Stop extends Location {

	
	public Stop(JSONObject jsonObject) throws JSONException {
		
		if(!jsonObject.getString("type").equals("1"))
			throw new JSONException("not a hlp");
		x = jsonObject.getString("xcoord");
		y = jsonObject.getString("ycoord");
		displayname = jsonObject.getString("value");
			if(!displayname.contains(" "))
				throw new JSONException("not a hlp");
		locationid = Double.parseDouble(removeStartingZeros(jsonObject
				.getString("extId")));
		theAllmightyJSON = jsonObject;
	}

	private String removeStartingZeros(String string) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '0' && i < 3)
				continue;
			builder.append(string.charAt(i));

		}
		return builder.toString();
	}

}
