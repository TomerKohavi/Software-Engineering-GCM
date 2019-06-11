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
	
	
	public int numMonths = -1; //temp_variable

	private Subscription(int id, int cityId, int userId, Date purchaseDate, double fullPrice, double pricePayed,
			Date expirationDate) {
		super(id, cityId, userId, purchaseDate, fullPrice, pricePayed);
		this.expirationDate = expirationDate;
		this.numMonths = calcNumMonths();
	}

	public static Subscription _createSubscription(int id, int cityId, int userId, Date purchaseDate, double fullPrice,
			double pricePayed, Date expirationDate) { // friend to Database
		return new Subscription(id, cityId, userId, purchaseDate, fullPrice, pricePayed, expirationDate);
	}

	public Subscription(Customer u, int cityId, Date purchaseDate, double fullPrice, double pricePayed, Date expirationDate) {
		super(u.getId(), cityId, purchaseDate, fullPrice, pricePayed);
		this.expirationDate = expirationDate;
		this.numMonths = calcNumMonths();
	}

	public void saveToDatabase() {
		Database._saveSubscription(this);
	}

	public void deleteFromDatabase() {
		Database._deleteSubscription(this.getId());
	}

	public boolean isGoingToEnd(Date date) {
		return expirationDate.getTime() - date.getTime() < closeTime.getTime();
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
		this.numMonths = calcNumMonths();
	}
	
	private int calcNumMonths() {
		 int months = (this.expirationDate.getYear()-super.getPurchaseDate().getYear())*12+this.expirationDate.getMonth()-super.getPurchaseDate().getMonth();
		 return months;
	}
	
	public int getNumMonths() {
		return this.numMonths;
	}

	public void setNumMonths(int _months) {
		 this.numMonths = _months;
		 Date pd=super.getPurchaseDate();
		 int newYear=pd.getYear()+_months/12;
		 int newMonth=pd.getMonth()+_months%12;
		 if(newMonth>12) {
			 newYear++;
			 newMonth=newMonth%12;
		 }
		 this.expirationDate=new Date(newYear,newMonth,pd.getDay());
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Subscription && ((Subscription) o).getId() == this.getId();
	}
}