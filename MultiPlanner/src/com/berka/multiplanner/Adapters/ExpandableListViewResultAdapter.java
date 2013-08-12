package com.berka.multiplanner.Adapters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.groupalpha.berka.multiplanner.R;
import com.berka.multiplanner.Models.ShareModel;
import com.berka.multiplanner.Models.Trip;
import com.berka.multiplanner.Models.Travel.Segment;
import com.berka.multiplanner.Models.Travel.Traveler;

public class ExpandableListViewResultAdapter extends BaseExpandableListAdapter {

	private static final class ViewHolder {
        TextView byten;
        TextView totalRestid;
        TextView Resinär;
        TextView tidFramme;

		TextView restyp;
		TextView linje;
		TextView fromPlace;
		TextView toPlace;
		TextView fromTime;
		TextView toTime;
	}

private List<Traveler> itemList;
private final Context context;
List<Pair<Integer,Integer>> toPaintGreen;


public ExpandableListViewResultAdapter(Context context, List<Traveler> itemList) {
		this.context = context;
        this.itemList = itemList;
        
        shouldBackgroundbeGreen();
}

@Override
public void notifyDataSetChanged()
{
	
	super.notifyDataSetChanged();
	shouldBackgroundbeGreen();
}


public void changeData(List<Traveler> itemList)
{
	this.itemList.clear();
	this.itemList.addAll(itemList);
	
}

@Override
public Segment getChild(int groupPosition, int childPosition) {

        return itemList.get(groupPosition).getSteps().get(childPosition);
}

@Override
public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
}

@Override
public int getChildrenCount(int groupPosition) {
        return itemList.get(groupPosition).getSteps().size();
}

@Override
public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                         final ViewGroup parent) {
	Log.e("STARTROW", "Position:["+groupPosition+","+childPosition+"]");
        
        
        View resultView = convertView;



        if (resultView == null) {
        	LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.activity_main, null);
                resultView = infalInflater.inflate(R.layout.tripresult_group_detail, null); //TODO change layout id
//               TextView fromPlace = (TextView) resultView.findViewById(R.id.child_from_place);
//               holder.toPlace = (TextView) resultView.findViewById(R.id.child_to_place);
//               holder.toTime = (TextView) resultView.findViewById(R.id.child_to_time);
//               holder.fromTime = (TextView)resultView.findViewById(R.id.child_from_time);
//               holder.linje = (TextView)resultView.findViewById(R.id.child_linje_id);
//               holder.restyp = (TextView)resultView.findViewById(R.id.child_restyp);
//               
//                resultView.setTag(holder);
        } 
//        else {
//                holder = (ChildViewHolder) resultView.getTag();
//        }

        final Segment item = getChild(groupPosition, childPosition);
        
        TextView fromPlace = (TextView) resultView.findViewById(R.id.child_from_place);
      TextView toPlace = (TextView) resultView.findViewById(R.id.child_to_place);
      TextView toTime = (TextView) resultView.findViewById(R.id.child_to_time);
      TextView fromTime = (TextView)resultView.findViewById(R.id.child_from_time);
      TextView linje = (TextView)resultView.findViewById(R.id.child_linje_id_real);
      TextView restyp = (TextView)resultView.findViewById(R.id.child_restyp);
      TextView from_from = (TextView)resultView.findViewById(R.id.child_from_from);
      TextView to_to = (TextView)resultView.findViewById(R.id.child_to_to);
       
     fromPlace.setText(item.getDeparture().getLocation().getDisplayname());
        toPlace.setText(item.getArrival().getLocation().getDisplayname());
       fromTime.setText(item.getDeparture().getDatetime());
        toTime.setText(item.getArrival().getDatetime());
       LinearLayout layout= (LinearLayout) resultView.findViewById(R.id.row_background);
       
        if(item.getSegmentid().getCarrier() != null)
        {
        	restyp.setText(item.getSegmentid().getCarrier().getName());
        	linje.setText(item.getSegmentid().getMot().getText()+" "+item.getSegmentid().getCarrier().getNumber().intValue());
        }else if(item.getSegmentid().getMot().getDisplayType().equals("J"))
        {
        	restyp.setText(item.getSegmentid().getMot().getText());
        	linje.setText(" Tåg");
        }
        else
        {
        	restyp.setText(item.getSegmentid().getMot().getText());
        	linje.setText("");
        }
        layout.setBackgroundResource(R.drawable.gradientbackground_blue);
        
        Boolean isGreen = false;
        for(Pair<Integer,Integer> p : toPaintGreen)
        {
        //	if(groupPosition == 0 && childPosition == 0)
        	//	break;
        	if(p.first == groupPosition && p.second == childPosition)
        	{
        		Log.e("START", "POINT:["+p.first+","+p.second+"]");
        		Log.e("Paiting:", "Group: " + groupPosition + " Child: " + childPosition +  " With display: "+item.getSegmentid().getMot().getText());
        		layout.setBackgroundResource(R.drawable.gradientbackground_green);
        		Log.e("END", "--");
        		isGreen = true;
        		break;
        	}
        }
//        ObjectAnimator animator = ObjectAnimator.ofFloat(resultView, View.ALPHA, 0,1);
//	       animator.setDuration(800);
//
//	        animator.start();
        if(!isGreen)
        {
        	fromPlace.setTextColor(Color.WHITE);
    		toPlace.setTextColor(Color.WHITE);
    		toTime.setTextColor(Color.WHITE);
    		fromTime.setTextColor(Color.WHITE);
    		linje.setTextColor(Color.WHITE);
    		restyp.setTextColor(Color.WHITE);
    		from_from.setTextColor(Color.WHITE);
    		to_to.setTextColor(Color.WHITE);
        }
        else
        {
        	fromPlace.setTextColor(Color.BLACK);
    		toPlace.setTextColor(Color.BLACK);
    		toTime.setTextColor(Color.BLACK);
    		fromTime.setTextColor(Color.BLACK);
    		linje.setTextColor(Color.BLACK);
    		restyp.setTextColor(Color.BLACK);
    		from_from.setTextColor(Color.BLACK);
    		to_to.setTextColor(Color.BLACK);
        }
        
       
       // holder.textLabel.setText(item.toString());
        Log.e("END ROW", "--");
//        if(convertView==null)
//	       {
	    	   
	    	
//	       }
        
        return resultView;
}
private void shouldBackgroundbeGreen()
{
	HashMap<Pair<Integer,Integer>,Segment> remember= new HashMap<Pair<Integer,Integer>,Segment>();

	toPaintGreen = new ArrayList<Pair<Integer,Integer>>();
	
	for(int i = 0; i<this.getGroupCount(); i++)
		for(int j = 0 ; j<this.getChildrenCount(i);j++)
		{
			remember.put(new Pair<Integer,Integer>(i,j), this.getChild(i, j));
		}
	for(Pair<Integer,Integer> seg1: remember.keySet())
	for(Pair<Integer, Integer> seg : remember.keySet())
	{
		if(seg1.first.intValue() == seg.first.intValue() && seg1.second.intValue() == seg.second.intValue())
			continue;
		else{
			try{
			Segment child1 = remember.get(seg1);
			Segment child2 = remember.get(seg);
			if(child1.getArrival().getDatetime().equals(child2.getArrival().getDatetime()))
					{
						if(child1.getSegmentid().getCarrier()!=null && child2.getSegmentid().getCarrier()!=null){
							if(child1.getSegmentid().getCarrier().getName().equals(child2.getSegmentid().getCarrier().getName()))
							{
								if(child1.getSegmentid().getCarrier().getNumber().intValue() == child2.getSegmentid().getCarrier().getNumber().intValue()){
									if(child1.getArrival().getLocation().getDisplayname().equals(child2.getArrival().getLocation().getDisplayname()))	
									{
										Log.e("1: G:"+seg1.first+" C:"+seg1.second, "Date " + child1.getArrival().getDatetime()+" Name: " + child1.getSegmentid().getCarrier().getName()
												+" number: " + child1.getSegmentid().getCarrier().getNumber()+" displayName: "+child1.getArrival().getLocation().getDisplayname());
										Log.e("2: G:"+seg.first+" C:"+seg.second, "Date " + child2.getArrival().getDatetime()+" Name: " + child2.getSegmentid().getCarrier().getName()
												+" number: " + child2.getSegmentid().getCarrier().getNumber()+" displayName: "+child2.getArrival().getLocation().getDisplayname());
										toPaintGreen.add(seg1);
										toPaintGreen.add(seg);
									}
								}
							}
						}//gång
						if(child1.getSegmentid().getMot().getText().equals(child2.getSegmentid().getMot().getText()))
							{
								if(child1.getArrival().getLocation().getDisplayname().equals(child2.getArrival().getLocation().getDisplayname()))
								{
									if(child1.getDeparture().getLocation().getDisplayname().equals(child2.getDeparture().getLocation().getDisplayname()))
									{
									
										toPaintGreen.add(seg1);
										toPaintGreen.add(seg);
									}
								}
							}
						}
		}catch(Exception e){
			
		}
		}
		
			
	}
	
}
@Override
public Traveler getGroup(int groupPosition) {
        return itemList.get(groupPosition);
}

@Override
public int getGroupCount() {
        return itemList.size();
}

@Override
public long getGroupId(final int groupPosition) {
        return groupPosition;
}

@Override
public View getGroupView(int groupPosition, boolean isExpanded, View theConvertView, ViewGroup parent) {
	 

     View resultView = theConvertView;
    
        ViewHolder holder;

        if (resultView == null) {
        	LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	resultView = infalInflater.inflate(R.layout.tripresult_group, null);
        } 

        TextView byten = (TextView) resultView.findViewById(R.id.group_byten); //TODO change view id
        TextView Resinär =(TextView) resultView.findViewById(R.id.traveler_id);
        TextView tidFramme = (TextView) resultView.findViewById(R.id.group_framme);
        TextView totalRestid= (TextView) resultView.findViewById(R.id.group_restid);
        RelativeLayout layout =(RelativeLayout)resultView.findViewById(R.id.group_on_share);
        
        RelativeLayout changeBg =(RelativeLayout)resultView.findViewById(R.id.group_on_share_);
        ImageButton shareButton = (ImageButton)resultView.findViewById(R.id.imageButton1);
        
        final Traveler item = getGroup(groupPosition);
        Resinär.setText(item.getSteps().get(0).getDeparture().getLocation().getDisplayname());
        List<Segment> segments = item.getSteps();
        tidFramme.setText(item.getSteps().get(item.getSteps().size()-1).getArrival().getDatetime());
       //fix
        Calendar tripTime = Trip.getTripTime(item.getSteps());
        
        int hours = Trip.getTripTimeHours(item.getSteps());
        
        int min  = Trip.getTripTimeMin(item.getSteps());
        
//        totalRestid.setText(tripTime.get(Calendar.HOUR_OF_DAY)+" h "+tripTime.get(Calendar.MINUTE)+" min");
        totalRestid.setText(hours+" h "+min+" min");
        byten.setText(segments.size()+"");
     	 ShareOnClickListener s=  new ShareOnClickListener(context,item,layout,changeBg,shareButton);
     	 layout.setFocusable(false);

//        if(theConvertView==null)
//	       {
	    	   
//	    	   ObjectAnimator animator = ObjectAnimator.ofFloat(resultView, View.ALPHA, 0,1);
//	       animator.setDuration(350);
//
//	        animator.start();
//	       }
        return resultView;
}

static class ShareOnClickListener implements OnClickListener

{
	Context context;
	Traveler traveler;
	RelativeLayout layout;
	RelativeLayout parent;
	ImageButton button;
	public ShareOnClickListener(Context context,Traveler traveler,RelativeLayout parent, RelativeLayout layout,ImageButton button)
	{
		this.context = context;
		this.traveler = traveler;
		this.layout = layout;
		this.button = button;
		this.parent=parent;
		layout.setOnClickListener(this);
		button.setOnClickListener(this);
		parent.setOnClickListener(this);

		
	}

	@Override
	public void onClick(View v) {
		
		AlertDialog dialog = new AlertDialog.Builder(context).create();

		dialog.setTitle(context.getString(R.string.share_popup_string));
		android.content.DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

					if(which == AlertDialog.BUTTON_POSITIVE)
					{
						
						ShareModel.addToCalender(new ShareModel(traveler), context);
						
					}else
					{
						StringBuilder b = new StringBuilder();
						b.append("FRÅN: "+traveler.getSteps().get(0).getDeparture().getLocation().getDisplayname()+" "+traveler.getSteps().get(0).getDeparture().getDatetime()+"\n");
						for(Segment s : traveler.getSteps())
						{
							b.append("TA: "+s.getSegmentid().getMot().getText() + " " +(s.getSegmentid().getCarrier()==null?"":s.getSegmentid().getCarrier().getNumber().intValue())+" "+
						s.getDeparture().getDatetime()+"\n");
							b.append("TILL: "+s.getArrival().getLocation().getDisplayname()+" " + s.getArrival().getDatetime()+"\n");
						}
						final Intent sendIntent = new Intent();
						sendIntent.setAction(Intent.ACTION_SEND);
						sendIntent.putExtra(Intent.EXTRA_TEXT, "");
						sendIntent.setType("text/plain");
						context.startActivity(Intent.createChooser(sendIntent, context.getResources().getString(R.string.dela_resway)));
					}
				
			}
		}; 
		
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, "KALENDER", listener);
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ANNAT", listener);
		dialog.show();
		Log.d("Click on group", "!!!!!!!!!!!!!!");
		

		
		
	}

	
}


@Override
public boolean hasStableIds() {
        return true;
}

@Override
public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
}

}
