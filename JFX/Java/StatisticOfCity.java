public class StatisticOfCity
{
    int cityId;
    int numOneTimePurchases;
    int numSubscriptions;
    int numSubscriptionsRenewal;

    public StatisticOfCity(int cityId) {
        this.cityId = cityId;
        this.numOneTimePurchases=0;
        this.numSubscriptions=0;
        this.numSubscriptionsRenewal=0;
    }

    public void addOneTimePurchase() {
        this.numOneTimePurchases+=1;
    }

    public void addSubscription() {
        this.numSubscriptions+=1;
    }

    public void addSubscriptionRenewal() {
        this.numSubscriptionsRenewal+=1;
    }

    public int getCityId() {
        return cityId;
    }

    public int getNumOneTimePurchases() {
        return numOneTimePurchases;
    }

    public int getNumSubscriptions() {
        return numSubscriptions;
    }

    public int getNumSubscriptionsRenewal() {
        return numSubscriptionsRenewal;
    }

    public void setNumOneTimePurchases(int numOneTimePurchases) {
        this.numOneTimePurchases = numOneTimePurchases;
    }

    public void setNumSubscriptions(int numSubscriptions) {
        this.numSubscriptions = numSubscriptions;
    }

    public void setNumSubscriptionsRenewal(int numSubscriptionsRenewal) {
        this.numSubscriptionsRenewal = numSubscriptionsRenewal;
    }
}
