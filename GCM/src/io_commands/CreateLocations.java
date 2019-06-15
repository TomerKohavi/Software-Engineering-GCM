package io_commands;

import java.util.ArrayList;

import objectClasses.Location;

/**
 * @author sigal
 * Create location request from the client to the server
 */
public class CreateLocations extends Command
{
	/**
	 * The constructor of the location request
	 * @param locList list of location to create
	 */
	public CreateLocations(ArrayList<Location> locList)
	{
		this.locList = locList;
	}

	/**
	 * delete the request
	 */
	public void delete()
	{
		this.locList = null;
	}

	public ArrayList<Location> locList;
	public ArrayList<Integer> idList;
}
