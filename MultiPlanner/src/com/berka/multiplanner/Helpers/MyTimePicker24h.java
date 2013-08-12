package com.berka.multiplanner.Helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TimePicker;

public class MyTimePicker24h extends TimePicker {

	public MyTimePicker24h(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public MyTimePicker24h(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public MyTimePicker24h(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init()
	{
		setIs24HourView(true);
	}
}
