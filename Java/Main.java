import java.sql.Date;
import java.util.Calendar;

import javafx.scene.chart.PieChart.Data;

public class Main {
	public static void main(String [] args)
	{
		Database.createConnection();
		Date today = new Date(Calendar.getInstance().getTime().getTime());
		City c=new City("Haifa", "boring city");
		CityDataVersion cdv=new CityDataVersion(c,"0.11",29.90,132);
		PlaceOfInterest p=new PlaceOfInterest(c.getId(), "Haifa Universita", PlaceOfInterest.PlaceType.RESTURANT, "gaddddi", false);
		p.saveToDatabase();
		PlaceOfInterestSight ps=new PlaceOfInterestSight(cdv, p);
		cdv.addPlaceOfInterestSight(ps);
		c.addPublishedCityDataVersion(cdv);
		c.saveToDatabase();
		City c2=Database.getCityById(c.getId());
		CityDataVersion cdv2=c2.getPublishedVersion();
		System.out.println(cdv2.getPlaceOfInterestSightByPlaceOfInterestId(p.getId())==null);
		cdv2.removePlaceOfInterestSightById(ps.getId());
		cdv2.saveToDatabase();
		cdv2=c2.getPublishedVersion();
		System.out.println(cdv2.getPlaceOfInterestSightByPlaceOfInterestId(p.getId())==null);
		Customer cust = new Customer("Tal20", "11235", "a@a.com", "Tal", "Shahnov", "055");
		Subscription sub=new Subscription(cust, c, today, 2000, 1999.99, new Date(2028,5,12));
		cust.addSubscription(sub);
		cust.saveToDatabase();
		Customer cust2=Database.getCustomerById(cust.getId());
		System.out.println(Database.searchCustomer("Tal20", "11235"));
		System.out.println(cust2.getCopyUnactiveSubscription().size());
		Date d2=new Date(2028,5,12);
		System.out.println(d2.compareTo(today));
		
		cust.deleteFromDatabase();
		Database.closeConnection();
	}
}
