package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import objectClasses.City;
import objectClasses.CityDataVersion;
import objectClasses.PlaceOfInterest;
import objectClasses.PlaceOfInterestSight;

/**
 * Controller class that handles the search city query 
 * 
 * @author Ron Cohen
 */
public final class SearchCatalog {
	/**
	 * change the default constructor to private,
	 * this class cannot be created as object.
	 */
	private SearchCatalog() {
	}

	/**
	 * Function that search the database for the desired cities with the properties of this function input (search only published versions).
	 * @param cityName the city name (or part of it)
	 * @param cityDescription the city description (or words it contains)
	 * @param placeName the place of interest that in the city name (or part of it)
	 * @param placeDescription the place of interest that in the city description (or words it contains)
	 * @return search result- list of cities that was found
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<City> SearchCity(String cityName, String cityDescription, String placeName,
											 String placeDescription) throws SQLException
	{
		return SearchCity( cityName,  cityDescription,  placeName,
				 placeDescription,false);
	}
	
	/**
	 * Function that search the database for the desired cities with the properties of this function input.
	 * @param cityName the city name (or part of it)
	 * @param cityDescription the city description (or words it contains)
	 * @param placeName the place of interest that in the city name (or part of it)
	 * @param placeDescription the place of interest that in the city description (or words it contains)
	 * @param useUnpublished if to also search in the unpublished versions of the cities
	 * @return search result- list of cities that was found
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<City> SearchCity(String cityName, String cityDescription, String placeName,
			String placeDescription,boolean useUnpublished) throws SQLException // they can be null
	{
		System.out.println("Search City:" +cityName+"|"+cityDescription+"|"+placeName+"|"+placeDescription+"|"+useUnpublished);
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
					if(Database.searchPlaceOfInterestSight(cdv.getId(), p.getId()).size()>0)
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
						if(Database.searchPlaceOfInterestSight(cdv.getId(), id).size()>0) {
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
