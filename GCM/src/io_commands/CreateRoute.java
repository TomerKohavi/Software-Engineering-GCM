package io_commands;

import objectClasses.RouteSight;

public class CreateRoute extends Command
{

	public CreateRoute(int cityId, String info, int cdvId)
	{
		this.cityId = cityId;
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
	public RouteSight routeS;
}
