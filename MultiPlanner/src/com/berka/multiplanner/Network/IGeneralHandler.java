package com.berka.multiplanner.Network;

import org.json.JSONObject;

public interface IGeneralHandler {

JSONObject	getToAutoCompleteString(String from, String to);
JSONObject getFromAutoCompleteString(String from);	
}
