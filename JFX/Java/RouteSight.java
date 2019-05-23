public class RouteSight
{
    private int id;
    private int cityDataVersionId;
    private int routeId;

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
