package io_commands;

import java.util.ArrayList;

import objectClasses.RouteStop;

public class CreateRouteStops extends Command
{
	
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
