import java.util.ArrayList;
import java.util.Date;

public class Customer extends User{
    private String personalDetails;
    // list of subscription
    //list of one time purchase


    public Customer(int id, String userName, String password, String email, String firstName, String lastName, String phoneNumber, String personalDetails) {
        super(id, userName, password, email, firstName, lastName, phoneNumber);
        this.personalDetails = personalDetails;
    }

    public static Customer _createCustomer(int id, String userName, String password, String email, String firstName, String lastName, String phoneNumber, String personalDetails){ //friend to Database
        return new Customer(id,userName, password, email, firstName,  lastName,  phoneNumber,  personalDetails);
    }

    public Customer(String userName, String password, String email, String firstName, String lastName, String phoneNumber, String personalDetails) {
        super(userName, password, email, firstName, lastName, phoneNumber);
        this.personalDetails = personalDetails;
    }

    public Subscription getActiveSubscribeToCity(int cityId,Date dateToCheck)
    {
        int[] ids=Database.searchSubscription(super.getId(),cityId,null,dateToCheck);
        if(ids.length>1)
            for (int id: ids)
            {
                Subscription s=Database._getSubscriptionById(id);
                if(s!=null)
                    return s;
            }
        return null;
    }
    
    public boolean isActiveSubscribeToCity(int cityId,Date dateToCheck)
    {
        return getActiveSubscribeToCity(cityId,dateToCheck)!=null;
    }
    
    private ArrayList<Subscription> generateListSubscriptions(int[] ids)
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

    public ArrayList<Subscription> getAllSubscriptions() {
        int[] ids = Database.searchSubscription(super.getId(), null, null, null);
        return generateListSubscriptions(ids);
    }
        

    public int getNumSubscriptions(){
        return getAllSubscriptions().size();
    }

    public ArrayList<Subscription> getAllActiveSubscriptions(Date dateToCheck) {
        int[] ids=Database.searchSubscription(super.getId(),null,null,dateToCheck);
        return generateListSubscriptions(ids);
    }

    public int getNumActiveSubscriptions(Date dateToCheck){
        return getAllActiveSubscriptions(dateToCheck).size();
    }

    public Subscription getSubscriptionById(int subId)
    {
        Subscription sub=Database._getSubscriptionById(subId);
        if(sub==null || sub.getUserId()!=super.getId())
            return null;
        if(Database.getCityById(sub.getCityId())==null)
        {
            Database._deleteSubscription(sub.getId());
            return null;
        }
        return sub;
    }

    public Subscription addSubscription(int cityId, Date purchaseDate, double fullPrice, double pricePayed, Date expirationDate)
    {
        if(Database.getCityById(cityId)==null)
            return null;
        Subscription sub=new Subscription(super.getId(),cityId,purchaseDate,fullPrice,pricePayed,expirationDate);
        Database._saveSubscription(sub);
        return sub;
    }

    public Subscription removeSubscriptionById(int subId)
    {
        Subscription sub=getSubscriptionById(subId);
        if(sub==null || sub.getUserId()!=super.getId())
            return null;
        Database._deleteSubscription(sub.getId());
        return sub;
    }


    public OneTimePurchase getOneTimePurchaseToCity(int cityId,boolean wasDownload)
    {
        int[] ids=Database.searchOneTimePurchase(super.getId(),cityId,null,wasDownload);
        if(ids.length>1)
            for (int id: ids)
            {
                OneTimePurchase s=Database._getOneTimePurchaseById(id);
                if(s!=null)
                    return s;
            }
        return null;
    }


    public OneTimePurchase getActiveOneTimePurchaseToCity(int cityId)
    {
        return getOneTimePurchaseToCity(cityId,false);
    }

    public boolean isActiveOneTimePurchaseToCity(int cityId)
    {
        return getActiveOneTimePurchaseToCity(cityId)!=null;
    }

    private ArrayList<OneTimePurchase> generateListOneTimePurchases(int[] ids)
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

    public ArrayList<OneTimePurchase> getAllOneTimePurchases() {
        int[] ids = Database.searchOneTimePurchase(super.getId(), null, null, null);
        return generateListOneTimePurchases(ids);
    }


    public int getNumOneTimePurchases(){
        return getAllOneTimePurchases().size();
    }

    public ArrayList<OneTimePurchase> getAllActiveOneTimePurchases() {
        int[] ids=Database.searchOneTimePurchase(super.getId(),null,null,false);
        return generateListOneTimePurchases(ids);
    }

    public int getNumActiveOneTimePurchases(){
        return getAllActiveOneTimePurchases().size();
    }

    public OneTimePurchase getOneTimePurchaseById(int subId)
    {
        OneTimePurchase otp=Database._getOneTimePurchaseById(subId);
        if(otp==null || otp.getUserId()!=super.getId())
            return null;
        if(Database.getCityById(otp.getCityId())==null)
        {
            Database._deleteOneTimePurchase(otp.getId());
            return null;
        }
        return otp;
    }

    public OneTimePurchase addOneTimePurchase(int cityId, Date purchaseDate, double fullPrice, double pricePayed)
    {
        if(Database.getCityById(cityId)==null)
            return null;
        OneTimePurchase otp=new OneTimePurchase(super.getId(),cityId,purchaseDate,fullPrice,pricePayed);
        Database._saveOneTimePurchase(otp);
        return otp;
    }

    public OneTimePurchase removeOneTimePurchaseById(int otpId)
    {
        OneTimePurchase otp=getOneTimePurchaseById(otpId);
        if(otp==null || otp.getUserId()!=super.getId())
            return null;
        Database._deleteOneTimePurchase(otp.getId());
        return otp;
    }

    public String getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(String personalDetails) {
        this.personalDetails = personalDetails;
    }
}