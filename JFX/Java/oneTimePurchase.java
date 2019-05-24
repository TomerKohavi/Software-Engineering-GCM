import java.util.Date;

public class OneTimePurchase extends CityPurchase
{
    boolean wasDownload;

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
