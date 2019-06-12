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

	public void delete()
	{
	}

	public Integer cityId;
	public Ops op;
}
