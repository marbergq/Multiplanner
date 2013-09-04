package com.berka.multiplanner.Models.Trips;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.text.format.DateFormat;
import android.util.Log;

import com.berka.multiplanner.Helpers.TimeFilter;
import com.berka.multiplanner.Models.Travel.Segment;

public class Trip implements Comparable<Trip>{

	String ankomstTid;
	List<List<Segment>> segments;
	String to;
	Calendar ankomstTidCalendar;
	public String getAnkomstTid() {
		return ankomstTid;
	}
	
	public Calendar getAnkomstTidCalendar() throws ParseException
	{
		if(ankomstTidCalendar!=null)
			return ankomstTidCalendar;
		else
		ankomstTidCalendar= TimeFilter.getDateFromString(getAnkomstTid());
		return ankomstTidCalendar;
	}
	
	public String getAnkomstTID() throws ParseException
	{
		if(ankomstTidCalendar== null)
			getAnkomstTidCalendar();
		return ankomstTidCalendar.get(Calendar.HOUR_OF_DAY)+":"+
				ankomstTidCalendar.get(Calendar.MINUTE);
		
	}
	
	public String getAnkomstDatum() throws ParseException
	{
		if(ankomstTidCalendar== null)
			getAnkomstTidCalendar();
		return ankomstTidCalendar.get(Calendar.YEAR)+
				"-"+ankomstTidCalendar.get(Calendar.MONTH)+
				"-"+ankomstTidCalendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public void setAnkomstTid(String ankomstTid) {
		this.ankomstTid = ankomstTid;
	}
	
	public List<List<Segment>> getSegments() {
		return segments;
	}
	
	public static Integer getTripTimeHours(List<Segment> segs)
	{
		try {
			Calendar StartTime = TimeFilter.getDateFromString(segs.get(0).getDeparture().getDatetime());
			
			Calendar endTime = TimeFilter.getDateFromString(segs.get(segs.size()-1).getArrival().getDatetime());
			
			long diff = (endTime.getTime().getTime()-StartTime.getTime().getTime());
			
			Log.d("Diff (MS): ", ""+diff+" MS");
			
			return (int)((diff/(60*1000*60)));
		}catch(Exception e)
		{
			return 0;
		}
	}
	
	public static Integer getTripTimeMin(List<Segment> segs)
	{
		try {
			Calendar StartTime = TimeFilter.getDateFromString(segs.get(0).getDeparture().getDatetime());
			
			Calendar endTime = TimeFilter.getDateFromString(segs.get(segs.size()-1).getArrival().getDatetime());
			
			long diff = (endTime.getTime().getTime()-StartTime.getTime().getTime());
			
			Log.d("Diff (MS): ", ""+diff+" MS");
			
			return (int)(
					(diff/(60  * 1000))>59?
							((diff/(60  * 1000))%60)
							:(diff/(60  * 1000))
						);
		}catch(Exception e)
		{
			return 0;
		}
	}
	
	public static Calendar getTripTime(List<Segment> segs)
	{
		try {
		Calendar StartTime = TimeFilter.getDateFromString(segs.get(0).getDeparture().getDatetime());
		
		Calendar endTime = TimeFilter.getDateFromString(segs.get(segs.size()-1).getArrival().getDatetime());
		
		long diff = (endTime.getTime().getTime()-StartTime.getTime().getTime());
		
		Log.d("Diff (MS): ", ""+diff+" MS");
		
		 Date d = new Date(diff);
		 
		 Calendar k=  Calendar.getInstance();
		 k.set(Calendar.HOUR_OF_DAY, 0);
		 k.set(Calendar.MINUTE, 0);
		 k.set(Calendar.SECOND,1);
		 k.add(Calendar.MINUTE, (int)(diff/(60 * 1000)));
		// k.setTime(d);
		 return k;
		 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public void addSegments(List<Segment> x) {
		if(this.segments == null)
			this.segments = new ArrayList<List<Segment>>();
		this.segments.add(x);
	}
	public void addAllSegments(Collection<List<Segment>> segmentsSegs)
	{
		for(List<Segment> x : segmentsSegs)
			addSegments(x);
	}
	public String getTo() {
		return segments.get(0).get(segments.get(0).size()-1).getArrival().getLocation().getDisplayname();
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	

	


	@Override
	public int compareTo(Trip arg0) {
		
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.ENGLISH);
			
			Date date = formatter.parse(arg0.getAnkomstTid());
			
			Date thisdate = formatter.parse(this.getAnkomstTid());
			
			Calendar other = new GregorianCalendar();
			other.setTime(date);
			
			Calendar thisC = new GregorianCalendar();
			thisC.setTime(thisdate);
			
			if(thisC.after(other))
				return 1;
			else if(thisC.before(other))
				return -1;
			else
				return 0;
			
		}catch(Exception e){
			Log.e("Sorting","Couldn't sort",e);
			return 0;
			
			
		}
		
		
	}
}
