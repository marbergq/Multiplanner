package com.berka.multiplanner.Models.Trips;

import java.text.ParseException;
import java.util.Calendar;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.berka.multiplanner.Helpers.TimeFilter;
import com.berka.multiplanner.Models.Interface.ITraveler;
import com.berka.multiplanner.Models.Travel.Segment;

public class ShareModel {

	private ITraveler traveler;
	
	public ShareModel(ITraveler traveler) {
		// TODO Auto-generated constructor stub
		this.traveler=traveler;
	}
	
	public ITraveler getTraveler()
	{return traveler;}
	
	static class CalendarObject{
		 long calID = 0;
		    String displayName ;
		    String accountName ;
		    String ownerName ; 
		    
		    @Override
		    public String toString()
		    {
		    	return displayName==null?"":displayName;
		    }
	}
	
	private  static Cursor getCalendarCrusor(Context context)
	
	{
		String[] EVENT_PROJECTION = new String[] {
			    BaseColumns._ID,                           // 0
			    Calendars.ACCOUNT_NAME,                  // 1
			    Calendars.CALENDAR_DISPLAY_NAME,         // 2
			    Calendars.OWNER_ACCOUNT                  // 3
			};
//		String[] EVENT_PROJECTION = new String[] {
//				 "_id", "name" 
//			};
			// The indices for the projection array above.
		
		
		Cursor  cursor  = null;
		ContentResolver cr = context.getContentResolver();
		Uri uri = Calendars.CONTENT_URI;
		String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND (" 
		                        + Calendars.ACCOUNT_TYPE + " = ?) AND ("
		                        + Calendars.OWNER_ACCOUNT + " = ?))";
		String[] selectionArgs = new String[] {"com.google"}; 
		// Submit the query and get a Cursor object back. 

		return cr.query(uri, EVENT_PROJECTION, null, null, null);
		
		
		
	}
	
	public static void addToCalender(ShareModel model,final Context context) {

		final ArrayAdapter<CalendarObject> items = setupDialogForSelectingCalendar(context,model);
		
		
		
		addCalendarObjects(context, items);
		
	}

	private static ArrayAdapter<CalendarObject> setupDialogForSelectingCalendar(
			final Context context,final ShareModel model) {
		final Dialog dialog = new Dialog(context);
		dialog.setTitle("KALENDER");
		
		final ArrayAdapter<CalendarObject> items = new ArrayAdapter<CalendarObject>(
				context, android.R.layout.simple_spinner_dropdown_item);
		
		items.setNotifyOnChange(true);
		final Button button = new Button(context);
		button.setText("OK");
		final Spinner spinner = new Spinner(context);
		spinner.setAdapter(items);

		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams( new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		layout.setOrientation(LinearLayout.VERTICAL);

		layout.addView(spinner,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		layout.addView(button,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int pos = spinner.getSelectedItemPosition();
				CalendarObject obj = items.getItem(pos);
				
				ContentResolver cr = context.getContentResolver();
				
				
				try {
					
					
					addCalendarEvent(model, obj, cr);
					
				} catch (ParseException e) {
					Toast.makeText(context, "Kunde inte spara i kalendern", Toast.LENGTH_LONG).show();
					return;
				}
				Toast.makeText(context, "Sparat i kalender", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				
			}
		});
		
		dialog.show();
		return items;
	}

	private static void addCalendarEvent(ShareModel model,CalendarObject obj,ContentResolver cr) throws ParseException
	{
		ContentValues values = new ContentValues();
		values.put(Events.CALENDAR_ID, obj.calID);
		Calendar beginTime;
	
		//but the standard values
		beginTime = TimeFilter.getDateFromString(model.getTraveler().getDeparture().getDatetime());
		values.put(Events.EVENT_TIMEZONE, beginTime.getTimeZone().getID());
	Calendar endTime = TimeFilter.getDateFromString(model.getTraveler().getLastSegment().getArrival().getDatetime());
	values.put(Events.DTSTART, beginTime.getTimeInMillis());
	values.put(Events.DTEND, endTime.getTimeInMillis());
	values.put(Events.TITLE, "Resa från: " + model.getTraveler().getDeparture().getLocation().getDisplayname()+
			" Till: " + model.getTraveler().getLastSegment().getArrival().getLocation().getDisplayname());
		
	StringBuilder string = new StringBuilder();
	for(Segment seg : model.getTraveler().getSteps())
			{
			
		String SPACE =" ";
			String BR = "\n";
			String FRÅN="FRÅN:"+SPACE;
			String TILL ="TILL:"+SPACE;
			
			//Om det är gång eller inte
			if(seg.getSegmentid().getCarrier()!=null)
				string.append(seg.getSegmentid().getCarrier().getName()+SPACE+seg.getSegmentid().getMot().getText()+SPACE+seg.getSegmentid().getCarrier().getNumber().intValue()+BR);
			else
				string.append(seg.getSegmentid().getMot().getText()+BR);
			
			string.append(FRÅN+seg.getDeparture().getLocation().getDisplayname()+SPACE+seg.getDeparture().getDatetime()+BR);
			string.append(TILL+seg.getArrival().getLocation().getDisplayname()+SPACE+seg.getArrival().getDatetime()+BR+BR);
		}
	values.put(Events.DESCRIPTION, string.toString()
			);
	 cr.insert(Events.CONTENT_URI, values);
	}

	private static void addCalendarObjects(final Context context,
			final ArrayAdapter<CalendarObject> items) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
	
		
		 final int PROJECTION_ID_INDEX = 0;
		 final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
		 final int PROJECTION_DISPLAY_NAME_INDEX = 2;
		 final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
		 final Cursor cursor = getCalendarCrusor(context);
		 
		while(cursor.moveToNext())
		{
			CalendarObject obj = new CalendarObject();
			      
			    // Get the field values
			    obj.calID = cursor.getLong(PROJECTION_ID_INDEX);
			    obj.displayName = cursor.getString(PROJECTION_DISPLAY_NAME_INDEX);
			    obj.accountName = cursor.getString(PROJECTION_ACCOUNT_NAME_INDEX);
			    obj.ownerName = cursor.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
			    
			    items.add(obj);
		}
		
		
		}
	};
	r.run();
	}

}
