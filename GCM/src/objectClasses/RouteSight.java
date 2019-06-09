package objectClasses;

import java.io.Serializable;

import controller.Database;
import otherClasses.ClassMustProperties;

public class RouteSight implements ClassMustProperties, Serializable {
	private int id;
	private int cityDataVersionId;
	private int routeId;
	private boolean isFavorite;

	private Route temp_route;

	private RouteSight(int id, int cityDataVersionId, int routeId,boolean isFavorite) {
		this.id = id;
		this.cityDataVersionId = cityDataVersionId;
		this.routeId = routeId;
		this.isFavorite=isFavorite;
		reloadTempsFromDatabase();
	}

	public static RouteSight _createRouteSight(int id, int cityDataVersionId, int routeId,boolean isFavorite) { // friend to Database
		return new RouteSight(id, cityDataVersionId, routeId,isFavorite);
	}

	public RouteSight(CityDataVersion cdv, Route r,boolean isFavorite) {
		this.id = Database.generateIdRouteSight();
		this.cityDataVersionId = cdv.getId();
		this.routeId = r.getId();
		this.isFavorite=isFavorite;
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

	public int getId() {
		return id;
	}

	public int getCityDataVersionId() {
		return cityDataVersionId;
	}

	public int getRouteId() {
		return routeId;
	}

	public Route getCopyRoute() {
		return temp_route;
	}

	public boolean getIsFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean favorite) {
		isFavorite = favorite;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof RouteSight && ((RouteSight) o).getId() == this.getId();
	}
}
