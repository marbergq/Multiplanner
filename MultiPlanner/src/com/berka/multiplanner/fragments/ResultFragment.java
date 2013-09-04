package com.berka.multiplanner.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.berka.multiplanner.MainActivity;
import com.berka.multiplanner.Adapters.ListResultViewAdapter;
import com.berka.multiplanner.Helpers.ResultListViewUpdater;
import com.berka.multiplanner.Helpers.Interface.IResult;
import com.berka.multiplanner.Models.ListViewUpdaterModel;
import com.berka.multiplanner.Models.Interface.ILocation;
import com.berka.multiplanner.Models.Travel.Segment;
import com.berka.multiplanner.Models.Trips.Trip;
import com.berka.multiplanner.Network.MultiPlannerAsyncTasks;
import com.berka.multiplanner.Network.PlanSearchHandler;
import com.berka.multiplanner.Planner.Planner;
import com.groupalpha.berka.multiplanner.R;

public class ResultFragment extends Fragment implements Observer {
	
	
	private Planner planner;
	ListResultViewAdapter adapter;
	CheckBox AnkomstSammaTid;
	CheckBox SnabbasteResväg;
	PlanSearchHandler handler;
	IResult results;
	TextView seekbarText;
	SeekBar bar;
	ProgressBar progressBar;
	TextView progressText;
	LinearLayout placeTripsLayout;
	ResultListViewUpdater asyncListViewUpdater;
	RelativeLayout loadingListViewWithContent;
	RelativeLayout noResultsView;
	List<TextView> fromTrips = new LinkedList<TextView>();
	private TextView AnkomstIntervallText;
	
	
	public Planner getPlanner()
	{
		return planner;
	}
	
	public void setPlanner(Planner planner)
	{
		if(handler != null){
			Log.d("MartinINFO", "Canceld? "+handler.cancel(true));
			
		}
				this.planner = planner;
				this.planner.addObserver(this);
				
		if(adapter != null){
			SearchForTrips();
			updateTripTable();
			
		}
		
		
		
	}
	
	private void updateTripTable()
	{
		if(placeTripsLayout != null && planner != null)
		{

			for(TextView view : fromTrips)
				placeTripsLayout.removeView(view);
		
			fromTrips.clear();
			
			for(ILocation location : planner.getFrom())
			{	TextView view = new TextView(getActivity());
				view.setText(location.getDisplayname().toUpperCase(Locale.ROOT));
				view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				view.setId((int)System.currentTimeMillis());
				view.setTextColor(Color.WHITE);
				fromTrips.add(view);
			}
			for(TextView view : fromTrips)
			{
				placeTripsLayout.addView(view);
			}
			
		}

	}
	

	private void updateListView()
	{
		Log.i("updateListView", "");
		if(asyncListViewUpdater != null)
			asyncListViewUpdater.cancel(true);
		
		MultiPlannerAsyncTasks.resultListViewUpdater(results, adapter,
				new ListViewUpdaterModel(planner,AnkomstSammaTid.isChecked(),SnabbasteResväg.isChecked())
					, noResultsView, loadingListViewWithContent);
//		
//		asyncListViewUpdater = new ResultListViewUpdater(results){
//			@Override
//			protected 
//			void onPostExecute(List<Trip> result){
//				
//				if(result != null){
//					
//				adapter.addAll(result);
//				adapter.notifyDataSetChanged();
//				
//				}else
//				{
//					Log.e("ListViewUpdater", "Was NUll");
//					
//				}
//				endListLoad();
//			}
//			
//			@Override
//			protected void onCancelled()
//			{
//
//			}
//		};
//		startListLoad();
//		asyncListViewUpdater.execute(new ListViewUpdaterModel(planner,AnkomstSammaTid.isChecked(),SnabbasteResväg.isChecked()));
		
	}
	
	private void startSearch()
	{
		getActivity().findViewById(R.id.load).setVisibility(View.VISIBLE);
	}
	
	private void endSearch()
	{
		getActivity().findViewById(R.id.load).setVisibility(View.INVISIBLE);
	}
	
	private void startListLoad()
	{
		Log.i("startListLoad", "");
		
		noResultsView.setVisibility(View.INVISIBLE);
		if(loadingListViewWithContent.getVisibility() != View.VISIBLE)
			loadingListViewWithContent.setVisibility(View.VISIBLE);
		
	}
	
	private void endListLoad()
	{
		Log.i("endListLoad", "");
		loadingListViewWithContent.setVisibility(View.INVISIBLE);
		noResultsView.setVisibility(View.INVISIBLE);
		displayNoResults();
	}
	
	private void displayNoResults()
	{
		Log.i("ADAPTER MSG", "Adapter Count: "+adapter.getCount());
		if(adapter.getCount()==0)
			noResultsView.setVisibility(View.VISIBLE);
		else
			noResultsView.setVisibility(View.GONE);
		
	}
	
	
	
	
	
	private void goToDetailFragment(List<List<Segment>> data)
	{
		MainActivity act = (MainActivity)getActivity();
		act.getAdapter().ShowDetails(data);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.results, container,
				false);
		
		placeTripsLayout = (LinearLayout)rootView.findViewById(R.id.result_place_trips);
		
		ListView list = (ListView) rootView.findViewById(R.id.resultlist);
		
		adapter = new ListResultViewAdapter(getActivity(),R.id.resultlist,new ArrayList<Trip>());
		list.setAdapter(adapter);
		
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				
				goToDetailFragment(adapter.getItem(position).getSegments());
				
				
			}
		});
		
		AnkomstIntervallText = (TextView)rootView.findViewById(R.id.results_ankomstintervall_textview);
		progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar2);
		progressText = (TextView)rootView.findViewById(R.id.result_search_progress_text);
		AnkomstSammaTid = (CheckBox)rootView.findViewById(R.id.result_only_selected_stops);
		loadingListViewWithContent =(RelativeLayout)rootView.findViewById(R.id.result_loading_listView);
		noResultsView = (RelativeLayout)rootView.findViewById(R.id.Results_no_results);
		SnabbasteResväg =(CheckBox)rootView.findViewById(R.id.result_only_fastest_stops);
		
		
		setupMenu(rootView);
		checkBoxListener(AnkomstSammaTid);
		checkBoxListener(SnabbasteResväg);
		updateTripTable();
		return rootView;
	}
	
	private void checkBoxListener(CheckBox checkbox)
	{
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(buttonView.getId() == AnkomstSammaTid.getId())
				{
					if(isChecked)
						AnkomstIntervallText.setText(getActivity().getText(R.string.ankomst_intervall_1));
					else
						AnkomstIntervallText.setText(getActivity().getText(R.string.ankomst_intervall_2));
				}
				
				updateListView();
			}
		});	
	}
	
	private void setupMenu(View rootView) {
		 bar = (SeekBar)rootView.findViewById(R.id.result_seekbar_selector);
		 seekbarText = (TextView)rootView.findViewById(R.id.result_progress_result);
		
		 if(planner != null)
			 bar.setProgress(planner.getAnkomstIntervall());
		 else 
			bar.setProgress(10);
		
		 seekbarText.setText(""+bar.getProgress());
		CheckBox checkBox = (CheckBox)rootView.findViewById(R.id.result_only_fastest_stops);
		 
		 checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
			}
		});
		 
		
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(progress < 5){
					seekBar.setProgress(5);
					return;
				}
				seekbarText.setText(progress+"");
				planner.setAnkomstIntervall(progress);
				updateListView();
			}
		});
		
	}
	
	private void SearchForTrips()
	{
		if(handler!=null)
			handler.cancel(true);
		startSearch();
		
		handler = new PlanSearchHandler(progressBar, progressText) {
			@Override
			protected void onPostExecute(IResult result) {
				try {
					startListLoad();
					if (result != null) {
						adapter.clear();
						List<Trip> trips;

						
						trips = result.getTrips(planner,
								AnkomstSammaTid.isChecked(),
								SnabbasteResväg.isChecked());
						
						if (trips == null) {
							adapter.clear();
							adapter.notifyDataSetChanged();
							return;
						}

						if (trips.size() != 0)
							Collections.sort(trips);
						adapter.addAll(trips);
						results = result;
						adapter.notifyDataSetChanged();
						
					} else {
						Toast.makeText(getActivity(), "error",
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG)
							.show();
				}
				finally
				{
					endSearch();
					endListLoad();
				}
			}
			

		};

		
		handler.execute(planner);

		
		
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof Integer)
			setSeekbarValue((Integer)arg1);
		
		
	}
	private void setSeekbarValue(Integer arg1) {
		if(bar.getProgress() != arg1)
			bar.setProgress(arg1);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if(handler != null)
			handler.cancel(true);
	}
	
	
	}

