package objectClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * Class of Route object
 * @author Ron Cohen
 *
 */
@SuppressWarnings("serial")
public class Route implements ClassMustProperties, Serializable
{
	private int id;
	private int cityId;
	private String name;
	private String info;

	ArrayList<RouteStop> temp_routeStops;

	ArrayList<RouteStop> temp_removeRouteStops;

	/**
	 * This is a private constructor of route object
	 * 
	 * @param id the route id
	 * @param cityId the city id that contains the route
	 * @param name the route name
	 * @param info the route info
	 */
	private Route(int id, int cityId,String name, String info)
	{
		this.id = id;
		this.cityId = cityId;
		this.name=name;
		this.info = info;
		reloadTempsFromDatabase();
	}

	/**
	 * This function create route object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id the route id
	 * @param cityId the city id that contains the route
	 * @param name the route name
	 * @param info the route info
	 * @return new route object
	 */
	public static Route _createRoute(int id, int cityId,String name, String info)
	{ // friend to
		// Database
		return new Route(id, cityId,name, info);
	}

	/**
	 * This is the normal public constructor for City object
	 * 
	 * @param cityId the city id that contains the route
	 * @param name the route name
	 * @param info the route info
	 */
	public Route(int cityId,String name, String info)
	{
		this.id = Database.generateIdRoute();
		this.cityId = cityId;
		this.name=name;
		this.info = info;
		this.temp_routeStops = new ArrayList<>();
		this.temp_removeRouteStops = new ArrayList<>();
	}
	
	/**
	 * Copy constructor that generate new id
	 * 
	 * @param other route object to copy
	 */
	public Route(Route other) {
		this.id = Database.generateIdRoute();
		this.cityId = other.cityId;
		this.info = other.info;
		this.name=other.name;
		this.temp_routeStops = new ArrayList<>();
		for(RouteStop rs:other.getCopyRouteStops()) {
			RouteStop newRs=new RouteStop(this, rs.getCopyPlace(), rs.getRecommendedTime());
			temp_routeStops.add(newRs);
		}
		this.temp_removeRouteStops = new ArrayList<>();
	}

	/**
	 * Return list of route stops that in the route
	 * 
	 * @return list of route stops that in the route
	 */
	private ArrayList<RouteStop> generateRouteStops()
	{
		ArrayList<Integer> routeStopsIds = Database.searchRouteStop(this.id, null, null);
		ArrayList<RouteStop> arrList = new ArrayList<RouteStop>();
		for (int rdId : routeStopsIds)
		{
			RouteStop rss = Database._getRouteStopById(rdId);
			if (rss == null)
				continue;
			arrList.add(rss);
		}
		Collections.sort(arrList);
		for (int i = 0; i < arrList.size(); i++)
			arrList.get(i).setNumStop(i);
		return arrList;
	}

	public void saveToDatabase()
	{
		Database.saveRoute(this);
		// delete removes
		for (RouteStop rs : temp_removeRouteStops)
		{
			if (!temp_routeStops.contains(rs))
				rs.deleteFromDatabase();
		}
		this.temp_removeRouteStops = new ArrayList<>();
		// save list
		for (RouteStop rs : temp_routeStops)
			rs.saveToDatabase();
	}

	public void deleteFromDatabase()
	{
		Database.deleteRoute(this.id);
		for (RouteStop rs : temp_removeRouteStops)
			rs.deleteFromDatabase();
		this.temp_removeRouteStops = new ArrayList<>();
		for (RouteStop rs : temp_routeStops)
			rs.deleteFromDatabase();
		// delete all routeSights
		ArrayList<Integer> ids = Database.searchRouteSight(null, this.id, null);
		for (int id : ids)
		{
			RouteSight rs = Database._getRouteSightById(id);
			if (rs != null)
				rs.deleteFromDatabase();
		}
	}

	public void reloadTempsFromDatabase()
	{
		this.temp_routeStops = generateRouteStops();
		this.temp_removeRouteStops = new ArrayList<>();
	}

	/**
	 * Add new route stop to the route
	 * @param rs route stop object to add
	 * @return if the new route stop was added successfully
	 */
	public boolean addRouteStop(RouteStop rs)
	{
		if (rs.getRouteId() != this.id || rs.getCopyPlace().getCityId() != this.getCityId())
			return false;
		rs.setNumStop(temp_routeStops.size());
		temp_routeStops.add(rs);
		return true;
	}

	/**
	 * Set all the route stops to given route stop list
	 * @param stopList list of route stops
	 */
	public void setRouteStops(ArrayList<RouteStop> stopList)
	{
		this.temp_routeStops = stopList;
	}

	/**
	 * Add new route stop to the route in spesific place
	 * 
	 * @param rs route stop object to add
	 * @param index where in the route t add the new route stop
	 * @return if the new route stop was added successfully
	 */
	public boolean addRouteStop(RouteStop rs, int index)
	{
		if (rs.getRouteId() != this.id || rs.getCopyPlace().getCityId() != this.getCityId())
			return false;
		temp_routeStops.add(index, rs);
		rs.setNumStop(index);
		for (int i = index; i < temp_routeStops.size(); i++)
			temp_routeStops.get(i).setNumStop(i);
		return true;
	}

	/**
	 * Returns route stop object that contain the place with the given id
	 * 
	 * @param placeId the id of the place we are looking for
	 * @return route stop object 
	 */
	public RouteStop getRouteStopByPlaceId(int placeId)
	{
		for (RouteStop rs : temp_routeStops)
		{
			if (rs.getPlaceId() == placeId)
				return rs;
		}
		return null;
	}

	/**
	 * Return route stop object of the route stop in given index in the route
	 * @param num the place of the route stop we are searching
	 * @return route stop object in the specific place in the route
	 */
	public RouteStop getRouteStopAtNum(int num)
	{
		if (num < 0 || num >= temp_routeStops.size())
			return null;
		return temp_routeStops.get(num);
	}

	/**
	 * Return route stop object with the given id
	 * @param rsId route stop id to search
	 * @return route stop object with the given id
	 */
	public RouteStop getRouteStopById(int rsId)
	{
		for (RouteStop rs : temp_routeStops)
		{
			if (rs.getId() == rsId)
				return rs;
		}
		return null;
	}

	/**
	 * Remove route stop in given index in the route and return this route stop
	 * @param index index of route stop to remove
	 * @return route stop object that was removed
	 */
	public RouteStop removeRouteStopAtIndex(int index)
	{
		if (index < 0 || index >= temp_routeStops.size())
			return null;
		RouteStop rs = temp_routeStops.remove(index);
		for (int i = index; i < temp_routeStops.size(); i++)
			temp_routeStops.get(i).setNumStop(i);
		temp_removeRouteStops.add(rs);
		return rs;
	}

	/**
	 * Return list of copied route stops
	 * @return list of copied route stops
	 */
	public ArrayList<RouteStop> getCopyRouteStops()
	{
		return new ArrayList<>(temp_routeStops);
	}

	/**
	 * Sets the route info
	 * 
	 * @param info the new route info
	 */
	public void setInfo(String info)
	{
		this.info = info;
	}

	/**
	 * Return the route id
	 * 
	 * @return the route id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Return the route info
	 * 
	 * @return the route info
	 */
	public String getInfo()
	{
		return info;
	}

	/**
	 * Return true if the route is acceptability to disabled
	 * 
	 * @return true if the route is acceptability to disabled
	 */
	public boolean isAcceptabilityToDisabled()
	{
		int counter = 0;
		for (RouteStop rs : temp_routeStops)
		{
			if (rs.getCopyPlace() != null)
			{
				counter++;
				if (!rs.getCopyPlace().isAccessibilityToDisabled())
					return false;
			}
		}
		System.out.println(counter + " vs " + temp_routeStops.size());
		return true;
	}

	/**
	 * Return the number of the stops in the route
	 * 
	 * @return the number of the stops in the route
	 */
	public int getNumStops()
	{
		return temp_routeStops.size();
	}
	
	/**
	 * Return the route name
	 * 
	 * @return the route name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the route name
	 * 
	 * @param name the new route name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the city id that contains the route
	 * 
	 * @return the city id that contains the route
	 */
	public int getCityId()
	{
		return cityId;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Route && ((Route) o).getId() == this.getId();
	}
}
