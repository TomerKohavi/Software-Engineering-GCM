
package objectClasses;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * Abstract class of a general purchase of city
 * @author Ron Cohen
 */
@SuppressWarnings("serial")
public abstract class CityPurchase implements Comparable<CityPurchase>, Serializable, ClassMustProperties {
	private int id;
	private int cityId;
	private int userId;
	private LocalDate purchaseDate;
	private double fullPrice;
	private double pricePayed;
	
	protected String cityName;

	/**
	 * This is a private constructor of city purchase abstract object
	 * 
	 * @param id the id of the city purchase
	 * @param cityId the city id that contains the city purchase
	 * @param userId the user id that buy the city purchase
	 * @param purchaseDate when does the city purchase bought
	 * @param fullPrice the full price of the buy
	 * @param pricePayed how much payed for the buy
	 * @throws SQLException if the access to database failed
	 */
	protected CityPurchase(int id, int cityId, int userId, LocalDate purchaseDate, double fullPrice, double pricePayed) throws SQLException {
		this.id = id;
		this.cityId = cityId;
		this.userId = userId;
		this.purchaseDate = purchaseDate;
		this.fullPrice = fullPrice;
		this.pricePayed = pricePayed;
		reloadTempsFromDatabase();
	}
	
	protected CityPurchase(int id, int cityId, int userId, LocalDate purchaseDate, double fullPrice, double pricePayed, String cityName) {
		this.id = id;
		this.cityId = cityId;
		this.userId = userId;
		this.purchaseDate = purchaseDate;
		this.fullPrice = fullPrice;
		this.pricePayed = pricePayed;
		this.cityName = cityName;
	}

	/**
	 * This is the normal public constructor for city purchase object
	 * 
	 * @param userId the user id that buy the city purchase
	 * @param cityId the city id that contains the city purchase
	 * @param purchaseDate when does the city purchase bought
	 * @param fullPrice the full price of the buy
	 * @param pricePayed how much payed for the buy
	 * @throws SQLException if the access to database failed
	 */
	public CityPurchase(int userId, int cityId, LocalDate purchaseDate, double fullPrice, double pricePayed) throws SQLException {
		this.id = Database.generateIdCityPurchase();
		this.cityId = cityId;
		this.userId = userId;
		this.purchaseDate = purchaseDate;
		this.fullPrice = fullPrice;
		this.pricePayed = pricePayed;
		reloadTempsFromDatabase();
	}
	
	/**
	 * reload temporal city purchase from the data base
	 * @throws SQLException if the access to database failed
	 */
	public void reloadTempsFromDatabase() throws SQLException{
		this.cityName=Database.getCityNameById(cityId);
    }

	/**
	 * Returns the user id
	 * 
	 * @return the user id
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Returns the city purchase id
	 * 
	 * @return the city purchase id
	 */
	public int getId() {
		return id;
	}

	public void _setId(int id)
	{
		this.id = id;
	}


	
	/**
	 * Returns the city id
	 * 
	 * @return the city id
	 */
	public int getCityId() {
		return cityId;
	}

	/**
	 * Returns the full price
	 * 
	 * @return the full price
	 */
	public double getFullPrice() {
		return fullPrice;
	}

	/**
	 * Returns the price that was actually payed
	 * 
	 * @return the price that was actually payed
	 */
	public double getPricePayed() {
		return pricePayed;
	}

	/**
	 * Sets the city purchased id
	 * 
	 * @param cityPurchasedId the city purchased id
	 */
	public void setCityPurchasedId(int cityPurchasedId) {
		this.cityId = cityPurchasedId;
	}

	/**
	 * Sets the full price value
	 * 
	 * @param fullPrice the full price value
	 */
	public void setFullPrice(double fullPrice) {
		this.fullPrice = fullPrice;
	}

	/**
	 * Sets the price payed value
	 * 
	 * @param pricePayed the price payed value
	 */
	public void setPricePayed(double pricePayed) {
		this.pricePayed = pricePayed;
	}

	/**
	 * Returns the purchase date
	 * 
	 * @return the purchase date
	 */
	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	/**
	 * Sets the purchase date
	 * 
	 * @param purchaseDate the purchase date
	 */
	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	/**
	 * Returns the city name
	 * 
	 * @return the city name
	 */
	public String getCityName() {
		return this.cityName;
	}

	/**
	 * comparing two city purchase object by purchase date
	 * @param o city purchase object to compare 
	 * @return if this is the same purchase
	 */
	@Override
	public int compareTo(CityPurchase o) {
		return this.getPurchaseDate().compareTo(o.getPurchaseDate());
	}
}