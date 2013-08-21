package com.berka.multiplanner.Helpers;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;

import com.berka.multiplanner.StaticItems;

public class ExtendedEntityUtils {


	public static String toString(HttpResponse response) throws ParseException, IOException
	{

		HttpEntity entity = response.getEntity();
		String content = convertStreamToString(entity.getContent());
		return content;
	}

	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is,StaticItems.DefaultCharset).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}



}
