import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map {
    private int id;
    private String name;
    private String info;
    private ArrayList<Integer> locationsId;
    private BufferedImage mapPicture;

    public Map(String name,String info,BufferedImage mapPicture)
    {
        this.id=Database.generateIdMap();
        this.name=name;
        this.info=info;
        this.locationsId=new ArrayList<Integer>();
        this.mapPicture=mapPicture;
    }

    public Map(String name,String info,BufferedImage mapPicture,ArrayList<Integer> locationsId)
    {
        this.id=Database.generateIdMap();
        this.name=name;
        this.info=info;
        this.locationsId=locationsId;
        this.mapPicture=mapPicture;
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

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public BufferedImage getMapPicture() {
        return mapPicture;
    }

    public int getNumLocations() {
        return locationsId.size();
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setMapPicture(BufferedImage mapPicture) {
        this.mapPicture = mapPicture;
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