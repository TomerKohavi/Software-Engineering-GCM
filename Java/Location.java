import java.io.Serializable;

public class Location implements ClassMustProperties, Serializable {
    private int id;
    private int mapId;
    private int placeOfInterestId;
    private double[] coordinates;

    private PlaceOfInterest temp_place;

    private Location(int id, int mapId, int placeOfInterestId, double[] coordinates) {
        this.id = id;
        this.mapId = mapId;
        this.placeOfInterestId = placeOfInterestId;
        this.coordinates = coordinates;
        reloadTempsFromDatabase();
    }

    public static Location _createLocation(int id, int mapId, int placeOfInterestId, double[] coordinates){ //friend to Database
        return new Location( id,  mapId,  placeOfInterestId, coordinates);
    }

    public Location(Map m, PlaceOfInterest p, double[] coordinates) {
        this.id=Database.generateIdLocation();
        this.mapId = m.getId();
        this.placeOfInterestId = p.getId();
        this.coordinates = coordinates;
        this.temp_place=p;
    }

    public void saveToDatabase(){
        Database._saveLocation(this);
    }

    public void deleteFromDatabase(){
        Database._deleteLocation(this.getId());
    }

    public void reloadTempsFromDatabase(){
        this.temp_place=Database.getPlaceOfInterestById(this.placeOfInterestId);
    }

    public int getId() {
        return id;
    }

    public int getMapId() {
        return mapId;
    }

    public int getPlaceOfInterestId() {
        return placeOfInterestId;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public PlaceOfInterest getCopyPlaceOfInterest() {
        return temp_place;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Location && ((Location) o).getId()==this.getId();
    }
}