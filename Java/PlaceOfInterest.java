import java.util.ArrayList;

public class PlaceOfInterest
{
    public enum PlaceType
    {
        HISTORICAL(0),
        MUSIEM(1),
        HOTEL(2),
        RESTURANT(3),
        PUBLIC(4),
        PARK(5),
        STORE(6),
        CINEMA(7);

    	private final int value;
        
        PlaceType(final int nv)
        {
        	value = nv;
        }
        
        public int getValue()
        {
        	return value;
        }
    }

    private int id;
    private int cityId;
    private String name;
    private PlaceType type;
    private String placeDescription;
    private boolean accessibilityToDisabled;

    private PlaceOfInterest(int id,int cityId, String name, PlaceType type, String placeDescription, boolean accessibilityToDisabled) {
        this.id = id;
        this.cityId=cityId;
        this.name = name;
        this.type = type;
        this.placeDescription = placeDescription;
        this.accessibilityToDisabled = accessibilityToDisabled;
    }

    public static PlaceOfInterest _createPlaceOfInterest(int id,int cityId, String name, PlaceType type, String placeDescription, boolean accessibilityToDisabled){ //friend to Database
        return new PlaceOfInterest( id, cityId,  name,  type,  placeDescription,  accessibilityToDisabled);
    }

    public PlaceOfInterest(int cityId,String name, PlaceType type, String info, boolean accessibilityToDisabled)
    {
        this.id=Database.generateIdPlaceOfInterest();
        this.cityId=cityId;
        this.name = name;
        this.type = type;
        this.placeDescription = info;
        this.accessibilityToDisabled = accessibilityToDisabled;
    }

    public ArrayList<Location> getAllLocations() {
        ArrayList<Integer> routeStopsIds= Database.searchLocation(null,this.id);
        ArrayList<Location> arrList=new ArrayList<Location>();
        for(int lId : routeStopsIds)
            arrList.add(Database._getLocationById(lId));
        return arrList;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PlaceType getType() {
        return type;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public boolean isAccessibilityToDisabled() {
        return accessibilityToDisabled;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(PlaceType type) {
        this.type = type;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public void setAccessibilityToDisabled(boolean accessibilityToDisabled) {
        this.accessibilityToDisabled = accessibilityToDisabled;
    }

    public int getCityId() {
        return cityId;
    }
}
