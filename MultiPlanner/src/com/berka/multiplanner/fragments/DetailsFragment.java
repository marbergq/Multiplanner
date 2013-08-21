package com.berka.multiplanner.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.R.color;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.berka.multiplanner.Adapters.ExpandableListViewResultAdapter;
import com.berka.multiplanner.Models.Interface.ITraveler;
import com.berka.multiplanner.Models.Travel.Segment;
import com.berka.multiplanner.Models.Travel.Traveler;
import com.berka.multiplanner.Network.CompanyImageFinder;
import com.groupalpha.berka.multiplanner.R;

public class DetailsFragment extends Fragment {
	
	
	List<ITraveler> travelers;
	ExpandableListViewResultAdapter adapter;
	ExpandableListView listView;
	String to="";
	TextView toText;
	public void setData(List<List<Segment>> data)
	{
		if(travelers == null)
			travelers = new ArrayList<ITraveler>();
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
				travelers=new ArrayList<ITraveler>();
		adapter = new ExpandableListViewResultAdapter(getActivity(),travelers);
				listView.setAdapter(adapter);
				expandGroups();

				listView.setOnChildClickListener(
						childInformationClickHandler());
				
				
		return rootView;
	}

	private OnChildClickListener childInformationClickHandler() {
		return new OnChildClickListener() {

@Override
public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		final Segment s = adapter.getChild(groupPosition, childPosition);
		
		if(s.getSegmentid().getCarrier() != null)
		{
			final Dialog tripDialog = new Dialog(getActivity());
			tripDialog.setTitle("RESA MED");
			tripDialog.setContentView(R.layout.tripinfodialog);
			
			//View includedView = tripDialog.findViewById(R.id.detail_popup_travelplan);

			
			TextView restyp = (TextView)tripDialog.findViewById(R.id.child_restyp);
			TextView linje = (TextView)tripDialog.findViewById(R.id.child_linje_id_real);
			TextView from = (TextView)tripDialog.findViewById(R.id.child_from_place);
			TextView to = (TextView)tripDialog.findViewById(R.id.child_to_place);
			TextView fromTime = (TextView)tripDialog.findViewById(R.id.child_from_time);
			TextView toTime = (TextView)tripDialog.findViewById(R.id.child_to_time);
			TextView company = (TextView)tripDialog.findViewById(R.id.detail_popup_company_name);

from.setText(s.getDeparture().getLocation()
			.getDisplayname());
to.setText(s.getArrival().getLocation().getDisplayname());
fromTime.setText(s.getDeparture().getDatetime());
toTime.setText(s.getArrival().getDatetime());

restyp.setText(s.getSegmentid().getCarrier().getName());
linje.setText(s.getSegmentid().getMot().getText()
			+ " "
			+ s.getSegmentid().getCarrier().getNumber()
					.intValue());
company.setText(s.getSegmentid().getCarrier().getName());
			
			
			final ImageView companyImage = (ImageView)tripDialog.findViewById(R.id.detail_popup_company_image);
			
			CompanyImageFinder finder = new CompanyImageFinder(){

				@Override
				protected void onPostExecute(Bitmap result) {
					Log.d("result!", "");
					if(result != null)
					{
						Log.d("RESULT", "NOT NULL!");
						companyImage.setImageBitmap(result);
					}else
						Log.d("RESULT", "NULL!");
				}
				
			};
			finder.execute(s.getSegmentid().getCarrier().getId().intValue());
			
			
			Button buttonNo = (Button) tripDialog.findViewById(R.id.detail_popup_button_cancel);
			Button buttonGO = (Button) tripDialog.findViewById(R.id.detail_popup_button_go);
			
			
			
			
			buttonNo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					tripDialog.dismiss();
					
				}
			});
			
			buttonGO.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_VIEW, 
						       Uri.parse(s.getSegmentid().getCarrier().getUrl()));
						startActivity(i);
					
				}
			});
			
			
			tripDialog.show();
			return true;
			
		}
		return false;
}
};
	}

}
