public class PlaceOfInterestSight
{
    private int id;
    private int cityDataVersionId;
    private int placeOfInterestId;

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
