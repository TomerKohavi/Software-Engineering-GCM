import java.util.ArrayList;
import java.util.Date;

public class StatisticOfDate implements Comparable<StatisticOfDate>
{
    Date date;
    ArrayList<StatisticOfCity> statisticOfCities;

    public StatisticOfDate()
    {
        this.date=new Date();
        this.statisticOfCities= new ArrayList<StatisticOfCity>();
    }

    public StatisticOfDate(Date date)
    {
        this.date=date;
        this.statisticOfCities= new ArrayList<StatisticOfCity>();
    }

    private StatisticOfCity findCity(int cityId){
        for(StatisticOfCity c: statisticOfCities)
            if(c.getCityId()==cityId)
                return c;
        StatisticOfCity c=new StatisticOfCity(cityId);
        statisticOfCities.add(c);
        return c;
    }

    public void addOneTimePurchase(int cityId) {
        StatisticOfCity c=findCity(cityId);
        c.addOneTimePurchase();
    }

    public void addSubscription(int cityId) {
        StatisticOfCity c=findCity(cityId);
        c.addSubscription();
    }

    public void addSubscriptionRenewal(int cityId) {
        StatisticOfCity c=findCity(cityId);
        c.addSubscriptionRenewal();
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<StatisticOfCity> getStatisticOfCities() {
        return statisticOfCities;
    }

    public void setStatisticOfCities(ArrayList<StatisticOfCity> statisticOfCities) {
        this.statisticOfCities = statisticOfCities;
    }

    @Override
    public int compareTo(StatisticOfDate o) {
		return this.getDate().compareTo(o.getDate());
    }
}
