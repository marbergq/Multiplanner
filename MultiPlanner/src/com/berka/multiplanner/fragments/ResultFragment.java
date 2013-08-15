package com.berka.multiplanner.fragments;

import java.text.ParseException;
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
import com.berka.multiplanner.Helpers.Logic.MultipleResult;
import com.berka.multiplanner.Models.ListViewUpdaterModel;
import com.berka.multiplanner.Models.Travel.Segment;
import com.berka.multiplanner.Models.Trips.Location;
import com.berka.multiplanner.Models.Trips.Trip;
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
	RelativeLayout loadingListViewLayout;
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
		
	
		if(adapter != null){
			getActivity().findViewById(R.id.load).setVisibility(View.VISIBLE);
			SearchForTrips();
		}
		updateTripTable();
		
	}
	
	private void updateTripTable()
	{
		if(placeTripsLayout != null && planner != null)
		{

			for(TextView view : fromTrips)
				placeTripsLayout.removeView(view);
		
			fromTrips.clear();
			
			for(Location location : planner.getFrom())
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
		if(asyncListViewUpdater != null)
			asyncListViewUpdater.cancel(true);
		asyncListViewUpdater = new ResultListViewUpdater(results){
			@Override
			protected 
			void onPostExecute(List<Trip> result){
				if(result != null){
					
				adapter.addAll(result);
				adapter.notifyDataSetChanged();
				
				loadingListViewLayout.setVisibility(View.GONE);
				displayNoResults();
				}else
				{
					loadingListViewLayout.setVisibility(View.GONE);
					displayNoResults();
					Log.e("ListViewUpdater", "Was NUll");
				}
				
			}
			
			@Override
			protected void onCancelled()
			{
				loadingListViewLayout.setVisibility(View.GONE);
				displayNoResults();
			}
		};
		
		asyncListViewUpdater.execute(new ListViewUpdaterModel(planner,AnkomstSammaTid.isChecked(),SnabbasteResväg.isChecked()));
		loadingListViewLayout.setVisibility(View.VISIBLE);
		
	}
	
	private void displayNoResults()
	{
		Log.d("ADAPTER MSG", "Adapter Count: "+adapter.getCount());
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
		planner.addObserver(this);
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
		loadingListViewLayout =(RelativeLayout)rootView.findViewById(R.id.result_loading_listView);
		noResultsView = (RelativeLayout)rootView.findViewById(R.id.Results_no_results);
		SnabbasteResväg =(CheckBox)rootView.findViewById(R.id.result_only_fastest_stops);
		
		if(savedInstanceState == null)
		SearchForTrips();
		
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
		 bar.setProgress(planner.getAnkomstIntervall());
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
		
		 handler= new PlanSearchHandler(progressBar,progressText){
			@Override
			protected 
			void onPostExecute(IResult result)
			{
				try {
					if(result != null){
					adapter.clear();
					List<Trip> trips;
					
					trips = result.getTrips(planner,AnkomstSammaTid.isChecked(),SnabbasteResväg.isChecked());
					if(trips==null){
						adapter.clear();
						adapter.notifyDataSetChanged();
						return;
					}
					if(trips.size() != 0)
						Collections.sort(trips);
					adapter.addAll(trips);
					results = result;
					adapter.notifyDataSetChanged();
					
					
					getActivity().findViewById(R.id.load).setVisibility(View.GONE);
					loadingListViewLayout.setVisibility(View.GONE);
					displayNoResults();
				}
				else 
				{
					displayNoResults();
					Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				displayNoResults();
				Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
			}
			}
			
		};
		
		handler.execute(planner);

		
		
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		
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

