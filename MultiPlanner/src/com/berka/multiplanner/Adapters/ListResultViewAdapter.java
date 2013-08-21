package com.berka.multiplanner.Adapters;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.berka.multiplanner.Models.Trips.Trip;
import com.groupalpha.berka.multiplanner.R;

public class ListResultViewAdapter extends ArrayAdapter<Trip> {

	Context context;
	List<Trip> values;
	public ListResultViewAdapter(Context context, int resource, List<Trip> objects) {
		super(context, resource,  objects);
		// TODO Auto-generated constructor stub
		this.context=context;
		values = Collections.synchronizedList(objects);
		
		
	}

	
	
	  @Override
	public void add(Trip object) {
		// TODO Auto-generated method stub
		super.add(object);
	}

	@Override
	public void addAll(Collection<? extends Trip> collection) {
		// TODO Auto-generated method stub
		super.clear();
			super.addAll(collection);
	
	}

	@Override
	public void addAll(Trip... items) {
		super.clear();
			super.addAll(items);
	
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	         View view = convertView;
	        if (view == null) {
	            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = inflater.inflate(R.layout.result_list_row,parent, false);
	            
	     	     
	     	        
	        }

	        Trip item = values.get(position);
	        if (item!= null) {
	            // My layout has only one TextView
	            TextView destination = (TextView) view.findViewById(R.id.destination_row);
	            TextView time = (TextView) view.findViewById(R.id.time_row);
	            if (destination != null) {
	               destination.setText(item.getTo());
	            }if(time !=null){
	               time.setText(item.getAnkomstTid());
	            }
	         }
	       
	        
//	  
//	       if(convertView==null)
//	       {
	    	   
	    	   ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.ALPHA, 0,1);
     	       animator.setDuration(300);
     
     	        animator.start();
//	       }
	        
	        return view;
	    }
	
	
}
