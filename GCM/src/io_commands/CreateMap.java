package io_commands;

import objectClasses.MapSight;

public class CreateMap extends Command
{
	public CreateMap(int cityId, String name, String info, String imgURL, int cdvId)
	{
		this.cityId = cityId;
		this.name = name;
		this.info = info;
		this.imgURL = imgURL;
		this.cdvId = cdvId;
	}
	
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
