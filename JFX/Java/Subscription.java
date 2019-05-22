import java.sql.Time;
import java.util.Date;

public class Subscription extends CityPurchase {
    private static final Time closeTime=new Time(3*24,0,0);
    private Date expirationDate;

    public Subscription(int cityPurchasedId, double fullPrice, double pricePayed, Date expirationDate) {
        super(cityPurchasedId, fullPrice, pricePayed);
        this.expirationDate = expirationDate;
    }

    public boolean isGoingToEnd(Date currentDate)
    {
        return expirationDate.getTime()-currentDate.getTime()<closeTime.getTime();
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}