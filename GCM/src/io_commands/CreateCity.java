package io_commands;

import objectClasses.City;
import objectClasses.CityDataVersion;

public class CreateCity extends Command
{
	public CreateCity(String name, String info, double priceOneTime, double pricePeriod)
	{
		this.name = name;
		this.info = info;
		this.priceOneTime = priceOneTime;
		this.pricePeriod = pricePeriod;
	}
	
	public void delete()
	{
		name = info = null;
		priceOneTime = pricePeriod = null;
	}
	
	public City city;
	public CityDataVersion cdv;
	
	public String name, info;
	public Double priceOneTime, pricePeriod;
}
