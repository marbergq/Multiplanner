package com.berka.multiplanner.Helpers;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.groupalpha.berka.multiplanner.R;
import com.berka.multiplanner.Models.Trips.Location;

public class AutoCompleteArrayAdapter extends ArrayAdapter<Location> {

	Context context;
	int resource;
	List<Location> objects;
	public AutoCompleteArrayAdapter(Context context, int textViewResourceId,List<Location> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.resource=textViewResourceId;
		
		this.objects = objects;
		
	}
	
	
	
	
	@Override
	 public View getView(int position, View convertView, ViewGroup parent) {
		
		
		View row = convertView;
		
		StationHolder holder = null;
		
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder = new StationHolder();
            holder.text = (TextView)row.findViewById(R.id.completed);
            row.setTag(holder);
		}
		else
		{
			holder = (StationHolder)row.getTag();
		}
		
		Location station = objects.get(position);
		holder.text.setText(station.getDisplayname());
	
		return row;
	}
	
	static class StationHolder
	{
		TextView text;
	}

}
