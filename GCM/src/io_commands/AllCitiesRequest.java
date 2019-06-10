package io_commands;

import java.util.ArrayList;

import otherClasses.Pair;

/**
 * @author sigal
 * take all the cities from server
 */
public class AllCitiesRequest extends Command
{
	public void delete() {}
	
	public ArrayList<Pair<String,Integer>> cityList;
}
