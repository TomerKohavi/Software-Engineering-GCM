
package objectClasses;
import java.io.Serializable;
import java.util.ArrayList;

import controller.Database;
import otherClasses.ClassMustProperties;

public class CityDataVersion implements ClassMustProperties, Serializable {
	private int id;
	private int cityId;
	private String versionName;
	private double priceOneTime;
	private double pricePeriod;

	private ArrayList<PlaceOfInterestSight> temp_placeSights;
	private ArrayList<PlaceOfInterestSight> temp_removePlaceSights;
	private ArrayList<MapSight> temp_mapSights;
	private ArrayList<MapSight> temp_removeMapSights;
	private ArrayList<RouteSight> temp_routeSights;
	private ArrayList<RouteSight> temp_removeRouteSights;

	private CityDataVersion(int id, int cityId, String versionName, double priceOneTime, double pricePeriod) {
		this.id = id;
		this.cityId = cityId;
		this.versionName = versionName;
		this.priceOneTime = priceOneTime;
		this.pricePeriod = pricePeriod;
		reloadTempsFromDatabase();
	}

	public static CityDataVersion _createCityDataVersion(int id, int cityId, String versionName, double priceOneTime,
			double pricePeriod) { // friend Database
		return new CityDataVersion(id, cityId, versionName, priceOneTime, pricePeriod);
	}

	public CityDataVersion(City c, String versionName, double priceOneTime, double pricePeriod) {
		this.id = Database.generateIdCityDataVersion();
		this.versionName = versionName;
		this.priceOneTime = priceOneTime;
		this.pricePeriod = pricePeriod;
		this.cityId = c.getId();
		this.temp_placeSights = new ArrayList<>();
		this.temp_removePlaceSights = new ArrayList<>();
		this.temp_mapSights = new ArrayList<>();
		this.temp_removeMapSights = new ArrayList<>();
		this.temp_routeSights = new ArrayList<>();
		this.temp_removeRouteSights = new ArrayList<>();
	}

	public CityDataVersion(CityDataVersion other) {
		this.id = Database.generateIdCityDataVersion();
		this.versionName = other.versionName;
		this.priceOneTime = other.priceOneTime;
		this.pricePeriod = other.pricePeriod;
		this.cityId = other.cityId;
		this.temp_placeSights = new ArrayList<>(other.temp_placeSights);
		this.temp_removePlaceSights = new ArrayList<>(other.temp_removePlaceSights);
		this.temp_mapSights = new ArrayList<>(other.temp_mapSights);
		this.temp_removeMapSights = new ArrayList<>(other.temp_removeMapSights);
		this.temp_routeSights = new ArrayList<>(other.temp_routeSights);
		this.temp_removeRouteSights = new ArrayList<>(other.temp_removeRouteSights);
	}

	public void saveToDatabase() {
		Database._saveCityDataVersion(this);
		// delete removes
		for (PlaceOfInterestSight ps : temp_removePlaceSights) {
			if (!temp_placeSights.contains(ps))
            {
                ps.deleteFromDatabase();
                System.out.println("print of POIS from city, very big");
                int pId=ps.getCopyPlace().getId();
                //remove RouteStops and Locations of that POI
                for (MapSight ms : temp_mapSights)
                {
                    Map m=ms.getCopyMap();
                    Location l=m.getLocationByPlaceOfInterestId(pId);
                    while(l!=null)
                    {
                        m.removeLocationById(l.getId());
                        l=m.getLocationByPlaceOfInterestId(pId);
                    }
                    m.saveToDatabase();
                }
                for (RouteSight rs : temp_routeSights)
                {
                    Route r=rs.getCopyRoute();
                    RouteStop rss=r.getRouteStopByPlaceId(pId);
                    while(rss!=null)
                    {
                        r.removeRouteStopAtIndex(rss.getNumStop());
                        rss=r.getRouteStopByPlaceId(pId);
                    }
                    r.saveToDatabase();
                }
            }
		}
		temp_removePlaceSights = new ArrayList<>();
		for (MapSight ms : temp_removeMapSights) {
			if (!temp_mapSights.contains(ms))
				ms.deleteFromDatabase();
		}
		temp_removeMapSights = new ArrayList<>();
		for (RouteSight rs : temp_removeRouteSights) {
			if (!temp_routeSights.contains(rs))
				rs.deleteFromDatabase();
		}
		temp_removeRouteSights = new ArrayList<>();
		// save temps
		for (PlaceOfInterestSight ps : temp_placeSights)
			ps.saveToDatabase();
		for (MapSight ms : temp_mapSights)
			ms.saveToDatabase();
		for (RouteSight rs : temp_routeSights)
			rs.saveToDatabase();
	}

	public void deleteFromDatabase() {
		Database._deleteCityDataVersion(this.id);
		// delete removes
		for (PlaceOfInterestSight ps : temp_removePlaceSights)
			ps.deleteFromDatabase();
		temp_removePlaceSights = new ArrayList<>();
		for (MapSight ms : temp_removeMapSights)
			ms.deleteFromDatabase();
		temp_removeMapSights = new ArrayList<>();
		for (RouteSight rs : temp_removeRouteSights)
			rs.deleteFromDatabase();
		temp_removeRouteSights = new ArrayList<>();
		// save temps
		for (PlaceOfInterestSight ps : temp_placeSights)
			ps.deleteFromDatabase();
		for (MapSight ms : temp_mapSights)
			ms.deleteFromDatabase();
		for (RouteSight rs : temp_routeSights)
			rs.deleteFromDatabase();
	}

	public void reloadTempsFromDatabase() {
		this.temp_placeSights = generatePlaceOfInterestSights();
		this.temp_removePlaceSights = new ArrayList<>();
		this.temp_mapSights = generateMapSights();
		this.temp_removeMapSights = new ArrayList<>();
		this.temp_routeSights = generateRouteSights();
		this.temp_removeRouteSights = new ArrayList<>();
	}

	private ArrayList<MapSight> generateMapSights() {
		ArrayList<Integer> ids = Database.searchMapSight(this.id, null);
		ArrayList<MapSight> arrList = new ArrayList<MapSight>();
		for (int id : ids) {
			MapSight o = Database._getMapSightById(id);
			if (o == null)
				continue;
			if (Database.getMapById(o.getMapId()) == null)
				Database._deleteMapSight(id);
			else
				arrList.add(o);
		}
		return arrList;
	}

	public int getNumMapSights() {
		return temp_mapSights.size();
	}

	public MapSight getMapSightByMapId(int mapId) {
		for (MapSight ms : temp_mapSights) {
			if (ms.getMapId() == mapId)
				return ms;
		}
		return null;
	}

	public MapSight getMapSightById(int msId) {
		for (MapSight ms : temp_mapSights) {
			if (ms.getId() == msId)
				return ms;
		}
		return null;
	}

	public boolean addMapSight(MapSight ms) {
		if (ms.getCopyMap().getCityId() != this.getCityId() || ms.getCityDataVersionId() != this.id)
			return false;
		this.temp_mapSights.add(ms);
		return true;
	}

	public MapSight removeMapSightById(int msId) {
		for (MapSight ms : new ArrayList<>(temp_mapSights)) {
			if (ms.getId() == msId) {
				temp_mapSights.remove(ms);
				temp_removeMapSights.add(ms);
				return ms;
			}
		}
		return null;
	}

	private ArrayList<PlaceOfInterestSight> generatePlaceOfInterestSights() {
		ArrayList<Integer> ids = Database.searchPlaceOfInterestSight(this.id, null);
		ArrayList<PlaceOfInterestSight> arrList = new ArrayList<PlaceOfInterestSight>();
		for (int id : ids) {
			PlaceOfInterestSight o = Database._getPlaceOfInterestSightById(id);
			if (o == null)
				continue;
			if (Database.getPlaceOfInterestById(o.getPlaceOfInterestId()) == null)
				Database._deletePlaceOfInterestSight(id);
			else
				arrList.add(o);
		}
		return arrList;
	}

	public int getNumPlaceOfInterestSights() {
		return temp_placeSights.size();
	}

	public PlaceOfInterestSight getPlaceOfInterestSightByPlaceOfInterestId(int placeId) {
		for (PlaceOfInterestSight ps : temp_placeSights) {
			if (ps.getPlaceOfInterestId() == placeId)
				return ps;
		}
		return null;
	}

	public PlaceOfInterestSight getPlaceOfInterestSightById(int psId) {
		for (PlaceOfInterestSight ps : temp_placeSights) {
			if (ps.getId() == psId)
				return ps;
		}
		return null;
	}

	public boolean addPlaceOfInterestSight(PlaceOfInterestSight ps) {
		if (ps.getCopyPlace().getCityId() != this.cityId || ps.getCityDataVersionId() != this.id)
			return false;
		temp_placeSights.add(ps);
		return true;
	}

	public PlaceOfInterestSight removePlaceOfInterestSightById(int psId) {
		for (PlaceOfInterestSight ps : new ArrayList<>(temp_placeSights)) {
			if (ps.getId() == psId) {
				temp_placeSights.remove(ps);
				temp_removePlaceSights.add(ps);
				return ps;
			}
		}
		return null;
	}

	private ArrayList<RouteSight> generateRouteSights() {
		ArrayList<Integer> ids = Database.searchRouteSight(this.id, null,null);
		ArrayList<RouteSight> arrList = new ArrayList<RouteSight>();
		for (int id : ids) {
			RouteSight o = Database._getRouteSightById(id);
			if (o == null)
				continue;
			if (Database.getRouteById(o.getRouteId()) == null)
				Database._deleteRouteSight(id);
			else
				arrList.add(o);
		}
		return arrList;
	}

	public int getNumRouteSights() {
		return temp_routeSights.size();
	}

	public RouteSight getRouteSightByRouteId(int routeId) {
		for (RouteSight rs : temp_routeSights) {
			if (rs.getRouteId() == routeId)
				return rs;
		}
		return null;
	}

	public RouteSight getRouteSightById(int rsId) {
		for (RouteSight rs : temp_routeSights) {
			if (rs.getId() == rsId)
				return rs;
		}
		return null;
	}

	public boolean addRouteSight(RouteSight rs) {
		if (rs.getCopyRoute().getCityId() != this.cityId || rs.getCityDataVersionId() != this.getId())
			return false;
		temp_routeSights.add(rs);
		return true;
	}

	public RouteSight removeRouteSightById(int rsId) {
		for (RouteSight rs : new ArrayList<>(temp_routeSights)) {
			if (rs.getId() == rsId) {
				temp_routeSights.remove(rs);
				temp_removeRouteSights.add(rs);
				return rs;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public int getCityId() {
		return cityId;
	}

	public String getVersionName() {
		return versionName;
	}

	public double getPriceOneTime() {
		return priceOneTime;
	}

	public double getPricePeriod() {
		return pricePeriod;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public void setPriceOneTime(double priceOneTime) {
		this.priceOneTime = priceOneTime;
	}

	public void setPricePeriod(double pricePeriod) {
		this.pricePeriod = pricePeriod;
	}

	public ArrayList<PlaceOfInterestSight> getCopyPlaceSights() {
		return new ArrayList<>(temp_placeSights);
	}

	public ArrayList<MapSight> getCopyMapSights() {
		return new ArrayList<>(temp_mapSights);
	}

	public ArrayList<RouteSight> getCopyRouteSights() {
		return new ArrayList<>(temp_routeSights);
	}

	public ArrayList<RouteSight> getCopyFavoriteRouteSights() {
		ArrayList<RouteSight> favorList=new ArrayList<>();
		for(RouteSight r:temp_routeSights)
		{
			if(r.getIsFavorite())
				favorList.add(r);
		}
		return favorList;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof CityDataVersion && ((CityDataVersion) o).getId() == this.getId();
	}
}
