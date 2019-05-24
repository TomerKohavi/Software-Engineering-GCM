import java.util.Arrays;
import java.util.Date;

public class Database
{
    /*
    Things to save in Tables:
    * All classes that have at list one nonstatic function (for now all classes except InformationSystem)
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
    public static Location getLocationById(int LocationId){ return null;}
    public static boolean saveLocation(Location l){return true;}//return if it's already in the database
    public static boolean deleteLocation(int LocationId) {return true;}//return true if it was deleted (false if it weren't in the database)

    public static PlaceOfInterest getPlaceOfInterestById(int placeId){ return null;}
    public static boolean savePlaceOfInterest(PlaceOfInterest p){return true;}
    public static boolean deletePlaceOfInterest(int placeId) {return true;}

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

    public static RouteStop getRouteStopById(int routeStopId){ return null;}
    public static boolean saveRouteStop(RouteStop rs){return true;}

    public static boolean deleteRouteStop(int routeStopId) {return true;}

    public static MapSight getMapSightById(int mapSightId){ return null;}
    public static boolean saveMapSight(MapSight ms){return true;}
    public static boolean deleteMapSight(int mapSightId) {return true;}

    public static PlaceOfInterestSight getPlaceOfInterestSightById(int placeOfInterestSightId){ return null;}
    public static boolean savePlaceOfInterestSight(PlaceOfInterestSight pois){return true;}
    public static boolean deletePlaceOfInterestSight(int placeOfInterestSightId) {return true;}

    public static RouteSight getRouteSightById(int routeSightId){ return null;}
    public static boolean saveRouteSight(RouteSight rs){return true;}
    public static boolean deleteRouteSight(int routeSightId) {return true;}

    public static CityDataVersion getCityDataVersionById(int cityDataVersionId){ return null;}
    public static boolean saveCityDataVersion(CityDataVersion cdv){return true;}
    public static boolean deleteCityDataVersion(int cityDataVersionId) {return true;}

    public static Statistic getStatisticById(int statisticId){ return null;}
    public static boolean saveStatistic(Statistic s){return true;}
    public static boolean deleteStatistic(int statisticId) {return true;}

    public static Subscription getSubscriptionById(int subscriptionId){ return null;}
    public static boolean saveSubscription(Subscription s){return true;}
    public static boolean deleteSubscription(int subscriptionId) {return true;}

    public static OneTimePurchase getOneTimePurchaseById(int oneTimePurchaseId){ return null;}
    public static boolean saveOneTimePurchase(OneTimePurchase otp){return true;}
    public static boolean deleteOneTimePurchase(int oneTimePurchaseId) {return true;}


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
