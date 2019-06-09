package io_commands;

import java.util.ArrayList;

import objectClasses.City;

public class Search extends Command
{
	public Search(String cityName, String cityInfo, String poiName, String poiInfo)
	{
		this.cityName = checkEmpty(cityName);
		this.cityInfo = checkEmpty(cityInfo);
		this.poiName = checkEmpty(poiName);
		this.poiInfo = checkEmpty(poiInfo);
	}

	public void delete()
	{
		cityName = cityInfo = poiName = poiInfo = null;
	}

	public static String checkEmpty(String s)
	{
		return s.equals("") ? null : s;
	}

	public String cityName, cityInfo, poiName, poiInfo;
	public ArrayList<City> searchResult;
}
