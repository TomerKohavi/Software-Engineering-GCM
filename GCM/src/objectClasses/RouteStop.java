package objectClasses;

import java.io.Serializable;
import java.sql.Time;

import controller.Database;
import otherClasses.ClassMustProperties;

public class RouteStop implements Comparable<RouteStop>, ClassMustProperties, Serializable
{
	private int id;
	private int routeId;
	private int placeId;
	private int numStop;
	public Time recommendedTime;

	private RouteStop(int id, int routeId, int placeId, int numStop, Time recommendedTime)
	{
		this.id = id;
		this.routeId = routeId;
		this.placeId = placeId;
		this.numStop = numStop;
		this.recommendedTime = recommendedTime;
	}

	public static RouteStop _createRouteStop(int id, int routeId, int placeId, int numStop, Time recommendedTime)
	{ // friend to Database
		return new RouteStop(id, routeId, placeId, numStop, recommendedTime);
	}

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
	}

	public int getId()
	{
		return id;
	}
	
	public void _setId(int id)
	{
		this.id = id;
	}

	public void setNumStop(int numStop)
	{
		this.numStop = numStop;
	}

	public int getNumStop()
	{
		return numStop;
	}

	public int getRouteId()
	{
		return routeId;
	}

	public Route getRoute()
	{
		return Database.getRouteById(routeId);
	}

	public int getPlaceId()
	{
		return placeId;
	}

	public PlaceOfInterest getCopyPlace()
	{
		return Database.getPlaceOfInterestById(placeId);
	}

	public Time getRecommendedTime()
	{
		return recommendedTime;
	}

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
}
