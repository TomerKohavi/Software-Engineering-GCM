import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Route implements ClassMustProperties, Serializable {
	private int id;
	private int cityId;
	private String info;

	ArrayList<RouteStop> temp_routeStops;

	ArrayList<RouteStop> temp_removeRouteStops;

	private Route(int id, int cityId, String info) {
		this.id = id;
		this.cityId = cityId;
		this.info = info;
		reloadTempsFromDatabase();
	}

	public static Route _createRoute(int id, int cityId, String info) { // friend to
																											// Database
		return new Route(id, cityId, info);
	}

	public Route(int cityId, String info) {
		this.id = Database.generateIdRoute();
		this.cityId = cityId;
		this.info = info;
		this.temp_routeStops = new ArrayList<>();
		this.temp_removeRouteStops = new ArrayList<>();
	}

	private ArrayList<RouteStop> generateRouteStops() {
		ArrayList<Integer> routeStopsIds = Database.searchRouteStop(this.id, null, null);
		ArrayList<RouteStop> arrList = new ArrayList<RouteStop>();
		for (int rdId : routeStopsIds)
			arrList.add(Database._getRouteStopById(rdId));
		Collections.sort(arrList);
		for (int i = 0; i < arrList.size(); i++)
			arrList.get(i).setNumStop(i);
		return arrList;
	}

	public void saveToDatabase() {
		Database.saveRoute(this);
		// delete removes
		for (RouteStop rs : temp_removeRouteStops) {
			if (!temp_routeStops.contains(rs))
				rs.deleteFromDatabase();
		}
		this.temp_removeRouteStops = new ArrayList<>();
		// save list
		for (RouteStop rs : temp_routeStops)
			rs.saveToDatabase();
	}

	public void deleteFromDatabase() {
		Database.deleteRoute(this.id);
		for (RouteStop rs : temp_removeRouteStops)
			rs.deleteFromDatabase();
		this.temp_removeRouteStops = new ArrayList<>();
		for (RouteStop rs : temp_routeStops)
			rs.deleteFromDatabase();
		// delete all routeSights
		ArrayList<Integer> ids = Database.searchRouteSight(null, this.id, null);
		for (int id : ids) {
			RouteSight rs = Database._getRouteSightById(id);
			if (rs != null)
				rs.deleteFromDatabase();
		}
	}

	public void reloadTempsFromDatabase() {
		this.temp_routeStops = generateRouteStops();
		this.temp_removeRouteStops = new ArrayList<>();
	}

	public boolean addRouteStop(RouteStop rs) {
		if (rs.getRouteId() != this.id || rs.getCopyPlace().getCityId() != this.getCityId())
			return false;
		rs.setNumStop(temp_routeStops.size());
		temp_routeStops.add(rs);
		return true;
	}

	public boolean addRouteStop(RouteStop rs, int index) {
		if (rs.getRouteId() != this.id || rs.getCopyPlace().getCityId() != this.getCityId())
			return false;
		temp_routeStops.add(index, rs);
		rs.setNumStop(index);
		for (int i = index; i < temp_routeStops.size(); i++)
			temp_routeStops.get(i).setNumStop(i);
		return true;
	}

	public RouteStop getRouteStopByPlaceId(int placeId) {
		for (RouteStop rs : temp_routeStops) {
			if (rs.getPlaceId() == placeId)
				return rs;
		}
		return null;
	}

	public RouteStop getRouteStopAtNum(int num) {
		if (num < 0 || num >= temp_routeStops.size())
			return null;
		return temp_routeStops.get(num);
	}

	public RouteStop getRouteStopById(int rsId) {
		for (RouteStop rs : temp_routeStops) {
			if (rs.getId() == rsId)
				return rs;
		}
		return null;
	}

	public RouteStop removeRouteStopAtIndex(int index) {
		if (index < 0 || index >= temp_routeStops.size())
			return null;
		RouteStop rs = temp_routeStops.remove(index);
		for (int i = index; i < temp_routeStops.size(); i++)
			temp_routeStops.get(i).setNumStop(i);
		temp_removeRouteStops.add(rs);
		return rs;
	}

	public ArrayList<RouteStop> getCopyRouteStops() {
		return new ArrayList<>(temp_routeStops);
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getId() {
		return id;
	}

	public String getInfo() {
		return info;
	}

	public boolean isAcceptabilityToDisabled() {
		for(RouteStop rs:temp_routeStops){
			if(!rs.getCopyPlace().isAccessibilityToDisabled())
				return false;
		}
		return true;
	}

	public int getNumStops() {
		return temp_routeStops.size();
	}

	public int getCityId() {
		return cityId;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Route && ((Route) o).getId() == this.getId();
	}
}
