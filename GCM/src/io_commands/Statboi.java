package io_commands;

import java.sql.Date;

import objectClasses.Statistic;


public class Statboi extends Command
{
	public Statboi(Integer cityId,Date from,Date end)
	{
		this.cityId = cityId;
		this.from = from;
		this.end = end;
	}
	
	public void delete()
	{
		cityId = null;
		from = end = null;
	}
	
	public Statistic statboi;
	public Integer cityId;
	public Date from, end;
}
