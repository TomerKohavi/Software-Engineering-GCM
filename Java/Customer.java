import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class Customer extends User{
    private String personalDetails;
    private ArrayList<CityPurchase> activeCityPurchase;
    private ArrayList<CityPurchase> unActiveCityPurchase;

    public Customer(String userName, String password, String email, String firstName, String lastName, String phoneNumber, String personalDetails) {
        super(userName, password, email, firstName, lastName, phoneNumber);
        this.personalDetails = personalDetails;
        this.activeCityPurchase=new ArrayList<CityPurchase>();
        this.unActiveCityPurchase=new ArrayList<CityPurchase>();
    }

    public Customer(String userName, String password, String email, String firstName, String lastName, String phoneNumber, String personalDetails, ArrayList<CityPurchase> activeCityPurchase, ArrayList<CityPurchase> unActiveCityPurchase) {
        super(userName, password, email, firstName, lastName, phoneNumber);
        this.personalDetails = personalDetails;
        this.activeCityPurchase = activeCityPurchase;
        this.unActiveCityPurchase = unActiveCityPurchase;
    }

    public CityPurchase findCityPurchaseByCityId(int cityId)
    {
        //TODO: check if it return the biggest day or smallest
        for(CityPurchase purchase : this.activeCityPurchase)
        {
            if(purchase.getCityPurchasedId()==cityId)
                return purchase;
        }
        for(CityPurchase purchase : this.unActiveCityPurchase)
        {
            if(purchase.getCityPurchasedId()==cityId)
                return purchase;
        }
        return null;
    }

    public void addActiveCityPurchase(CityPurchase purchase)
    {
        this.activeCityPurchase.add(purchase);
    }

    public void addUnActiveCityPurchase(CityPurchase purchase)
    {
        this.unActiveCityPurchase.add(purchase);
        sortCityPurchase(unActiveCityPurchase);
    }

    public CityPurchase getActivePurchaseByCityID(int cityId)
    {
        for(CityPurchase purchase : this.activeCityPurchase)
        {
            if(purchase.getCityPurchasedId()==cityId)
                return purchase;
        }
        return null;
    }

    public CityPurchase getUnActivePurchaseByCityID(int cityId)
    {
        for(CityPurchase purchase : this.unActiveCityPurchase)
        {
            if(purchase.getCityPurchasedId()==cityId)
                return purchase;
        }
        return null;
    }

    private void sortCityPurchase(ArrayList<CityPurchase> list)
    {
        Collections.sort(list,Collections.reverseOrder());
    }

    public boolean active2UnactiveByCityId(int cityId)
    {
        for(CityPurchase purchase : this.activeCityPurchase)
        {
            if(purchase.getCityPurchasedId()==cityId)
                this.unActiveCityPurchase.add(purchase);
            sortCityPurchase(unActiveCityPurchase);
            this.activeCityPurchase.remove(purchase);
            return true;
        }
        return false;
    }

    public ArrayList<Subscription> getSubscriptionGoingToEnd()
    {
        return getSubscriptionGoingToEnd(new Date());
    }

    public ArrayList<Subscription> getSubscriptionGoingToEnd(Date d)
    {
        ArrayList<Subscription> isGoingToEnd=new ArrayList<Subscription>();
        for(CityPurchase purchase : this.activeCityPurchase)
        {
            if(purchase instanceof Subscription && ((Subscription) purchase).isGoingToEnd(d))
            {
                isGoingToEnd.add((Subscription) purchase);
            }
        }
        return isGoingToEnd;
    }


}