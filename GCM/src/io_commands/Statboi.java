package io_commands;

import java.sql.Date;

import objectClasses.Statistic;


/**
 * @author sigal
 * treat statistics request from the client to the server and return the result
 */
public class Statboi extends Command
{
	/**
	 * Constructor.
	 * @param cityId the city id of the statistics 
	 * @param from from when that statistics is start to be taken
	 * @param end until when that statistics is taken
	 */
	public Statboi(Integer cityId,Date from,Date end)
	{
		this.cityId = cityId;
		this.from = from;
		this.end = end;
	}
	
	/**
	 * delete the request
	 */
	public void delete()
	{
		cityId = null;
		from = end = null;
	}
	
	public Statistic statboi;
	public Integer cityId;
	public Date from, end;
}
