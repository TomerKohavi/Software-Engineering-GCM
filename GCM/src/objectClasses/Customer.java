package objectClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Calendar;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * Class of customer properties
 * @author Ron Cohen
 */
@SuppressWarnings("serial")
public class Customer extends User implements ClassMustProperties, Serializable {
	
	private String creditCardNum;
	private String creditCardExpires;
	private String cvc;

	ArrayList<Subscription> temp_activeSubscription;
	ArrayList<Subscription> temp_unactiveSubscription;
	ArrayList<OneTimePurchase> temp_oneTimePurchase;
	ArrayList<Subscription> temp_removeSubscription;
	ArrayList<OneTimePurchase> temp_removeOneTimePurchase;

	/**
	 * This is a private constructor of customer object
	 * 
	 * @param id the customer id
	 * @param userName the customer user name
	 * @param password the customer password
	 * @param email the customer email
	 * @param firstName the customer first name
	 * @param lastName the customer last name
	 * @param phoneNumber the customer phone number
	 * @param creditCardNum the customer credit card number
	 * @param creditCardExpires the customer credit card expires time
	 * @param cvc the customer credit card cvc
	 */
	private Customer(int id, String userName, String password, String email, String firstName, String lastName,
			String phoneNumber,String creditCardNum,String creditCardExpires,String cvc) {
		super(id, userName, password, email, firstName, lastName, phoneNumber);
		this.creditCardNum=creditCardNum;
		this.creditCardExpires=creditCardExpires;
		this.cvc=cvc;
		reloadTempsFromDatabase();
	}

	/**
	 * This function create customer object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id the customer id
	 * @param userName the customer user name
	 * @param password the customer password
	 * @param email the customer email
	 * @param firstName the customer first name
	 * @param lastName the customer last name
	 * @param phoneNumber the customer phone number
	 * @param creditCardNum the customer credit card number
	 * @param creditCardExpires the customer credit card expires time
	 * @param cvc the customer credit card cvc
	 * @return the new customer object
	 */
	public static Customer _createCustomer(int id, String userName, String password, String email, String firstName,
			String lastName, String phoneNumber,String creditCardNum,String creditCardExpires,String cvc) { // friend to Database
		return new Customer(id, userName, password, email, firstName, lastName, phoneNumber,creditCardNum,creditCardExpires,cvc);
	}

	/**
	 * This is the normal public constructor for City object
	 * 
	 * @param userName the customer user name
	 * @param password the customer password
	 * @param email the customer email
	 * @param firstName the customer first name
	 * @param lastName the customer last name
	 * @param phoneNumber the customer phone number
	 * @param creditCardNum the customer credit card number
	 * @param creditCardExpires the customer credit card expires time
	 * @param cvc the customer credit card cvc
	 */
	public Customer(String userName, String password, String email, String firstName, String lastName,
			String phoneNumber,String creditCardNum,String creditCardExpires,String cvc) {
		super(userName, password, email, firstName, lastName, phoneNumber);
		this.creditCardNum=creditCardNum;
		this.creditCardExpires=creditCardExpires;
		this.cvc=cvc;
		this.temp_activeSubscription = new ArrayList<>();
		this.temp_unactiveSubscription = new ArrayList<>();
		this.temp_oneTimePurchase = new ArrayList<>();
		this.temp_removeSubscription = new ArrayList<>();
		this.temp_removeOneTimePurchase = new ArrayList<>();
	}

	public void reloadTempsFromDatabase() {
		LocalDate today = LocalDate.now();
		this.temp_activeSubscription = generateActiveSubscriptions(today);
		this.temp_unactiveSubscription = generateUnactiveSubscriptions(today);
		this.temp_oneTimePurchase = generateOneTimePurchases();
		this.temp_removeSubscription = new ArrayList<>();
		this.temp_removeOneTimePurchase = new ArrayList<>();
	}

	/**
	 * generate active subscriptions from given date
	 * @param dateToCheck from which date to generate the active subscriptions
	 * @return list of active subscriptions 
	 */
	private ArrayList<Subscription> generateActiveSubscriptions(LocalDate dateToCheck) {
		ArrayList<Integer> ids = Database.searchSubscription(super.getId(), null, dateToCheck, true);
		return generateListSubscriptions(ids);
	}

	/**
	 * generate unactive subscriptions from given date
	 * @param dateToCheck from which date to generate the unactive subscriptions
	 * @return list of unactive subscriptions 
	 */
	private ArrayList<Subscription> generateUnactiveSubscriptions(LocalDate dateToCheck) {
		ArrayList<Integer> ids = Database.searchSubscription(super.getId(), null, dateToCheck, false);
		return generateListSubscriptions(ids);
	}

	/**
	 * generate list of subscriptions from given list of ids
	 * @param ids ids of the subscriptions we want to get
	 * @return list of subscriptions objects
	 */
	private ArrayList<Subscription> generateListSubscriptions(ArrayList<Integer> ids) {
		ArrayList<Subscription> arrList = new ArrayList<Subscription>();
		for (int id : ids) {
			Subscription o = Database._getSubscriptionById(id);
			if (o == null)
				continue;
			arrList.add(o);
		}
		return arrList;
	}

	/**
	 * Returns the customer one time purchase list
	 * @return list of one time purchase object of the customer
	 */
	private ArrayList<OneTimePurchase> generateOneTimePurchases() {
		ArrayList<Integer> ids = Database.searchOneTimePurchase(super.getId(), null, null, null);
		return generateListOneTimePurchases(ids);
	}

	/**
	 * generate list of one time purchase from given list of ids
	 * @param ids ids of the one time purchase we want to get
	 * @return list of one time purchase objects
	 */
	private ArrayList<OneTimePurchase> generateListOneTimePurchases(ArrayList<Integer> ids) {
		ArrayList<OneTimePurchase> arrList = new ArrayList<OneTimePurchase>();
		for (int id : ids) {
			OneTimePurchase o = Database._getOneTimePurchaseById(id);
			if (o == null)
				continue;
			arrList.add(o);
		}
		return arrList;
	}

	public void saveToDatabase() {
		Database.saveCustomer(this);
		// delete removes
		for (Subscription s : temp_removeSubscription) {
			if (!temp_unactiveSubscription.contains(s) && !temp_activeSubscription.contains(s))
				s.deleteFromDatabase();
		}
		this.temp_removeSubscription = new ArrayList<>();
		for (OneTimePurchase otp : temp_removeOneTimePurchase) {
			if (!temp_oneTimePurchase.contains(otp))
				otp.deleteFromDatabase();
		}
		this.temp_removeOneTimePurchase = new ArrayList<>();
		// saves lists
		for (Subscription s : temp_activeSubscription)
			s.saveToDatabase();
		for (Subscription s : temp_unactiveSubscription)
			s.saveToDatabase();
		for (OneTimePurchase otp : temp_oneTimePurchase)
			otp.saveToDatabase();
	}

	public void deleteFromDatabase() {
		Database.deleteCustomer(super.getId());
		for (Subscription s : temp_removeSubscription)
			s.deleteFromDatabase();
		this.temp_removeSubscription = new ArrayList<>();
		for (OneTimePurchase otp : temp_removeOneTimePurchase)
			otp.deleteFromDatabase();
		this.temp_removeOneTimePurchase = new ArrayList<>();
		for (Subscription s : temp_activeSubscription)
			s.deleteFromDatabase();
		for (Subscription s : temp_unactiveSubscription)
			s.deleteFromDatabase();
		for (OneTimePurchase otp : temp_oneTimePurchase)
			otp.deleteFromDatabase();
	}

	/**
	 * @param sub subscription object to add to the customer
	 * @return if the subscription is added successfully
	 */
	public boolean addSubscription(Subscription sub) {
		if (sub.getUserId() != this.getId())
			return false;
		LocalDate today =LocalDate.now();
		if (sub.getPurchaseDate().compareTo(today) >= 0)
			temp_activeSubscription.add(sub);
		else
			temp_unactiveSubscription.add(sub);
		return true;
	}

	/**
	 * @param otp one time purchase object to add to the customer
	 * @return if the one time purchase is added successfully
	 */
	public boolean addOneTimePurchase(OneTimePurchase otp) {
		if (otp.getUserId() != this.getId())
			return false;
		temp_oneTimePurchase.add(otp);
		return true;
	}

	/**
	 * Returns list of subscription that is going to be expired
	 * @return list of subscription that is going to be expired
	 */
	public ArrayList<Subscription> getGoingToEnd() {
		ArrayList<Subscription> result = new ArrayList<>();
		LocalDate today = LocalDate.now();
		for (Subscription s : temp_activeSubscription)
			if (s.isGoingToEnd(today))
				result.add(s);
		return result;
	}

	/** 
	 * Returns boolean if the customer has active subscription today to the city
	 * @param cityId the city id
	 * @return if the customer allowed to view the city
	 */
	public boolean canViewCityWithSubscription(int cityId) {
		for (Subscription s : temp_activeSubscription)
			if (s.getCityId() == cityId)
				return true;
		return false;
	}

	/**
	 * get the active one time purchase in the city
	 * @param cityId the city id
	 * @return one time purchase object that active in the city
	 */
	public OneTimePurchase getActiveOneTimePurchaseByCity(int cityId) {
		for (OneTimePurchase otp : temp_oneTimePurchase)
			if (otp.getCityId() == cityId && !otp.getWasDownload())
				return otp;
		return null;
	}
	
	/**
	 * get the active subscriptions of the city
	 * @param cityId the city id
	 * @return list of subscriptions
	 */
	public ArrayList<Subscription> getActiveSubscriptionsByCity(int cityId) {
		ArrayList<Subscription> subs=new ArrayList<Subscription>();
		for (Subscription s : temp_activeSubscription)
			if (s.getCityId()==cityId)
				subs.add(s);
		return subs;
	}

	/**
	 * remove subscription with given id
	 * @param subscriptionId the id of the subscription we want to remove 
	 * @return the subscription object that we remove
	 */
	public Subscription removeSubscription(int subscriptionId) {
		for (Subscription s : new ArrayList<>(temp_activeSubscription)) {
			if (s.getId() == subscriptionId) {
				temp_activeSubscription.remove(s);
				temp_removeSubscription.add(s);
				return s;
			}
		}
		for (Subscription s : new ArrayList<>(temp_unactiveSubscription)) {
			if (s.getId() == subscriptionId) {
				temp_unactiveSubscription.remove(s);
				temp_removeSubscription.add(s);
				return s;
			}
		}
		return null;
	}

	/**
	 * get subscription object from given id
	 * @param subscriptionId the id of the subscription we want to get
	 * @return subscription object we wanted
	 */
	public Subscription getSubscription(int subscriptionId) {
		for (Subscription s : temp_activeSubscription)
			if (s.getId() == subscriptionId)
				return s;
		for (Subscription s : temp_unactiveSubscription)
			if (s.getId() == subscriptionId)
				return s;
		return null;
	}

	/**
	 * get the one time purchase by given id subscription
	 * @param subscriptionId the subscription id  
	 * @return one time purchase object that contains the subscription  
	 */
	public OneTimePurchase getOneTimePurchase(int subscriptionId) {
		for (OneTimePurchase otp : temp_oneTimePurchase)
			if (otp.getId() == subscriptionId)
				return otp;
		return null;
	}

	/**
	 * remove one time purchase by given id
	 * @param otpId one time purchase id to remove
	 * @return the one time purchase that removed
	 */
	public OneTimePurchase removeOneTimePurchase(int otpId) {
		for (OneTimePurchase otp : new ArrayList<>(temp_oneTimePurchase)) {
			if (otp.getId() == otpId) {
				temp_oneTimePurchase.remove(otp);
				temp_removeOneTimePurchase.add(otp);
				return otp;
			}
		}
		return null;
	}

	/**
	 * returns list of copied active subscriptions
	 * @return list of copied active subscriptions
	 */
	public ArrayList<Subscription> getCopyActiveSubscription() {
		return new ArrayList<>(temp_activeSubscription);
	}

	/**
	 * returns list of copied unactive subscriptions
	 * @return list of copied unactive subscriptions
	 */
	public ArrayList<Subscription> getCopyUnactiveSubscription() {
		return new ArrayList<>(temp_unactiveSubscription);
	}

	/**
	 * returns list of copied one time purchase
	 * @return list of copied one time purchase
	 */
	public ArrayList<OneTimePurchase> getCopyOneTimePurchase() {
		return new ArrayList<>(temp_oneTimePurchase);
	}

	/**
	 * Return credit card number
	 * 
	 * @return credit card number
	 */
	public String getCreditCardNum() {
		return creditCardNum;
	}

	/**
	 * Sets the credit card number
	 * 
	 * @param creditCardNum the new credit card number
	 */
	public void setCreditCardNum(String creditCardNum) {
		this.creditCardNum = creditCardNum;
	}

	/**
	 * Return credit card expired date
	 * 
	 * @return credit card expired date
	 */
	public String getCreditCardExpires() {
		return creditCardExpires;
	}

	/**
	 * Sets the credit card expired date
	 * 
	 * @param creditCardExpires the new credit card expired date
	 */
	public void setCreditCardExpires(String creditCardExpires) {
		this.creditCardExpires = creditCardExpires;
	}

	/**
	 * Return the credit card cvc
	 * 
	 * @return the credit card cvc
	 */
	public String getCvc() {
		return cvc;
	}

	/**
	 * Sets the credit card cvc
	 * 
	 * @param cvc the new credit card cvc
	 */
	public void setCvc(String cvc) {
		this.cvc = cvc;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Customer && ((Customer) o).getId() == this.getId();
	}
}