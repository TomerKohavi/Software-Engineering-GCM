import java.sql.Time;

public class RouteStop implements Comparable<RouteStop>
{
    int id;
    int routeId;
    int placeId;
    int numStop;
    Time recommendedTime;

    PlaceOfInterest temp_place;

    private RouteStop(int id, int routeId, int placeId, int numStop, Time recommendedTime) {
        this.id = id;
        this.routeId = routeId;
        this.placeId = placeId;
        this.numStop = numStop;
        this.recommendedTime = recommendedTime;
        this.temp_place=Database.getPlaceOfInterestById(placeId);
    }

    public static RouteStop _createRouteStop(int id, int routeId, int placeId, int numStop, Time recommendedTime){ //friend to Database
        return new RouteStop( id,  routeId,  placeId,  numStop,  recommendedTime);
    }

    public RouteStop(int placeId, int numStop, Time recommendedTime) {
        this.id=Database.generateIdRouteStop();
        this.routeId = Database.generateIdRouteStop();
        this.placeId = placeId;
        this.recommendedTime = recommendedTime;
        this.numStop=numStop;
        this.temp_place=Database.getPlaceOfInterestById(placeId);
    }

    public void saveToDatabase() {
        Database._saveRouteStop(this);
    }

    public void deleteFromDatabase() {
        Database._deleteRouteStop(this.id);
    }

    public PlaceOfInterest getCopyPlaceOfInterest(){
        return temp_place;
    }

    public int getId() {
        return id;
    }

    public void setNumStop(int numStop) {
        this.numStop = numStop;
    }

    public int getNumStop() {
        return numStop;
    }

    public int getRouteId() {
        return routeId;
    }

    public Route getRoute(){
        return Database.getRouteById(routeId);
    }

    public int getPlaceId() {
        return placeId;
    }

    public PlaceOfInterest getPlace(){
        return Database.getPlaceOfInterestById(placeId);
    }

    public Time getRecommendedTime() {
        return recommendedTime;
    }

    public void setRecommendedTime(Time recommendedTime) {
        this.recommendedTime = recommendedTime;
    }

    @Override
    public int compareTo(RouteStop o) {
        return this.numStop-o.numStop;
    }
}
