import java.util.Date;

public class OneTimePurchase extends CityPurchase
{
    boolean wasDownload;

    private OneTimePurchase(int id, int cityId, int userId, Date purchaseDate, double fullPrice, double pricePayed, boolean wasDownload) {
        super(id, cityId, userId, purchaseDate, fullPrice, pricePayed);
        this.wasDownload = wasDownload;
    }

    public static OneTimePurchase _createOneTimePurchase(int id, int cityId, int userId, Date purchaseDate, double fullPrice, double pricePayed, boolean wasDownload){ //friend to Database
        return new OneTimePurchase(id,cityId,userId,purchaseDate, fullPrice,pricePayed,wasDownload);
    }

    public OneTimePurchase(int userId, int cityId, Date purchaseDate, double fullPrice, double pricePayed) {
        super(userId, cityId, purchaseDate, fullPrice, pricePayed);
        this.wasDownload = false;
    }

    public boolean getWasDownload() {
        return wasDownload;
    }

    public void updateToWasDownload() {
        this.wasDownload = true;
    }
}
