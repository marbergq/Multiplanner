package com.berka.multiplanner.Factories;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.berka.multiplanner.StaticItems;
import com.berka.multiplanner.Factories.Interface.IURL;
import com.berka.multiplanner.Models.Trips.Location;
import com.berka.multiplanner.Planner.Planner;

public class URLFactory implements IURL{



	final String findLocationBaseUrl= "http://reseplanerare.resrobot.se/bin/ajax-getstop.exe/sny?L=vs_resrobot&REQ0JourneyStopsS0A=255&REQ0JourneyStopsB=12&S=";
	public static final String searchUrl = "https://api.trafiklab.se/samtrafiken/resrobot/Search.json?" +
			"apiVersion=2.1&coordSys=RT90" +"&key="+StaticItems.APIKEY;
	
	
	public HttpClient getClient() {
		return new DefaultHttpClient();
	}

	public HttpRequest makeRequest(int requestType,Object[] parameters) throws HttpException {
		if(requestType == AUTOCOMPLETE)
		{
			try {
				String url = findLocationBaseUrl+URLEncoder.encode((String) parameters[0], "UTF-8")+"?&js=true&";
				 return new HttpGet(url);
			} catch (UnsupportedEncodingException e) {
		
				throw new HttpException("NO REQUESTTYPE SPECIFIED OR ILLEGAL");
			}
			
		}
		else if(requestType == SEARCH)
		{
			
			Location location = (Location) parameters[0]; 
			
			Planner plan = (Planner)((Planner[])parameters[1])[0];
			String requestUrl = searchUrl
					+ "&fromId="
					+ location.getLocationid().intValue()
					+ "&toId="
					+ plan.getTo().getLocationid().intValue() + "&date="
					+ plan.getDateString() + "&time="
					+ plan.getTimeString()
					+ "&arrival=true" + "&searchType=F";
			
			return new HttpGet(requestUrl);
			
		}
		else throw new HttpException("NO REQUESTTYPE SPECIFIED OR ILLEGAL");
			
	}


	
}
