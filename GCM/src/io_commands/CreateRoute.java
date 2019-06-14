package io_commands;

import objectClasses.RouteSight;

/**
 * @author sigal
 * treat create route request from the client to the server
 */
public class CreateRoute extends Command
{

	/**
	 * @param cityId the city id of the route
	 * @param info the info of the route
	 * @param cdvId city data version id of the route
	 */
	public CreateRoute(int cityId, String name, String info, int cdvId)
	{
		this.cityId = cityId;
		this.name = name;
		this.info = info;
		this.cdvId = cdvId;
	}

	public void delete()
	{
		this.cityId = null;
		this.info = null;
	}

	public Integer cityId, cdvId;
	public String info;
	public String name;
	public RouteSight routeS;
}
