package objectClasses;

import java.io.Serializable;
import java.util.ArrayList;

import controller.Database;
import otherClasses.ClassMustProperties;

public class PlaceOfInterest implements ClassMustProperties, Serializable {
	public enum PlaceType {
		HISTORICAL(0), MUSEUM(1), HOTEL(2), RESTAURANT(3), PUBLIC(4), PARK(5), STORE(6), CINEMA(7);

		private final int value;

		PlaceType(final int nv) {
			value = nv;
		}

		public int getValue() {
			return value;
		}


		@Override
		public String toString() {
			switch (this)
			{
				case HISTORICAL: return "Historical";
				case MUSEUM: return "Museum";
				case HOTEL: return "HOTEL";
				case RESTAURANT: return "Restaurant";
				case PUBLIC: return "Public";
				case PARK: return "Park";
				case STORE: return "Store";
				case CINEMA: return "Cinema";
				default: return "unknown type";
			}
		}
	}

	private int id;
	private int cityId;
	private String name;
	private PlaceType type;
	private String placeDescription;
	private boolean accessibilityToDisabled;

	private PlaceOfInterest(int id, int cityId, String name, PlaceType type, String placeDescription,
			boolean accessibilityToDisabled) {
		this.id = id;
		this.cityId = cityId;
		this.name = name;
		this.type = type;
		this.placeDescription = placeDescription;
		this.accessibilityToDisabled = accessibilityToDisabled;
	}

	public static PlaceOfInterest _createPlaceOfInterest(int id, int cityId, String name, PlaceType type,
			String placeDescription, boolean accessibilityToDisabled) { // friend to Database
		return new PlaceOfInterest(id, cityId, name, type, placeDescription, accessibilityToDisabled);
	}

	public PlaceOfInterest(int cityId, String name, PlaceType type, String info, boolean accessibilityToDisabled) {
		this.id = Database.generateIdPlaceOfInterest();
		this.cityId = cityId;
		this.name = name;
		this.type = type;
		this.placeDescription = info;
		this.accessibilityToDisabled = accessibilityToDisabled;
	}

	public void saveToDatabase() {
		Database.savePlaceOfInterest(this);
	}

	public void deleteFromDatabase() {
		Database.deletePlaceOfInterest(this.id);
		// delete PlacesSights
		ArrayList<Integer> ids = Database.searchPlaceOfInterestSight(null, this.id);
		for (int id : ids) {
			PlaceOfInterestSight ps = Database._getPlaceOfInterestSightById(id);
			if (ps != null)
				ps.deleteFromDatabase();
		}
		// delete locations
		ids = Database.searchLocation(null, this.id);
		for (int id : ids) {
			Location l = Database._getLocationById(id);
			if (l != null)
				l.deleteFromDatabase();
		}
		// delete routeStops
		ids = Database.searchRouteStop(null, this.id, null);
		for (int id : ids) {
			RouteStop rs = Database._getRouteStopById(id);
			if (rs != null)
				rs.deleteFromDatabase();
		}
	}

	public void reloadTempsFromDatabase() {
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public PlaceType getType() {
		return type;
	}

	public String getPlaceDescription() {
		return placeDescription;
	}

	public boolean isAccessibilityToDisabled() {
		return accessibilityToDisabled;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(PlaceType type) {
		this.type = type;
	}

	public void setPlaceDescription(String placeDescription) {
		this.placeDescription = placeDescription;
	}

	public void setAccessibilityToDisabled(boolean accessibilityToDisabled) {
		this.accessibilityToDisabled = accessibilityToDisabled;
	}

	public int getCityId() {
		return cityId;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof PlaceOfInterest && ((PlaceOfInterest) o).getId() == this.getId();
	}

	@Override
	public String toString() {
		return this.name+":\n- id: "+this.id+"\n- type: "+this.type+"\n- description: "+this.placeDescription+
				"- accessibility to disabled: "+this.accessibilityToDisabled+"\n";
	}
}
