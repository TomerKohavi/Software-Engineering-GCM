import java.util.ArrayList;

public class CityDataVersion
{
    private int id;
    private int cityId;
    private String versionName;
    // list of mapSight
    // list of placeSight
    // list of routeSight
    private double priceOneTime;
    private double pricePeriod;

    private CityDataVersion(int id, int cityId, String versionName, double priceOneTime, double pricePeriod) {
        this.id = id;
        this.cityId = cityId;
        this.versionName = versionName;
        this.priceOneTime = priceOneTime;
        this.pricePeriod = pricePeriod;
    }

    public static CityDataVersion _createCityDataVersion(int id, int cityId, String versionName, double priceOneTime, double pricePeriod){ //friend Database
        return new CityDataVersion(id,cityId,versionName,priceOneTime,pricePeriod);
    }

    public CityDataVersion(String versionName, double priceOneTime, double pricePeriod, int cityId) {
        this.id=Database.generateIdCityDataVersion();
        this.versionName = versionName;
        this.priceOneTime = priceOneTime;
        this.pricePeriod = pricePeriod;
        this.cityId=cityId;
    }

    public ArrayList<MapSight> getAllMapSights() {
        ArrayList<Integer> ids= Database.searchMapSight(this.id,null);
        ArrayList<MapSight> arrList=new ArrayList<MapSight>();
        for(int id : ids)
        {
            MapSight o=Database._getMapSightById(id);
            if(o==null)
                continue;
            if(Database.getMapById(o.getMapId())==null)
                Database._deleteMapSight(id);
            else
                arrList.add(o);
        }
        return arrList;
    }

    public int getNumMapSights(){
        return getAllMapSights().size();
    }

    public MapSight getMapSightByMapId(int mapId)
    {
        ArrayList<Integer> msId= Database.searchMapSight(this.id,mapId);
        if(msId.size()!=1)
            return null;
        MapSight o=Database._getMapSightById(msId.get(0));
        if(Database.getMapById(o.getMapId())==null)
        {
            Database._deleteMapSight(o.getId());
            return null;
        }
        return o;
    }

    public MapSight getMapSightById(int msId)
    {
        MapSight ms=Database._getMapSightById(msId);
        if(ms==null || ms.getCityDataVersionId()!=this.id)
            return null;
        if(Database.getMapById(ms.getMapId())==null)
        {
            Database._deleteMapSight(ms.getId());
            return null;
        }
        return ms;
    }

    public MapSight addMapSight(int mapId)
    {
        if(Database.getMapById(mapId)==null)
            return null;
        MapSight ms=new MapSight(this.id,mapId);
        Database._saveMapSight(ms);
        return ms;
    }

    public MapSight removeMapSightById(int msId)
    {
        MapSight ms=getMapSightById(msId);
        if(ms==null || ms.getCityDataVersionId()!=this.id)
            return null;
        Database._deleteMapSight(ms.getId());
        return ms;
    }

    public ArrayList<PlaceOfInterestSight> getAllPlaceOfInterestSights() {
        ArrayList<Integer> ids= Database.searchPlaceOfInterestSight(this.id,null);
        ArrayList<PlaceOfInterestSight> arrList=new ArrayList<PlaceOfInterestSight>();
        for(int id : ids)
        {
            PlaceOfInterestSight o=Database._getPlaceOfInterestSightById(id);
            if(o==null)
                continue;
            if(Database.getPlaceOfInterestById(o.getPlaceOfInterestId())==null)
                Database._deletePlaceOfInterestSight(id);
            else
                arrList.add(o);
        }
        return arrList;
    }

    public int getNumPlaceOfInterestSights(){
        return getAllPlaceOfInterestSights().size();
    }

    public PlaceOfInterestSight getPlaceOfInterestSightByPlaceOfInterestId(int placeId)
    {
        ArrayList<Integer> ids= Database.searchPlaceOfInterestSight(this.id,placeId);
        if(ids.size()!=1)
            return null;
        PlaceOfInterestSight o=Database._getPlaceOfInterestSightById(ids.get(0));
        if(Database.getPlaceOfInterestById(o.getPlaceOfInterestId())==null)
        {
            Database.deletePlaceOfInterest(o.getId());
            return null;
        }
        return o;
    }

    public PlaceOfInterestSight getPlaceOfInterestSightById(int psId)
    {
        PlaceOfInterestSight ps=Database._getPlaceOfInterestSightById(psId);
        if(ps==null || ps.getCityDataVersionId()!=this.id)
            return null;
        if(Database.getPlaceOfInterestById(ps.getPlaceOfInterestId())==null)
        {
            Database.deletePlaceOfInterest(ps.getId());
            return null;
        }
        return ps;
    }

    public PlaceOfInterestSight addPlaceOfInterestSight(int placeId)
    {
        if(Database.getPlaceOfInterestById(placeId)==null)
            return null;
        PlaceOfInterestSight ps=new PlaceOfInterestSight(this.id,placeId);
        Database._savePlaceOfInterestSight(ps);
        return ps;
    }

    public PlaceOfInterestSight removePlaceOfInterestSightById(int psId)
    {
        PlaceOfInterestSight ps=getPlaceOfInterestSightById(psId);
        if(ps==null || ps.getCityDataVersionId()!=this.id)
            return null;
        Database._deletePlaceOfInterestSight(ps.getId());
        return ps;
    }

    public ArrayList<RouteSight> getAllRouteSights() {
        ArrayList<Integer> ids= Database.searchRouteSight(this.id,null);
        ArrayList<RouteSight> arrList=new ArrayList<RouteSight>();
        for(int id : ids)
        {
            RouteSight o=Database._getRouteSightById(id);
            if(o==null)
                continue;
            if(Database.getRouteById(o.getRouteId())==null)
                Database._deleteRouteSight(id);
            else
                arrList.add(o);
        }
        return arrList;
    }

    public int getNumRouteSights(){
        return getAllRouteSights().size();
    }

    public RouteSight getRouteSightByRouteId(int routeId) {
        ArrayList<Integer> ids = Database.searchRouteSight(this.id, routeId);
        if (ids.size() != 1)
            return null;
        RouteSight o = Database._getRouteSightById(ids.get(0));
        if (Database.getRouteById(o.getRouteId()) == null)
        {
            Database._deleteRouteSight(o.getId());
            return null;
        }
        return o;
    }

    public RouteSight getRouteSightById(int rsId)
    {
        RouteSight rs=Database._getRouteSightById(rsId);
        if(rs==null || rs.getCityDataVersionId()!=this.id)
            return null;
        if (Database.getRouteById(rs.getRouteId()) == null)
        {
            Database._deleteRouteSight(rs.getId());
            return null;
        }
        return rs;
    }

    public RouteSight addRouteSight(int routeId)
    {
        if (Database.getRouteById(routeId) == null)
            return null;
        RouteSight rs=new RouteSight(this.id,routeId);
        Database._saveRouteSight(rs);
        return rs;
    }

    public RouteSight removeRouteSightById(int rsId)
    {
        RouteSight rs=getRouteSightById(rsId);
        if(rs==null || rs.getCityDataVersionId()!=this.id)
            return null;
        Database._deleteRouteSight(rs.getId());
        return rs;
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
}
