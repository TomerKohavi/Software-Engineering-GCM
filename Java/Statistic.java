import java.io.Serializable;
import java.util.Date;

public class Statistic implements Comparable<Statistic>,ClassMustProperties
{
    int id;
    int cityId;
    Date date;
    int numOneTimePurchases;
    int numSubscriptions;
    int numSubscriptionsRenewal;
    int numVisited;

    private Statistic(int id, int cityId, Date date, int numOneTimePurchases, int numSubscriptions, int numSubscriptionsRenewal,int numVisited) {
        this.id = id;
        this.cityId = cityId;
        this.date = date;
        this.numOneTimePurchases = numOneTimePurchases;
        this.numSubscriptions = numSubscriptions;
        this.numSubscriptionsRenewal = numSubscriptionsRenewal;
        this.numVisited=numVisited;
    }

    public static Statistic _createStatistic(int id, int cityId, Date date, int numOneTimePurchases, int numSubscriptions, int numSubscriptionsRenewal,int numVisited){ //friend to Database
        return new Statistic( id,  cityId,  date,  numOneTimePurchases,  numSubscriptions,  numSubscriptionsRenewal,numVisited);
    }

    public Statistic(int cityId, Date date) {
        this.id=Database.generateIdStatistic();
        this.cityId = cityId;
        this.date=date;
        this.numOneTimePurchases=0;
        this.numSubscriptions=0;
        this.numSubscriptionsRenewal=0;
        this.numVisited=0;
    }

    static Statistic createBlankStatistic()
    {
        return new Statistic(-1,-1,null,0,0,0,0);
    }

    static Statistic addStatistics(Statistic s1,Statistic s2)
    {
        Statistic s=createBlankStatistic();
        s.setNumOneTimePurchases(s1.numOneTimePurchases+s2.numOneTimePurchases);
        s.setNumSubscriptions(s1.numSubscriptions+s2.numSubscriptions);
        s.setNumSubscriptionsRenewal(s1.numSubscriptionsRenewal+s2.numSubscriptionsRenewal);
        s.setNumVisited(s1.numVisited+s2.numVisited);
        return s;
    }

    public void saveToDatabase() {
        if(this.id>=0)
            Database._saveStatistic(this);
    }

    public void deleteFromDatabase() {
        if(this.id>=0)
            Database._deleteStatistic(this.id);
    }

    public void reloadTempsFromDatabase() {}

    public int getId() {
        return id;
    }

    public void addOneTimePurchase() {
        this.numOneTimePurchases+=1;
    }

    public void addVisit() {
        this.numVisited+=1;
    }

    public void addSubscription() {
        this.numSubscriptions += 1;
    }

    public void addSubscriptionRenewal() {
        this.numSubscriptionsRenewal += 1;
    }

    public void setNumOneTimePurchases(int numOneTimePurchases) {
        this.numOneTimePurchases = numOneTimePurchases;
    }

    public void setNumSubscriptions(int numSubscriptions) {
        this.numSubscriptions = numSubscriptions;
    }

    public void setNumVisited(int numVisited) {
        this.numVisited = numVisited;
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

    public int getNumVisited() {
        return numVisited;
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
