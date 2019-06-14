package objectClasses;

import java.sql.Date;

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
	private Date date;
	private int numOneTimePurchases;
	private int numSubscriptions;
	private int numSubscriptionsRenewal;
	private int numVisited;
	private int numSubDownloads;
	private boolean newVersionPublished;
	private int numMaps;

	private Statistic(int id, int cityId, Date date, int numOneTimePurchases, int numSubscriptions,
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

	public static Statistic _createStatistic(int id, int cityId, Date date, int numOneTimePurchases,
			int numSubscriptions, int numSubscriptionsRenewal, int numVisited, int numSubDownloads,
			boolean newVersionPublished, int numMaps) { // friend to Database
		return new Statistic(id, cityId, date, numOneTimePurchases, numSubscriptions, numSubscriptionsRenewal,
				numVisited, numSubDownloads, newVersionPublished, numMaps);
	}

	public Statistic(int cityId, Date date) {
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

	public static Statistic createBlankStatistic() {
		return new Statistic(-1, -1, null, 0, 0, 0, 0, 0, false, -1);
	}

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

	public int getId() {
		return id;
	}

	public void addOneTimePurchase() {
		this.numOneTimePurchases += 1;
	}

	public void addVisit() {
		this.numVisited += 1;
	}

	public void addSubscription() {
		this.numSubscriptions += 1;
	}

	public void addSubscriptionRenewal() {
		this.numSubscriptionsRenewal += 1;
	}

	public void addSubDownload() {
		this.numSubDownloads += 1;
	}

	public void newVersionWasPublished() {
		this.newVersionPublished = true;
	}

	public void setNumSubDownloads(int numSubDownloads) {
		this.numSubDownloads = numSubDownloads;
	}

	public int getNumSubDownload(int numSubDownloads) {
		return this.numSubDownloads;
	}

	public void setNumOneTimePurchases(int numOneTimePurchases) {
		this.numOneTimePurchases = numOneTimePurchases;
	}

	public void setNumSubscriptions(int numSubscriptions) {
		this.numSubscriptions = numSubscriptions;
	}

	public void setNumVisited(int numVisited) {
		this.numVisited = numVisited;
	}

	public void setNumSubscriptionsRenewal(int numSubscriptionsRenewal) {
		this.numSubscriptionsRenewal = numSubscriptionsRenewal;
	}

	public int getCityId() {
		return cityId;
	}

	public Date getDate() {
		return date;
	}

	public int getNumVisited() {
		return numVisited;
	}

	public int getNumOneTimePurchases() {
		return numOneTimePurchases;
	}

	public int getNumSubscriptions() {
		return numSubscriptions;
	}

	public int getNumSubscriptionsRenewal() {
		return numSubscriptionsRenewal;
	}

	public int getNumSubDownloads() {
		return numSubDownloads;
	}

	@Override
	public int compareTo(Statistic o) {
		return this.date.compareTo(o.date);
	}

	public boolean isNewVersionPublished() {
		return newVersionPublished;
	}

	public void setNumMaps(int numMaps) {
		this.numMaps = numMaps;
	}

	public int getNumMaps() {
		return this.numMaps;
	}

	public void setNewVersionPublished(boolean newVersionPublished) {
		this.newVersionPublished = newVersionPublished;
	}
}
