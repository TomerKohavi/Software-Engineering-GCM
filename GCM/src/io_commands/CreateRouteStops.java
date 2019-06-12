package io_commands;

import java.util.ArrayList;

import objectClasses.RouteStop;

/**
 * @author sigal
 * treat create route stops request from the client to the server
 */
public class CreateRouteStops extends Command
{
	
	/**
	 * @param stopList list of route stop to handle
	 */
	public CreateRouteStops(ArrayList<RouteStop> stopList)
	{
		this.stopList = stopList;
	}
	
	public void delete()
	{
		stopList = null;
	}
	
	public ArrayList<Integer> idList;
	public ArrayList<RouteStop> stopList;
}
