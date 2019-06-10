package io_commands;

import objectClasses.PlaceOfInterestSight;
import objectClasses.PlaceOfInterest.PlaceType;;

public class CreatePOI extends Command
{

	public CreatePOI(int cityId, String name, PlaceType type, String placeDescription, boolean accessibilityToDisabled,
			int cdvId)
	{
		this.cityId = cityId;
		this.name = name;
		this.type = type;
		this.placeDescription = placeDescription;
		this.accessibilityToDisabled = accessibilityToDisabled;
		this.cdvId = cdvId;
	}

	public void delete()
	{
		this.cityId = null;
		this.name = null;
		this.type = null;
		this.placeDescription = null;
		this.accessibilityToDisabled = null;

	}

	public Integer cityId, cdvId;
	public PlaceType type;
	public String name, placeDescription;
	public Boolean accessibilityToDisabled;
	public PlaceOfInterestSight poiS;
}
