package classes;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;

public class Subscription extends CityPurchase implements ClassMustProperties, Serializable {

	@SuppressWarnings("deprecation")
	private static final Time closeTime = new Time(3 * 24, 0, 0);
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

	public Subscription(User u, City c, Date purchaseDate, double fullPrice, double pricePayed, Date expirationDate) {
		super(u.getId(), c.getId(), purchaseDate, fullPrice, pricePayed);
		this.expirationDate = expirationDate;
	}

	public Subscription resubscribe(double fullPrice,double pricePayed)
	{
		int id=Database.generateIdCityPurchase();
		Date expirationDate=new Date(this.expirationDate.getTime()+this.expirationDate.getTime()-this.getPurchaseDate().getTime());
		Subscription s=new Subscription(id,this.getCityId(),this.getUserId(),this.expirationDate,fullPrice,pricePayed,
				expirationDate);
		return s;
	}

	public void saveToDatabase() {
		Database._saveSubscription(this);
	}

	public void deleteFromDatabase() {
		Database._deleteSubscription(this.getId());
	}

	public void reloadTempsFromDatabase() {
	}

	public boolean isGoingToEnd(Date currentDate) {
		return expirationDate.getTime() - currentDate.getTime() < closeTime.getTime();
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