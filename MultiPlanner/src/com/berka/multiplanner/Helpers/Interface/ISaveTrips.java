package com.berka.multiplanner.Helpers.Interface;

import java.util.Observer;
import java.util.Set;

import com.berka.multiplanner.Models.Interface.ILocation;

public interface ISaveTrips extends Observer {

	void saveStop(ILocation stop);
	Set<ILocation> getStops();
	
}
