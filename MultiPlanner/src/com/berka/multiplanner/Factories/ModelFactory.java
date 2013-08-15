package com.berka.multiplanner.Factories;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.berka.multiplanner.Factories.Interface.IModel;
import com.berka.multiplanner.Helpers.Interface.IResult;
import com.berka.multiplanner.Helpers.Logic.FindLogic;
import com.berka.multiplanner.Helpers.Logic.MultipleResult;
import com.berka.multiplanner.Models.Interface.IStop;
import com.berka.multiplanner.Models.Travel.Timetableresult;
import com.berka.multiplanner.Models.autocomplete.Suggestions;

public class ModelFactory implements IModel{

	
	public IResult createResultList(List<Timetableresult> timetable)
	{
		return new MultipleResult(
				FindLogic.getResult(timetable)
				);
	}
	
	public IStop createStopsFromResponse(HttpEntity entity) throws ParseException, JSONException, IOException
	{
		
		return new Suggestions(
				EntityUtils.toString(
						entity
						,HTTP.UTF_8)
						);
	}
	
}
