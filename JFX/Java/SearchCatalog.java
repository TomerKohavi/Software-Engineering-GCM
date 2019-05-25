import java.util.ArrayList;

public final class SearchCatalog
{
    private SearchCatalog() {}

    public ArrayList<City> SearchCity(String cityName,String cityDescription,String placeName,String placeDescription) // they can be null
    {
        ArrayList<City> listCities=new ArrayList<>();
        ArrayList<City> listCitiesAccordingToPlaces=new ArrayList<>();
        int[] citiesIds=Database.searchCity(cityName,cityDescription);
        int[] placesIds=Database.searchPlaceOfInterest(placeName,placeDescription,null);
        for(int id:citiesIds)
        {
            City c=Database.getCityById(id);
            if(c==null) continue;
            if(!c.isTherePublishedVersion()) continue;
            listCities.add(c);
        }
        for(int id:placesIds)
        {
            PlaceOfInterest p=Database.getPlaceOfInterestById(id);
            if(p==null) continue;
            City c=Database.getCityById(p.getCityId());
            if(c==null) continue;
            if(!c.isTherePublishedVersion()) continue;
            listCitiesAccordingToPlaces.add(c);
        }
        if(placeName==null && placeDescription==null)
            return listCities;
        if(cityName==null & cityDescription==null)
            return listCitiesAccordingToPlaces;
        return Database.intersection(listCities,listCitiesAccordingToPlaces);
    }

    public ArrayList<Map> SearchMap(String cityName,String cityDescription,String placeName,String placeDescription) // they can be null
    {
        ArrayList<Map> listMapAccordingToCities=new ArrayList<>();
        ArrayList<Map> listMapAccordingToPlaces=new ArrayList<>();
        int[] citiesIds=Database.searchCity(cityName,cityDescription);
        int[] placesIds=Database.searchPlaceOfInterest(placeName,placeDescription,null);
        for(int id:citiesIds)
        {
            City c=Database.getCityById(id);
            if(c==null) continue;
            Integer publishedVersionId=c.getPublishedVersionId();
            if(publishedVersionId==null) continue;
            CityDataVersion cdv=Database._getCityDataVersionById(publishedVersionId);
            if(cdv==null) continue;
            ArrayList<MapSight> listMS=cdv.getAllMapSights();
            for(MapSight ms:listMS)
            {
                Map m=Database.getMapById(ms.getMapId());
                if(m==null) continue;
                listMapAccordingToCities.add(m);
            }
        }
        for(int id:placesIds)
        {
            int[] locIds=Database.searchLocation(null,id);
            for(int locId:locIds)
            {
                Location c=Database._getLocationById(locId);
                if(c==null) continue;
                Map m=Database.getMapById(c.getMapId());
                if(m==null) continue;
                listMapAccordingToPlaces.add(m);
            }
        }
        if(placeName==null && placeDescription==null)
            return listMapAccordingToCities;
        if(cityName==null & cityDescription==null)
            return listMapAccordingToPlaces;
        return Database.intersection(listMapAccordingToPlaces,listMapAccordingToPlaces);
    }

    public ArrayList<PlaceOfInterest> SearchPOI(String placeName,String placeDescription,String cityName,String cityDescription) // they can be null
    {
        ArrayList<PlaceOfInterest> listPlacesAccordingToCities=new ArrayList<>();
        ArrayList<PlaceOfInterest> listPlaces=new ArrayList<>();
        int[] citiesIds=Database.searchCity(cityName,cityDescription);
        int[] placesIds=Database.searchPlaceOfInterest(placeName,placeDescription,null);
        for(int id:citiesIds)
        {
            City c=Database.getCityById(id);
            if(c==null) continue;
            Integer publishedVersionId=c.getPublishedVersionId();
            if(publishedVersionId==null) continue;
            CityDataVersion cdv=Database._getCityDataVersionById(publishedVersionId);
            if(cdv==null) continue;
            ArrayList<PlaceOfInterestSight> listPOIS=cdv.getAllPlaceOfInterestSights();
            for(PlaceOfInterestSight ps:listPOIS)
            {
                PlaceOfInterest p=Database.getPlaceOfInterestById(ps.getPlaceOfInterestId());
                if(p==null) continue;
                listPlacesAccordingToCities.add(p);
            }
        }
        for(int id:placesIds)
        {
            PlaceOfInterest p=Database.getPlaceOfInterestById(id);
            if(p==null) continue;
            listPlaces.add(p);
        }
        if(placeName==null && placeDescription==null)
            return listPlacesAccordingToCities;
        if(cityName==null & cityDescription==null)
            return listPlaces;
        return Database.intersection(listPlaces,listPlacesAccordingToCities);
    }

}
