package com.berka.multiplanner.Network;

import android.view.View;
import android.widget.Adapter;

import com.berka.multiplanner.Adapters.ListResultViewAdapter;
import com.berka.multiplanner.Helpers.ResultListViewUpdater;
import com.berka.multiplanner.Helpers.Interface.IResult;
import com.berka.multiplanner.Models.ListViewUpdaterModel;

public class MultiPlannerAsyncTasks {
	
	private static PlanSearchHandler searchHandler=null;
	private static CompanyImageFinder imageFinder=null;
	private static AutoComplete autoCompleter = null;
	private static ResultListViewUpdater resultListViewUpdater=null;
	
	public static void autoComplete(Adapter adapter,String params)
	{
		if(autoCompleter != null)
			autoCompleter.cancel(true);
		autoCompleter = new AutoComplete(adapter);
		autoCompleter.execute(params);
	}
	
	public static void companyImageFinder(View drawable)
	{
		
	}
	
	public static void resultListViewUpdater(IResult result, ListResultViewAdapter adapter,ListViewUpdaterModel model, View noResult, View loading)
	{
		if(resultListViewUpdater != null)
			resultListViewUpdater.cancel(true);
		resultListViewUpdater = new ResultListViewUpdater(result,adapter,model,noResult,loading);
		resultListViewUpdater.execute(model);
		
	}
	
	public static void searchHandler(Adapter adapter)
	{
		
	}
	
}
