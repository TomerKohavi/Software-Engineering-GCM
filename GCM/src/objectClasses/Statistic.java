package objectClasses;

import java.time.LocalDate;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * class of statistic object
 * which describe statistic information of a specific city in a specific date
 * @author Ron Cohen
 *
 */
@SuppressWarnings("serial")
public class Statistic implements Comparable<Statistic>, ClassMustProperties {
	private int id;
	private int cityId;
	private LocalDate date;
	private int numOneTimePurchases;
	private int numSubscriptions;
	private int numSubscriptionsRenewal;
	private int numVisited;
	private int numSubDownloads;
	private boolean newVersionPublished;
	private int numMaps;

	/**
	 * This is a private constructor of statistic object
	 * 
	 * @param id the statistic id
	 * @param cityId the city id of the statistic 
	 * @param date the date of the statistic
	 * @param numOneTimePurchases the number of one time purchases 
	 * @param numSubscriptions the number of the subscriptions
	 * @param numSubscriptionsRenewal the number of the Subscriptions that renewal 
	 * @param numVisited the number of visitors
	 * @param numSubDownloads the numbers of the subscriptions that was download
	 * @param newVersionPublished the newest version of published
	 * @param numMaps the number of maps
	 */
	private Statistic(int id, int cityId, LocalDate date, int numOneTimePurchases, int numSubscriptions,
			int numSubscriptionsRenewal, int numVisited, int numSubDownloads, boolean newVersionPublished,
			int numMaps) {
		this.id = id;
		this.cityId = cityId;
		this.date = date;
		this.numOneTimePurchases = numOneTimePurchases;
		this.numSubscriptions = numSubscriptions;
		this.numSubscriptionsRenewal = numSubscriptionsRenewal;
		this.numVisited = numVisited;
		this.numSubDownloads = numSubDownloads;
		this.newVersionPublished = newVersionPublished;
		this.numMaps = numMaps;
	}

	/**
	 * This function create statistic object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id the statistic id
	 * @param cityId the city id of the statistic 
	 * @param date the date of the statistic
	 * @param numOneTimePurchases the number of one time purchases 
	 * @param numSubscriptions the number of the subscriptions
	 * @param numSubscriptionsRenewal the number of the Subscriptions that renewal 
	 * @param numVisited the number of visitors
	 * @param numSubDownloads the numbers of the subscriptions that was download
	 * @param newVersionPublished the newest version of published
	 * @param numMaps the number of maps
	 * @return the new statistic object
	 */
	public static Statistic _createStatistic(int id, int cityId, LocalDate date, int numOneTimePurchases,
			int numSubscriptions, int numSubscriptionsRenewal, int numVisited, int numSubDownloads,
			boolean newVersionPublished, int numMaps) { // friend to Database
		return new Statistic(id, cityId, date, numOneTimePurchases, numSubscriptions, numSubscriptionsRenewal,
				numVisited, numSubDownloads, newVersionPublished, numMaps);
	}

	/**
	 * This is the normal public constructor for City object
	 * 
	 * @param cityId the city id 
	 * @param date the date of the statistic
	 */
	public Statistic(int cityId, LocalDate date) {
		this.id = Database.generateIdStatistic();
		this.cityId = cityId;
		this.date = date;
		this.numOneTimePurchases = 0;
		this.numSubscriptions = 0;
		this.numSubscriptionsRenewal = 0;
		this.numVisited = 0;
		this.numSubDownloads = 0;
		this.newVersionPublished = false;
		this.numMaps = -1;
	}

	/**
	 * Create an empty statistic object 
	 * @return empty statistic object 
	 */
	public static Statistic createBlankStatistic() {
		return new Statistic(-1, -1, null, 0, 0, 0, 0, 0, false, -1);
	}

	/**
	 * Add between two statistics and return a new statistic object 
	 * @param s1 first statistic object to add
	 * @param s2 second statistic object to add
	 * @return new statistic object 
	 */
	public static Statistic addStatistics(Statistic s1, Statistic s2) {
		Statistic s = createBlankStatistic();
		s.setNumOneTimePurchases(s1.numOneTimePurchases + s2.numOneTimePurchases);
		s.setNumSubscriptions(s1.numSubscriptions + s2.numSubscriptions);
		s.setNumSubscriptionsRenewal(s1.numSubscriptionsRenewal + s2.numSubscriptionsRenewal);
		s.setNumVisited(s1.numVisited + s2.numVisited);
		s.setNumSubDownloads(s1.numSubDownloads + s2.numSubDownloads);
		s.setNewVersionPublished(s1.newVersionPublished | s2.newVersionPublished);
		return s;
	}

	public void saveToDatabase() {
		if (this.id >= 0)
			Database._saveStatistic(this);
	}

	public void deleteFromDatabase() {
		if (this.id >= 0)
			Database._deleteStatistic(this.id);
	}

	public void reloadTempsFromDatabase() {
	}

	/**
	 * Returns the statistic id
	 * 
	 * @return the statistic id
	 */
	public int getId() {
		return id;
	}

	
	/**
	 * add one time purchase to the statistic 
	 */
	public void addOneTimePurchase() {
		this.numOneTimePurchases += 1;
	}

	/**
	 * add visitor to the statistic
	 */
	public void addVisit() {
		this.numVisited += 1;
	}

	/**
	 * add subscription to the statistic
	 */
	public void addSubscription() {
		this.numSubscriptions += 1;
	}

	/**
	 * add subscription renewal to the statistic
	 */
	public void addSubscriptionRenewal() {
		this.numSubscriptionsRenewal += 1;
	}

	/**
	 * add download subscription  to the statistic
	 */
	public void addSubDownload() {
		this.numSubDownloads += 1;
	}

	/**
	 * Set new version that was published to true
	 */
	public void newVersionWasPublished() {
		this.newVersionPublished = true;
	}

	/**
	 * Sets the number of download subscription
	 * 
	 * @param numSubDownloads the number of download subscription
	 */
	public void setNumSubDownloads(int numSubDownloads) {
		this.numSubDownloads = numSubDownloads;
	}

	/**
	 * Returns the number of download subscription
	 * 
	 * @param numSubDownloads number of subscription downloads
	 * @return the number of download subscription
	 */
	public int getNumSubDownload(int numSubDownloads) {
		return this.numSubDownloads;
	}

	/**
	 * Sets the number of one time purchases 
	 * 
	 * @param numOneTimePurchases the number of one time purchases
	 */
	public void setNumOneTimePurchases(int numOneTimePurchases) {
		this.numOneTimePurchases = numOneTimePurchases;
	}

	/**
	 * Sets the number of the subscriptions 
	 * 
	 * @param numSubscriptions the number of the subscriptions 
	 */
	public void setNumSubscriptions(int numSubscriptions) {
		this.numSubscriptions = numSubscriptions;
	}

	/**
	 * Sets the number of visitors
	 * 
	 * @param numVisited the number of visitors
	 */
	public void setNumVisited(int numVisited) {
		this.numVisited = numVisited;
	}

	/**
	 * Sets the number of subscription renewal
	 * 
	 * @param numSubscriptionsRenewal the number of subscription renewal
	 */
	public void setNumSubscriptionsRenewal(int numSubscriptionsRenewal) {
		this.numSubscriptionsRenewal = numSubscriptionsRenewal;
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
	 * Returns the statistic date
	 * 
	 * @return the statistic date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Returns the number of visitors 
	 * 
	 * @return the number of visitors 
	 */
	public int getNumVisited() {
		return numVisited;
	}

	/**
	 * Returns the number of one time purchases
	 * 
	 * @return the number of one time purchases
	 */
	public int getNumOneTimePurchases() {
		return numOneTimePurchases;
	}

	/**
	 * Returns the number of subscriptions
	 * 
	 * @return the number of subscriptions
	 */
	public int getNumSubscriptions() {
		return numSubscriptions;
	}

	/**
	 * Returns the number of subscriptions renewal
	 * 
	 * @return the number of subscriptions renewal
	 */
	public int getNumSubscriptionsRenewal() {
		return numSubscriptionsRenewal;
	}

	/**
	 * Returns the number of subscriptions that download
	 * 
	 * @return the number of subscriptions that download
	 */
	public int getNumSubDownloads() {
		return numSubDownloads;
	}

	@Override
	public int compareTo(Statistic o) {
		return this.date.compareTo(o.date);
	}

	/**
	 * Return if it is the newest published version
	 * @return if it is the newest published version
	 */
	public boolean isNewVersionPublished() {
		return newVersionPublished;
	}

	/**
	 * Sets the number of the maps
	 * 
	 * @param numMaps the number of the maps
	 */
	public void setNumMaps(int numMaps) {
		this.numMaps = numMaps;
	}

	/**
	 * Returns the number of the maps
	 * 
	 * @return the number of the maps
	 */
	public int getNumMaps() {
		return this.numMaps;
	}

	/**
	 * Sets the set new version published
	 * 
	 * @param newVersionPublished the set new version published
	 */
	public void setNewVersionPublished(boolean newVersionPublished) {
		this.newVersionPublished = newVersionPublished;
	}
}
