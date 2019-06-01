import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Customer extends User implements ClassMustProperties, Serializable {
    private String personalDetails;

    ArrayList<Subscription> temp_activeSubscription;
    ArrayList<Subscription> temp_unactiveSubscription;
    ArrayList<OneTimePurchase> temp_activeOneTimePurchase;
    ArrayList<OneTimePurchase> temp_unactiveOneTimePurchase;
    ArrayList<Subscription> temp_removeSubscription;
    ArrayList<OneTimePurchase> temp_removeOneTimePurchase;


    private Customer(int id, String userName, String password, String email, String firstName, String lastName, String phoneNumber, String personalDetails) {
        super(id, userName, password, email, firstName, lastName, phoneNumber);
        Date today=new Date();
        reloadTempsFromDatabase();
    }

    public static Customer _createCustomer(int id, String userName, String password, String email, String firstName, String lastName, String phoneNumber, String personalDetails){ //friend to Database
        return new Customer(id,userName, password, email, firstName,  lastName,  phoneNumber,  personalDetails);
    }

    public Customer(String userName, String password, String email, String firstName, String lastName, String phoneNumber, String personalDetails) {
        super(userName, password, email, firstName, lastName, phoneNumber);
        this.personalDetails = personalDetails;
        this.temp_activeSubscription=new ArrayList<>();
        this.temp_unactiveSubscription=new ArrayList<>();
        this.temp_activeOneTimePurchase=new ArrayList<>();
        this.temp_unactiveOneTimePurchase=new ArrayList<>();
        this.temp_removeSubscription=new ArrayList<>();
        this.temp_removeOneTimePurchase=new ArrayList<>();
    }

    public void reloadTempsFromDatabase() {
        Date today=new Date();
        this.temp_activeSubscription=generateActiveSubscriptions(today);
        this.temp_unactiveSubscription=generateUnactiveSubscriptions(today);
        this.temp_activeOneTimePurchase=generateActiveOneTimePurchases();
        this.temp_unactiveOneTimePurchase=generateUnactiveOneTimePurchases();
        this.personalDetails = personalDetails;
        this.temp_removeSubscription=new ArrayList<>();
        this.temp_removeOneTimePurchase=new ArrayList<>();
    }

    private ArrayList<Subscription> generateActiveSubscriptions(Date dateToCheck) {
        ArrayList<Integer> ids=Database.searchSubscription(super.getId(),null,null,dateToCheck,true);
        return generateListSubscriptions(ids);
    }

    private ArrayList<Subscription> generateUnactiveSubscriptions(Date dateToCheck) {
        ArrayList<Integer> ids=Database.searchSubscription(super.getId(),null,null,dateToCheck,false);
        return generateListSubscriptions(ids);
    }
    
    private ArrayList<Subscription> generateListSubscriptions(ArrayList<Integer> ids)
    {
        ArrayList<Subscription> arrList=new ArrayList<Subscription>();
        for(int id : ids)
        {
            Subscription o=Database._getSubscriptionById(id);
            if(o==null)
                continue;
            if(Database.getCityById(o.getCityId())==null)
                Database._deleteSubscription(id);
            else
                arrList.add(o);
        }
        return arrList;
    }

    private ArrayList<OneTimePurchase> generateActiveOneTimePurchases() {
        ArrayList<Integer> ids=Database.searchOneTimePurchase(super.getId(),null,null,false);
        return generateListOneTimePurchases(ids);
    }

    private ArrayList<OneTimePurchase> generateUnactiveOneTimePurchases() {
        ArrayList<Integer> ids=Database.searchOneTimePurchase(super.getId(),null,null,true);
        return generateListOneTimePurchases(ids);
    }

    private ArrayList<OneTimePurchase> generateListOneTimePurchases(ArrayList<Integer> ids)
    {
        ArrayList<OneTimePurchase> arrList=new ArrayList<OneTimePurchase>();
        for(int id : ids)
        {
            OneTimePurchase o=Database._getOneTimePurchaseById(id);
            if(o==null)
                continue;
            if(Database.getCityById(o.getCityId())==null)
                Database._deleteOneTimePurchase(id);
            else
                arrList.add(o);
        }
        return arrList;
    }

    public void saveToDatabase()
    {
        Database.saveCustomer(this);
        //delete removes
        for(Subscription s:temp_removeSubscription)
        {
            if(!temp_unactiveSubscription.contains(s) && !temp_activeSubscription.contains(s))
                s.deleteFromDatabase();
        }
        this.temp_removeSubscription=new ArrayList<>();
        for(OneTimePurchase otp:temp_removeOneTimePurchase)
        {
            if(!temp_activeOneTimePurchase.contains(otp) && !temp_unactiveOneTimePurchase.contains(otp))
                otp.deleteFromDatabase();
        }
        this.temp_removeOneTimePurchase=new ArrayList<>();
        //saves lists
        for(Subscription s:temp_activeSubscription)
            s.saveToDatabase();
        for(Subscription s:temp_unactiveSubscription)
            s.saveToDatabase();
        for(OneTimePurchase otp:temp_activeOneTimePurchase)
            otp.saveToDatabase();
        for(OneTimePurchase otp:temp_unactiveOneTimePurchase)
            otp.saveToDatabase();
    }

    public void deleteFromDatabase()
    {
        Database.deleteCustomer(super.getId());
        for(Subscription s:temp_removeSubscription)
            s.deleteFromDatabase();
        this.temp_removeSubscription=new ArrayList<>();
        for(OneTimePurchase otp:temp_removeOneTimePurchase)
            otp.deleteFromDatabase();
        this.temp_removeOneTimePurchase=new ArrayList<>();
        for(Subscription s:temp_activeSubscription)
            s.deleteFromDatabase();
        for(Subscription s:temp_unactiveSubscription)
            s.deleteFromDatabase();
        for(OneTimePurchase otp:temp_activeOneTimePurchase)
            otp.deleteFromDatabase();
        for(OneTimePurchase otp:temp_unactiveOneTimePurchase)
            otp.deleteFromDatabase();
    }

    public boolean addSubscription(Subscription sub)
    {
        if(sub.getUserId()!=this.getId())
            return false;
        Date today=new Date();
        if(sub.getPurchaseDate().compareTo(today)>0)  //TODO: not sure if <0 or >0
            temp_activeSubscription.add(sub);
        else
            temp_unactiveSubscription.add(sub);
        return true;
    }

    public boolean addOneTimePurchase(OneTimePurchase otp)
    {
        if(otp.getUserId()!=this.getId())
            return false;
        Date today=new Date();
        if(!otp.wasDownload)
            temp_activeOneTimePurchase.add(otp);
        else
            temp_unactiveOneTimePurchase.add(otp);
        return true;
    }

    public ArrayList<Subscription> getGoingToEnd()
    {
        ArrayList<Subscription> result=new ArrayList<>();
        Date today=new Date();
        for(Subscription s:temp_activeSubscription)
            if(s.isGoingToEnd(today))
                result.add(s);
        return result;
    }

    public boolean canViewCityWithSubscription(int cityId)
    {
        for(Subscription s:temp_activeSubscription)
            if(s.getCityId()==cityId)
                return true;
        return false;
    }

    public OneTimePurchase getActiveOneTimePurchaseByCity(int cityId)
    {
        for(OneTimePurchase otp:temp_activeOneTimePurchase)
            if(otp.getCityId()==cityId)
                return otp;
        return null;
    }

    public Subscription removeSubscription(int subscriptionId)
    {
        for(int i=0;i<temp_activeSubscription.size();i++) {
            if (temp_activeSubscription.get(i).getId() == subscriptionId)
            {
                Subscription s=temp_activeSubscription.remove(i);
                temp_removeSubscription.add(s);
                return s;
            }
        }
        for(int i=0;i<temp_unactiveSubscription.size();i++) {
            if (temp_unactiveSubscription.get(i).getId() == subscriptionId)
            {
                Subscription s=temp_unactiveSubscription.remove(i);
                temp_removeSubscription.add(s);
                return s;
            }
        }
        return null;
    }

    public Subscription getSubscription(int subscriptionId)
    {
        for(Subscription s:temp_activeSubscription)
            if(s.getId()==subscriptionId)
                return s;
        for(Subscription s:temp_unactiveSubscription)
            if(s.getId()==subscriptionId)
                return s;
        return null;
    }

    public OneTimePurchase getOneTimePurchase(int subscriptionId)
    {
        for(OneTimePurchase otp:temp_activeOneTimePurchase)
            if(otp.getId()==subscriptionId)
                return otp;
        for(OneTimePurchase otp:temp_unactiveOneTimePurchase)
            if(otp.getId()==subscriptionId)
                return otp;
        return null;
    }

    public OneTimePurchase removeOneTimePurchase(int otpId)
    {
        for(int i=0;i<temp_activeOneTimePurchase.size();i++) {
            if (temp_activeOneTimePurchase.get(i).getId() == otpId)
            {
                OneTimePurchase otp= temp_activeOneTimePurchase.remove(i);
                temp_removeOneTimePurchase.add(otp);
                return otp;
            }
        }
        for(int i=0;i<temp_unactiveOneTimePurchase.size();i++) {
            if (temp_unactiveOneTimePurchase.get(i).getId() == otpId)
            {
                OneTimePurchase otp= temp_unactiveOneTimePurchase.remove(i);
                temp_removeOneTimePurchase.add(otp);
                return otp;
            }
        }
        return null;
    }

    public ArrayList<Subscription> getCopyActiveSubscription() {
        return new ArrayList<>(temp_activeSubscription);
    }

    public ArrayList<Subscription> getCopyUnactiveSubscription() {
        return new ArrayList<>(temp_unactiveSubscription);
    }

    public ArrayList<OneTimePurchase> getCopyActiveOneTimePurchase() {
        return new ArrayList<>(temp_activeOneTimePurchase);
    }

    public ArrayList<OneTimePurchase> getCopyUnactiveOneTimePurchase() {
        return new ArrayList<>(temp_unactiveOneTimePurchase);
    }

    public String getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(String personalDetails) {
        this.personalDetails = personalDetails;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Customer && ((Customer) o).getId()==this.getId();
    }
}