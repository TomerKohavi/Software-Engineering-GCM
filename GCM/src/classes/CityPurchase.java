
package classes;
import java.io.Serializable;
import java.sql.Date;

public abstract class CityPurchase implements Comparable<CityPurchase>, Serializable {
	private int id;
	private int cityId;
	private int userId;
	private Date purchaseDate;
	private double fullPrice;
	private double pricePayed;

	protected CityPurchase(int id, int cityId, int userId, Date purchaseDate, double fullPrice, double pricePayed) {
		this.id = id;
		this.cityId = cityId;
		this.userId = userId;
		this.purchaseDate = purchaseDate;
		this.fullPrice = fullPrice;
		this.pricePayed = pricePayed;
	}

	public CityPurchase(int userId, int cityId, Date purchaseDate, double fullPrice, double pricePayed) {
		this.id = Database.generateIdCityPurchase();
		this.cityId = cityId;
		this.userId = userId;
		this.purchaseDate = purchaseDate;
		this.fullPrice = fullPrice;
		this.pricePayed = pricePayed;
	}

	public int getUserId() {
		return userId;
	}

	public int getId() {
		return id;
	}

	public int getCityId() {
		return cityId;
	}

	public double getFullPrice() {
		return fullPrice;
	}

	public double getPricePayed() {
		return pricePayed;
	}

	public void setCityPurchasedId(int cityPurchasedId) {
		this.cityId = cityPurchasedId;
	}

	public void setFullPrice(double fullPrice) {
		this.fullPrice = fullPrice;
	}

	public void setPricePayed(double pricePayed) {
		this.pricePayed = pricePayed;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	@Override
	public int compareTo(CityPurchase o) {
		return this.getPurchaseDate().compareTo(o.getPurchaseDate());
	}
}