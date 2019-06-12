package io_commands;

import java.util.ArrayList;

public class FetchSights extends Command
{
	public FetchSights(int cdvId, Class<?> sightType)
	{
		this.cdvId = cdvId;
		this.sightType = sightType;
	}

	public void delete()
	{
		this.cdvId = null;
	}

	public ArrayList<?> sightList;
	public Integer cdvId;
	public Class<?> sightType;
}
