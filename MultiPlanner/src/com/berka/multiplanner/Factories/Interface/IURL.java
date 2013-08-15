package com.berka.multiplanner.Factories.Interface;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;

public interface IURL {
	
	public static final int AUTOCOMPLETE=0,SEARCH = 1;
	HttpClient getClient();
	HttpRequest makeRequest(int requestType,Object[] Parameters) throws HttpException;
	
}
