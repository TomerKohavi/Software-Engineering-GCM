public class Location {
    private int id;
    private int mapId;
    private int placeOfInterestId;
    private double[] coordinates;

    private Location(int id, int mapId, int placeOfInterestId, double[] coordinates) {
        this.id = id;
        this.mapId = mapId;
        this.placeOfInterestId = placeOfInterestId;
        this.coordinates = coordinates;
    }

    public static Location _createLocation(int id, int mapId, int placeOfInterestId, double[] coordinates){ //friend to Database
        return new Location( id,  mapId,  placeOfInterestId, coordinates);
    }

    public Location(int mapId, int placeOfInterestId, double[] coordinates) {
        this.id=Database.generateIdLocation();
        this.mapId = mapId;
        this.placeOfInterestId = placeOfInterestId;
        this.coordinates = coordinates;
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

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}