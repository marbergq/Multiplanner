package com.berka.multiplanner.Models;

import com.berka.multiplanner.Planner.Planner;

public class ListViewUpdaterModel {
	private Planner planner;

	private Boolean onlyFastest;
	private Boolean onlySelectedStops;

		// TODO Auto-generated constructor stub
		
		public ListViewUpdaterModel(Planner planner,Boolean onlySelectedStops , Boolean onlyFastest)
		{
			this.planner=planner;
			this.onlyFastest=onlyFastest;
			this.onlySelectedStops=onlySelectedStops;
			
		}
		
		public Planner getPlanner() {
			return planner;
		}

		public Boolean getOnlyFastest() {
			return onlyFastest;
		}

		public Boolean getOnlySelectedStops() {
			return onlySelectedStops;
		}

		
		
	
}
