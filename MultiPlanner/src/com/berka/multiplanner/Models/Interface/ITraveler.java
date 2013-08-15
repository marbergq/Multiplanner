package com.berka.multiplanner.Models.Interface;

import java.util.List;

import com.berka.multiplanner.Models.Travel.Departure;
import com.berka.multiplanner.Models.Travel.Segment;

public interface ITraveler {
	public List<Segment> getSteps();
	public Segment getLastSegment();
	public Departure getDeparture();
}
