package objectClasses;

import java.io.Serializable;

import controller.Database;
import otherClasses.ClassMustProperties;

public class MapSight implements ClassMustProperties, Serializable {
	private int id;
	private int mapId;
	private int cityDataVersionId;

	Map temp_map;

	private MapSight(int id, int mapId, int cityDataVersionId) {
		this.id = id;
		this.mapId = mapId;
		this.cityDataVersionId = cityDataVersionId;
		reloadTempsFromDatabase();
	}

	public static MapSight _createMapSight(int id, int mapId, int cityDataVersionId) { // friend to Database
		return new MapSight(id, mapId, cityDataVersionId);
	}

	public MapSight(CityDataVersion cdv, Map m) {
		this.id = Database.generateIdMapSight();
		this.mapId = m.getId();
		this.cityDataVersionId = cdv.getId();
		this.temp_map = m;
	}

	public void saveToDatabase() {
		Database._saveMapSight(this);
	}

	public void deleteFromDatabase() {
		Database._deleteMapSight(this.id);
	}

	public void reloadTempsFromDatabase() {
		this.temp_map = Database.getMapById(mapId);
	}

	public Map getCopyMap() {
		return temp_map;
	}

	public int getId() {
		return id;
	}

	public int getMapId() {
		return mapId;
	}

	public int getCityDataVersionId() {
		return cityDataVersionId;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof MapSight && ((MapSight) o).getId() == this.getId();
	}
}
