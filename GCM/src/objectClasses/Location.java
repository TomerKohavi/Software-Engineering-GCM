package objectClasses;

import java.io.Serializable;
import java.sql.SQLException;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * Class of location object, a POI + cords in map
 * @author Ron Cohen
 */
@SuppressWarnings("serial")
public class Location implements ClassMustProperties, Serializable
{
	private int id;
	private int mapId;
	private int placeOfInterestId;
	private double[] coordinates;

	private PlaceOfInterest temp_place;

	/**
	 * This is a private constructor of location object
	 * 
	 * @param id                the id of the location
	 * @param mapId             the map id that contains the location
	 * @param placeOfInterestId the place of interest id that contains the location
	 * @param coordinates       the coordinates of the location
	 * @throws SQLException if the access to database failed
	 */
	private Location(int id, int mapId, int placeOfInterestId, double[] coordinates) throws SQLException
	{
		this.id = id;
		this.mapId = mapId;
		this.placeOfInterestId = placeOfInterestId;
		this.coordinates = coordinates;
		reloadTempsFromDatabase();
	}

	/**
	 * This function create location object according to all the inputs (supposed to
	 * be used only in Database)
	 * 
	 * @param id                the id of the location
	 * @param mapId             the map id that contains the location
	 * @param placeOfInterestId the place of interest id that contains the location
	 * @param coordinates       the coordinates of the location
	 * @return the new location object
	 * @throws SQLException if the access to database failed
	 */
	public static Location _createLocation(int id, int mapId, int placeOfInterestId, double[] coordinates) throws SQLException
	{ // friend
		// to
		// Database
		return new Location(id, mapId, placeOfInterestId, coordinates);
	}

	/**
	 * Create location by given map, point of interest and coordinates
	 * 
	 * @param m           the map
	 * @param p           the point of interest
	 * @param coordinates the coordinates of the location
	 * @throws SQLException if the access to database failed
	 */
	public Location(Map m, PlaceOfInterest p, double[] coordinates) throws SQLException
	{
		this.id = Database.generateIdLocation();
		this.mapId = m.getId();
		this.placeOfInterestId = p.getId();
		this.coordinates = coordinates;
		this.temp_place = p;
	}
	
	/**
	 * Create location by given map, point of interest and coordinates
	 * 
	 * @param m           the map
	 * @param p           the point of interest
	 * @param coordinates the coordinates of the location
	 */
	private Location(int id, int mapId, PlaceOfInterest p, double[] coordinates)
	{
		this.id = id;
		this.mapId = mapId;
		this.placeOfInterestId = p.getId();
		this.coordinates = coordinates;
		this.temp_place = p;
	}
	
	public static Location _createLocalLocation( PlaceOfInterest p, double[] coordinates)
	{
		return new Location(-1, -1, p, coordinates);
	}

	public void saveToDatabase() throws SQLException
	{
		Database._saveLocation(this);
	}

	public void deleteFromDatabase() throws SQLException
	{
		Database._deleteLocation(this.getId());
	}

	public void reloadTempsFromDatabase() throws SQLException
	{
		this.temp_place = Database.getPlaceOfInterestById(this.placeOfInterestId);
	}

	/**
	 * Return the location id
	 * 
	 * @return the location id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Return the map id
	 * 
	 * @return the map id
	 */
	public int getMapId()
	{
		return mapId;
	}

	/**
	 * Return the place of interest id
	 * 
	 * @return the place of interest id
	 */
	public int getPlaceOfInterestId()
	{
		return placeOfInterestId;
	}

	/**
	 * Return the coordinates of the location
	 * 
	 * @return the coordinates of the location
	 */
	public double[] getCoordinates()
	{
		return coordinates;
	}

	/**
	 * Return a copied place of interest
	 * 
	 * @return copied place of interest
	 */
	public PlaceOfInterest getCopyPlaceOfInterest()
	{
		return temp_place;
	}

	/**
	 * Sets the coordinates of the location
	 * 
	 * @param coordinates the new coordinates of the location
	 */
	public void setCoordinates(double[] coordinates)
	{
		this.coordinates = coordinates;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Location && ((Location) o).getId() == this.getId();
	}

	public void _setId(int id)
	{
		this.id = id;
	}
	
	public void _setMapId(int mapId)
	{
		this.mapId = mapId;
	}
}
