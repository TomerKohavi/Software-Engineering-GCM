import java.util.Date;
public abstract class CityPurchase implements Comparable<CityPurchase>{
    private int  id;
    private int cityPurchasedId;
    private Date purchaseDate;
    private double fullPrice;
    private double pricePayed;

    public CityPurchase(int cityPurchasedId, double fullPrice, double pricePayed) {
        this.id=Database.generateIdCityPurchase();
        this.cityPurchasedId = cityPurchasedId;
        this.fullPrice = fullPrice;
        this.pricePayed = pricePayed;
        this.purchaseDate=new Date();
    }

    public int getId() {
        return id;
    }

    public int getCityPurchasedId() {
        return cityPurchasedId;
    }

    public double getFullPrice() {
        return fullPrice;
    }

    public double getPricePayed() {
        return pricePayed;
    }

    public void setCityPurchasedId(int cityPurchasedId) {
        this.cityPurchasedId = cityPurchasedId;
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