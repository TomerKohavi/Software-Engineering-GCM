package classes;
import java.io.Serializable;
import java.util.ArrayList;

public class City implements ClassMustProperties, Serializable {
	private int id;
	private String cityName;
	private String cityDescription;
	private Integer publishedVersionId;

	private ArrayList<CityDataVersion> temp_unpublishedVersions;
	private ArrayList<CityDataVersion> temp_removeVersions;
	private CityDataVersion temp_publishedVersion;

	private City(int id, String cityName, String cityDescription, Integer publishedVersionId) {
		   /**
		   * This method is private Constractor for City
		   * @param id the id of the city
		   * @param cityName the name of the city 
		   * @param cityDescription the description of the city 
		   * @param publishedVersionId the id of the publish
		   * @return City object
		   */
		this.id = id;
		this.cityName = cityName;
		this.cityDescription = cityDescription;
		this.publishedVersionId = publishedVersionId;
		reloadTempsFromDatabase();
	}

	public static City _createCity(int id, String cityName, String cityDescription, Integer publishedVersionId) { // friend
																													// to
																													// Database
		   /**
		   * This method is public Constractor that get id for City
		   * @param id the id of the city
		   * @param cityName the name of the city 
		   * @param cityDescription the description of the city 
		   * @param publishedVersionId the id of the publish
		   * @return City object
		   */
		return new City(id, cityName, cityDescription, publishedVersionId);
	}

	public City(String cityName, String cityDescription) {
		   /**
		   * This method is public Constractor that generete get id for City
		   * @param cityName the name of the city 
		   * @param cityDescription the description of the city 
		   * @return City object that genetere his id
		   */
		this.id = Database.generateIdCity();
		this.cityName = cityName;
		this.cityDescription = cityDescription;
		this.publishedVersionId = null;
		this.temp_unpublishedVersions = new ArrayList<>();
		this.temp_removeVersions = new ArrayList<>();
		this.temp_publishedVersion = null;
	}

	public void saveToDatabase() {
		   /**
		   * save the city object to the data base
		   * @return None
		   */
		Database.saveCity(this);
		// delete removes
		for (CityDataVersion cdv : temp_removeVersions) {
			if (!temp_unpublishedVersions.contains(cdv) && !temp_publishedVersion.equals(cdv))
				cdv.deleteFromDatabase();
		}
		temp_removeVersions = new ArrayList<>();
		// save
		for (CityDataVersion cdv : temp_unpublishedVersions)
			cdv.saveToDatabase();
		if (temp_publishedVersion != null)
			temp_publishedVersion.saveToDatabase();
	}

	public void deleteFromDatabase() {
		   /**
		   * delete the city object from the data base
		   * @return None
		   */
		Database.deleteCity(this.getId());
		// delete removes
		for (CityDataVersion cdv : temp_removeVersions)
			cdv.deleteFromDatabase();
		temp_removeVersions = new ArrayList<>();

		for (CityDataVersion cdv : temp_unpublishedVersions)
			cdv.deleteFromDatabase();
		if (temp_publishedVersion != null)
			temp_publishedVersion.deleteFromDatabase();
		// remove statistics
		ArrayList<Integer> ids = Database.searchStatistic(this.id, null, null, null);
		for (int id : ids) {
			Statistic s = Database._getStatisticById(id);
			if (s != null)
				s.deleteFromDatabase();
		}
	}

	public void reloadTempsFromDatabase() {
		   /**
		   * load the city object from the data base
		   * @return None
		   */
		this.temp_unpublishedVersions = generateUnpublishedCityDataVersions();
		this.temp_removeVersions = new ArrayList<>();
		if (this.publishedVersionId == null)
			this.temp_publishedVersion = null;
		else {
			this.temp_publishedVersion = Database._getCityDataVersionById(publishedVersionId);
			if (temp_publishedVersion == null)
				this.publishedVersionId = null;
		}
	}

	private ArrayList<CityDataVersion> generateUnpublishedCityDataVersions() {
		   /**
		   * generate unpublished city data versions
		   * @return array list of city data version
		   */
		ArrayList<Integer> ids = Database.searchCityDataVersion(this.id);
		ArrayList<CityDataVersion> arrList = new ArrayList<CityDataVersion>();
		for (int id : ids) {
			CityDataVersion o = Database._getCityDataVersionById(id);
			if (o != null && id != this.publishedVersionId)
				arrList.add(o);
		}
		return arrList;
	}

	public int getNumUnpublishedCityDataVersions() {
		   /**
		   * @return int - number of un published city data version
		   */
		return temp_unpublishedVersions.size();
	}

	public CityDataVersion getUnpublishedCityDataVersionById(int cdvId) {
		   /**
		   * load the city data version by id from data base
		   * @return city data version
		   */
		for (CityDataVersion cdv : temp_unpublishedVersions) {
			if (cdv.getId() == cdvId)
				return cdv;
		}
		return null;
	}

	public boolean addUnpublishedCityDataVersion(CityDataVersion cdv) {
		   /**
		   * add unpublished city data version
		   * @return boolean - true if that was added
		   */
		if (cdv.getCityId() != this.getId())
			return false;
		temp_unpublishedVersions.add(cdv);
		return true;
	}

	public boolean addPublishedCityDataVersion(CityDataVersion cdv) {
		   /**
		   * add published city data version
		   * @return boolean - true if that was added
		   */
		if (cdv.getCityId() != this.getId())
			return false;
		if (this.publishedVersionId != null && this.temp_publishedVersion != null)
			temp_unpublishedVersions.add(temp_publishedVersion);
		publishedVersionId = cdv.getId();
		temp_publishedVersion = cdv;
		return true;
	}

	public boolean setUnpublishedToPublishedByVersionId(int cdvId) {
		   /**
		   * set unpublished to published by versionId
		   * @return boolean - true if that was been in the data base
		   */
		CityDataVersion cdv = null;
		for (CityDataVersion temp : new ArrayList<>(temp_unpublishedVersions)) {
			if (temp.getId() == cdvId) {
				temp_unpublishedVersions.remove(temp);
				cdv = temp;
				break;
			}
		}
		if (cdv == null)
			return false;
		setPublishedToUnpublished();
		this.publishedVersionId = cdv.getId();
		this.temp_publishedVersion = cdv;
		return true;
	}

	public boolean setPublishedToUnpublished() {
		   /**
		   * set published to unpublished
		   * @return boolean - true if that was been in the data base
		   */
		if (this.publishedVersionId == null || this.temp_publishedVersion == null)
			return false;
		temp_unpublishedVersions.add(temp_publishedVersion);
		publishedVersionId = null;
		temp_publishedVersion = null;
		return true;
	}

	public CityDataVersion getPublishedVersion() {
		   /**
		   * get published version
		   * @return city data version
		   */
		return temp_publishedVersion;
	}

	public Integer getPublishedVersionId() {
		   /**
		   * get get published version Id
		   * @return integer of the id
		   */
		return publishedVersionId;
	}

	public boolean isTherePublishedVersion() {
		   /**
		   * check if there is published version
		   * @return boolean
		   */
		return getPublishedVersion() != null;
	}

	public CityDataVersion removeCityDataVersionById(int cdvId) {
		   /**
		   * remove the city data version with the id from the data base
		   * @param the id of the city
		   * @return city data version
		   */
		if (cdvId == publishedVersionId) {
			CityDataVersion cdv = temp_publishedVersion;
			publishedVersionId = null;
			temp_removeVersions.add(cdv);
			temp_publishedVersion = null;
			return cdv;
		}
		for (CityDataVersion cdv : new ArrayList<>(temp_unpublishedVersions)) {
			if (cdv.getId() == cdvId) {
				temp_unpublishedVersions.remove(cdv);
				temp_removeVersions.add(cdv);
				return cdv;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public String getCityName() {
		return cityName;
	}

	public String getCityDescription() {
		return cityDescription;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setCityDescription(String cityDescription) {
		this.cityDescription = cityDescription;
	}

	public CityDataVersion getCopyPublishedVersion() {
		return temp_publishedVersion;
	}

	public ArrayList<CityDataVersion> getCopyUnpublishedVersions() {
		return new ArrayList<>(temp_unpublishedVersions);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof City && ((City) o).getId() == this.getId();
	}
}
