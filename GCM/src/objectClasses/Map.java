package objectClasses;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * class of map object
 * 
 * @author Ron Cohen
 *
 */
@SuppressWarnings("serial")
public class Map implements ClassMustProperties, Serializable
{
	private int id;
	private int cityId;
	private String name;
	private String info;
	private String imgURL;

	private ArrayList<Location> temp_locations;
	private ArrayList<Location> temp_removeLocations;

	/**
	 * This is a private constructor of map object
	 * 
	 * @param id     the map id
	 * @param cityId the id of the city that contains the map
	 * @param name   the name of the map
	 * @param info   the info of the map
	 * @param imgURL the path of the image that in the map
	 * @throws SQLException if the access to database failed
	 */
	private Map(int id, int cityId, String name, String info, String imgURL) throws SQLException
	{
		this.id = id;
		this.cityId = cityId;
		this.name = name;
		this.info = info;
		this.imgURL = imgURL;
		reloadTempsFromDatabase();
	}

	/**
	 * This function create map object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id     the map id
	 * @param cityId the id of the city that contains the map
	 * @param name   the name of the map
	 * @param info   the info of the map
	 * @param imgURL the path of the image that in the map
	 * @return the new map object
	 * @throws SQLException if the access to database failed
	 */
	public static Map _createMap(int id, int cityId, String name, String info, String imgURL) throws SQLException
	{ // friend to Database
		return new Map(id, cityId, name, info, imgURL);
	}

	/**
	 * This is the normal public constructor for City object
	 * 
	 * @param cityId the id of the city that contains the map
	 * @param name   the name of the map
	 * @param info   the info of the map
	 * @param imgURL the path of the image that in the map
	 * @throws SQLException if the access to database failed
	 */
	public Map(int cityId, String name, String info, String imgURL) throws SQLException
	{
		this.id = Database.generateIdMap();
		this.cityId = cityId;
		this.name = name;
		this.info = info;
		this.imgURL = imgURL;
		this.temp_locations = new ArrayList<>();
		this.temp_removeLocations = new ArrayList<>();
	}

	/**
	 * Copy constructor of class map with new id
	 * 
	 * @param other map object to copy
	 * @throws SQLException if the access to database failed
	 */
	public Map(Map other) throws SQLException
	{
		this.id = Database.generateIdMap();
		this.cityId = other.cityId;
		this.name = other.name;
		this.info = other.info;
		this.imgURL = other.imgURL;
		this.temp_locations = new ArrayList<>();
		for (Location l : other.getCopyLocations())
		{
			Location newL = new Location(this, l.getCopyPlaceOfInterest(), l.getCoordinates());
			temp_locations.add(newL);
		}
		this.temp_removeLocations = new ArrayList<>();
	}

	/**
	 * Returns a list of locations in the map
	 * 
	 * @return list of locations in the map
	 * @throws SQLException if the access to database failed
	 */
	private ArrayList<Location> generateLocations() throws SQLException
	{
		ArrayList<Integer> ids = Database.searchLocation(this.id, null);
		ArrayList<Location> arrList = new ArrayList<Location>();
		for (int id : ids)
		{
			Location o = Database._getLocationById(id);
			if (o == null)
				continue;
			if (Database.getPlaceOfInterestById(o.getPlaceOfInterestId()) == null)
				Database._deleteLocation(id);
			else
				arrList.add(o);
		}
		return arrList;
	}

	public void saveToDatabase() throws SQLException
	{
		Database.saveMap(this);
		// delete removes
		for (Location l : temp_removeLocations)
		{
			if (!temp_locations.contains(l))
				l.deleteFromDatabase();
		}
		this.temp_removeLocations = new ArrayList<>();
		// save locations
		for (Location l : temp_locations)
			l.saveToDatabase();
	}

	public void deleteFromDatabase() throws SQLException
	{
		Database.deleteMap(this.id);
		// delete removes
		for (Location l : temp_removeLocations)
			l.deleteFromDatabase();
		this.temp_removeLocations = new ArrayList<>();
		for (Location l : temp_locations)
			l.deleteFromDatabase();
		// delete all mapSights
		ArrayList<Integer> ids = Database.searchMapSight(null, this.id);
		for (int id : ids)
		{
			Map m = Database.getMapById(id);
			if (m != null)
				m.deleteFromDatabase();
		}
	}

	public void reloadTempsFromDatabase() throws SQLException
	{
		this.temp_locations = generateLocations();
		this.temp_removeLocations = new ArrayList<>();
	}

	/**
	 * Returns the number of locations in the map
	 * 
	 * @return the number of locations in the map
	 */
	public int getNumLocations()
	{
		return temp_locations.size();
	}

	/**
	 * Return location object of the point of interest with given id
	 * 
	 * @param placeOfInterestId the id of the place of interest we want to look at
	 * @return the location that of the place of interest
	 */
	public Location getLocationByPlaceOfInterestId(int placeOfInterestId)
	{
		for (Location l : temp_locations)
		{
			if (l.getPlaceOfInterestId() == placeOfInterestId)
				return l;
		}
		return null;
	}

	/**
	 * Return location object by given id
	 * 
	 * @param locId the id of the location we search
	 * @return location object
	 */
	public Location getLocationById(int locId)
	{
		for (Location l : temp_locations)
		{
			if (l.getId() == locId)
				return l;
		}
		return null;
	}

	/**
	 * Add new location to the map and return if the location was added successfully
	 * 
	 * @param l location object to add
	 * @return true if the location is added successfully
	 */
	public boolean addLocation(Location l)
	{
		if (l.getCopyPlaceOfInterest().getCityId() != this.cityId || l.getMapId() != this.id)
			return false;
		temp_locations.add(l);
		return true;
	}

	/**
	 * Remove location from the map by given id and return him
	 * 
	 * @param locId id of location to remove
	 * @return the location object that removed
	 */
	public Location removeLocationById(int locId)
	{
		for (Location l : new ArrayList<>(temp_locations))
		{
			if (l.getId() == locId)
			{
				temp_locations.remove(l);
				temp_removeLocations.add(l);
				return l;
			}
		}
		return null;
	}

	/**
	 * Returns the map id
	 * 
	 * @return the map id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Returns the name of the map
	 * 
	 * @return the name of the map
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns the info of the map
	 * 
	 * @return the info of the map
	 */
	public String getInfo()
	{
		return info;
	}

	/**
	 * Sets the name of the map
	 * 
	 * @param name the new name of the map
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Sets the map info
	 * 
	 * @param info the new map info
	 */
	public void setInfo(String info)
	{
		this.info = info;
	}

	/**
	 * Returns the image path of the map
	 * 
	 * @return image path of the map
	 */
	public String getImgURL()
	{
		return imgURL;
	}

	/**
	 * Sets the path of the image in the map
	 * 
	 * @param imgURL the new image path of the map
	 */
	public void setImgURL(String imgURL)
	{
		this.imgURL = imgURL;
	}

	/**
	 * Returns the city id
	 * 
	 * @return the city id
	 */
	public int getCityId()
	{
		return cityId;
	}

	/**
	 * Returns list of copied locations
	 * 
	 * @return list of copied locations that in the map
	 */
	public ArrayList<Location> getCopyLocations()
	{
		return new ArrayList<>(temp_locations);
	}

	/**
	 * sets the locations list
	 * 
	 * @param locList locations list
	 */
	public void _setLocationsList(ArrayList<Location> locList)
	{
		this.temp_locations = locList;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Map && ((Map) o).getId() == this.getId();
	}
}