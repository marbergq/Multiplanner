package com.berka.multiplanner.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.berka.multiplanner.MainActivity;
import com.berka.multiplanner.Helpers.MyTimePicker24h;
import com.berka.multiplanner.Models.Abstraction.AbstractStop;
import com.berka.multiplanner.Models.Interface.ILocation;
import com.berka.multiplanner.Models.Interface.IStops;
import com.berka.multiplanner.Models.Trips.Location;
import com.berka.multiplanner.Models.autocomplete.Stop;
import com.berka.multiplanner.Network.AutoComplete;
import com.berka.multiplanner.Network.MultiPlannerAsyncTasks;
import com.berka.multiplanner.Planner.Planner;
import com.groupalpha.berka.multiplanner.R;

public class SearchFragment extends Fragment implements Observer {
	
	private static final String SharedPrefsName="MULTIPLANNERPREFS";
	private static final String PURCHACED_KEY ="PURCHASE";
	private static final String FIRST_START ="FIRST";
	int maximumSearchAllowed = 2;
	/**
	 * IS THIS THE FIRST START?
	 * @return
	 */
	private Boolean isFirstStart()
	{
		SharedPreferences settings = getActivity().getSharedPreferences(SharedPrefsName, 0);
		return settings.getBoolean(FIRST_START, true);
	}
	
	/**
	 * TO RUN IF THIS IS THE 
	 */
	private void initialStart()
	{
		SharedPreferences settings = getActivity().getSharedPreferences(SharedPrefsName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("PURCHASE", false);
		editor.commit();
	}
	
	private boolean hasPurchaced()
	{
		return true;
//		SharedPreferences settings = getActivity().getSharedPreferences(SharedPrefsName, 0);
//		if(settings.getBoolean(PURCHACED_KEY, false))
//		{
//			maximumSearchAllowed=4;
//			return true;
//		}else
//			return false;
	}
	
	/**
	 * SETUP INAPP BILLING (GET PEOPLE TO PAY FOR THE SERVICE)
	 */
	public void setupInAppBilling(final Planner planner)
	{
		AlertDialog d = new AlertDialog.Builder(getActivity()).create();
		d.setMessage("KÖPA APP?");
		d.setTitle("APPLIKATION INTE KÖPT!");
		
		d.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), "KÖPTE INTE!", Toast.LENGTH_LONG).show();
				// TODO Auto-generated method stub
				
			}
		});
		
	d.setButton(DialogInterface.BUTTON_POSITIVE, "BUY", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences settings = getActivity().getSharedPreferences(SharedPrefsName, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("PURCHASE", true);
				editor.commit();
				Toast.makeText(getActivity(), "KÖPT!", Toast.LENGTH_LONG).show();
			
				MainActivity act = (MainActivity) getActivity();
				act.getAdapter().ShowResults(planner);
			}
		});		

	d.show();
		
	}
	
	ArrayAdapter<ILocation> adapter;
	AutoComplete autocomp;
	Planner plan;
	Button okbutton;
	OnEditorActionListener listner;
	ArrayList<MultiAutoCompleteTextView> textViews;
	View rootView;
	MultiAutoCompleteTextView autoTo;
	TextView seekBarResult;
	SeekBar seek;
	ScrollView scrollView;

	public void DisplayLoading(Boolean bool) {
		if (!bool)
			rootView.findViewById(R.id.planner_loading)
					.setVisibility(View.GONE);
		else
			rootView.findViewById(R.id.planner_loading).setVisibility(
					View.VISIBLE);
	}
	
	

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	public Planner getPlanner()
	{
		return plan;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.plannerfragment, container,
				false);

		plan = new Planner();
		plan.addObserver(this);
		SetupDropdown(rootView);
		SetupOkButton(rootView);
		setupSearchDatumButton(rootView);
		MainActivity act = (MainActivity) getActivity();
		setupProgressSelector(rootView);
		setupScrollView(rootView);
		this.rootView = rootView;
		addFromMultTextView(rootView);
		DisplayLoading(false);
		textViews.get(0).requestFocus();
		return rootView;

	}
	
	private void addFromMultTextView(final View root)
	{
		Button button =(Button)root.findViewById(R.id.search_add_from);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			
				if(textViews.size()<8){
					LinearLayout scroll = (LinearLayout)root.findViewById(R.id.fromlayout);
					scroll.addView(createNewMultiCompleteTextView(),scroll.indexOfChild(textViews.get(textViews.size()-2) )+1 );
				}
				else
					Toast.makeText(getActivity(), "Maximaltantal resande är : 8", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	
	private void setupScrollView(View root) {
		// TODO Auto-generated method stub
		scrollView =(ScrollView) root.findViewById(R.id.scrollView1);
		
	}

	private void ScrollToView(View viewToScrollTo)
	{
	
		float getY=viewToScrollTo.getY();
		float height = viewToScrollTo.getHeight();
		float y = getY-height;
		if(y < 0)
			y=viewToScrollTo.getY();
		scrollView.smoothScrollTo((int)viewToScrollTo.getX(), (int)y);
	}


	private void setupProgressSelector(View rootView) {
		seek = (SeekBar) rootView.findViewById(R.id.search_progress_selector);
		seekBarResult = (TextView) rootView
				.findViewById(R.id.search_progress_result);

		seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (progress < 4) {
					seekBar.setProgress(5);
					return;
				}

				seekBarResult.setText(progress + "");
				plan.setAnkomstIntervall(progress);
			}
		});
		
	

	}

	private void setSeekbarValue(int value) {
		if (seek.getProgress() != value)
			seek.setProgress(value);
	}

	private void setupSearchDatumButton(View rootView) {
		final Calendar c = Calendar.getInstance();
		final Button button = (Button) rootView.findViewById(R.id.Search_datum);
		
		
			button.setText(plan.getDateString());
			button.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View v) { 
					// TODO Auto-generated method stub
	
					
					DatePickerDialog dialog = new DatePickerDialog(getActivity(),
							new OnDateSetListener() {
	
								@Override
								public void onDateSet(DatePicker view, int year,
										int monthOfYear, int dayOfMonth) {
									// TODO Auto-generated method stub
									plan.setYear(year);
									plan.setDay(dayOfMonth);
									plan.setMonth(getMonth(monthOfYear));
									button.setText(plan.getDateString());
									Log.d("DateSet", plan.getDateString());
								}
							}, plan.getYear(), plan.getMonth()-1, plan.getDay());
					dialog.show();
					
	
				}
	
			});
		
	}

	private int getMonth(int month) {
		switch (month) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
			return month + 1;
		case 12:
			return 1;

		}
		return 0;
	}

	private void enableButton() {
		int containing = 0;
		for (MultiAutoCompleteTextView view : textViews) {
			if (!view.getText().toString().isEmpty())
				containing++;
		}
		if(autoTo.getText().toString().isEmpty()){
			okbutton.setEnabled(false);
			return;
		}
		if (containing > 1){
				okbutton.setEnabled(true);
		}else okbutton.setEnabled(false);
	}

	private void goToResultFragment(Planner planner) {
		Boolean hasBought = hasPurchaced();
		if(!hasBought && plan.getFrom().size() > maximumSearchAllowed)
		{
			setupInAppBilling(planner);
		}
		else
		{
			MainActivity act = (MainActivity) getActivity();
			act.getAdapter().ShowResults(planner);
		}
	}

	private void SetupOkButton(View viewRoot) {
		okbutton = (Button) viewRoot.findViewById(R.id.searchbutton);
		final AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.create();
		final MyTimePicker24h time = setupTimePicker(viewRoot);
		enableButton();
		okbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onOkButtonClick(time);

			}

		});
		


	}

	private MyTimePicker24h setupTimePicker(View viewRoot) {
		final MyTimePicker24h time = (MyTimePicker24h) viewRoot
				.findViewById(R.id.toTimePicker);
		Calendar c= Calendar.getInstance();
		c.add(Calendar.MINUTE, 10);
		time.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
		time.setCurrentMinute(c.get(Calendar.MINUTE));
		return time;
	}

	private void onOkButtonClick(TimePicker time) {
		if (time != null) {
			plan.setHour(time.getCurrentHour());
			plan.setMinute(time.getCurrentMinute());

		}
		
		
		
		if(plan.getTo()==null || !plan.getTo().getDisplayname().equals(autoTo.getText().toString()))
		{
			displayMenadeDu();
			return;
		}
		if (plannerSameSizeAsTextViews() == 0)
			goToResultFragment(plan);
		else if (plannerSameSizeAsTextViews() == 1) {
			displayMenadeDu();
		}
		
		
	}

	private MultiAutoCompleteTextView getTextWithoutPlan() {
		if(plan.getTo()==null || !plan.getTo().getDisplayname().equals(autoTo.getText().toString()))
			return autoTo;
		if (plan.getFrom() == null)
			return textViews.get(0);
		else {
			for (int i = 0; i < plan.getFrom().size(); i++) {
				String text = textViews.get(i).getText().toString();
				String planText = plan.getFrom().get(i).getDisplayname();
				if (!text.equals(planText))
					return textViews.get(i);

			}
			for (int i = 0; i < textViews.size(); i++) {
				if (textViews.get(i).getText().toString().length() > 0) {
					if (i > (plan.getFrom().size() - 1))
						return textViews.get(i);
					else if (plan.getFrom().get(i).getDisplayname()
							.equals(textViews.get(i).getText().toString()))
						continue;
					else
						return textViews.get(i);
				}

			}
		}
	
		return null;

	}

	protected void displayMenadeDu() {

		final Dialog dialog = new Dialog(getActivity());
		final ArrayList<ILocation> results = new ArrayList<ILocation>();
		final ArrayAdapter<ILocation> items = new ArrayAdapter<ILocation>(
				getActivity(), android.R.layout.simple_spinner_dropdown_item);
		
		final Button button = new Button(getActivity());
		button.setText("OK");
		final Spinner spinner = new Spinner(getActivity());
		final MultiAutoCompleteTextView textView = getTextWithoutPlan();

		if (textView == null) {
			Toast.makeText(getActivity(), "didn't find any textview",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if(textView != autoTo)
			button.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					try {
						plan.addFrom(new Location(((Location) spinner.getSelectedItem()).getTheJSONBluePrint()));
					
					dialog.dismiss();
					textView.setText(((Location) spinner.getSelectedItem())
							.getDisplayname());
					} catch (JSONException e) {try{
						// TODO Auto-generated catch block
						textView.setText(((Location) spinner.getSelectedItem())
								.getDisplayname());
						plan.addFrom(new Stop(((Location) spinner.getSelectedItem()).getTheJSONBluePrint()));
					}catch(JSONException ex)
						{}
					}
					dialog.dismiss();
					onOkButtonClick(null);
	
				}
			});
		else
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						plan.setTo(new Location(((Location) spinner.getSelectedItem()).getTheJSONBluePrint()));
					
					dialog.dismiss();
					textView.setText(((Location) spinner.getSelectedItem())
							.getDisplayname());
					} catch (JSONException e) {try{
						// TODO Auto-generated catch block
						textView.setText(((ILocation) spinner.getSelectedItem())
								.getDisplayname());
						plan.setTo(new Stop(((ILocation) spinner.getSelectedItem()).getTheJSONBluePrint()));
					}catch(JSONException ex)
						{}
					}
					dialog.dismiss();
					onOkButtonClick(null);
	
				}
					
				
			});

		items.setNotifyOnChange(true);
		autocomp = new AutoComplete() {
			@Override
			public void onPostExecute(IStops result) {
				DisplayLoading(false);
				if (result == null || (result.getStops() == null) || result.getStops().size()==0)
					{
					textView.setSelection(0, textView.getText().length());
					textView.requestFocus();
					Toast.makeText(getActivity(), "Hittade ingen station vid namn: "+textView.getText(), Toast.LENGTH_SHORT).show();
					return;}
				for (ILocation x : result.getStops()) {
					results.add(x);
				}
				items.addAll(results);
				dialog.show();

			}
		};
		if (textView != null) {
			autocomp.execute(textView.getText().toString());
			dialog.setTitle(getActivity().getString(R.string.Did_you_mean)+textView.getText().toString());
			
			DisplayLoading(true);

			spinner.setAdapter(items);
			LinearLayout layout = new LinearLayout(getActivity());
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.addView(spinner, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			layout.addView(button, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}

	}

	protected Boolean doesPlanContainthis(String location) {
		if (plan.getFrom() == null)
			return false;
		else {
			for (ILocation l : plan.getFrom()) {
				if (l.getDisplayname().equals(location))
					return true;
			}
		}
		return false;
	}

	protected void setTextInTextViews(Location selectedItem) {
		if (plan.getFrom() != null) {
			// for(Location l : plan.getFrom())
			for (MultiAutoCompleteTextView view : textViews) {

				if (view.getText().toString().length() > 0
						&& !doesPlanContainthis(view.getText().toString())) {
					view.setText(selectedItem.getDisplayname());
					break;
				}
			}
		} else {
			textViews.get(0).setText(selectedItem.getDisplayname());
		}

	}

	private int plannerSameSizeAsTextViews() {
		int plancount = 0;
		int textViewCount = 0;
		if (plan.getFrom() != null)
			plancount = plan.getFrom().size();
		for (MultiAutoCompleteTextView textView : textViews) {
			if (!textView.getText().toString().isEmpty())
				textViewCount++;
		}
		if (plancount == textViewCount)
			return 0;
		else if (plancount > textViewCount)
			return -1;
		else
			return 1;

	}

	private void cancelAsyncTask() {
		if (autocomp != null) {
			autocomp.cancel(true);
			autocomp.cancelRequest();
		}

	}

	/***
	 * AsyncTaskHandler
	 */
	private void setupAsyncTask() {
//		MultiPlannerAsyncTasks.autoComplete(adapter);
//		autocomp = new AutoComplete() {
//			@Override
//			public void onPostExecute(IStop result) {
//				if (result == null || (result.getStops() == null) || result.getStops().size() == 0)
//					return;
//				adapter.clear();
//				adapter.addAll(result.getStops());
//				
//			}
//		};
	}

	private void setupTextViewDropdowns(MultiAutoCompleteTextView view) {

		view.setAdapter(adapter);
		adapter.setNotifyOnChange(true);
		view.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {

			@Override
			public CharSequence terminateToken(CharSequence arg0) {
				// TODO Auto-generated method stub

				return arg0;
			}

			@Override
			public int findTokenStart(CharSequence arg0, int arg1) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int findTokenEnd(CharSequence arg0, int arg1) {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		// view.setTokenizer(new SpaceTokenizer());
		view.setThreshold(3);

		setFromOnItemClick(view);

		view.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				if (plan.getFrom() != null)
					for (ILocation l : plan.getFrom()) {
						if (l.getDisplayname().equals(s.toString())) {
							plan.getFrom().remove(l);
							break;
						}
					}
				

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
//				for (int i = 0; i < adapter.getCount(); i++)
//					if (adapter.getItem(i).getDisplayname()
//							.equals(s.toString()))
//						return;
					
				MultiPlannerAsyncTasks.autoComplete(adapter, s.toString());
				Runnable r= new Runnable() {
					
					@Override
					public void run() {
						enableButton();
						
					}
				};
				r.run();
			}
		});
	

	}

	private void setFromOnItemClick(MultiAutoCompleteTextView view) {
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (((ILocation) parent.getItemAtPosition(position))
						.getLocationid().intValue() == -1)
					return;
				if (!plan.isThisFromAllreadyAdded(((ILocation) parent
						.getItemAtPosition(position)))) {
					plan.addFrom(adapter.getItem(position));

					// clicked.setText(text);
					enableButton();
				}
			}
		});
	}

	private void setToOnItemClick(MultiAutoCompleteTextView view) {
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				plan.setTo(((ILocation) parent.getItemAtPosition(position)));

				enableButton();
			}
		});
	}

	private void clearAdapter() {
		adapter.clear();
		Location l = new Location();
		l.setDisplayname("Loading..");
		l.setLocationid(-1);
		adapter.add(l);
	}

	
	private MultiAutoCompleteTextView createNewMultiCompleteTextView(){
		
		MultiAutoCompleteTextView view = new MultiAutoCompleteTextView(getActivity());
		view.setLayoutParams(textViews.get(0).getLayoutParams());
		view.setAdapter(adapter);
		setupTextViewDropdowns(view);
		view.setHint(getActivity().getString(R.string.from));
		textViews.add(view);
		setupImeTypesAndListner(view);
		setupfocusCHangeListener(view);
		
		return view;
		
	}
	
	private void SetupDropdown(View rootView) {
		adapter = new ArrayAdapter<ILocation>(getActivity(),
				android.R.layout.simple_dropdown_item_1line){
			
		}
				;
		Location l = new Location();
		l.setDisplayname("Loading");
		l.setLocationid(-1);
		adapter.add(l);
		textViews = new ArrayList<MultiAutoCompleteTextView>();
		adapter.setNotifyOnChange(true);

		// the from
		MultiAutoCompleteTextView auto1 = (MultiAutoCompleteTextView) rootView
				.findViewById(R.id.from1);
		MultiAutoCompleteTextView auto2 = (MultiAutoCompleteTextView) rootView
				.findViewById(R.id.from2);
		MultiAutoCompleteTextView auto3 = (MultiAutoCompleteTextView) rootView
				.findViewById(R.id.from3);
		MultiAutoCompleteTextView auto4 = (MultiAutoCompleteTextView) rootView
				.findViewById(R.id.from4);
		// the to
		 autoTo = (MultiAutoCompleteTextView) rootView
				.findViewById(R.id.to);

		// InitialSetup
		setupTextViewDropdowns(auto1);
		setupTextViewDropdowns(auto2);
		setupTextViewDropdowns(auto3);
		setupTextViewDropdowns(auto4);
		setupTextViewDropdowns(autoTo);
		// ClickListeners
		setFromOnItemClick(auto1);
		setFromOnItemClick(auto2);
		setFromOnItemClick(auto3);
		setFromOnItemClick(auto4);
		setToOnItemClick(autoTo);
		textViews.add(auto1);
		textViews.add(auto2);
		textViews.add(auto3);
		textViews.add(auto4);
		setupfocusCHangeListener(auto1);
		setupfocusCHangeListener(auto2);
		setupfocusCHangeListener(auto3);
		setupfocusCHangeListener(auto4);
		setupfocusCHangeListener(autoTo);
		
		setupListener();
		 		
			setupImeTypesAndListner(autoTo);
			for(int i=0;i < textViews.size();i++)
				setupImeTypesAndListner(textViews.get(i));
			
	}

	private void setupImeTypesAndListner(MultiAutoCompleteTextView view)
	{
		view.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		view.setRawInputType(InputType.TYPE_CLASS_TEXT); 
		view.setOnEditorActionListener(listner);
	}
	
	private void setupfocusCHangeListener(MultiAutoCompleteTextView view)
	{
		view.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if(hasFocus)
						ScrollToView(v);
					}
				});
	}
	
	private void setupListener()
	{
		if(listner==null)
		listner = new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				   
				Log.d("here2", "!!!");
	         if ( actionId == EditorInfo.IME_ACTION_NEXT )
	        {               
	        	 if(v.getId()==autoTo.getId())
	        	 {
		           // hide virtual keyboard
		           InputMethodManager imm = 
		              (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		           imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		          scrollView.smoothScrollTo(0, scrollView.getBottom());
		           return true;
	        	 }
	        	 else
	        	 {
	        		 if(textViews.indexOf(v)+1>=textViews.size())
	        			 autoTo.requestFocus();
	        		 else
	        			 textViews.get(textViews.indexOf(v)+1).requestFocus();
	        		 
//	        		 switch(textViews.indexOf(v))
//	        		 {
//		        		 case 0: textViews.get(1).requestFocus();break;
//		        		 case 1: textViews.get(2).requestFocus();break;	
//		        		 case 2: textViews.get(3).requestFocus();break;
//		        		 case 3: autoTo.requestFocus();break;
//	        		 }
	        		 
	        		 return true;
	        	 }
	        }
	        
	        return false;
			}
		};
	}
	
	

	@Override
	public void update(Observable arg0, Object arg1) {

		setSeekbarValue((Integer) arg1);

	}
}
