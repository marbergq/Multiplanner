package com.berka.multiplanner.Models.Travel;

import java.util.List;

public class Traveler {
	
	List<Segment> steps;

	public List<Segment> getSteps() {
		return steps;
	}
	
	public Segment getFirstStep()
	{
		return steps.get(0);
		
	}
	
	public Segment getLastSegment()
	{
		return steps.get(steps.size()-1);
	}

	public void setSteps(List<Segment> steps) {
		this.steps = steps;
	}
	

}
