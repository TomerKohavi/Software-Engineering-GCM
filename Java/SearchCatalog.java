import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class SearchCatalog {
	private SearchCatalog() {
	}

	public ArrayList<City> SearchCity(String cityName, String cityDescription, String placeName,
			String placeDescription) // they can be null
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
					;
				result.add(c);
			}
			return result;
		}
		// just place
		if (cityName == null && cityDescription == null) {
			Set<Integer> citiesIds = new HashSet<>();
			ArrayList<Integer> placesIds = Database.searchPlaceOfInterest(placeName, placeDescription, null);
			for (int id : placesIds) {
				PlaceOfInterest p = Database.getPlaceOfInterestById(id);
				if (p != null)
					citiesIds.add(p.getCityId());
			}
			for (int id : citiesIds) {
				City c = Database.getCityById(id);
				if (c != null)
					;
				result.add(c);
			}
			return result;
		}
		// both
		ArrayList<Integer> citiesIds = Database.searchCity(cityName, cityDescription);
		for (Integer id : new ArrayList<>(citiesIds)) {
			ArrayList<Integer> placesOfCity = Database.searchPlaceOfInterest(placeName, placeDescription, id);
			if (placesOfCity.size() == 0)
				citiesIds.remove(id);
		}
		for (int id : citiesIds) {
			City c = Database.getCityById(id);
			if (c != null)
				;
			result.add(c);
		}
		return result;
	}

}
