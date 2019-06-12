package io_commands;
import java.sql.Date;

import controller.InformationSystem.Ops;

public class AddStat extends Command
{
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
