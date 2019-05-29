public class MapSight
{
    int id;
    int mapId;
    int cityDataVersionId;

    Map temp_map;

    private MapSight(int id, int mapId, int cityDataVersionId) {
        this.id = id;
        this.mapId = mapId;
        this.cityDataVersionId = cityDataVersionId;
        this.temp_map=Database.getMapById(mapId);
    }

    public static MapSight _createMapSight(int id, int mapId, int cityDataVersionId){ //friend to Database
        return new MapSight( id,  mapId,  cityDataVersionId);
    }

    public MapSight(int mapId, int cityDataVersionId) {
        this.id=Database.generateIdMapSight();
        this.mapId = mapId;
        this.cityDataVersionId = cityDataVersionId;
        this.temp_map=Database.getMapById(mapId);
    }

    public void saveToDatabase(){
        Database._saveMapSight(this);
    }

    public Map getCopyMap(){
        return temp_map;
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
