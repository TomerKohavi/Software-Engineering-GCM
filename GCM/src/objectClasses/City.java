package objectClasses;

import java.io.Serializable;
import java.sql.SQLException;
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

	private double priceOneTime;
	private double pricePeriod;
	private boolean ceoNeedsToApprovePrices;
	private double toBePriceOneTime;
	private double toBePricePeriod;

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
	 * @throws SQLException if the access to database failed
	 */
	private City(int id, String cityName, String cityDescription, Integer publishedVersionId,
			boolean managerNeedsToPublish, double priceOneTime, double pricePeriod, boolean ceoNeedsToApprovePrices,
			double toBePriceOneTime, double toBePricePeriod) throws SQLException
	{
		this.id = id;
		this.cityName = cityName;
		this.cityDescription = cityDescription;
		this.publishedVersionId = publishedVersionId;
		this.managerNeedsToPublish = managerNeedsToPublish;
		this.priceOneTime = priceOneTime;
		this.pricePeriod = pricePeriod;
		this.ceoNeedsToApprovePrices = ceoNeedsToApprovePrices;
		this.toBePriceOneTime = toBePriceOneTime;
		this.toBePricePeriod = toBePricePeriod;
		reloadTempsFromDatabase();
	}

	/**
	 * This function create City object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id                    the city id
	 * @param cityName              the city name
	 * @param cityDescription       the city description
	 * @param publishedVersionId    the id of the published version of this city
	 * @param managerNeedsToPublish approval of managers
	 * @param priceOneTime price 1 time purchase
	 * @param pricePeriod price for period
	 * @param ceoNeedsToApprovePrices need approval
	 * @param toBePriceOneTime to be price
	 * @param toBePricePeriod to be price
	 * @return the new city object
	 * @throws SQLException if the access to database failed
	 */
	public static City _createCity(int id, String cityName, String cityDescription, Integer publishedVersionId,
			boolean managerNeedsToPublish, double priceOneTime, double pricePeriod, boolean ceoNeedsToApprovePrices,
			double toBePriceOneTime, double toBePricePeriod) throws SQLException
	{
		return new City(id, cityName, cityDescription, publishedVersionId, managerNeedsToPublish, priceOneTime,
				pricePeriod, ceoNeedsToApprovePrices, toBePriceOneTime, toBePricePeriod);
	}

	/**
	 * This is the normal public constructor for City object
	 * 
	 * @param cityName        the city name
	 * @param cityDescription the city description
	 * @param priceOneTime    price of one time purchase
	 * @param pricePeriod     of subscription
	 * @throws SQLException if the access to database failed
	 */
	public City(String cityName, String cityDescription, double priceOneTime, double pricePeriod) throws SQLException
	{
		this.id = Database.generateIdCity();
		this.cityName = cityName;
		this.cityDescription = cityDescription;
		this.managerNeedsToPublish = false;
		this.priceOneTime = priceOneTime;
		this.pricePeriod = pricePeriod;
		this.ceoNeedsToApprovePrices = false;
		this.toBePriceOneTime = priceOneTime;
		this.toBePricePeriod = pricePeriod;
		this.publishedVersionId = null;
		this.temp_unpublishedVersions = new ArrayList<>();
		this.temp_removeVersions = new ArrayList<>();
		this.temp_publishedVersion = null;
	}

	/**
	 * Saves the City object to the database, and it's data versions
	 * 
	 * @throws SQLException if the access to database failed
	 */
	public void saveToDatabase() throws SQLException
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
	 * 
	 * @throws SQLException if the access to database failed
	 */
	public void deleteFromDatabase() throws SQLException
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
	 * 
	 * @throws SQLException if the access to database failed
	 */
	public void reloadTempsFromDatabase() throws SQLException
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
	 * @throws SQLException if the access to database failed
	 */
	private ArrayList<CityDataVersion> generateUnpublishedCityDataVersions() throws SQLException
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
		
		//remove all other unpublished versions
		this.temp_removeVersions.addAll(this.temp_unpublishedVersions);
		this.temp_unpublishedVersions=new ArrayList<CityDataVersion>();
		
		temp_unpublishedVersions.add(cdv);
		return true;
	}

	/**
	 * Adds a new published version to this city, if there already a published
	 * version it becomes unpublished
	 * 
	 * @param cdv the new published city data version
	 * @return boolean if the insertion succeeded
	 * @throws SQLException 
	 */
	public boolean _addPublishedCityDataVersion(CityDataVersion cdv) throws SQLException
	{
		if (cdv.getCityId() != this.getId())
			return false;
		if (this.publishedVersionId != null && this.temp_publishedVersion != null)
			temp_unpublishedVersions.add(temp_publishedVersion);
		publishedVersionId = cdv.getId();
		temp_publishedVersion = cdv;
		
		
		String newVName=(Double.parseDouble(cdv.getVersionName())+1)+"";
		this.addUnpublishedCityDataVersion(new CityDataVersion(cdv, newVName));
		
		return true;
	}

	/**
	 * Set unpublished version to published, and the current published to
	 * unpublished
	 * 
	 * @param cdvId the city data version id
	 * @return boolean if change succeeded
	 * @throws SQLException 
	 */
	public boolean _setUnpublishedToPublishedByVersionId(int cdvId) throws SQLException
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
		if (cdv.getNumMapSights() == 0)
			return false;
		setPublishedToUnpublished();
		this.publishedVersionId = cdv.getId();
		this.temp_publishedVersion = cdv;
		
		String newVName=(Double.parseDouble(cdv.getVersionName())+1)+"";
		this.addUnpublishedCityDataVersion(new CityDataVersion(cdv, newVName));
		
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
	 * Approve the to be prices
	 */
	public void approveToBePrices()
	{
		this.priceOneTime = this.toBePriceOneTime;
		this.pricePeriod = this.toBePricePeriod;
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
	 * 
	 * @param managerNeedsToPublish if the a worker requested the manager to publish
	 *                              the unpublished version
	 */
	public void setManagerNeedsToPublish(boolean managerNeedsToPublish)
	{
		this.managerNeedsToPublish = managerNeedsToPublish;
	}

	/**
	 * Return boolean if the a worker requested the manager to publish the
	 * unpublished version
	 * 
	 * @return boolean if the a worker requested the manager to publish the
	 *         unpublished version
	 */
	public boolean getManagerNeedsToPublish()
	{
		return this.managerNeedsToPublish;
	}

	/**
	 * Returns the price of one time buy
	 * 
	 * @return the price of one time buy
	 */
	public double getPriceOneTime()
	{
		return priceOneTime;
	}

	/**
	 * Returns the price of period buy
	 * 
	 * @return the price of period buy
	 */
	public double getPricePeriod()
	{
		return pricePeriod;
	}

	/**
	 * Returns the price of period buy after discount
	 * 
	 * @return the price of period buy after discount
	 */
	public double getPricePeriodWithDiscount()
	{
		return pricePeriod * 0.9;
	}

	/**
	 * Sets the price of one time buy
	 * 
	 * @param priceOneTime the price of one time buy
	 */
	public void setPriceOneTime(double priceOneTime)
	{
		this.priceOneTime = priceOneTime;
	}

	/**
	 * Sets the price of period buy
	 * 
	 * @param pricePeriod the price of period buy
	 */
	public void setPricePeriod(double pricePeriod)
	{
		this.pricePeriod = pricePeriod;
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

	/**
	 * Return if CEO needs to approve the new prices
	 * 
	 * @return if CEO needs to approve the new prices
	 */
	public boolean isCeoNeedsToApprovePrices()
	{
		return ceoNeedsToApprovePrices;
	}

	/**
	 * Set if CEO needs to approve the new prices
	 * 
	 * @param ceoNeedsToApprovePrices if CEO needs to approve the new prices
	 */
	public void setCeoNeedsToApprovePrices(boolean ceoNeedsToApprovePrices)
	{
		this.ceoNeedsToApprovePrices = ceoNeedsToApprovePrices;
	}

	/**
	 * Return the one time purchase price that is not approved
	 * 
	 * @return the one time purchase price that is not approved
	 */
	public double getToBePriceOneTime()
	{
		return toBePriceOneTime;
	}

	/**
	 * Set the one time purchase price that is not approved
	 * 
	 * @param toBePriceOneTime the one time purchase price that is not approved
	 */
	public void setToBePriceOneTime(double toBePriceOneTime)
	{
		this.toBePriceOneTime = toBePriceOneTime;
	}

	/**
	 * Return the subscription one month purchase price that is not approved
	 * 
	 * @return the subscription one month purchase price that is not approved
	 */
	public double getToBePricePeriod()
	{
		return toBePricePeriod;
	}

	/**
	 * Set the subscription one month purchase price that is not approved
	 * 
	 * @param toBePricePeriod the subscription one month purchase price that is not
	 *                        approved
	 */
	public void setToBePricePeriod(double toBePricePeriod)
	{
		this.toBePricePeriod = toBePricePeriod;
	}
}
