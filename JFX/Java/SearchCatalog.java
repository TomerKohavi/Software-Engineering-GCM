import java.util.ArrayList;

public final class SearchCatalog
{
    private SearchCatalog() {}

    public ArrayList<City> SearchCity(String cityName,String cityDescription,String placeName,String placeDescription) // they can be null
    {
        int[] citiesIds=Database.searchCity(cityName,cityDescription);
        int[] placesIds=Database.searchPlaceOfInterest(placeName,placeDescription);
        ArrayList<Integer> listCitiesIds=new ArrayList<Integer>();
        for(int id:citiesIds)
            listCitiesIds.add(id);
        ArrayList<Integer> listCitiesOfPlacesIds=new ArrayList<Integer>();
        for(int id:placesIds)
        {
            PlaceOfInterest p=Database.getPlaceOfInterestById(id);
            if(p!=null)
                listCitiesOfPlacesIds.add(p.getCityId());
        }
        ArrayList<Integer> listIntersection=Database.intersection(listCitiesIds,listCitiesOfPlacesIds);
        ArrayList<City> listResult=new ArrayList<City>();
        for(int id:listIntersection)
        {
            City c=Database.getCityById(id);
            if(c!=null)
                listResult.add(c);
        }
        return listResult;
    }

    public ArrayList<PlaceOfInterest> SearchPOI(String placeName,String placeDescription,String cityName,String cityDescription) // they can be null
    {
        int[] citiesIds=Database.searchCity(cityName,cityDescription);
        int[] placesIds=Database.searchPlaceOfInterest(placeName,placeDescription);
        ArrayList<Integer> listCitiesIds=new ArrayList<Integer>();
        for(int id:citiesIds)
            listCitiesIds.add(id);
        ArrayList<PlaceOfInterest> listResult=new ArrayList<PlaceOfInterest>();
        for(int id:placesIds)
        {
            PlaceOfInterest p=Database.getPlaceOfInterestById(id);
            if(p!=null && listCitiesIds.contains(p.getCityId()))
                listResult.add(p);
        }
        return listResult;
    }

}
