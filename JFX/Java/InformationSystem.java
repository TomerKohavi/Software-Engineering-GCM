import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class InformationSystem
{
    //list of Statistic
    public static Statistic getStatistic(int cityId, Date d){
        int[] ids=Database.searchStatistic(cityId,d);
        if(ids.length!=1)
            return null;
        Statistic s=Database.getStatisticById(ids[0]);
        if(s==null)
            return null;
        if(Database.getCityById(s.getCityId())==null)
        {
            Database.deleteStatistic(s.getId());
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
        Database.saveStatistic(s);
    }

    public void addSubscription(int cityId) {
        addSubscription(cityId,new Date());
    }

    public void addSubscription(int cityId,Date d) {
        Statistic s=getStatistic(cityId,d);
        if(s==null)
            s=new Statistic(cityId,d);
        s.addSubscription();
        Database.saveStatistic(s);
    }

    public void addSubscriptionRenewal(int cityId) {
        addSubscriptionRenewal(cityId,new Date());
    }

    public void addSubscriptionRenewal(int cityId,Date d) {
        Statistic s=getStatistic(cityId,d);
        if(s==null)
            s=new Statistic(cityId,d);
        s.addSubscriptionRenewal();
        Database.saveStatistic(s);
    }

    public ArrayList<Statistic> getAllStatistics() {
        int[] ids= Database.searchMapSight(-1,-1);
        ArrayList<Statistic> arrList=new ArrayList<Statistic>();
        for(int id : ids)
        {
            Statistic o=Database.getStatisticById(id);
            if(o==null)
                continue;
            if(Database.getCityById(o.getCityId())==null)
                Database.deleteStatistic(id);
            else
                arrList.add(o);
        }
        Collections.sort(arrList,Collections.reverseOrder());
        return arrList;
    }
}
