package objectClasses;

import java.io.Serializable;
import java.sql.Time;

import controller.Database;
import otherClasses.ClassMustProperties;

import java.sql.Date;

@SuppressWarnings("serial")
public class Subscription extends CityPurchase implements ClassMustProperties, Serializable
{

	public static final Time closeTime = new Time(3 * 24, 0, 0);
	private Date expirationDate;

	public int numMonths = -1; // temp_variable

	/**
	 * This is a private constructor of subscription object
	 * 
	 * @param id the subscription id
	 * @param cityId the city id that contains the subscription 
	 * @param userId the user id that create the subscription
	 * @param purchaseDate the subscription purchase date
	 * @param fullPrice the full price of the subscription
	 * @param pricePayed how much is actually for the subscription
	 * @param expirationDate when does the subscription is expired
	 */
	private Subscription(int id, int cityId, int userId, Date purchaseDate, double fullPrice, double pricePayed,
			Date expirationDate)
	{
		super(id, cityId, userId, purchaseDate, fullPrice, pricePayed);
		this.expirationDate = expirationDate;
		this.numMonths = calcNumMonths();
	}
	
	/**
	 * This is a private constructor of subscription object
	 * 
	 * @param id the subscription id
	 * @param cityId the city id that contains the subscription 
	 * @param userId the user id that create the subscription
	 * @param purchaseDate the subscription purchase date
	 * @param fullPrice the full price of the subscription
	 * @param pricePayed how much is actually for the subscription
	 * @param expirationDate when does the subscription is expired
	 * @param cityName the city name
	 */
	private Subscription(int id, int cityId, int userId, Date purchaseDate, double fullPrice, double pricePayed,
			Date expirationDate, String cityName)
	{
		super(id, cityId, userId, purchaseDate, fullPrice, pricePayed, cityName);
		this.expirationDate = expirationDate;
		this.numMonths = calcNumMonths();
	}


	/**
	 * This function create subscription object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id the subscription id
	 * @param cityId the city id that contains the subscription 
	 * @param userId the user id that create the subscription
	 * @param purchaseDate the subscription purchase date
	 * @param fullPrice the full price of the subscription
	 * @param pricePayed how much is actually for the subscription
	 * @param expirationDate when does the subscription is expired
	 * @return new subscription object
	 */
	public static Subscription _createSubscription(int id, int cityId, int userId, Date purchaseDate, double fullPrice,
			double pricePayed, Date expirationDate)
	{ // friend to Database
		return new Subscription(id, cityId, userId, purchaseDate, fullPrice, pricePayed, expirationDate);
	}

	/**
	 * This function create subscription object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id the subscription id
	 * @param cityId the city id that contains the subscription 
	 * @param userId the user id that create the subscription
	 * @param purchaseDate the subscription purchase date
	 * @param fullPrice the full price of the subscription
	 * @param pricePayed how much is actually for the subscription
	 * @param expirationDate when does the subscription is expired
	 * @param cityName the city name
	 * @return new subscription object
	 */
	public static Subscription _createLocalSubscription(int id, int cityId, int userId, Date purchaseDate, double fullPrice,
			double pricePayed, Date expirationDate, String cityName)
	{ // friend to Database
		return new Subscription(id, cityId, userId, purchaseDate, fullPrice, pricePayed, expirationDate, cityName);
	}
	
	/**
	 * This is the normal public constructor for subscription object
	 * 
	 * @param u the customer object that buy the subscription
	 * @param cityId the city id of the subscription
	 * @param purchaseDate the subscription purchase date
	 * @param fullPrice the full price of the subscription
	 * @param pricePayed how much is actually for the subscription
	 * @param expirationDate when does the subscription is expired
	 */
	public Subscription(Customer u, int cityId, Date purchaseDate, double fullPrice, double pricePayed,
			Date expirationDate)
	{
		super(u.getId(), cityId, purchaseDate, fullPrice, pricePayed);
		this.expirationDate = expirationDate;
		this.numMonths = calcNumMonths();
	}

	public void saveToDatabase()
	{
		Database._saveSubscription(this);
	}

	public void deleteFromDatabase()
	{
		Database._deleteSubscription(this.getId());
	}

	/**
	 * Return if the subscription is going to expired by given date
	 * @param date the date we check the time from him 
	 * @return true if the subscription is going to expired
	 */
	public boolean isGoingToEnd(Date date)
	{
		return expirationDate.getTime() - date.getTime() < closeTime.getTime();
	}

	/**
	 * Return the subscription expiration date
	 * 
	 * @return the subscription expiration date
	 */
	public Date getExpirationDate()
	{
		return expirationDate;
	}

	/**
	 * Sets the subscription expiration date
	 * 
	 * @param expirationDate the new subscription expiration date
	 */
	public void setExpirationDate(Date expirationDate)
	{
		this.expirationDate = expirationDate;
		this.numMonths = calcNumMonths();
	}

	/**
	 * Calculate the number of months left until expired
	 * @return the number of months left until expired
	 */
	private int calcNumMonths()
	{
		int months = (this.expirationDate.getYear() - super.getPurchaseDate().getYear()) * 12
				+ this.expirationDate.getMonth() - super.getPurchaseDate().getMonth();
		return months;
	}

	/**
	 * Return the number of months left until expired
	 * 
	 * @return the number of months left until expired
	 */
	public int getNumMonths()
	{
		return this.numMonths;
	}

	/**
	 * Sets the number of months left until expired
	 * 
	 * @param _months the new number of months left until expired
	 */
	public void setNumMonths(int _months)
	{
		this.numMonths = _months;
		Date pd = super.getPurchaseDate();
		int newYear = pd.getYear() + _months / 12;
		int newMonth = pd.getMonth() + _months % 12;
		if (newMonth > 12)
		{
			newYear++;
			newMonth = newMonth % 12;
		}
		this.expirationDate = new Date(newYear, newMonth, pd.getDay());
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Subscription && ((Subscription) o).getId() == this.getId();
	}
}