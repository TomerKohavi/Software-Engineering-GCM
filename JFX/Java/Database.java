public class Database
{
    /*
    Things to save in Tables:
    * Location- id
    * PlaceOfInterest - id, name, description
    * Map- id
    * Route- id
    * City- id, name, description
	* User- id, username, password
    
	Things to save (single object):
	* all the ids counter for each class below
	* InformationSystem
	
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


    //simple get/save by id
    public static Location getLocationById(int LocationId){ return null;}
    public static boolean saveLocation(Location l){return true;}//return if it's already in the database

    public static PlaceOfInterest getPlaceOfInterestById(int placeId){ return null;}
    public static boolean savePlaceOfInterest(PlaceOfInterest p){return true;}

    public static Map getMapById(int mapId){ return null;}
    public static boolean saveMap(Map m){return true;}

    public static Route getRouteById(int routeId){ return null;}
    public static boolean saveRoute(Route r){return true;}

    public static City getCityById(int cityId){ return null;}
    public static boolean saveCity(City c){return true;}
	
	public static User getUserById(int userId){ return null;}
    public static boolean saveUser(User u){return true;}


    //other types of search
    public static int[] searchCityByName(String cityName){return null;}
    public static int[] searchCityByPlaceOfInterestName(String placeName){return null;} //search placeOfInterest in sql and do .getCityId()
    public static int[] searchCityByCityDescription(String cityDescription){return null;}
    public static int[] searchCityByPlaceOfInterestDescription(String placeDescription){return null;} //search placeOfInterest in sql and do .getCityId()
	public static int[] searchUserByUsernameAndPassword(String username, String password){return null;}
}
