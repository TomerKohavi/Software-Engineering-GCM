package io_commands;
import java.sql.Date;

import controller.InformationSystem.Ops;

/**
 * @author sigal
 * treat add statistics request
 */
public class AddStat extends Command
{
	/**
	 * @param cityId the city id of the statistics
	 * @param op the kind of the statistics
	 */
	public AddStat(int cityId, Ops op)
	{
		this.cityId = cityId;
		this.op = op;
	}
	
	public AddStat(int cityId, int numMaps)
	{
		this.cityId = cityId;
		this.numMaps=numMaps;
		this.op = null;
	}

	public void delete()
	{
	}

	public Integer cityId;
	public Ops op;
	public Integer numMaps;
}
