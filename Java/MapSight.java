public class MapSight
{
    int id;
    int mapId;
    int cityDataVersionId;

    private MapSight(int id, int mapId, int cityDataVersionId) {
        this.id = id;
        this.mapId = mapId;
        this.cityDataVersionId = cityDataVersionId;
    }

    public static MapSight _createMapSight(int id, int mapId, int cityDataVersionId){ //friend to Database
        return new MapSight( id,  mapId,  cityDataVersionId);
    }

    public MapSight(int mapId, int cityDataVersionId) {
        this.id=Database.generateIdMapSight();
        this.mapId = mapId;
        this.cityDataVersionId = cityDataVersionId;
    }

    public int getId() {
        return id;
    }

    public int getMapId() {
        return mapId;
    }

    public int getCityDataVersionId() {
        return cityDataVersionId;
    }
}
