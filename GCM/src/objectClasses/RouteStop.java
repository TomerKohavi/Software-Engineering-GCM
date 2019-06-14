package objectClasses;

import java.io.Serializable;
import java.sql.Time;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * Class of a single route stop object
 * @author Ron Cohen
 *
 */
@SuppressWarnings("serial")
public class RouteStop implements Comparable<RouteStop>, ClassMustProperties, Serializable
{
	private int id;
	private int routeId;
	private int placeId;
	private int numStop;
	public Time recommendedTime;
	private String placeName;

	/**
	 * This is a private constructor of route stop object
	 * 
	 * @param id route stop id
	 * @param routeId route id that contains the route stop
	 * @param placeId place id of the route stop
	 * @param numStop the number of the stop in the route
	 * @param recommendedTime how much time is recommended for this stop
	 */
	private RouteStop(int id, int routeId, int placeId, int numStop, Time recommendedTime)
	{
		this.id = id;
		this.routeId = routeId;
		this.placeId = placeId;
		this.numStop = numStop;
		this.recommendedTime = recommendedTime;
		reloadTempsFromDatabase();
	}

	/**
	 * This function create route stop object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id route stop id
	 * @param routeId route id that contains the route stop
	 * @param placeId place id of the route stop
	 * @param numStop the number of the stop in the route
	 * @param recommendedTime how much time is recommended for this stop
	 * @return the new route stop object
	 */
	public static RouteStop _createRouteStop(int id, int routeId, int placeId, int numStop, Time recommendedTime)
	{ // friend to Database
		return new RouteStop(id, routeId, placeId, numStop, recommendedTime);
	}
	
	/**
	 * This is a private constructor of route stop object
	 * 
	 * @param id route stop id
	 * @param routeId route id that contains the route stop
	 * @param placeId place id of the route stop
	 * @param POIName the name of the place of interest
	 * @param numStop the number of the stop in the route
	 * @param recommendedTime how much time is recommended for this stop
	 */
	private RouteStop(int id, int routeId, int placeId, String POIName, int numStop, Time recommendedTime)
	{
		this.id = id;
		this.routeId = routeId;
		this.placeId = placeId;
		this.numStop = numStop;
		this.recommendedTime = recommendedTime;
		this.placeName = POIName;
	}

	/**
	 * This function create route stop object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id route stop id
	 * @param routeId route id that contains the route stop
	 * @param placeId place id of the route stop
	 * @param POIName the name of the place of interest
	 * @param numStop the number of the stop in the route
	 * @param recommendedTime how much time is recommended for this stop
	 * @return the new route stop object
	 */
	public static RouteStop _createRouteStop(int id, int routeId, int placeId, String POIName, int numStop, Time recommendedTime)
	{ // friend to Database
		return new RouteStop(id, routeId, placeId, POIName, numStop, recommendedTime);
	}


	/**
	 * This is the normal public constructor for route stop object
	 * 
	 * @param r the route contains the route stop
	 * @param p the place of interest that in the route stop
	 * @param recommendedTime the recommended time for the route stop
	 */
	public RouteStop(Route r, PlaceOfInterest p, Time recommendedTime)
	{
		this.id = Database.generateIdRouteStop();
		this.routeId = r.getId();
		this.placeId = p.getId();
		this.recommendedTime = recommendedTime;
		this.numStop = -1;
	}

	public void saveToDatabase()
	{
		Database._saveRouteStop(this);
	}

	public void deleteFromDatabase()
	{
		Database._deleteRouteStop(this.id);
	}

	public void reloadTempsFromDatabase()
	{
		this.placeName = Database.getPlaceOfInterestById(placeId).getName();
	}

	/**
	 * Return the route stop id
	 * 
	 * @return the route stop id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Sets the route stop id
	 * 
	 * @param id the new route stop id
	 */
	public void _setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * Return the point of interest name of the route stop 
	 * 
	 * @return the point of interest name of the route stop
	 */
	public String getPOIName()
	{
		return placeName;
	}
	
	/**
	 * Sets the point of interest name of the route stop 
	 * 
	 * @param POIName the new point of interest name of the route stop 
	 */
	public void setPlaceName(String POIName)
	{
		this.placeName = POIName;	
	}
	
	/**
	 * Return the name of the place of interest name
	 * 
	 * @return the name of the place of interest name
	 */
	public String getPlaceName()
	{
		return this.placeName;	
	}

	/**
	 * Sets the index of the route stop in the route
	 * 
	 * @param numStop the new index of the route stop in the route
	 */
	public void setNumStop(int numStop)
	{
		this.numStop = numStop;
	}

	/**
	 * Return the number of the stop in the whole route
	 * 
	 * @return the number of the stop in the whole route
	 */
	public int getNumStop()
	{
		return numStop;
	}

	/**
	 * Return the route id
	 * 
	 * @return the route id
	 */
	public int getRouteId()
	{
		return routeId;
	}

	/**
	 * Return the route
	 * 
	 * @return the route id
	 */
	public Route getRoute()
	{
		return Database.getRouteById(routeId);
	}

	/**
	 * Return the place of interest id
	 * 
	 * @return the place of interest id
	 */
	public int getPlaceId()
	{
		return placeId;
	}

	/**
	 * Return copy of the place of interest 
	 * 
	 * @return copy of the place of interest 
	 */
	public PlaceOfInterest getCopyPlace()
	{
		return Database.getPlaceOfInterestById(placeId);
	}

	/**
	 * Return the recommended time
	 * 
	 * @return the recommended time
	 */
	public Time getRecommendedTime()
	{
		return recommendedTime;
	}

	/**
	 * Sets the recommended time
	 * 
	 * @param recommendedTime the new recommended time
	 */
	public void setRecommendedTime(Time recommendedTime)
	{
		this.recommendedTime = recommendedTime;
	}

	@Override
	public int compareTo(RouteStop o)
	{
		return this.numStop - o.numStop;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof RouteStop && ((RouteStop) o).getId() == this.getId();
	}

	/**
	 * Sets the route id
	 * 
	 * @param routeId the new route id
	 */
	public void _setRouteId(int routeId)
	{
		this.routeId = routeId;
	}
}
