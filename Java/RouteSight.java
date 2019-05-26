public class RouteSight
{
    private int id;
    private int cityDataVersionId;
    private int routeId;

    private RouteSight(int id, int cityDataVersionId, int routeId) {
        this.id = id;
        this.cityDataVersionId = cityDataVersionId;
        this.routeId = routeId;
    }

    public static RouteSight _createRouteSight(int id, int cityDataVersionId, int routeId){ //friend to Database
        return new RouteSight( id,  cityDataVersionId,  routeId);
    }

    public RouteSight(int cityDataVersionId, int routeId) {
        this.id=Database.generateIdRouteSight();
        this.cityDataVersionId = cityDataVersionId;
        this.routeId = routeId;
    }

    public int getId() {
        return id;
    }

    public int getCityDataVersionId() {
        return cityDataVersionId;
    }

    public int getRouteId() {
        return routeId;
    }
}
