package io_commands;

import objectClasses.PlaceOfInterestSight;
import objectClasses.PlaceOfInterest.PlaceType;;

/**
 * @author sigal
 * treat create point of interest request from the client to the server
 */
public class CreatePOI extends Command
{

	/**
	 * @param cityId the city id of the point of interest
	 * @param name the name of the point of interest
	 * @param type which type is the point of interest
	 * @param placeDescription the description of the point of interest
	 * @param accessibilityToDisabled if the point of interest is accessibility or not 
	 * @param cdvId the city data version id of the point of interest 
	 */
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
