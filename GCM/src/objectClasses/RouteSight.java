package objectClasses;

import java.io.Serializable;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * Class of Route sight object
 * (an object solves the many to many issue with route and city version)
 * @author Ron Cohen
 *
 */
@SuppressWarnings("serial")
public class RouteSight implements ClassMustProperties, Serializable {
	private int id;
	private int cityDataVersionId;
	private int routeId;
	private boolean isFavorite;

	private Route temp_route;

	/**
	 * This is a private constructor of route sight object
	 * 
	 * @param id route sight id
	 * @param cityDataVersionId the city data version id
	 * @param routeId the route id
	 * @param isFavorite if the route sight is favorite or not
	 */
	private RouteSight(int id, int cityDataVersionId, int routeId,boolean isFavorite) {
		this.id = id;
		this.cityDataVersionId = cityDataVersionId;
		this.routeId = routeId;
		this.isFavorite=isFavorite;
		reloadTempsFromDatabase();
	}

	/**
	 * This function create route sight object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id route sight id
	 * @param cityDataVersionId the city data version id
	 * @param routeId the route id
	 * @param isFavorite if the route sight is favorite or not
	 * @return new route sight object
	 */
	public static RouteSight _createRouteSight(int id, int cityDataVersionId, int routeId,boolean isFavorite) { // friend to Database
		return new RouteSight(id, cityDataVersionId, routeId,isFavorite);
	}

	/**
	 * This is the normal public constructor for route sight object
	 * 
	 * @param cdvId the city data version id
	 * @param r the route object
	 * @param isFavorite if the route sight is favorite or not
	 */
	public RouteSight(int cdvId, Route r,boolean isFavorite) {
		this.id = Database.generateIdRouteSight();
		this.cityDataVersionId = cdvId;
		this.routeId = r.getId();
		this.isFavorite = isFavorite;
		this.temp_route = r;
	}

	public void saveToDatabase() {
		Database._saveRouteSight(this);
	}

	public void deleteFromDatabase() {
		Database._deleteRouteSight(this.id);
	}

	public void reloadTempsFromDatabase() {
		this.temp_route = Database.getRouteById(routeId);
	}

	/**
	 * Returns the route sight id
	 * 
	 * @return the route sight id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the city data version id of the route sight
	 * 
	 * @return the city data version id of the route sight
	 */
	public int getCityDataVersionId() {
		return cityDataVersionId;
	}

	/**
	 * Returns the route id
	 * 
	 * @return the route id
	 */
	public int getRouteId() {
		return routeId;
	}

	/**
	 * Returns copied route object
	 * 
	 * @return copied route object
	 */
	public Route getCopyRoute() {
		return temp_route;
	}

	/**
	 * Returns if the route sight is favorite or not
	 * 
	 * @return if the route sight is favorite or not
	 */
	public boolean getIsFavorite() {
		return isFavorite;
	}

	/**
	 * Sets the route sight favorite
	 * 
	 * @param cityName the new favorite of the route sight
	 */
	public void setFavorite(boolean favorite) {
		isFavorite = favorite;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof RouteSight && ((RouteSight) o).getId() == this.getId();
	}
}
