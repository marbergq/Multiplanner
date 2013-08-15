package com.berka.multiplanner.Factories;

import com.berka.multiplanner.Factories.Interface.IModel;
import com.berka.multiplanner.Factories.Interface.IURL;

public class AbstractFactory  {
	public static final int AUTOCOMPLETE=0,SEARCH = 1;
	public static IURL getIURLFactory()
	{
		return new URLFactory();
	}
	
	public static IModel getModelFactory()
	{
		return new ModelFactory();
	}
	
}
