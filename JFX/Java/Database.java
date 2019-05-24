import java.util.Arrays;
import java.util.Date;

public class Database
{
    /*
    Things to save in Tables:
    * All non abstract classes
    */

    //generate id
    public static int generateIdUser() {return 0;}
    public static int generateIdPlaceOfInterest() {return 0;}
    public static int generateIdMap() {return 0;}
    public static int generateIdLocation() {return 0;}
    public static int generateIdCityDataVersion() {return 0;}
    public static int generateIdRoute() {return 0;}
    public static int generateIdCityPurchase() {return 0;}
    public static int generateIdCity() {return 0;}
    public static int generateIdRouteStop() {return 0;}
    public static int generateIdMapSight() {return 0;}
    public static int generateIdPlaceOfInterestSight() {return 0;}
    public static int generateIdRouteSight() {return 0;}
    public static int generateIdStatistic() {return 0;}


    //simple get/save by id
    public static PlaceOfInterest getPlaceOfInterestById(int placeId){ return null;} // use PlaceOfInterest._createPlaceOfInterest() function
    public static boolean savePlaceOfInterest(PlaceOfInterest p){return true;}//return if it's already in the database
    public static boolean deletePlaceOfInterest(int placeId) {return true;}//return true if it was deleted (false if it weren't in the database)

    public static Map getMapById(int mapId){ return null;}
    public static boolean saveMap(Map m){return true;}
    public static boolean deleteMap(int mapId) {return true;}

    public static Route getRouteById(int routeId){ return null;}
    public static boolean saveRoute(Route r){return true;}
    public static boolean deleteRoute(int routeId) {return true;}

    public static City getCityById(int cityId){ return null;}
    public static boolean saveCity(City c){return true;}
    public static boolean deleteCity(int cityId) {return true;}
	
	public static User getUserById(int userId){ return null;}
    public static boolean saveUser(User u){return true;}
    public static boolean deleteUser(int userId) {return true;}

    public static Location _getLocationById(int LocationId){ return null;} //friend to Map
    public static boolean _saveLocation(Location l){return true;} //friend to Map
    public static boolean _deleteLocation(int LocationId) {return true;} //friend to Map

    public static RouteStop _getRouteStopById(int routeStopId){ return null;} //friend to Route
    public static boolean _saveRouteStop(RouteStop rs){return true;} //friend to Route
    public static boolean _deleteRouteStop(int routeStopId) {return true;} //friend to Route

    public static MapSight _getMapSightById(int mapSightId){ return null;} //friend to CityDataVersion
    public static boolean _saveMapSight(MapSight ms){return true;} //friend to CityDataVersion
    public static boolean _deleteMapSight(int mapSightId) {return true;} //friend to CityDataVersion

    public static PlaceOfInterestSight _getPlaceOfInterestSightById(int placeOfInterestSightId){ return null;} //friend to CityDataVersion
    public static boolean _savePlaceOfInterestSight(PlaceOfInterestSight pois){return true;} //friend to CityDataVersion
    public static boolean _deletePlaceOfInterestSight(int placeOfInterestSightId) {return true;} //friend to CityDataVersion

    public static RouteSight _getRouteSightById(int routeSightId){ return null;} //friend to CityDataVersion
    public static boolean _saveRouteSight(RouteSight rs){return true;} //friend to CityDataVersion
    public static boolean _deleteRouteSight(int routeSightId) {return true;} //friend to CityDataVersion

    public static CityDataVersion _getCityDataVersionById(int cityDataVersionId){ return null;} //friend to City
    public static boolean _saveCityDataVersion(CityDataVersion cdv){return true;} //friend to City
    public static boolean _deleteCityDataVersion(int cityDataVersionId) {return true;} //friend to City

    public static Statistic _getStatisticById(int statisticId){ return null;} //friend to InformationSystem
    public static boolean _saveStatistic(Statistic s){return true;} //friend to InformationSystem
    public static boolean _deleteStatistic(int statisticId) {return true;} //friend to InformationSystem

    public static Subscription _getSubscriptionById(int subscriptionId){ return null;} //friend to Customer
    public static boolean _saveSubscription(Subscription s){return true;} //friend to Customer
    public static boolean _deleteSubscription(int subscriptionId) {return true;} //friend to Customer

    public static OneTimePurchase _getOneTimePurchaseById(int oneTimePurchaseId){ return null;} //friend to Customer
    public static boolean _saveOneTimePurchase(OneTimePurchase otp){return true;} //friend to Customer
    public static boolean _deleteOneTimePurchase(int oneTimePurchaseId) {return true;} //friend to Customer


    //other types of search
    // something=null means don't search according to it!
    public static int[] searchCity(String cityName,String cityDescription){return null;}

	public static int[] searchUser(String username, String password){return null;}

	public static int[] searchPlaceOfInterest(String placeName,String placeDescription){return null;}

    public static int[] searchRouteStop(Integer routeId,Integer placeId,Integer numStop){return null;}

    public static int[] searchLocation(Integer mapId,Integer placeId){return null;}

    public static int[] searchMapSight(Integer cityDataVersionId,Integer mapId){return null;}

    public static int[] searchPlaceOfInterestSight(Integer cityDataVersionId,Integer placeId){return null;}

    public static int[] searchRouteSight(Integer cityDataVersionId,Integer routeId){return null;}

    public static int[] searchCityDataVersion(Integer cityId){return null;}

    public static int[] searchStatistic(Integer cityId, Date date){return null;}

    public static int[] searchSubscription(Integer userId,Integer cityId, Date purchaseDate,Date expirationDateAfterThis){return null;}// note: check the expirationDate is bigger than expirationDateAfterThis (not equal)

    public static int[] searchOneTimePurchase(Integer userId,Integer cityId, Date purchaseDate,Boolean wasDownload){return null;}

    /*public static int[] intersection(int[] a, int[] b) {
        return Arrays.stream(a)
                .distinct()
                .filter(x -> Arrays.stream(b).anyMatch(y -> y == x))
                .toArray();
    }*/
}
