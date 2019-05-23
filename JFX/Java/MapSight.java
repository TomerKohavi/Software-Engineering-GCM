public class MapSight
{
    int id;
    int mapId;
    int cityDataVersionId;

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
