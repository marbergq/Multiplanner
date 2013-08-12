package com.berka.multiplanner.Helpers.Logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.berka.multiplanner.Models.Travel.Segment;
import com.berka.multiplanner.Models.Travel.Timetableresult;
import com.berka.multiplanner.Models.Travel.Ttitem;

public class FindLogic {

	public static HashMap<String, List<List<Segment>>> getResult(List<Timetableresult> results) {
		List<Segment> listOfSegments = new ArrayList<Segment>();
		List<Segment> sameArrivalTime = new ArrayList<Segment>();

		// TheBestOnes
		HashMap<String, List<List<Segment>>> mapOfFirstOrderSegments = new HashMap<String, List<List<Segment>>>();

		// Foreach result found
		for (Timetableresult result : results)
			for (Ttitem x : result.getTtitem()) {
				for (List<Segment> seg : x.getSegment()) {
					// the last segment in the array => the segment containing
					// arrival to the dest
					Segment segment = seg.get(seg.size() - 1);

					if (!mapOfFirstOrderSegments.containsKey(segment
							.getArrival().getDatetime())) {
						List<List<Segment>> buffList = new ArrayList<List<Segment>>();
						buffList.add(seg);
						mapOfFirstOrderSegments.put(segment.getArrival()
								.getDatetime(), buffList);
					} else {
						mapOfFirstOrderSegments.get(
								segment.getArrival().getDatetime()).add(seg);
					}

				}
			}

		return mapOfFirstOrderSegments;

	}

}
