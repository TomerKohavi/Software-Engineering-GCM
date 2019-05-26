import java.sql.Time;
import java.util.Date;

public class Subscription extends CityPurchase {
    private static final Time closeTime=new Time(3*24,0,0);
    private Date expirationDate;

    private Subscription(int id, int cityId, int userId, Date purchaseDate, double fullPrice, double pricePayed, Date expirationDate) {
        super(id, cityId, userId, purchaseDate, fullPrice, pricePayed);
        this.expirationDate = expirationDate;
    }

    public static Subscription _createSubscription(int id, int cityId, int userId, Date purchaseDate, double fullPrice, double pricePayed, Date expirationDate){ //friend to Database
        return new Subscription(id,cityId,userId,purchaseDate,fullPrice,pricePayed,expirationDate);
    }

    public Subscription(int userId, int cityId, Date purchaseDate, double fullPrice, double pricePayed, Date expirationDate) {
        super(userId, cityId, purchaseDate, fullPrice, pricePayed);
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