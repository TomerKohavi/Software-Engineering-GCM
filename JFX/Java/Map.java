import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map {
    private int id;
    private String name;
    private String info;
    //list of locations
    private BufferedImage mapPicture;

    public Map(String name,String info,BufferedImage mapPicture)
    {
        this.id=Database.generateIdMap();
        this.name=name;
        this.info=info;
        this.mapPicture=mapPicture;
    }

    public ArrayList<Location> getAllLocations() {
        int[] ids= Database.searchLocation(this.id,null);
        ArrayList<Location> arrList=new ArrayList<Location>();
        for(int id : ids)
        {
            Location o=Database.getLocationById(id);
            if(o==null)
                continue;
            if(Database.getPlaceOfInterestById(o.getPlaceOfInterestId())==null)
                Database.deleteLocation(id);
            else
                arrList.add(o);
        }
        return arrList;
    }

    public int getNumLocations(){
        return getAllLocations().size();
    }

    public Location getLocationByPlaceOfInterestId(int placeOfInterestId)
    {
        int[] locId= Database.searchLocation(this.id,placeOfInterestId);
        if(locId.length!=1)
            return null;
        Location o=Database.getLocationById(locId[0]);
        if(Database.getPlaceOfInterestById(o.getPlaceOfInterestId())==null)
        {
            Database.deleteLocation(o.getId());
            return null;
        }
        return o;
    }

    public Location getLocationById(int locId)
    {
        Location loc=Database.getLocationById(locId);
        if(loc==null || loc.getMapId()!=this.id)
            return null;
        if(Database.getPlaceOfInterestById(loc.getPlaceOfInterestId())==null)
        {
            Database.deleteLocation(loc.getId());
            return null;
        }
        return loc;
    }

    public Location addLocation(int placeOfInterestId,double[] coordinates)
    {
        if(Database.getPlaceOfInterestById(placeOfInterestId)==null)
            return null;
        Location loc=new Location(this.id,placeOfInterestId,coordinates);
        Database.saveLocation(loc);
        return loc;
    }

    public Location removeLocationById(int locId)
    {
        Location loc=getLocationById(locId);
        if(loc==null || loc.getMapId()!=this.id)
            return null;
        Database.deleteLocation(loc.getId());
        return loc;
    }

    public int getId() {
        return id;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setMapPicture(BufferedImage mapPicture) {
        this.mapPicture = mapPicture;
    }
}