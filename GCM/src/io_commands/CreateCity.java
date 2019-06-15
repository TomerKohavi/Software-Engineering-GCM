package io_commands;

import objectClasses.City;
import objectClasses.CityDataVersion;

/**
 * @author sigal
 * Create city request from the client to the server
 */
public class CreateCity extends Command
{
	/**
	 * Constructor of the create city request
	 * @param name the city name
	 * @param info the city info
	 * @param priceOneTime the city price for one time buy
	 * @param pricePeriod the city price for a period
	 */
	public CreateCity(String name, String info, double priceOneTime, double pricePeriod)
	{
		this.name = name;
		this.info = info;
		this.priceOneTime = priceOneTime;
		this.pricePeriod = pricePeriod;
	}
	
	/**
	 * delete the request
	 */
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
