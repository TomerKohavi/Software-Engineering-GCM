import java.util.ArrayList;

public class PlaceOfInterest
{
    public enum PlaceType
    {
        HISTORICAL,
        MUSIEM,
        HOTEL,
        RESTURANT,
        PUBLIC,
        PARK,
        STORE,
        CINEMA
    }

    private int id;
    private String name;
    private ArrayList<Integer> locationsId;
    private PlaceType type;
    private String placeDescription;
    private boolean accessibilityToDisabled;
    private int cityId;

    public PlaceOfInterest(String name, PlaceType type, String info, boolean accessibilityToDisabled,ArrayList<Integer> locationsId,int cityId) {
        this.id=Database.generateIdPlaceOfInterest();
        this.name = name;
        this.locationsId=locationsId;
        this.type = type;
        this.placeDescription = info;
        this.accessibilityToDisabled = accessibilityToDisabled;
        this.cityId=cityId;
    }

    public PlaceOfInterest(String name, PlaceType type, String info, boolean accessibilityToDisabled,int cityId)
    {
        this.id=Database.generateIdPlaceOfInterest();
        this.name = name;
        this.locationsId=new ArrayList<Integer>();
        this.type = type;
        this.placeDescription = info;
        this.accessibilityToDisabled = accessibilityToDisabled;
        this.cityId=cityId;
    }

    public int getCityId() {
        return cityId;
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

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public boolean isAccessibilityToDisabled() {
        return accessibilityToDisabled;
    }

    public int getNumLocations()
    {
        return locationsId.size();
    }

    public Location getLocation(int newLocationId)
    {
        for(int locationId:locationsId)
        {
            if(locationId == newLocationId)
                return Database.getLocationById(newLocationId);
        }
        return null;
    }

    public boolean addLocation(Location newLocation)
    {
        int newLocationId=newLocation.getId();
        for(int locationId:locationsId)
        {
            if(locationId == newLocationId)
                return false;
        }
        Database.saveLocation(newLocation);
        locationsId.add(newLocationId);
        return true;
    }

    public boolean removeLocation(int locationId)
    {
        for(int i=0;i<locationsId.size();i++)
        {
            if(locationsId.get(i)== locationId)
            {
                locationsId.remove(i);
                return true;
            }
        }
        return false;
    }

    public void setType(PlaceType type) {
        this.type = type;
    }

    public void setAccessibilityToDisabled(boolean accessibilityToDisabled) {
        this.accessibilityToDisabled = accessibilityToDisabled;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getLocationsId() {
        return locationsId;
    }

    public void setLocationsId(ArrayList<Integer> locationsId) {
        this.locationsId = locationsId;
    }
}
