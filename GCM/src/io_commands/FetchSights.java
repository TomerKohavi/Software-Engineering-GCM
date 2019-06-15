package io_commands;

import java.util.ArrayList;

/**
 * @author sigal
 * Fetch sights request from the client to the server
 */
public class FetchSights extends Command
{
	/**
	 * The constructor of the request
	 * @param cdvId the city data version id
	 * @param sightType the type of the sight
	 */
	public FetchSights(int cdvId, Class<?> sightType)
	{
		this.cdvId = cdvId;
		this.sightType = sightType;
	}

	/**
	 * delete the request
	 */
	public void delete()
	{
		this.cdvId = null;
	}

	public ArrayList<?> sightList;
	public Integer cdvId;
	public Class<?> sightType;
}
