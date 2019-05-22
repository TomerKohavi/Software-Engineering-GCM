import java.sql.Time;
import java.util.ArrayList;

public class Route
{
    private int id;
    private String info;
    private ArrayList<Pair<Integer, Time>> placesIdAndTime;
    private boolean acceptabilityToDisabled;
    private int cityId;

    public Route(String info, ArrayList<Pair<Integer, Time>> placesIdAndTime, boolean acceptabilityToDisabled,int cityId)
    {
        this.id=Database.generateIdRoute();
        this.info = info;
        this.placesIdAndTime = placesIdAndTime;
        this.acceptabilityToDisabled = acceptabilityToDisabled;
        this.cityId=cityId;
    }

    public Route(String info, boolean acceptabilityToDisabled,int cityId)
    {
        this.id=Database.generateIdRoute();
        this.info = info;
        this.placesIdAndTime = new ArrayList<Pair<Integer, Time>>();
        this.acceptabilityToDisabled = acceptabilityToDisabled;
        this.cityId=cityId;
    }

    public int getCityId() {
        return cityId;
    }

    public ArrayList<Pair<Integer, Time>> getPlacesIdAndTime() {
        return placesIdAndTime;
    }

    public void setPlacesIdAndTime(ArrayList<Pair<Integer, Time>> placesIdAndTime) {
        this.placesIdAndTime = placesIdAndTime;
    }

    public int getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }

    public boolean isAcceptabilityToDisabled() {
        return acceptabilityToDisabled;
    }

    public int getNumPlaces()
    {
        return placesIdAndTime.size();
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setAcceptabilityToDisabled(boolean acceptabilityToDisabled) {
        this.acceptabilityToDisabled = acceptabilityToDisabled;
    }

    public Pair<PlaceOfInterest,Time> getPlaceAndTime(int placeId)
    {
        for(Pair<Integer,Time> p:placesIdAndTime)
        {
            if(p.a == placeId)
                return new Pair(Database.getPlaceOfInterestById(p.a),p.b);
        }
        return null;
    }

    public boolean addPlaceAndTime(PlaceOfInterest newPlace,Time recommendedTime)
    {
        int newPlaceId=newPlace.getId();
        Pair<Integer,Time> newP=new Pair<>(newPlaceId,recommendedTime);
        for(Pair<Integer,Time> p:placesIdAndTime)
        {
            if(p.a == newPlaceId)
                return false;
        }
        Database.savePlaceOfInterest(newPlace);
        placesIdAndTime.add(newP);
        return true;
    }

    public boolean addPlaceAndTime(PlaceOfInterest newPlace,Time recommendedTime,int index)
    {
        if(index<0 || index>placesIdAndTime.size())
            return false;
        int newPlaceId=newPlace.getId();
        Pair<Integer,Time> newP=new Pair<>(newPlaceId,recommendedTime);
        for(Pair<Integer,Time> p:placesIdAndTime)
        {
            if(p.a == newPlaceId)
                return false;
        }
        Database.savePlaceOfInterest(newPlace);
        placesIdAndTime.add(index,newP);
        return true;
    }

    public boolean removeLPlace(int placeId)
    {
        for(int i=0;i<placesIdAndTime.size();i++)
        {
            if(placesIdAndTime.get(i).a == placeId)
            {
                placesIdAndTime.remove(i);
                return true;
            }
        }
        return false;
    }
}
