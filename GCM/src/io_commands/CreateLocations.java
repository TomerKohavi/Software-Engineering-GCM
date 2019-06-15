package io_commands;

import java.util.ArrayList;

import objectClasses.Location;

public class CreateLocations extends Command
{
	public CreateLocations(ArrayList<Location> locList)
	{
		this.locList = locList;
	}

	public void delete()
	{
		this.locList = null;
	}

	public ArrayList<Location> locList;
	public ArrayList<Integer> idList;
}
