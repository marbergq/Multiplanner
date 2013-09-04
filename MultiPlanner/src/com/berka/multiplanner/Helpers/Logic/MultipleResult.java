package com.berka.multiplanner.Helpers.Logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.util.Log;
import android.util.Pair;

import com.berka.multiplanner.Helpers.TimeFilter;
import com.berka.multiplanner.Helpers.Interface.IResult;
import com.berka.multiplanner.Models.Interface.ILocation;
import com.berka.multiplanner.Models.Travel.Mot;
import com.berka.multiplanner.Models.Travel.Segment;
import com.berka.multiplanner.Models.Travel.SegmentId;
import com.berka.multiplanner.Models.Trips.Location;
import com.berka.multiplanner.Models.Trips.Trip;
import com.berka.multiplanner.Planner.Planner;

public class MultipleResult implements IResult {

	HashMap<String, List<List<Segment>>> map;
	public MultipleResult(HashMap<String,List<List<Segment>>> map)
	{
		this.map=map;
	}
	
	static class SearchForAllLocations
	{
		public List<Pair<ILocation,Boolean>> from;
		
		public void init(List<ILocation> from)
		{
			this.from = new ArrayList<Pair<ILocation,Boolean>>();
			
			for(ILocation l : from)
			{
				this.from.add(new Pair<ILocation, Boolean>(l, false));
			}
		}
		
		public Boolean AllFromIncluded()
		{
			for(Pair<ILocation, Boolean> p : this.from)
				if(p.second == false)
					return false;
			return true;
		}
		
		public void foundLocation(ILocation loc)
		{
			List<Pair<ILocation,Boolean>> temp = new ArrayList<Pair<ILocation,Boolean>>();
			for(int i = 0 ; i < this.from.size(); i++)
			{
				if(from.get(i).first.getLocationid().intValue() == loc.getLocationid().intValue()){
					 Pair<ILocation, Boolean> p =new Pair<ILocation, Boolean>(from.get(i).first, true);
					 temp.add(p);
				}
				else
				{
					temp.add(from.get(i));
				}
			}
			from.clear();
			from.addAll(temp);
			
		}
		
	}
	public Boolean AllFromIncluded(Planner planner, Trip trip)
	{
		return AllFromIncluded(planner, trip.getSegments());
	}
	public Boolean AllFromIncluded(Planner planner, List<List<Segment>> segs)
	{
		
		
		SearchForAllLocations allLoc = new SearchForAllLocations();
		allLoc.init(planner.getFrom());
		
		for(List<Segment> seg : segs){
			
			
		ILocation locatid = seg.get(0).getDeparture().getLocation();
		allLoc.foundLocation(locatid);
		}
		return allLoc.AllFromIncluded();
	}
	
	public List<Trip> getTrips(Planner planner,Boolean onlySameTimeArrival,Boolean onlyOneResultPerStation) throws ParseException
	{
		long startTime = System.currentTimeMillis();
		List<Trip> trips= getTrips(planner,onlySameTimeArrival);
		if(Thread.currentThread().isInterrupted())
			return null;
		if(onlyOneResultPerStation){
		

			List<Trip> buffs = new LinkedList<Trip>();
		for(Trip t : trips)
		{
			if(Thread.currentThread().isInterrupted())
				return null;
			Trip newTrip = new Trip();
			for(ILocation l : planner.getFrom())
			{
				if(Thread.currentThread().isInterrupted())
					return null;
				newTrip.addSegments(filterOutAllbutTheShortest(l,t));
				
			}
			newTrip.setAnkomstTid(findTheLatestTimeInTrip(newTrip));
			if(Thread.currentThread().isInterrupted())
				return null;
			buffs.add(newTrip);
		}
		
		
		
		Log.e("NOT ERROR", "Time spent in tripsearch(MS): "+(System.currentTimeMillis()-startTime));
		return  buffs;
		}
		else 
		{
			Log.e("NOT ERROR", "Time spent in tripsearch(MS): "+(System.currentTimeMillis()-startTime));			
			return trips;
		}

	}
	
	private List<Segment> filterOutAllbutTheShortest(ILocation l, Trip trip) {

		Calendar candidateTime = null;

		List<Segment> candidateSegments = null;
		for (List<Segment> segs : trip.getSegments()) {

			if (segs.get(0).getDeparture().getLocation().getLocationid()
					.intValue() == l.getLocationid().intValue()) {
				if (candidateTime == null) {
					candidateTime = Trip.getTripTime(segs);
					candidateSegments = segs;
					continue;
				}

				Calendar buff = Trip.getTripTime(segs);
				long diff = (candidateTime.getTime().getTime() - buff.getTime()
						.getTime());

				if (diff > 0) {
					candidateTime = buff;
					candidateSegments = segs;
				}
			}
		}
		return candidateSegments;
	}
	
	public List<Trip> getTrips(Planner planner,Boolean onlySameTimeArrival) throws ParseException
	{
		List<Trip> trips = new ArrayList<Trip>();
		if(onlySameTimeArrival){
		

		for(String key : TimeFilter.filterTrips(map,planner))
		{
			Trip trip = new Trip();
			trip.setAnkomstTid(key);
			
			if(map.get(key).size() < 2)
				continue;
			if(AllFromIncluded(planner,map.get(key))){
				for(List<Segment> segs : map.get(key)){
					trip.addSegments(segs);
				}
				trips.add(trip);
			}
		}

		}else
		{
			Set<Set<String>> available = getTripsWithinLimit(planner);
			for(Set<String> keys : available)
			{
				Trip trip = new Trip();
				for(String key : keys)
				{
					
					
					for(List<Segment> segs : map.get(key))
					{
						trip.addSegments(segs);
					}
					
					
				}
				if(AllFromIncluded(planner,trip))
				{
					trips.add(trip);
				}
				trip.setAnkomstTid(findTheLatestTimeInTrip(trip));
			}
			
		}
		return trips;
	}
	
	
	private String findTheLatestTimeInTrip(Trip trip) throws ParseException
	{
		Calendar candidate = null;
		for(List<Segment> seg : trip.getSegments())
		{
			if(Thread.currentThread().isInterrupted())
				return null;
			if(candidate == null){
				candidate = TimeFilter.getDateFromString(seg.get(seg.size()-1).getArrival().getDatetime());
				continue;
			}
			Calendar buff = TimeFilter.getDateFromString(seg.get(seg.size()-1).getArrival().getDatetime());
			if(candidate.before(buff))
				candidate = buff;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
		return formatter.format(candidate.getTime());
	}
	
	private Set<Set<String>> getTripsWithinLimit(Planner planner) throws ParseException
	{
		
		Set<Set<String>> setOfSets = new HashSet<Set<String>>();
		
		for(String key : getArrivalTimes()){
			Set<String> setOfRelevantKeys = new HashSet<String>();
			for(String key2 : getArrivalTimes())
				if(TimeFilter.isTimeWithinRange(key, key2, planner.getAnkomstIntervall()))
					setOfRelevantKeys.add(key2);
			
			setOfSets.add(setOfRelevantKeys);
		}

		
		
		return setOfSets;
	}
	

	public List<Trip> getTrips()
	{
		List<Trip> trips = new ArrayList<Trip>();

		for(String key : map.keySet())
		{
			Trip trip = new Trip();
			trip.setAnkomstTid(key);
			if(map.get(key).size() < 2)
				continue;
			for(List<Segment> segs : map.get(key))
				trip.addSegments(segs);
			trips.add(trip);
		}

		return trips;
	}
	
	public Set<String> getArrivalTimes()
	{
		return map.keySet();
	}
	
	public List<List<Segment>> getSegmentsForArrivalTime(String ArrivalTime)
	{
		if(!map.containsKey(ArrivalTime))
			throw new IllegalArgumentException("This arrival time does not exist");
		else
			return map.get(ArrivalTime);
	}
	
	public HashMap<Mot, List<List<Segment>>> getSegmentsWithSameLastTravel(String ArrivalTimne)
	{
		List<List<Segment>> arrival = getSegmentsForArrivalTime(ArrivalTimne);
		
		HashMap<Mot, List<List<Segment>>> found = new HashMap<Mot,List<List<Segment>>>();
		
		for(List<Segment> x : arrival)
		{
			Segment last = x.get(x.size()-1);
			SegmentId id = last.getSegmentid();
			if(!found.containsKey(id.getMot())){
				ArrayList<List<Segment>> buff = new ArrayList<List<Segment>>();
				buff.add(x);
				found.put(id.getMot(), buff);
			}
			else 
				found.get(id.getMot()).add(x);
		}
		HashMap<Mot, List<List<Segment>>> returns = new HashMap<Mot,List<List<Segment>>>();
		for(Mot x : found.keySet())
		{
			if(found.get(x).size() > 1)
				returns.put(x, found.get(x));
		}

		return returns;

	}
	
}
