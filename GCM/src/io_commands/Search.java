package io_commands;

import java.util.ArrayList;

import objectClasses.City;

/**
 * @author sigal
 * send search request to the server and return the result to the client
 */
public class Search extends Command
{
	/**
	 * Constructor
	 * @param cityName the name of the city we search
	 * @param cityInfo the info of the city we search
	 * @param poiName the name of the point of interest
	 * @param poiInfo the info of the point of interest
	 * @param useUnpublished if we use unpublished or not
	 */
	public Search(String cityName, String cityInfo, String poiName, String poiInfo,boolean useUnpublished)
	{
		this.cityName = checkEmpty(cityName);
		this.cityInfo = checkEmpty(cityInfo);
		this.poiName = checkEmpty(poiName);
		this.poiInfo = checkEmpty(poiInfo);
		this.useUnpublished=useUnpublished;
	}

	/**
	 * delete the request
	 */
	public void delete()
	{
		cityName = cityInfo = poiName = poiInfo = null;
	}

	/**
	 * convert from empty string to null
	 * @param s the string we want to convert
	 * @return the type after convert
	 */
	public static String checkEmpty(String s)
	{
		return s.equals("") ? null : s;
	}

	public String cityName, cityInfo, poiName, poiInfo;
	public boolean useUnpublished;
	public ArrayList<City> searchResult;
}
