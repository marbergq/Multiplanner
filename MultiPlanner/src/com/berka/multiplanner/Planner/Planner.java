package com.berka.multiplanner.Planner;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import com.berka.multiplanner.Models.Location;

public class Planner extends Observable{
	String testString;
	List<Location> from;
	String arrivalTime;
	int Year;
	int day;
	int month;
	int hour;
	int minute;
	protected boolean hasChanged=false;
	Location to;
	int ankomstIntervall;
	
	public int getAnkomstIntervall() {
		return ankomstIntervall;
		
	}
	public void setAnkomstIntervall(int ankomstIntervall) {
		this.ankomstIntervall = ankomstIntervall;
		setChanged();
		notifyObservers(this.getAnkomstIntervall());
	}
	
	public Boolean isDateSet()
	{
		if (Year == 0)
			return false;
		if(month == 0)
			return false;
		if(day == 0)
			return false;
		return true;
	}
	
	@Override
	public boolean hasChanged()
	{
		return hasChanged;
	}
	
	@Override
	public void setChanged()
	{
		hasChanged=true;
	}
	
	@Override 
	public void clearChanged()
	{
		hasChanged=false;
	}
	
	public Planner()
	{
		Calendar c = Calendar.getInstance();
		Year = c.get(Calendar.YEAR);
		month = getMonth(c.get(Calendar.MONTH));
		day = c.get(Calendar.DAY_OF_MONTH);
		this.ankomstIntervall = 10;
	}
	private int getMonth(int month)
	{
		switch(month)
		{
		case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:case 10:case 11:return month+1;
		case 12:return 1;

		}
		return 0;
	}
	public String getDateString()
	{
		return ""+getYear()+"-"+(getMonth()<10?"0"+getMonth():getMonth())+"-"+getDay();
		
	}
	
	public String getTimeString()
	{
		return ""+(getHour()<10?"0"+getHour():getHour())+"%3A"+(getMinute()<10?"0"+getMinute():getMinute());
	}
	
	
	public int getYear() {
		return Year;
	}

	public void setYear(int year) {
		Year = year;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	private void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
			
	public String getArrivalTime() {
	 return getYear()+"-"+getMonth()+"-"+getDay()+" "+getHour()+":"+(getMinute()<10?"0"+getMinute():getMinute());
	}
	
	public String getString() {
		return testString;
	}
	public void setString(String string) {
		testString = string;
	}
	public void addFrom(Location from)
	{
		if(this.from == null)
		{
			this.from = new LinkedList<Location>();
		}
		this.from.add(from);
	}
	public Boolean isOkToPlan()
	{
		if(this.from == null || this.from.size() == 0)
			return false;
		return true;
	}
	public Boolean isThisFromAllreadyAdded(Location from)
	{
		if(this.from == null || this.from.size() == 0)
			return false;
		for(Location x : this.from)
			if(x.getLocationid() == from.getLocationid())
				return true;
		return false;
	}
	public List<Location> getFrom() {
		return from;
	}
	public void setFrom(List<Location> from) {
		this.from = from;
	}
	public Location getTo() {
		return to;
	}
	public void setTo(Location to) {
		this.to = to;
	}
	
	
	

}
