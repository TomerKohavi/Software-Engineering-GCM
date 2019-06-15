package objectClasses;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * class of one time purchase object
 * 
 * @author Ron Cohen
 *
 */
@SuppressWarnings("serial")
public class OneTimePurchase extends CityPurchase implements ClassMustProperties, Serializable
{
	private boolean wasDownload;

	/**
	 * This is a private constructor of one time purchase object
	 * 
	 * @param id the one time purchase id
	 * @param cityId the city id of the one time purchase
	 * @param userId the user id that buy this one time purchase
	 * @param purchaseDate the date of the purchase
	 * @param fullPrice the full price of the buy
	 * @param pricePayed how much is actually payed 
	 * @param wasDownload if the one time purchase was download or not
	 * @throws SQLException if the access to database failed
	 */
	private OneTimePurchase(int id, int cityId, int userId, LocalDate purchaseDate, double fullPrice, double pricePayed,
			boolean wasDownload) throws SQLException
	{
		super(id, cityId, userId, purchaseDate, fullPrice, pricePayed);
		this.wasDownload = wasDownload;
	}

	/**
	 * This is a private constructor of one time purchase object
	 * 
	 * @param id the one time purchase id
	 * @param cityId the city id of the one time purchase
	 * @param userId the user id that buy this one time purchase
	 * @param purchaseDate the date of the purchase
	 * @param fullPrice the full price of the buy
	 * @param pricePayed how much is actually payed 
	 * @param wasDownload if the one time purchase was download or not
	 * @param cityName the name of the city
	 */
	private OneTimePurchase(int id, int cityId, int userId, LocalDate purchaseDate, double fullPrice, double pricePayed,
			boolean wasDownload, String cityName)
	{
		super(id, cityId, userId, purchaseDate, fullPrice, pricePayed, cityName);
		this.wasDownload = wasDownload;
	}

	/**
	 * This function create one time purchase object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id the one time purchase id
	 * @param cityId the city id of the one time purchase
	 * @param userId the user id that buy this one time purchase
	 * @param purchaseDate the date of the purchase
	 * @param fullPrice the full price of the buy
	 * @param pricePayed how much is actually payed 
	 * @param wasDownload if the one time purchase was download or not
	 * @return new one time purchase object
	 * @throws SQLException if the access to database failed
	 */
	public static OneTimePurchase _createOneTimePurchase(int id, int cityId, int userId, LocalDate purchaseDate,
			double fullPrice, double pricePayed, boolean wasDownload) throws SQLException
	{ // friend to Database
		return new OneTimePurchase(id, cityId, userId, purchaseDate, fullPrice, pricePayed, wasDownload);
	}
	
	/**
	 * This function create one time purchase object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id the one time purchase id
	 * @param cityId the city id of the one time purchase
	 * @param userId the user id that buy this one time purchase
	 * @param purchaseDate the date of the purchase
	 * @param fullPrice the full price of the buy
	 * @param pricePayed how much is actually payed 
	 * @param wasDownload if the one time purchase was download or not
	 * @param cityName the name of the city
	 * @return new one time purchase object
	 */
	public static OneTimePurchase _createLocalOneTimePurchase(int id, int cityId, int userId, LocalDate purchaseDate,
			double fullPrice, double pricePayed, boolean wasDownload, String cityName)
	{ // friend to Database
		return new OneTimePurchase(id, cityId, userId, purchaseDate, fullPrice, pricePayed, wasDownload, cityName);
	}

	
	/**
	 * This is the normal public constructor for City object
	 * 
	 * @param u the customer that buy the one time purchase
	 * @param cityId city ID
	 * @param purchaseDate purchase date
	 * @param fullPrice full price of the map
	 * @param pricePayed priced payed
	 * @throws SQLException if the access to database failed
	 */
	public OneTimePurchase(Customer u, int cityId, LocalDate purchaseDate, double fullPrice, double pricePayed) throws SQLException
	{
		super(u.getId(), cityId, purchaseDate, fullPrice, pricePayed);
		this.wasDownload = false;
	}

	public void saveToDatabase() throws SQLException
	{
		Database._saveOneTimePurchase(this);
	}

	public void deleteFromDatabase() throws SQLException
	{
		Database._deleteOneTimePurchase(this.getId());
	}

	/**
	 * Returns true if the one time purchase was download
	 * 
	 * @return true if the one time purchase was download
	 */
	public boolean getWasDownload()
	{
		return wasDownload;
	}

	/**
	 * Set the purchase to be download
	 */
	public void updateToWasDownload()
	{
		this.wasDownload = true;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof OneTimePurchase && ((OneTimePurchase) o).getId() == this.getId();
	}
}
