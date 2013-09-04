package com.berka.multiplanner.Models.Interface;

import org.json.JSONObject;

public interface ILocation {

	
String getx(String x);
String gety(String y);
Boolean getBestmatch();
String getDisplayname();
Number getLocationid();
String getType();
JSONObject getTheJSONBluePrint();
@Override
String toString();
}
