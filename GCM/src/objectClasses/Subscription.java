package objectClasses;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Calendar;

import application.ReSubscribeController;
import controller.Database;
import otherClasses.ClassMustProperties;

import java.time.temporal.ChronoUnit;

import java.time.LocalDate;

@SuppressWarnings("serial")
public class Subscription extends CityPurchase implements ClassMustProperties, Serializable
{

	public static final int closeNumDays = 3;
	private LocalDate expirationDate;

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
	 * @throws SQLException if the access to database failed
	 */
	private Subscription(int id, int cityId, int userId, LocalDate purchaseDate, double fullPrice, double pricePayed,
			LocalDate expirationDate) throws SQLException
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
	private Subscription(int id, int cityId, int userId, LocalDate purchaseDate, double fullPrice, double pricePayed,
			LocalDate expirationDate, String cityName)
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
	 * @throws SQLException if the access to database failed
	 */
	public static Subscription _createSubscription(int id, int cityId, int userId, LocalDate purchaseDate, double fullPrice,
			double pricePayed, LocalDate expirationDate) throws SQLException
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
	public static Subscription _createLocalSubscription(int id, int cityId, int userId, LocalDate purchaseDate, double fullPrice,
			double pricePayed, LocalDate expirationDate, String cityName)
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
	 * @throws SQLException if the access to database failed
	 */
	public Subscription(Customer u, int cityId, LocalDate purchaseDate, double fullPrice, double pricePayed,
			LocalDate expirationDate) throws SQLException
	{
		super(u.getId(), cityId, purchaseDate, fullPrice, pricePayed);
		this.expirationDate = expirationDate;
		this.numMonths = calcNumMonths();
	}
	
	/**
	 * Resubscribe the subscription with the same contract, different price
	 * @param sub the subscription that is going to end
	 * @param fullPrice the full price
	 * @param payedPrice the price after discount (if there is any)
	 * @return if it is a re subscription 
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _Resubscribe(Subscription sub, double fullPrice, double payedPrice) throws SQLException {
		if(sub==null)
			return false;
		LocalDate today = LocalDate.now();
		LocalDate exDate=today.plusMonths(sub.getNumMonths());
		Subscription newSub= new Subscription(Database.generateIdCityPurchase(),sub.getCityId(),sub.getUserId(), today,fullPrice, payedPrice,exDate);
		newSub.saveToDatabase();
		LocalDate yesturday=today.minusDays(1);
		LocalDate purDate=yesturday.minusMonths(sub.getNumMonths());
		sub.setExpirationDate(yesturday);
		sub.setPurchaseDate(purDate);
		sub.saveToDatabase();
		return true;
	}

	public void saveToDatabase() throws SQLException
	{
		Database._saveSubscription(this);
	}

	public void deleteFromDatabase() throws SQLException
	{
		Database._deleteSubscription(this.getId());
	}

	/**
	 * Return if the subscription is going to expired by given date
	 * @param LocalDate the LocalDate we check the time from him 
	 * @return true if the subscription is going to expired
	 */
	public boolean isGoingToEnd(LocalDate date)
	{
		int diffDays=(int)ChronoUnit.DAYS.between(date,expirationDate);
		return 0<=diffDays && diffDays<= closeNumDays;
	}

	/**
	 * Return the subscription expiration date
	 * 
	 * @return the subscription expiration date
	 */
	public LocalDate getExpirationDate()
	{
		return expirationDate;
	}

	/**
	 * Sets the subscription expiration date
	 * 
	 * @param expirationDate the new subscription expiration date
	 */
	public void setExpirationDate(LocalDate expirationDate)
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
		int months = (this.expirationDate.getYear()- super.getPurchaseDate().getYear()) * 12
				+ this.expirationDate.getMonthValue() - super.getPurchaseDate().getMonthValue();
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
		LocalDate pd = super.getPurchaseDate();
		int newYear = pd.getYear() + _months / 12;
		int newMonth = pd.getMonthValue() + _months % 12;
		if (newMonth > 12)
		{
			newYear++;
			newMonth = newMonth % 12;
		}
		this.expirationDate = LocalDate.of(newYear, newMonth, pd.getDayOfMonth());
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Subscription && ((Subscription) o).getId() == this.getId();
	}
}