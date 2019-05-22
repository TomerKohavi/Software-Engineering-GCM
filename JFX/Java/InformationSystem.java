import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class InformationSystem
{
    ArrayList<StatisticOfDate> StatisticOfDates;

    public InformationSystem()
    {
        this.StatisticOfDates=new ArrayList<StatisticOfDate>();
    }

    public InformationSystem(ArrayList<StatisticOfDate> statisticOfDates) {
        StatisticOfDates = statisticOfDates;
    }

    private StatisticOfDate findDate(Date d){
        for(StatisticOfDate sD: StatisticOfDates)
            if(sD.getDate().equals(d))
                return sD;
        StatisticOfDate sD=new StatisticOfDate(d);
        StatisticOfDates.add(sD);
        return sD;
    }

    private void sortStatisticOfDates() {
        Collections.sort(StatisticOfDates,Collections.reverseOrder());
    }

    public void addOneTimePurchase(int cityId) {
        addOneTimePurchase(cityId,new Date());
    }

    public void addOneTimePurchase(int cityId,Date d) {
        StatisticOfDate sD=findDate(d);
        sD.addOneTimePurchase(cityId);
    }

    public void addSubscription(int cityId) {
        addSubscription(cityId,new Date());
    }

    public void addSubscription(int cityId,Date d) {
        StatisticOfDate sD=findDate(d);
        sD.addSubscription(cityId);
    }

    public void addSubscriptionRenewal(int cityId) {
        addSubscriptionRenewal(cityId,new Date());
    }

    public void addSubscriptionRenewal(int cityId,Date d) {
        StatisticOfDate sD=findDate(d);
        sD.addOneTimePurchase(cityId);
    }

    public ArrayList<StatisticOfDate> getStatisticOfDates() {
        return StatisticOfDates;
    }

    public void setStatisticOfDates(ArrayList<StatisticOfDate> statisticOfDates) {
        StatisticOfDates = statisticOfDates;
    }
}
