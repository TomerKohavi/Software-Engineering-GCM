public class Location {
    private int id;
    private Map map;
    private double[] coordinates;

    public Location(Map map,double[] coordinates)
    {
        this.id=Database.generateIdLocation();
        this.map=map;
        this.coordinates=coordinates;
    }

    public int getId() {
        return id;
    }

    public Map getMap() {
        return map;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

}