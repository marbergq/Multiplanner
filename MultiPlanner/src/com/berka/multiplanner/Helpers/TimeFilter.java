package com.berka.multiplanner.Helpers;

import java.io.NotActiveException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.util.Log;

import com.berka.multiplanner.Models.Trip;
import com.berka.multiplanner.Models.Travel.Segment;
import com.berka.multiplanner.Planner.Planner;

public class TimeFilter {

	public static  List<Trip> filter(List<Trip> trips,int intervall) throws NotActiveException {
	
		
		throw new NotActiveException();
	
		
	}

	private static Calendar getMinMaxDate(String arrivalTime,int minMaxMinute,boolean isMaxRequest) throws ParseException
	{
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.ENGLISH);
		//Log.i("Time to parse", arrivalTime);
		Date arrivalDate = dateFormatter.parse(arrivalTime);
		
		Calendar c = new GregorianCalendar();
		c.setTime(arrivalDate);
	
		if(isMaxRequest)
			c.add(Calendar.MINUTE, minMaxMinute);
		else
			c.add(Calendar.MINUTE, -minMaxMinute);
		
		return c;
		
	}
	
	public static Calendar getDateFromString(String time) throws ParseException
	{
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.ENGLISH);
		Date arrivalDate = dateFormatter.parse(time);
		
		Calendar c = new GregorianCalendar();
		c.setTime(arrivalDate);
		return c;
	}
	
	
	public static Boolean isTimeWithinRange(String favTime,String otherTime,int interval) throws ParseException
	{
		Calendar optimalTime = getDateFromString(favTime);
		Calendar plusTime = getDateFromString(favTime);
			plusTime.add(Calendar.MINUTE, interval);
		Calendar minusTime = getDateFromString(favTime);
			minusTime.add(Calendar.MINUTE, -interval);
		Calendar timeToTest = getDateFromString(otherTime);
		
		if(minusTime.before(timeToTest)&&plusTime.after(timeToTest))
			return true;
		return false;
		
		
		
	}
	
	public static Set<String> filterTrips(HashMap<String, List<List<Segment>>> map,
			Planner planner) {
		try{
		int timeSpaan = planner.getAnkomstIntervall();
		String arrivalTime = planner.getArrivalTime();
		
		Calendar maxTime = getMinMaxDate(arrivalTime, timeSpaan, true);
		Calendar minTime = getMinMaxDate(arrivalTime, timeSpaan, false);
		
		Set<String> times = new HashSet<String>();
		
		for(String x : map.keySet())
		{
			Calendar buffCal = getDateFromString(x);
			
			if(buffCal.after(minTime) && buffCal.before(maxTime))
				times.add(x);
			
		}

		return times;
		}catch(Exception e)
		{
			Log.w("MULTIPLANNER TIMEPARSE EXCEPTION", "Couldn't Parse: " +planner.getArrivalTime(),e);
			return map.keySet();
		}
	}

}
