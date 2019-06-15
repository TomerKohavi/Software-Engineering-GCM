
package objectClasses;

import java.io.Serializable;
import java.util.ArrayList;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * Class of a version of city and all its content
 * @author Ron Cohen
 */
@SuppressWarnings("serial")
public class CityDataVersion implements ClassMustProperties, Serializable
{
	
	private int id;
	private int cityId;
	private String versionName;

	private int temp_numPlaces;
	// private ArrayList<PlaceOfInterestSight> temp_placeSights;
	// private ArrayList<PlaceOfInterestSight> temp_removePlaceSights;
	private int temp_numMaps;
	// private ArrayList<MapSight> temp_mapSights;
	// private ArrayList<MapSight> temp_removeMapSights;
	private int temp_numRoutes;
	// private ArrayList<RouteSight> temp_routeSights;
	// private ArrayList<RouteSight> temp_removeRouteSights;

	/**
	 * This is a private constructor of city data version object
	 * 
	 * @param id           the city data version id
	 * @param cityId       the city id that connect to the city data version
	 * @param versionName  the version name of the city data version
	 * @param priceOneTime the price for one time buy
	 * @param pricePeriod  the price for period buy
	 */
	private CityDataVersion(int id, int cityId, String versionName)
	{
		this.id = id;
		this.cityId = cityId;
		this.versionName = versionName;
		reloadTempsFromDatabase();
	}

	/**
	 * This function create City object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id           the city data version id
	 * @param cityId       the city id that connect to the city data version
	 * @param versionName  the version name of the city data version
	 * @param priceOneTime the price for one time buy
	 * @param pricePeriod  the price for period buy
	 * @return new city data version object
	 */
	public static CityDataVersion _createCityDataVersion(int id, int cityId, String versionName)
	{ // friend Database
		return new CityDataVersion(id, cityId, versionName);
	}

	/**
	 * This is the normal public constructor for city data version object
	 * 
	 * @param c            the city object of the city data version
	 * @param versionName  the version name of the city data version
	 * @param priceOneTime the price for one time buy
	 * @param pricePeriod  the price for period buy
	 */
	public CityDataVersion(City c, String versionName)
	{
		this.id = Database.generateIdCityDataVersion();
		this.versionName = versionName;
		this.cityId = c.getId();
		// this.temp_placeSights = new ArrayList<>();
		// this.temp_removePlaceSights = new ArrayList<>();
		// this.temp_mapSights = new ArrayList<>();
		// this.temp_removeMapSights = new ArrayList<>();
		// this.temp_routeSights = new ArrayList<>();
		// this.temp_removeRouteSights = new ArrayList<>();
		this.temp_numPlaces = 0;
		this.temp_numMaps = 0;
		this.temp_numRoutes = 0;
	}

	/**
	 * Copy constructor of city data version with new version name
	 *
	 * @param other       the city data version object we want to copy
	 * @param versionName the new version name
	 */
	public CityDataVersion(CityDataVersion other, String versionName)
	{
		this.id = Database.generateIdCityDataVersion();
		this.versionName = versionName;
		this.cityId = other.cityId;
		/*
		 * this.temp_placeSights = new ArrayList<>(); for(PlaceOfInterestSight
		 * ps:other.temp_placeSights) { PlaceOfInterestSight newPs=new
		 * PlaceOfInterestSight(this.id, ps.getCopyPlace());
		 * temp_placeSights.add(newPs); } this.temp_removePlaceSights = new
		 * ArrayList<>();
		 */
		for (PlaceOfInterestSight ps : other._generatePlaceSights(other.id))
		{
			PlaceOfInterestSight newPs = new PlaceOfInterestSight(this.id, ps.getCopyPlace());
			newPs.saveToDatabase();
		}
		/*
		 * this.temp_mapSights = new ArrayList<>(); for(MapSight
		 * ms:other.temp_mapSights) { Map newMap=new Map(ms.getCopyMap());
		 * newMap.saveToDatabase(); MapSight newMs=new MapSight(this.id, newMap);
		 * temp_mapSights.add(newMs); } this.temp_removeMapSights = new ArrayList<>();
		 */
		for (MapSight ms : _generateMapSights(other.id))
		{
			Map newMap = new Map(ms.getCopyMap());
			newMap.saveToDatabase();
			MapSight newMs = new MapSight(this.id, newMap);
			newMs.saveToDatabase();
		}

		/*
		 * this.temp_routeSights = new ArrayList<>(); for(RouteSight
		 * rs:other.temp_routeSights) { Route newR=new Route(rs.getCopyRoute());
		 * newR.saveToDatabase(); RouteSight newRs=new RouteSight(this.id,
		 * rs.getCopyRoute(), rs.getIsFavorite()); temp_routeSights.add(newRs); }
		 * this.temp_removeRouteSights = new ArrayList<>();
		 */
		for (RouteSight rs : _generateRouteSights(other.id))
		{
			Route newR = new Route(rs.getCopyRoute());
			newR.saveToDatabase();
			RouteSight newRs = new RouteSight(this.id, rs.getCopyRoute());
			newRs.saveToDatabase();
		}
		this.temp_numPlaces = other.temp_numPlaces;
		this.temp_numMaps = other.temp_numMaps;
		this.temp_numRoutes = other.temp_numRoutes;
	}

	public void saveToDatabase()
	{
		Database._saveCityDataVersion(this);
		// delete removes
		/*
		 * for (PlaceOfInterestSight ps : temp_removePlaceSights) { if
		 * (!temp_placeSights.contains(ps)) { ps.deleteFromDatabase();
		 * System.out.println("print of POIS from city, very big"); int
		 * pId=ps.getCopyPlace().getId(); //remove RouteStops and Locations of that POI
		 * for (MapSight ms : temp_mapSights) { Map m=ms.getCopyMap(); Location
		 * l=m.getLocationByPlaceOfInterestId(pId); while(l!=null) {
		 * m.removeLocationById(l.getId()); l=m.getLocationByPlaceOfInterestId(pId); }
		 * m.saveToDatabase(); } for (RouteSight rs : temp_routeSights) { Route
		 * r=rs.getCopyRoute(); RouteStop rss=r.getRouteStopByPlaceId(pId);
		 * while(rss!=null) { r.removeRouteStopAtIndex(rss.getNumStop());
		 * rss=r.getRouteStopByPlaceId(pId); } r.saveToDatabase(); } } }
		 * temp_removePlaceSights = new ArrayList<>(); for (MapSight ms :
		 * temp_removeMapSights) { if (!temp_mapSights.contains(ms))
		 * ms.deleteFromDatabase(); } temp_removeMapSights = new ArrayList<>(); for
		 * (RouteSight rs : temp_removeRouteSights) { if
		 * (!temp_routeSights.contains(rs)) rs.deleteFromDatabase(); }
		 * temp_removeRouteSights = new ArrayList<>(); // save temps for
		 * (PlaceOfInterestSight ps : temp_placeSights) ps.saveToDatabase(); for
		 * (MapSight ms : temp_mapSights) ms.saveToDatabase(); for (RouteSight rs :
		 * temp_routeSights) rs.saveToDatabase();
		 */
	}

	public void deleteFromDatabase()
	{
		Database._deleteCityDataVersion(this.id);
		// delete removes
		/*
		 * for (PlaceOfInterestSight ps : temp_removePlaceSights)
		 * ps.deleteFromDatabase(); temp_removePlaceSights = new ArrayList<>(); for
		 * (MapSight ms : temp_removeMapSights) ms.deleteFromDatabase();
		 * temp_removeMapSights = new ArrayList<>(); for (RouteSight rs :
		 * temp_removeRouteSights) rs.deleteFromDatabase(); temp_removeRouteSights = new
		 * ArrayList<>(); // save temps for (PlaceOfInterestSight ps : temp_placeSights)
		 * ps.deleteFromDatabase(); for (MapSight ms : temp_mapSights)
		 * ms.deleteFromDatabase(); for (RouteSight rs : temp_routeSights)
		 * rs.deleteFromDatabase();
		 */
		for (PlaceOfInterestSight ps : _generatePlaceSights(this.id))
			ps.deleteFromDatabase();
		for (MapSight ms : _generateMapSights(this.id))
			ms.deleteFromDatabase();
		for (RouteSight rs : _generateRouteSights(this.id))
			rs.deleteFromDatabase();
	}

	public void reloadTempsFromDatabase()
	{
		/*
		 * this.temp_placeSights = generatePlaceOfInterestSights();
		 * this.temp_removePlaceSights = new ArrayList<>(); this.temp_mapSights =
		 * generateMapSights(); this.temp_removeMapSights = new ArrayList<>();
		 * this.temp_routeSights = generateRouteSights(); this.temp_removeRouteSights =
		 * new ArrayList<>();
		 */
		ArrayList<Integer> idsPS = Database.searchPlaceOfInterestSight(this.id, null);
		this.temp_numPlaces = idsPS.size();
		ArrayList<Integer> idsMS = Database.searchMapSight(this.id, null);
		this.temp_numMaps = idsMS.size();
		ArrayList<Integer> idsRS = Database.searchRouteSight(this.id, null, null);
		this.temp_numRoutes = idsRS.size();
	}

	/**
	 * generate all the map sights from one city data version
	 * 
	 * @param cdvId the city data version id we ant to get there map sights
	 * @return list of map sight object we want to get
	 */
	public static ArrayList<MapSight> _generateMapSights(int cdvId)
	{
		ArrayList<Integer> ids = Database.searchMapSight(cdvId, null);
		ArrayList<MapSight> arrList = new ArrayList<MapSight>();
		for (int id : ids)
		{
			MapSight o = Database._getMapSightById(id);
			if (o == null || o.getCopyMap() == null)
				continue;
			arrList.add(o);
		}
		return arrList;
	}

	/**
	 * @return the number of map sights in the city data version
	 */
	public int getNumMapSights()
	{
		return temp_numMaps;
	}

	/*
	 * public MapSight getMapSightByMapId(int mapId) { for (MapSight ms :
	 * temp_mapSights) { if (ms.getMapId() == mapId) return ms; } return null; }
	 * 
	 * public MapSight getMapSightByMapName(String name) { for (MapSight ms :
	 * temp_mapSights) { if (ms.getCopyMap().getName().equals(name)) return ms; }
	 * return null; }
	 * 
	 * public MapSight getMapSightById(int msId) { for (MapSight ms :
	 * temp_mapSights) { if (ms.getId() == msId) return ms; } return null; }
	 * 
	 * public boolean addMapSight(MapSight ms) { if (ms.getCopyMap().getCityId() !=
	 * this.getCityId() || ms.getCityDataVersionId() != this.id) return false;
	 * this.temp_mapSights.add(ms); return true; }
	 * 
	 * public MapSight removeMapSightById(int msId) { for (MapSight ms : new
	 * ArrayList<>(temp_mapSights)) { if (ms.getId() == msId) {
	 * temp_mapSights.remove(ms); temp_removeMapSights.add(ms); return ms; } }
	 * return null; }
	 */

	/**
	 * generate all the point of interest from one city data version
	 * 
	 * @param cdvId the city data version id we ant to get there point of interest
	 * @return list of point of interest object we want to get
	 */
	public static ArrayList<PlaceOfInterestSight> _generatePlaceSights(int cdvId)
	{
		ArrayList<Integer> ids = Database.searchPlaceOfInterestSight(cdvId, null);
		ArrayList<PlaceOfInterestSight> arrList = new ArrayList<PlaceOfInterestSight>();
		for (int id : ids)
		{
			PlaceOfInterestSight o = Database._getPlaceOfInterestSightById(id);
			if (o == null || o.getCopyPlace() == null)
				continue;
			arrList.add(o);
		}
		return arrList;
	}

	/**
	 * @return the number of points of interest in the city data version
	 */
	public int getNumPlaceOfInterestSights()
	{
		return temp_numPlaces;
	}

	/*
	 * public PlaceOfInterestSight getPlaceOfInterestSightByPlaceOfInterestId(int
	 * placeId) { for (PlaceOfInterestSight ps : temp_placeSights) { if
	 * (ps.getPlaceOfInterestId() == placeId) return ps; } return null; }
	 * 
	 * public PlaceOfInterestSight getPlaceOfInterestSightById(int psId) { for
	 * (PlaceOfInterestSight ps : temp_placeSights) { if (ps.getId() == psId) return
	 * ps; } return null; }
	 * 
	 * public boolean addPlaceOfInterestSight(PlaceOfInterestSight ps) { if
	 * (ps.getCopyPlace().getCityId() != this.cityId || ps.getCityDataVersionId() !=
	 * this.id) return false; temp_placeSights.add(ps); return true; }
	 * 
	 * public PlaceOfInterestSight removePlaceOfInterestSightById(int psId) { for
	 * (PlaceOfInterestSight ps : new ArrayList<>(temp_placeSights)) { if
	 * (ps.getId() == psId) { temp_placeSights.remove(ps);
	 * temp_removePlaceSights.add(ps); return ps; } } return null; }
	 */

	/**
	 * generate all the route sights from one city data version
	 * 
	 * @param cdvId the city data version id we ant to get there route sights
	 * @return list of route sight object we want to get
	 */
	public static ArrayList<RouteSight> _generateRouteSights(int cdvId) {
		ArrayList<Integer> ids = Database.searchRouteSight(cdvId, null,null);
		ArrayList<RouteSight> arrList = new ArrayList<RouteSight>();
		for (int id : ids)
		{
			RouteSight o = Database._getRouteSightById(id);
			if (o == null || o.getCopyRoute() == null)
				continue;
			arrList.add(o);
		}
		return arrList;
	}

	/**
	 * Returns the number of route sights
	 * 
	 * @return the number of route sights
	 */
	public int getNumRouteSights()
	{
		return temp_numRoutes;
	}

	/*
	 * public RouteSight getRouteSightByRouteId(int routeId) { for (RouteSight rs :
	 * temp_routeSights) { if (rs.getRouteId() == routeId) return rs; } return null;
	 * }
	 * 
	 * public RouteSight getRouteSightByRouteInfo(String routeInfo) { for
	 * (RouteSight rs : temp_routeSights) { if
	 * (rs.getCopyRoute().getInfo().equals(routeInfo)) return rs; } return null; }
	 * 
	 * public RouteSight getRouteSightById(int rsId) { for (RouteSight rs :
	 * temp_routeSights) { if (rs.getId() == rsId) return rs; } return null; }
	 * 
	 * public boolean addRouteSight(RouteSight rs) { if
	 * (rs.getCopyRoute().getCityId() != this.cityId || rs.getCityDataVersionId() !=
	 * this.getId()) return false; temp_routeSights.add(rs); return true; }
	 * 
	 * public RouteSight removeRouteSightById(int rsId) { for (RouteSight rs : new
	 * ArrayList<>(temp_routeSights)) { if (rs.getId() == rsId) {
	 * temp_routeSights.remove(rs); temp_removeRouteSights.add(rs); return rs; } }
	 * return null; }
	 */

	/**
	 * Returns the id of the city data version
	 * 
	 * @return the id of the city data version
	 */
	public int getId()
	{
		return id;
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
	 * Returns the version name
	 * 
	 * @return the version name
	 */
	public String getVersionName()
	{
		return versionName;
	}


	/**
	 * Sets the version name
	 * 
	 * @param versionName the version name
	 */
	public void setVersionName(String versionName)
	{
		this.versionName = versionName;
	}
	

	/*
	 * public ArrayList<PlaceOfInterestSight> getCopyPlaceSights() { return new
	 * ArrayList<>(temp_placeSights); }
	 * 
	 * /*public ArrayList<MapSight> getCopyMapSights() { return new
	 * ArrayList<>(temp_mapSights); }
	 * 
	 * public ArrayList<RouteSight> getCopyRouteSights() { return new
	 * ArrayList<>(temp_routeSights); }
	 * 
	 * public ArrayList<RouteSight> getCopyFavoriteRouteSights() {
	 * ArrayList<RouteSight> favorList=new ArrayList<>(); for(RouteSight
	 * r:temp_routeSights) { if(r.getIsFavorite()) favorList.add(r); } return
	 * favorList; }
	 */

	@Override
	public boolean equals(Object o)
	{
		return o instanceof CityDataVersion && ((CityDataVersion) o).getId() == this.getId();
	}
}
