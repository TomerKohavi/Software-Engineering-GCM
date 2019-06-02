import java.io.Serializable;
import java.util.ArrayList;

public class Map implements ClassMustProperties, Serializable {
    private int id;
    private int cityId;
    private String name;
    private String info;
    private String imgURL;

    private ArrayList<Location> temp_locations;
    private ArrayList<Location> temp_removeLocations;

    private Map(int id,int cityId, String name, String info, String imgURL) {
        this.id = id;
        this.cityId=cityId;
        this.name = name;
        this.info = info;
        this.imgURL = imgURL;
        reloadTempsFromDatabase();
    }

    public static Map _createMap(int id,int cityId, String name, String info, String imgURL){ //friend to Database
        return new Map( id,cityId,  name,  info,  imgURL);
    }

    public Map(int cityId,String name, String info, String imgURL)
    {
        this.id=Database.generateIdMap();
        this.cityId=cityId;
        this.name=name;
        this.info=info;
        this.imgURL=imgURL;
        this.temp_locations=new ArrayList<>();
        this.temp_removeLocations=new ArrayList<>();
    }

    private ArrayList<Location> generateLocations() {
        ArrayList<Integer> ids= Database.searchLocation(this.id,null);
        ArrayList<Location> arrList=new ArrayList<Location>();
        for(int id : ids)
        {
            Location o=Database._getLocationById(id);
            if(o==null)
                continue;
            if(Database.getPlaceOfInterestById(o.getPlaceOfInterestId())==null)
                Database._deleteLocation(id);
            else
                arrList.add(o);
        }
        return arrList;
    }

    public void saveToDatabase()
    {
        Database.saveMap(this);
        //delete removes
        for(Location l:temp_removeLocations)
        {
            if(!temp_locations.contains(l))
                l.deleteFromDatabase();
        }
        this.temp_removeLocations=new ArrayList<>();
        //save locations
        for(Location l:temp_locations)
            l.saveToDatabase();
    }

    public void deleteFromDatabase()
    {
        Database.deleteMap(this.id);
        //delete removes
        for(Location l:temp_removeLocations)
            l.deleteFromDatabase();
        this.temp_removeLocations=new ArrayList<>();
        for(Location l:temp_locations)
            l.deleteFromDatabase();
        //delete all mapSights
        ArrayList<Integer> ids=Database.searchMapSight(null,this.id);
        for(int id:ids)
        {
            Map m=Database.getMapById(id);
            if(m!=null)
                m.deleteFromDatabase();
        }
    }

    public void reloadTempsFromDatabase(){
        this.temp_locations=generateLocations();
        this.temp_removeLocations=new ArrayList<>();
    }

    public int getNumLocations(){
        return temp_locations.size();
    }

    public Location getLocationByPlaceOfInterestId(int placeOfInterestId)
    {
        for(Location l: temp_locations){
            if(l.getPlaceOfInterestId()==placeOfInterestId)
                return l;
        }
        return null;
    }

    public Location getLocationById(int locId)
    {
        for(Location l: temp_locations){
            if(l.getId()==locId)
                return l;
        }
        return null;
    }

    public boolean addLocation(Location l)
    {
        if(l.getCopyPlaceOfInterest().getCityId()!=this.cityId || l.getMapId()!=this.id)
            return false;
        temp_locations.add(l);
        return true;
    }

    public Location removeLocationById(int locId)
    {
        for(Location l:new ArrayList<>(temp_locations)){
            if(l.getId()==locId)
            {
                temp_locations.remove(l);
                temp_removeLocations.add(l);
                return l;
            }
        }
        return null;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getCityId() {
        return cityId;
    }

    public ArrayList<Location> getCopyLocations() {
        return new ArrayList<>(temp_locations);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Map && ((Map) o).getId()==this.getId();
    }
}