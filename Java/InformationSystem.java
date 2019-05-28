import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public final class InformationSystem
{
    private InformationSystem(){}

    //list of Statistic
    public static Statistic getStatistic(int cityId, Date d){
        ArrayList<Integer> ids=Database.searchStatistic(cityId,d);
        if(ids.size()!=1)
            return null;
        Statistic s=Database._getStatisticById(ids.get(0));
        if(s==null)
            return null;
        if(Database.getCityById(s.getCityId())==null)
        {
            Database._deleteStatistic(s.getId());
            return null;
        }
        return s;
    }

    public void addOneTimePurchase(int cityId) {
        addOneTimePurchase(cityId,new Date());
    }

    public void addOneTimePurchase(int cityId,Date d) {
        Statistic s=getStatistic(cityId,d);
        if(s==null)
            s=new Statistic(cityId,d);
        s.addOneTimePurchase();
        Database._saveStatistic(s);
    }

    public void addSubscription(int cityId) {
        addSubscription(cityId,new Date());
    }

    public void addSubscription(int cityId,Date d) {
        Statistic s=getStatistic(cityId,d);
        if(s==null)
            s=new Statistic(cityId,d);
        s.addSubscription();
        Database._saveStatistic(s);
    }

    public void addSubscriptionRenewal(int cityId) {
        addSubscriptionRenewal(cityId,new Date());
    }

    public void addSubscriptionRenewal(int cityId,Date d) {
        Statistic s=getStatistic(cityId,d);
        if(s==null)
            s=new Statistic(cityId,d);
        s.addSubscriptionRenewal();
        Database._saveStatistic(s);
    }

    public ArrayList<Statistic> getAllStatistics() {
        ArrayList<Integer> ids= Database.searchMapSight(null,null);
        ArrayList<Statistic> arrList=new ArrayList<Statistic>();
        for(int id : ids)
        {
            Statistic o=Database._getStatisticById(id);
            if(o==null)
                continue;
            if(Database.getCityById(o.getCityId())==null)
                Database._deleteStatistic(id);
            else
                arrList.add(o);
        }
        Collections.sort(arrList,Collections.reverseOrder());
        return arrList;
    }
}
