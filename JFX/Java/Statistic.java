import jdk.dynalink.beans.StaticClass;

import java.util.Date;

public class Statistic implements Comparable<Statistic>
{
    int id;
    int cityId;
    Date date;
    int numOneTimePurchases;
    int numSubscriptions;
    int numSubscriptionsRenewal;

    public Statistic(int cityId, Date date) {
        this.id=Database.generateIdStatistic();
        this.cityId = cityId;
        this.date=date;
        this.numOneTimePurchases=0;
        this.numSubscriptions=0;
        this.numSubscriptionsRenewal=0;
    }

    public int getId() {
        return id;
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

    public void setNumOneTimePurchases(int numOneTimePurchases) {
        this.numOneTimePurchases = numOneTimePurchases;
    }

    public void setNumSubscriptions(int numSubscriptions) {
        this.numSubscriptions = numSubscriptions;
    }

    public void setNumSubscriptionsRenewal(int numSubscriptionsRenewal) {
        this.numSubscriptionsRenewal = numSubscriptionsRenewal;
    }

    public int getCityId() {
        return cityId;
    }

    public Date getDate() {
        return date;
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

    @Override
    public int compareTo(Statistic o) {
        return this.date.compareTo(o.date);
    }
}
