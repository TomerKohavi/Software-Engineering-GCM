package objectClasses;

import java.io.Serializable;
import java.util.ArrayList;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * class of place of interest object
 * @author Ron Cohen
 *
 */
@SuppressWarnings("serial")
public class PlaceOfInterest implements ClassMustProperties, Serializable {
	/**
	 * enum that contains the place types 
	 * @author Ron Cohen
	 *
	 */
	public enum PlaceType {
		HISTORICAL(0), MUSEUM(1), HOTEL(2), RESTAURANT(3), PUBLIC(4), PARK(5), STORE(6), CINEMA(7);

		private final int value;

		/**
		 * Constructor that get value and return the enum 
		 * 
		 * @param nv value of the enum
		 */
		PlaceType(final int nv) {
			value = nv;
		}

		/**
		 * Return the value of the enum object
		 * 
		 * @return the value of the enum object
		 */
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

	/**
	 * This is a private constructor of place of interest object
	 * 
	 * @param id the place of interest id
	 * @param cityId the city id that contains the place of interest
	 * @param name the place of interest name
	 * @param type the place of interest type
	 * @param placeDescription the place of interest description
	 * @param accessibilityToDisabled if the place of interest is accessibility to disabled or not
	 */
	private PlaceOfInterest(int id, int cityId, String name, PlaceType type, String placeDescription,
			boolean accessibilityToDisabled) {
		this.id = id;
		this.cityId = cityId;
		this.name = name;
		this.type = type;
		this.placeDescription = placeDescription;
		this.accessibilityToDisabled = accessibilityToDisabled;
	}

	/**
	 * This function create place of interest object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id the place of interest id
	 * @param cityId the city id that contains the place of interest
	 * @param name the place of interest name
	 * @param type the place of interest type
	 * @param placeDescription the place of interest description
	 * @param accessibilityToDisabled if the place of interest is accessibility to disabled or not
	 * @return the new place of interest object
	 */
	public static PlaceOfInterest _createPlaceOfInterest(int id, int cityId, String name, PlaceType type,
			String placeDescription, boolean accessibilityToDisabled) { // friend to Database
		return new PlaceOfInterest(id, cityId, name, type, placeDescription, accessibilityToDisabled);
	}

	/**
	 * This is the normal public constructor for City object
	 * 
	 * @param cityId the city id that contains the place of interest
	 * @param name the place of interest name
	 * @param type the place of interest type
	 * @param info the place of interest info
	 * @param accessibilityToDisabled if the place of interest is accessibility to disabled or not
	 */
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

	/**
	 * Returns the place of interest id
	 * 
	 * @return the place of interest id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the place of interest name
	 * 
	 * @return the place of interest name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the place of interest place type
	 * 
	 * @return the place of interest place type
	 */
	public PlaceType getType() {
		return type;
	}

	/**
	 * Returns the place of interest description
	 * 
	 * @return the place of interest description
	 */
	public String getPlaceDescription() {
		return placeDescription;
	}

	/**
	 * Returns if the point of interest is accessibility to disabled
	 * 
	 * @return the city id
	 */
	public boolean isAccessibilityToDisabled() {
		return accessibilityToDisabled;
	}

	/**
	 * Sets the point of interest name
	 * 
	 * @param name the point of interest name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the point of interest place type
	 * 
	 * @param type the point of interest place type
	 */
	public void setType(PlaceType type) {
		this.type = type;
	}

	/**
	 * Sets the point of interest description 
	 * 
	 * @param placeDescription the point of interest description 
	 */
	public void setPlaceDescription(String placeDescription) {
		this.placeDescription = placeDescription;
	}

	/**
	 * Sets the point of interest accessibility to disabled 
	 * 
	 * @param accessibilityToDisabled the point of interest accessibility to disabled
	 */
	public void setAccessibilityToDisabled(boolean accessibilityToDisabled) {
		this.accessibilityToDisabled = accessibilityToDisabled;
	}

	/**
	 * Returns the city id that contains the place of interest
	 * 
	 * @return the city id that contains the place of interest
	 */
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
