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

	private PlaceOfInterestSight(int id, int cityDataVersionId, int placeOfInterestId) {
		this.id = id;
		this.cityDataVersionId = cityDataVersionId;
		this.placeOfInterestId = placeOfInterestId;
		reloadTempsFromDatabase();
	}

	public static PlaceOfInterestSight _PlaceOfInterestSight(int id, int cityDataVersionId, int placeOfInterestId) { // friend
																														// to
																														// Database
		return new PlaceOfInterestSight(id, cityDataVersionId, placeOfInterestId);
	}

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

	public int getId() {
		return id;
	}

	public int getCityDataVersionId() {
		return cityDataVersionId;
	}

	public int getPlaceOfInterestId() {
		return placeOfInterestId;
	}

	public PlaceOfInterest getCopyPlace() {
		return temp_place;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof PlaceOfInterestSight && ((PlaceOfInterestSight) o).getId() == this.getId();
	}
}
