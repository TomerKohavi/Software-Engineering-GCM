package io_commands;

import java.util.ArrayList;

import objectClasses.City;

public class Search extends Command {
	public Search(String cityName, String cityInfo, String poiName, String poiInfo)
	{
		this.cityName = cityName;
		this.cityInfo = cityInfo;
		this.poiName = poiName;
		this.poiInfo = poiInfo;
	}
	
	public void delete()
	{
		cityName = cityInfo = poiName = poiInfo = null;
	}
	

	public String cityName, cityInfo, poiName, poiInfo;
	public ArrayList<City> searchResult;
}
