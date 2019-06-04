import java.io.Serializable;

public class RouteSight implements ClassMustProperties, Serializable {
	private int id;
	private int cityDataVersionId;
	private int routeId;

	private Route temp_route;

	private RouteSight(int id, int cityDataVersionId, int routeId) {
		this.id = id;
		this.cityDataVersionId = cityDataVersionId;
		this.routeId = routeId;
		reloadTempsFromDatabase();
	}

	public static RouteSight _createRouteSight(int id, int cityDataVersionId, int routeId) { // friend to Database
		return new RouteSight(id, cityDataVersionId, routeId);
	}

	public RouteSight(CityDataVersion cdv, Route r) {
		this.id = Database.generateIdRouteSight();
		this.cityDataVersionId = cdv.getId();
		this.routeId = r.getId();
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

	@Override
	public boolean equals(Object o) {
		return o instanceof RouteSight && ((RouteSight) o).getId() == this.getId();
	}
}
