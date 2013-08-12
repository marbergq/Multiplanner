package com.berka.multiplanner.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.groupalpha.berka.multiplanner.R;
import com.berka.multiplanner.Adapters.ExpandableListViewResultAdapter;
import com.berka.multiplanner.Models.Travel.Segment;
import com.berka.multiplanner.Models.Travel.Traveler;

public class DetailsFragment extends Fragment {
	
	
	List<Traveler> travelers;
	ExpandableListViewResultAdapter adapter;
	ExpandableListView listView;
	String to="";
	TextView toText;
	public void setData(List<List<Segment>> data)
	{
		if(travelers == null)
			travelers = new ArrayList<Traveler>();
		else
			travelers.clear();
		for(List<Segment> seg : data)
		{
			Traveler t = new Traveler();
			t.setSteps(seg);
			travelers.add(t);
		}
		if(data.size() > 0)
		{
			to=data.get(0).get(data.get(0).size()-1).getArrival().getLocation().getDisplayname().toUpperCase(Locale.ROOT);
			
		}
		if(adapter != null)
		{
		//	adapter.changeData(travelers);
			
			adapter.notifyDataSetChanged();
			expandGroups();
		}

		if(toText != null)
			toText.setText(to);
		
	}
	
	public void expandGroups()
	{
		if(adapter == null)
			return;
		else if(listView == null)
			return;
		for(int i = 0; i < adapter.getGroupCount(); i++)
			listView.expandGroup(i);
	}
	
	public void invalidateData()
	{
		if(adapter != null)
		{
			if(travelers != null)
			{
				travelers.clear();
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.trippresentation, container,
				false);

		 toText = (TextView)rootView.findViewById(R.id.detail_to_trip);
		toText.setText(to);
		listView = (ExpandableListView) rootView.findViewById(R.id.expandableListView1);
		if(travelers == null)
				travelers=new ArrayList<Traveler>();
		adapter = new ExpandableListViewResultAdapter(getActivity(),travelers);
				listView.setAdapter(adapter);
				expandGroups();

				
				
		return rootView;
	}

}
