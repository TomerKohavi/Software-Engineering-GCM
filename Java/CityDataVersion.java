import java.util.ArrayList;

public class CityDataVersion
{
    private int id;
    private String versionName;
    private ArrayList<Integer> mapsId;
    private ArrayList<Integer> placesOfInterestId;
    private ArrayList<Integer> routesId;
    private double priceOneTime;
    private double pricePeriod;
    private int cityId;

    public CityDataVersion(String versionName, ArrayList<Integer> mapsId, ArrayList<Integer> placesOfInterestId, ArrayList<Integer> routesId, double priceOneTime, double pricePeriod,int cityId) {
        this.id=Database.generateIdCityDataVersion();
        this.versionName = versionName;
        this.mapsId = mapsId;
        this.placesOfInterestId = placesOfInterestId;
        this.routesId = routesId;
        this.priceOneTime = priceOneTime;
        this.pricePeriod = pricePeriod;
        this.cityId=cityId;
    }

    public CityDataVersion(String versionName, double priceOneTime, double pricePeriod,int cityId) {
        this.id=Database.generateIdCityDataVersion();
        this.mapsId=new ArrayList<Integer>();
        this.placesOfInterestId=new ArrayList<Integer>();
        this.routesId=new ArrayList<Integer>();
        this.versionName = versionName;
        this.priceOneTime = priceOneTime;
        this.pricePeriod = pricePeriod;
        this.cityId=cityId;
    }

    public int getCityId() {
        return cityId;
    }

    public int getNumMaps(){
        return mapsId.size();
    }

    public int getNumPlaces(){
        return placesOfInterestId.size();
    }

    public int getNumRoutes(){
        return routesId.size();
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getMapsId() {
        return mapsId;
    }

    public ArrayList<Integer> getPlacesOfInterestId() {
        return placesOfInterestId;
    }

    public ArrayList<Integer> getRoutesId() {
        return routesId;
    }

    public double getPriceOneTime() {
        return priceOneTime;
    }

    public double getPricePeriod() {
        return pricePeriod;
    }

    public Map getMap(int newMapId)
    {
        for(int mapId:mapsId)
        {
            if(mapId == newMapId)
                return Database.getMapById(newMapId);
        }
        return null;
    }

    public boolean addMap(Map newMap)
    {
        int newMapId=newMap.getId();
        for(int mapId:mapsId)
        {
            if(mapId == newMapId)
                return false;
        }
        Database.saveMap(newMap);
        mapsId.add(newMapId);
        return true;
    }

    public boolean removeMap(int mapId)
    {
        for(int i=0;i<mapsId.size();i++)
        {
            if(mapsId.get(i)== mapId)
            {
                mapsId.remove(i);
                return true;
            }
        }
        return false;
    }

    public PlaceOfInterest getPlace(int newPlaceId)
    {
        for(int placeId:placesOfInterestId)
        {
            if(placeId == newPlaceId)
                return Database.getPlaceOfInterestById(newPlaceId);
        }
        return null;
    }

    public boolean addPlace(PlaceOfInterest newPlace)
    {
        int newPlaceId=newPlace.getId();
        for(int placeId:placesOfInterestId)
        {
            if(placeId == newPlaceId)
                return false;
        }
        Database.savePlaceOfInterest(newPlace);
        placesOfInterestId.add(newPlaceId);
        return true;
    }

    public boolean removePlace(int placeId)
    {
        for(int i=0;i<placesOfInterestId.size();i++)
        {
            if(placesOfInterestId.get(i)== placeId)
            {
                placesOfInterestId.remove(i);
                return true;
            }
        }
        return false;
    }

    public Route getRoute(int newRouteId)
    {
        for(int routeId:routesId)
        {
            if(routeId == newRouteId)
                return Database.getRouteById(newRouteId);
        }
        return null;
    }

    public boolean addRoute(Route newRoute)
    {
        int newRouteId=newRoute.getId();
        for(int routeId:routesId)
        {
            if(routeId == newRouteId)
                return false;
        }
        Database.saveRoute(newRoute);
        routesId.add(newRouteId);
        return true;
    }

    public boolean removeRoute(int RouteId)
    {
        for(int i=0;i<routesId.size();i++)
        {
            if(routesId.get(i)== RouteId)
            {
                routesId.remove(i);
                return true;
            }
        }
        return false;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setMapsId(ArrayList<Integer> mapsId) {
        this.mapsId = mapsId;
    }

    public void setPlacesOfInterestId(ArrayList<Integer> placesOfInterestId) {
        this.placesOfInterestId = placesOfInterestId;
    }

    public void setRoutesId(ArrayList<Integer> routesId) {
        this.routesId = routesId;
    }

    public void setPriceOneTime(double priceOneTime) {
        this.priceOneTime = priceOneTime;
    }

    public void setPricePeriod(double pricePeriod) {
        this.pricePeriod = pricePeriod;
    }
}
