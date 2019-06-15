package io_commands;

import objectClasses.MapSight;

/**
 * @author sigal
 * treat create map request from the client to the server
 */
public class CreateMap extends Command
{
	/**
	 * Constructor.
	 * @param cityId the city id of the map
	 * @param name the name of the map
	 * @param info the info of the map
	 * @param imgURL the path of the image in the map
	 * @param cdvId the city data version of the map 
	 */
	public CreateMap(int cityId, String name, String info, String imgURL, int cdvId)
	{
		this.cityId = cityId;
		this.name = name;
		this.info = info;
		this.imgURL = imgURL;
		this.cdvId = cdvId;
	}
	
	/**
	 * delete the request
	 */
	public void delete() {
		this.cityId = null;
		this.name = null;
		this.info = null;
		this.imgURL = null;
	}
	
	public Integer cityId, cdvId;
	public String name, info, imgURL;
	public MapSight mapS;
	//
}
