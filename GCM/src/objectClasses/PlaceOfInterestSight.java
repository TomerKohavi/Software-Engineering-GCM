package objectClasses;

import java.io.Serializable;
import java.util.ArrayList;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * Class of place of interest sight obect
 * (an object solves the many to many issue with POI and city version)
 * @author Ron Cohen
 *
 */
@SuppressWarnings("serial")
public class PlaceOfInterestSight implements ClassMustProperties, Serializable {
	private int id;
	private int cityDataVersionId;
	private int placeOfInterestId;

	private PlaceOfInterest temp_place;

	/**
	 * This is a private constructor of place of interest sight object
	 * 
	 * @param id the id of the place of interest sight
	 * @param cityDataVersionId the city data version id that contains the place of interest sight
	 * @param placeOfInterestId the place of interest id
	 */
	private PlaceOfInterestSight(int id, int cityDataVersionId, int placeOfInterestId) {
		this.id = id;
		this.cityDataVersionId = cityDataVersionId;
		this.placeOfInterestId = placeOfInterestId;
		reloadTempsFromDatabase();
	}

	/**
	 * This function create place of interest sight object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id the id of the place of interest sight
	 * @param cityDataVersionId the city data version id that contains the place of interest sight
	 * @param placeOfInterestId the place of interest id
	 * @return new place of interest sight object
	 */
	public static PlaceOfInterestSight _PlaceOfInterestSight(int id, int cityDataVersionId, int placeOfInterestId) { // friend
																														// to
																														// Database
		return new PlaceOfInterestSight(id, cityDataVersionId, placeOfInterestId);
	}

	/**
	 * This is the normal public constructor for place of interest sight object
	 * 
	 * @param cdvId city data version id
	 * @param p place of interest object
	 */
	public PlaceOfInterestSight(int cdvId, PlaceOfInterest p) {
		this.id = Database.generateIdPlaceOfInterestSight();
		this.cityDataVersionId = cdvId;
		this.placeOfInterestId = p.getId();
		this.temp_place = p;
	}

	public void saveToDatabase() {
		Database._savePlaceOfInterestSight(this);
	}

	public void deleteFromDatabase() {
		Database._deletePlaceOfInterestSight(this.id);
		// delete locations
		ArrayList<Integer> mapSIds = Database.searchMapSight(this.cityDataVersionId,null);
		for (int mId : mapSIds) {
			MapSight ms=Database._getMapSightById(mId);
			if(ms==null) continue;
			ArrayList<Integer> locationIds=Database.searchPlaceOfInterestSight(ms.getMapId(), this.placeOfInterestId);
			for(int id:locationIds) {
				Location l = Database._getLocationById(id);
				if (l != null)
					l.deleteFromDatabase();
			}
		}
		// delete routeStops
		ArrayList<Integer> routeSIds = Database.searchRouteSight(this.cityDataVersionId,null,null);
		for (int rsId : routeSIds) {
			RouteSight rs=Database._getRouteSightById(rsId);
			if(rs==null) continue;
			Route r=rs.getCopyRoute();
			if(r==null) continue;
			for(RouteStop rStop:r.getCopyRouteStops())
			{
				if (rStop != null)
					rStop.deleteFromDatabase();
			}
		}
	}

	public void reloadTempsFromDatabase() {
		this.temp_place = Database.getPlaceOfInterestById(placeOfInterestId);
	}

	/**
	 * Returns the place of interest sight id
	 * 
	 * @return the place of interest sight id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the city data version id that contains the point of interest sight
	 * 
	 * @return the city data version id that contains the point of interest sight
	 */
	public int getCityDataVersionId() {
		return cityDataVersionId;
	}

	/**
	 * Returns the place of interest id
	 * 
	 * @return the place of interest id
	 */
	public int getPlaceOfInterestId() {
		return placeOfInterestId;
	}

	/**
	 * Returns place of interest copied object
	 * 
	 * @return place of interest copied object
	 */
	public PlaceOfInterest getCopyPlace() {
		return temp_place;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof PlaceOfInterestSight && ((PlaceOfInterestSight) o).getId() == this.getId();
	}
}
