package objectClasses;

import java.io.Serializable;
import java.sql.SQLException;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * Class of object Map sight 
 * (an object solves the many to many issue with map and city version)
 * @author Ron Cohen
 *
 */
@SuppressWarnings("serial")
public class MapSight implements ClassMustProperties, Serializable {
	private int id;
	private int mapId;
	private int cityDataVersionId;

	Map temp_map;

	/**
	 * This is a private constructor of City object
	 * 
	 * @param id the id of the map sight 
	 * @param mapId the id of the map
	 * @param cityDataVersionId the id of the city data version
	 * @throws SQLException if the access to database failed
	 */
	private MapSight(int id, int mapId, int cityDataVersionId) throws SQLException {
		this.id = id;
		this.mapId = mapId;
		this.cityDataVersionId = cityDataVersionId;
		reloadTempsFromDatabase();
	}

	/**
	 * This function create map sight object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id the id of the map sight 
	 * @param mapId the id of the map
	 * @param cityDataVersionId the id of the city data version
	 * @return the new map sight object
	 * @throws SQLException if the access to database failed
	 */
	public static MapSight _createMapSight(int id, int mapId, int cityDataVersionId) throws SQLException { // friend to Database
		return new MapSight(id, mapId, cityDataVersionId);
	}

	/**
	 * This is the normal public constructor for map sight object
	 * 
	 * @param cdvId the id of the city data version
	 * @param m the map object
	 * @throws SQLException if the access to database failed
	 */
	public MapSight(int cdvId, Map m) throws SQLException {
		this.id = Database.generateIdMapSight();
		this.mapId = m.getId();
		this.cityDataVersionId = cdvId;
		this.temp_map = m;
	}

	public void saveToDatabase() throws SQLException {
		Database._saveMapSight(this);
	}

	public void deleteFromDatabase() throws SQLException {
		Database._deleteMapSight(this.id);
	}

	public void reloadTempsFromDatabase() throws SQLException {
		this.temp_map = Database.getMapById(mapId);
	}

	/**
	 * Return copy of the map
	 * 
	 * @return copied map object
	 */
	public Map getCopyMap() {
		return temp_map;
	}

	/**
	 * Return the id of the map sight
	 * 
	 * @return the id of the map sight
	 */
	public int getId() {
		return id;
	}

	/**
	 * Return the id of the map 
	 * 
	 * @return the id of the map 
	 */
	public int getMapId() {
		return mapId;
	}

	/**
	 * Return the id of the city data version
	 * 
	 * @return the id of the city data version
	 */
	public int getCityDataVersionId() {
		return cityDataVersionId;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof MapSight && ((MapSight) o).getId() == this.getId();
	}
}
