public class oneTimePurchase extends CityPurchase
{
    boolean wasDownload;

    public oneTimePurchase(int cityPurchasedId, double fullPrice, double pricePayed) {
        super(cityPurchasedId, fullPrice, pricePayed);
        this.wasDownload=false;
    }

    public oneTimePurchase(int cityPurchasedId, double fullPrice, double pricePayed, boolean wasDownload) {
        super(cityPurchasedId, fullPrice, pricePayed);
        this.wasDownload = wasDownload;
    }

    public boolean isWasDownload() {
        return wasDownload;
    }

    public void setWasDownload(boolean wasDownload) {
        this.wasDownload = wasDownload;
    }
}
