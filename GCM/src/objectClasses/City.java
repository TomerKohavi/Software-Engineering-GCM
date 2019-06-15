package objectClasses;

import java.io.Serializable;
import java.util.ArrayList;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * Class of a city object
 */
@SuppressWarnings("serial")
public class City implements ClassMustProperties, Serializable
{
	private int id;
	private String cityName;
	private String cityDescription;
	private Integer publishedVersionId;
	private boolean managerNeedsToPublish;

	private ArrayList<CityDataVersion> temp_unpublishedVersions;
	private ArrayList<CityDataVersion> temp_removeVersions;
	private CityDataVersion temp_publishedVersion;

	/**
	 * This is a private constructor of City object
	 * 
	 * @param id                 the city id
	 * @param cityName           the city name
	 * @param cityDescription    the city description
	 * @param publishedVersionId the id of the published version of this cirt
	 */
	private City(int id, String cityName, String cityDescription, Integer publishedVersionId,boolean managerNeedsToPublish)
	{
		this.id = id;
		this.cityName = cityName;
		this.cityDescription = cityDescription;
		this.publishedVersionId = publishedVersionId;
		this.managerNeedsToPublish=managerNeedsToPublish;
		reloadTempsFromDatabase();
	}

	/**
	 * This function create City object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id                 the city id
	 * @param cityName           the city name
	 * @param cityDescription    the city description
	 * @param publishedVersionId the id of the published version of this city
	 * @param managerNeedsToPublish approval of managers
	 * @return the new city object
	 */
	public static City _createCity(int id, String cityName, String cityDescription, Integer publishedVersionId,boolean managerNeedsToPublish)
	{
		return new City(id, cityName, cityDescription, publishedVersionId,managerNeedsToPublish);
	}

	/**
	 * This is the normal public constructor for City object
	 * 
	 * @param cityName        the city name
	 * @param cityDescription the city description
	 */
	public City(String cityName, String cityDescription)
	{
		this.id = Database.generateIdCity();
		this.cityName = cityName;
		this.cityDescription = cityDescription;
		this.managerNeedsToPublish=false;
		this.publishedVersionId = null;
		this.temp_unpublishedVersions = new ArrayList<>();
		this.temp_removeVersions = new ArrayList<>();
		this.temp_publishedVersion = null;
	}

	/**
	 * Saves the City object to the database, and it's data versions
	 */
	public void saveToDatabase()
	{
		Database.saveCity(this);
		// delete removes
		for (CityDataVersion cdv : temp_removeVersions)
		{
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

	/**
	 * Deletes this city from the database, and it's data versions
	 */
	public void deleteFromDatabase()
	{
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
		/*
		 * ArrayList<Integer> ids = Database.searchStatistic(this.id, null, null, null,
		 * null); for (int id : ids) { Statistic s = Database._getStatisticById(id); if
		 * (s != null) s.deleteFromDatabase(); }
		 */
	}

	/**
	 * Reload the temps variables from the database
	 */
	public void reloadTempsFromDatabase()
	{
		this.temp_unpublishedVersions = generateUnpublishedCityDataVersions();
		this.temp_removeVersions = new ArrayList<>();
		if (this.publishedVersionId == null)
			this.temp_publishedVersion = null;
		else
			this.temp_publishedVersion = Database._getCityDataVersionById(publishedVersionId);
	}

	/**
	 * This private function create list of the unpublished data versions of this
	 * city
	 * 
	 * @return list of the unpublished data versions of this city
	 */
	private ArrayList<CityDataVersion> generateUnpublishedCityDataVersions()
	{
		ArrayList<Integer> ids = Database.searchCityDataVersion(this.id);
		ArrayList<CityDataVersion> arrList = new ArrayList<CityDataVersion>();
		for (int id : ids)
		{
			CityDataVersion o = Database._getCityDataVersionById(id);
			if (o != null && (Integer) id != this.publishedVersionId)
				arrList.add(o);
		}
		return arrList;
	}

	/**
	 * Calculate the number of unpublished versions
	 * 
	 * @return the number of unpublished versions
	 */
	public int getNumUnpublishedCityDataVersions()
	{
		return temp_unpublishedVersions.size();
	}

	/**
	 * Returns the unpublished data version of this city according to id
	 * 
	 * @param cdvId The city data version id
	 * @return he unpublished data version of this city according to id
	 */
	public CityDataVersion getUnpublishedCityDataVersionById(int cdvId)
	{
		for (CityDataVersion cdv : temp_unpublishedVersions)
		{
			if (cdv.getId() == cdvId)
				return cdv;
		}
		return null;
	}

	/**
	 * Adds a new unpublished version to this city
	 * 
	 * @param cdv the new unpublished city data version
	 * @return boolean if the insertion succeeded
	 */
	public boolean addUnpublishedCityDataVersion(CityDataVersion cdv)
	{
		if (cdv.getCityId() != this.getId())
			return false;
		temp_unpublishedVersions.add(cdv);
		return true;
	}

	/**
	 * Adds a new published version to this city, if there already a published
	 * version it becomes unpublished
	 * 
	 * @param cdv the new published city data version
	 * @return boolean if the insertion succeeded
	 */
	public boolean addPublishedCityDataVersion(CityDataVersion cdv)
	{
		if (cdv.getCityId() != this.getId())
			return false;
		if(cdv.getNumMapSights()==0)
		{
			addUnpublishedCityDataVersion(cdv);
			return false;
		}
		if (this.publishedVersionId != null && this.temp_publishedVersion != null)
			temp_unpublishedVersions.add(temp_publishedVersion);
		publishedVersionId = cdv.getId();
		temp_publishedVersion = cdv;
		return true;
	}

	/**
	 * Set unpublished version to published, and the current published to
	 * unpublished
	 * 
	 * @param cdvId the city data version id
	 * @return boolean if change succeeded
	 */
	public boolean setUnpublishedToPublishedByVersionId(int cdvId)
	{
		CityDataVersion cdv = null;
		for (CityDataVersion temp : new ArrayList<>(temp_unpublishedVersions))
		{
			if (temp.getId() == cdvId)
			{
				temp_unpublishedVersions.remove(temp);
				cdv = temp;
				break;
			}
		}
		if (cdv == null)
			return false;
		if(cdv.getNumMapSights()==0)
			return false;
		setPublishedToUnpublished();
		this.publishedVersionId = cdv.getId();
		this.temp_publishedVersion = cdv;
		return true;
	}

	/**
	 * Sets the current published version to unpublished
	 * 
	 * @return if change succeeded
	 */
	public boolean setPublishedToUnpublished()
	{
		if (this.publishedVersionId == null || this.temp_publishedVersion == null)
			return false;
		temp_unpublishedVersions.add(temp_publishedVersion);
		publishedVersionId = null;
		temp_publishedVersion = null;
		return true;
	}

	/**
	 * Returns the current public version, if none it will return null
	 * 
	 * @return the current public version
	 */
	public CityDataVersion getPublishedVersion()
	{
		return temp_publishedVersion;
	}

	/**
	 * Returns the current public version id, if none it will return null
	 * 
	 * @return the current public version id
	 */
	public Integer getPublishedVersionId()
	{
		return publishedVersionId;
	}

	/**
	 * Returns if there is a published version
	 * 
	 * @return boolean if there is a published version
	 */
	public boolean isTherePublishedVersion()
	{
		return getPublishedVersion() != null;
	}

	/**
	 * Removes city data version by id
	 * 
	 * @param cdvId the city data version id
	 * @return the version that was removed, null if none was found
	 */
	public CityDataVersion removeCityDataVersionById(int cdvId)
	{
		if ((Integer) cdvId == publishedVersionId)
		{
			CityDataVersion cdv = temp_publishedVersion;
			publishedVersionId = null;
			temp_removeVersions.add(cdv);
			temp_publishedVersion = null;
			return cdv;
		}
		for (CityDataVersion cdv : new ArrayList<>(temp_unpublishedVersions))
		{
			if (cdv.getId() == cdvId)
			{
				temp_unpublishedVersions.remove(cdv);
				temp_removeVersions.add(cdv);
				return cdv;
			}
		}
		return null;
	}

	/**
	 * Returns the city id
	 * 
	 * @return the city id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Returns the city name
	 * 
	 * @return the city name
	 */
	public String getCityName()
	{
		return cityName;
	}

	/**
	 * Return the city description
	 * 
	 * @return the city description
	 */
	public String getCityDescription()
	{
		return cityDescription;
	}

	/**
	 * Sets the city name
	 * 
	 * @param cityName the new city name
	 */
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	/**
	 * Sets the city description
	 * 
	 * @param cityDescription the new city description
	 */
	public void setCityDescription(String cityDescription)
	{
		this.cityDescription = cityDescription;
	}

	/**
	 * Return a copy of the published version
	 * 
	 * @return a copy of the published version
	 */
	public CityDataVersion getCopyPublishedVersion()
	{
		return temp_publishedVersion;
	}

	/**
	 * Returns a copy of the unpublished versions list
	 * 
	 * @return a copy of the unpublished versions list
	 */
	public ArrayList<CityDataVersion> getCopyUnpublishedVersions()
	{
		return new ArrayList<>(temp_unpublishedVersions);
	}
	
	/**
	 * Set if the a worker requested the manager to publish the unpublished version
	 * @param managerNeedsToPublish if the a worker requested the manager to publish the unpublished version
	 */
	public void setManagerNeedsToPublish(boolean managerNeedsToPublish) {
		this.managerNeedsToPublish=managerNeedsToPublish;
	}
	
	/**
	 * Return boolean if the a worker requested the manager to publish the unpublished version
	 * @return boolean if the a worker requested the manager to publish the unpublished version
	 */
	public boolean getManagerNeedsToPublish() {
		return this.managerNeedsToPublish;
	}

	/**
	 * Define the compare to ratio with another City
	 * 
	 * @param o a city object
	 * @return ratio with another City
	 */
	@Override
	public boolean equals(Object o)
	{
		return o instanceof City && ((City) o).getId() == this.getId();
	}
}
