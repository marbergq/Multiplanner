package com.berka.multiplanner.Factories.Interface;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.json.JSONException;

import com.berka.multiplanner.Helpers.Interface.IResult;
import com.berka.multiplanner.Models.Interface.IStop;
import com.berka.multiplanner.Models.Travel.Timetableresult;

public interface IModel {
	IResult createResultList(List<Timetableresult> timetable);
	IStop createStopsFromResponse(HttpEntity entity) throws ParseException, JSONException, IOException;
	
}
