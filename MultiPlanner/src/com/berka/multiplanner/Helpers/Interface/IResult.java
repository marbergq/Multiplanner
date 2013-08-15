package com.berka.multiplanner.Helpers.Interface;

import java.text.ParseException;
import java.util.List;

import com.berka.multiplanner.Models.Trips.Trip;
import com.berka.multiplanner.Planner.Planner;

public interface IResult {

	public List<Trip>  getTrips();
	public List<Trip> getTrips(Planner planner,Boolean onlySameTimeArrival) throws ParseException;
	public List<Trip> getTrips(Planner planner, Boolean onlySameTime,Boolean onlyFastest)throws ParseException;
}
