package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import objectClasses.City;
import objectClasses.CityDataVersion;
import objectClasses.PlaceOfInterest;
import objectClasses.PlaceOfInterestSight;

public final class SearchCatalog {
	private SearchCatalog() {
	}

	public static ArrayList<City> SearchCity(String cityName, String cityDescription, String placeName,
											 String placeDescription)
	{
		return SearchCity( cityName,  cityDescription,  placeName,
				 placeDescription,false);
	}

	public static ArrayList<City> SearchCity(String cityName, String cityDescription, String placeName,
			String placeDescription,boolean useUnpublished) // they can be null
	{
		if (cityName == null && cityDescription == null && placeName == null && placeDescription == null)
			return null;

		ArrayList<City> result = new ArrayList<>();
		// just city
		if (placeName == null && placeDescription == null) {
			ArrayList<Integer> citiesIds = Database.searchCity(cityName, cityDescription);
			for (int id : citiesIds) {
				City c = Database.getCityById(id);
				if (c != null)
				{
					if(c.isTherePublishedVersion() || useUnpublished)
						result.add(c);
				}
			}
			return result;
		}
		// just place
		if (cityName == null && cityDescription == null) {
			Set<Integer> citiesIds = new HashSet<>();
			ArrayList<Integer> placesIds = Database.searchPlaceOfInterest(placeName, placeDescription, null);
			for (int id : placesIds) {
				PlaceOfInterest p = Database.getPlaceOfInterestById(id);
				if(p==null) continue;
				int cityId=p.getCityId();
				if(useUnpublished)
				{
					citiesIds.add(cityId);
				}
				else
				{
					if(citiesIds.contains(cityId)) continue;
					City c=Database.getCityById(cityId);
					if(c==null) continue;
					CityDataVersion cdv=c.getCopyPublishedVersion();
					if(cdv==null) continue;
					if(Database.searchPlaceOfInterestSight(cdv.getId(), p.getId())!=null)
						citiesIds.add(p.getCityId());
				}
			}
			for (int id : citiesIds) {
				City c = Database.getCityById(id);
				if (c != null)
					result.add(c);
			}
			return result;
		}
		// both
		ArrayList<Integer> citiesIds = Database.searchCity(cityName, cityDescription);
		for (int id : citiesIds) {
			City c = Database.getCityById(id);
			if (c != null)
				result.add(c);
		}
		if(useUnpublished)
		{
			for (City c: new ArrayList<>(result)) {
				ArrayList<Integer> placesOfCity = Database.searchPlaceOfInterest(placeName, placeDescription, c.getId());
				if (placesOfCity.size() == 0)
					result.remove(c);
			}
		}
		else
		{
			for (City c: new ArrayList<>(result)) {
				ArrayList<Integer> placesOfCity = Database.searchPlaceOfInterest(placeName, placeDescription, c.getId());
				boolean remove=true;
				for (int id : placesOfCity) {
						CityDataVersion cdv=c.getCopyPublishedVersion();
						if(cdv==null) continue;
						if(Database.searchPlaceOfInterestSight(cdv.getId(), id)!=null) {
							remove=false;
							break;
						}
							
				}
				if(remove)
					result.remove(c);
			}
		}
		return result;
	}

}
