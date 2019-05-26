public class PlaceOfInterestSight
{
    private int id;
    private int cityDataVersionId;
    private int placeOfInterestId;

    private PlaceOfInterestSight(int id, int cityDataVersionId, int placeOfInterestId) {
        this.id = id;
        this.cityDataVersionId = cityDataVersionId;
        this.placeOfInterestId = placeOfInterestId;
    }

    public static PlaceOfInterestSight _PlaceOfInterestSight(int id, int cityDataVersionId, int placeOfInterestId){ //friend to Database
        return new PlaceOfInterestSight( id,  cityDataVersionId,  placeOfInterestId);
    }

    public PlaceOfInterestSight(int cityDataVersionId, int placeOfInterestId) {
        this.id=Database.generateIdPlaceOfInterestSight();
        this.cityDataVersionId = cityDataVersionId;
        this.placeOfInterestId = placeOfInterestId;
    }

    public int getId() {
        return id;
    }

    public int getCityDataVersionId() {
        return cityDataVersionId;
    }

    public int getPlaceOfInterestId() {
        return placeOfInterestId;
    }
}
