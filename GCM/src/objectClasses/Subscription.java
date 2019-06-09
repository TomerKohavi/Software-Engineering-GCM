package objectClasses;

import java.io.Serializable;
import java.sql.Time;

import controller.Database;
import otherClasses.ClassMustProperties;

import java.sql.Date;

public class Subscription extends CityPurchase implements ClassMustProperties, Serializable {

	@SuppressWarnings("deprecation")
	public static final Time closeTime = new Time(3 * 24, 0, 0);
	private Date expirationDate;

	private Subscription(int id, int cityId, int userId, Date purchaseDate, double fullPrice, double pricePayed,
			Date expirationDate) {
		super(id, cityId, userId, purchaseDate, fullPrice, pricePayed);
		this.expirationDate = expirationDate;
	}

	public static Subscription _createSubscription(int id, int cityId, int userId, Date purchaseDate, double fullPrice,
			double pricePayed, Date expirationDate) { // friend to Database
		return new Subscription(id, cityId, userId, purchaseDate, fullPrice, pricePayed, expirationDate);
	}

	public Subscription(Customer u, City c, Date purchaseDate, double fullPrice, double pricePayed, Date expirationDate) {
		super(u.getId(), c.getId(), purchaseDate, fullPrice, pricePayed);
		this.expirationDate = expirationDate;
	}

	public void saveToDatabase() {
		Database._saveSubscription(this);
	}

	public void deleteFromDatabase() {
		Database._deleteSubscription(this.getId());
	}

	public void reloadTempsFromDatabase() {
	}

	public boolean isGoingToEnd(java.util.Date date) {
		return expirationDate.getTime() - date.getTime() < closeTime.getTime();
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Subscription && ((Subscription) o).getId() == this.getId();
	}
}