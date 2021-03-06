import java.io.Serializable;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Calendar;

public class Customer extends User implements ClassMustProperties, Serializable {
	
	private String creditCardNum;
	private String creditCardExpires;
	private String cvc;

	ArrayList<Subscription> temp_activeSubscription;
	ArrayList<Subscription> temp_unactiveSubscription;
	ArrayList<OneTimePurchase> temp_oneTimePurchase;
	ArrayList<Subscription> temp_removeSubscription;
	ArrayList<OneTimePurchase> temp_removeOneTimePurchase;

	private Customer(int id, String userName, String password, String email, String firstName, String lastName,
			String phoneNumber,String creditCardNum,String creditCardExpires,String cvc) {
		super(id, userName, password, email, firstName, lastName, phoneNumber);
		this.creditCardNum=creditCardNum;
		this.creditCardExpires=creditCardExpires;
		this.cvc=cvc;
		reloadTempsFromDatabase();
	}

	public static Customer _createCustomer(int id, String userName, String password, String email, String firstName,
			String lastName, String phoneNumber,String creditCardNum,String creditCardExpires,String cvc) { // friend to Database
		return new Customer(id, userName, password, email, firstName, lastName, phoneNumber,creditCardNum,creditCardExpires,cvc);
	}

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
		Date today = new Date(Calendar.getInstance().getTime().getTime());
		this.temp_activeSubscription = generateActiveSubscriptions(today);
		this.temp_unactiveSubscription = generateUnactiveSubscriptions(today);
		this.temp_oneTimePurchase = generateOneTimePurchases();
		this.temp_removeSubscription = new ArrayList<>();
		this.temp_removeOneTimePurchase = new ArrayList<>();
	}

	private ArrayList<Subscription> generateActiveSubscriptions(Date dateToCheck) {
		ArrayList<Integer> ids = Database.searchSubscription(super.getId(), null, dateToCheck, true);
		return generateListSubscriptions(ids);
	}

	private ArrayList<Subscription> generateUnactiveSubscriptions(Date dateToCheck) {
		ArrayList<Integer> ids = Database.searchSubscription(super.getId(), null, dateToCheck, false);
		return generateListSubscriptions(ids);
	}

	private ArrayList<Subscription> generateListSubscriptions(ArrayList<Integer> ids) {
		ArrayList<Subscription> arrList = new ArrayList<Subscription>();
		for (int id : ids) {
			Subscription o = Database._getSubscriptionById(id);
			if (o == null)
				continue;
			if (Database.getCityById(o.getCityId()) == null)
				Database._deleteSubscription(id);
			else
				arrList.add(o);
		}
		return arrList;
	}

	private ArrayList<OneTimePurchase> generateOneTimePurchases() {
		ArrayList<Integer> ids = Database.searchOneTimePurchase(super.getId(), null, null, null);
		return generateListOneTimePurchases(ids);
	}

	private ArrayList<OneTimePurchase> generateListOneTimePurchases(ArrayList<Integer> ids) {
		ArrayList<OneTimePurchase> arrList = new ArrayList<OneTimePurchase>();
		for (int id : ids) {
			OneTimePurchase o = Database._getOneTimePurchaseById(id);
			if (o == null)
				continue;
			if (Database.getCityById(o.getCityId()) == null)
				Database._deleteOneTimePurchase(id);
			else
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

	public boolean addSubscription(Subscription sub) {
		if (sub.getUserId() != this.getId())
			return false;
		Date today = new Date(Calendar.getInstance().getTime().getTime());
		if (sub.getPurchaseDate().compareTo(today) >= 0)
			temp_activeSubscription.add(sub);
		else
			temp_unactiveSubscription.add(sub);
		return true;
	}

	public boolean addOneTimePurchase(OneTimePurchase otp) {
		if (otp.getUserId() != this.getId())
			return false;
		temp_oneTimePurchase.add(otp);
		return true;
	}

	public ArrayList<Subscription> getGoingToEnd() {
		ArrayList<Subscription> result = new ArrayList<>();
		Date today = new Date(Calendar.getInstance().getTime().getTime());
		for (Subscription s : temp_activeSubscription)
			if (s.isGoingToEnd(today))
				result.add(s);
		return result;
	}

	public boolean canViewCityWithSubscription(int cityId) {
		for (Subscription s : temp_activeSubscription)
			if (s.getCityId() == cityId)
				return true;
		return false;
	}

	public OneTimePurchase getActiveOneTimePurchaseByCity(int cityId) {
		for (OneTimePurchase otp : temp_oneTimePurchase)
			if (otp.getCityId() == cityId && !otp.getWasDownload())
				return otp;
		return null;
	}

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

	public Subscription getSubscription(int subscriptionId) {
		for (Subscription s : temp_activeSubscription)
			if (s.getId() == subscriptionId)
				return s;
		for (Subscription s : temp_unactiveSubscription)
			if (s.getId() == subscriptionId)
				return s;
		return null;
	}

	public OneTimePurchase getOneTimePurchase(int subscriptionId) {
		for (OneTimePurchase otp : temp_oneTimePurchase)
			if (otp.getId() == subscriptionId)
				return otp;
		return null;
	}

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

	public ArrayList<Subscription> getCopyActiveSubscription() {
		return new ArrayList<>(temp_activeSubscription);
	}

	public ArrayList<Subscription> getCopyUnactiveSubscription() {
		return new ArrayList<>(temp_unactiveSubscription);
	}

	public ArrayList<OneTimePurchase> getCopyOneTimePurchase() {
		return new ArrayList<>(temp_oneTimePurchase);
	}

	public String getCreditCardNum() {
		return creditCardNum;
	}

	public void setCreditCardNum(String creditCardNum) {
		this.creditCardNum = creditCardNum;
	}

	public String getCreditCardExpires() {
		return creditCardExpires;
	}

	public void setCreditCardExpires(String creditCardExpires) {
		this.creditCardExpires = creditCardExpires;
	}

	public String getCvc() {
		return cvc;
	}

	public void setCvc(String cvc) {
		this.cvc = cvc;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Customer && ((Customer) o).getId() == this.getId();
	}
}