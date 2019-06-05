package classes;
import java.io.Serializable;

public class PlaceOfInterestSight implements ClassMustProperties, Serializable {
	private int id;
	private int cityDataVersionId;
	private int placeOfInterestId;

	private PlaceOfInterest temp_place;

	private PlaceOfInterestSight(int id, int cityDataVersionId, int placeOfInterestId) {
		this.id = id;
		this.cityDataVersionId = cityDataVersionId;
		this.placeOfInterestId = placeOfInterestId;
		reloadTempsFromDatabase();
	}

	public static PlaceOfInterestSight _PlaceOfInterestSight(int id, int cityDataVersionId, int placeOfInterestId) { // friend
																														// to
																														// Database
		return new PlaceOfInterestSight(id, cityDataVersionId, placeOfInterestId);
	}

	public PlaceOfInterestSight(CityDataVersion cdv, PlaceOfInterest p) {
		this.id = Database.generateIdPlaceOfInterestSight();
		this.cityDataVersionId = cdv.getId();
		this.placeOfInterestId = p.getId();
		this.temp_place = p;
	}

	public void saveToDatabase() {
		Database._savePlaceOfInterestSight(this);
	}

	public void deleteFromDatabase() {
		Database._deletePlaceOfInterestSight(this.id);
	}

	public void reloadTempsFromDatabase() {
		this.temp_place = Database.getPlaceOfInterestById(placeOfInterestId);
	}

	public int getId() {
		return id;
	}

	public int getCityDataVersionId() {
		return cityDataVersionId;
	}

	public int getPlaceOfInterestId() {
		return placeOfInterestId;
	}

	public PlaceOfInterest getCopyPlace() {
		return temp_place;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof PlaceOfInterestSight && ((PlaceOfInterestSight) o).getId() == this.getId();
	}
}
