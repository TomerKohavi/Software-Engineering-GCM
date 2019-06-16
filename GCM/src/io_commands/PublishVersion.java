package io_commands;

import java.sql.SQLException;

import io_commands.Command;
import objectClasses.City;
import objectClasses.CityDataVersion;

public class PublishVersion extends Command
{
	public PublishVersion(City city, CityDataVersion cdv)
	{
		this.city = city;
		this.cdv = cdv;
	}

	public void delete()
	{
	}

	/**
	 * publishes version (use only on server)
	 * 
	 * @throws SQLException error on saving to database
	 */
	public void publish() throws SQLException
	{
		city._addPublishedCityDataVersion(cdv);
	}

	private City city;
	private CityDataVersion cdv;
}
