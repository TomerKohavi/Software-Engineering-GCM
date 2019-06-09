package classes;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

public class Main {

	public static void main(String [] args)
	{
		Database.createConnection();
		try {
			Database.initDatabase("Tal", "11235813");
//			Date today = new Date(Calendar.getInstance().getTime().getTime());
//			City c=new City("Haifa", "boring city");
//			CityDataVersion cdv=new CityDataVersion(c,"0.11",29.90,132);
//			
//			PlaceOfInterest p=new PlaceOfInterest(c.getId(), "Haifa Universita", PlaceOfInterest.PlaceType.RESTAURANT,
//					"Gadi is the king", false);
//
//			p.saveToDatabase();
//			PlaceOfInterestSight ps=new PlaceOfInterestSight(cdv, p);
//			cdv.addPlaceOfInterestSight(ps);
//			c.addPublishedCityDataVersion(cdv);
//			c.saveToDatabase();
//			City c2=Database.getCityById(c.getId());
//			CityDataVersion cdv2=c2.getPublishedVersion();
//			System.out.println(cdv2.getPlaceOfInterestSightByPlaceOfInterestId(p.getId())==null);
//			
//			Route r = new Route(c.getId(), "C");
//			RouteStop rs=new RouteStop(r, p, new Time(213123));
//			r.addRouteStop(rs);
//			rs.saveToDatabase();
//			RouteSight rss=new RouteSight(cdv2, r, false);
//			cdv2.addRouteSight(rss);
//			//cdv2.removePlaceOfInterestSightById(ps.getId());
//			cdv2.saveToDatabase();
//			cdv2=c2.getPublishedVersion();
//			System.out.println(cdv2.getPlaceOfInterestSightByPlaceOfInterestId(p.getId())==null);
//			System.out.println(cdv2.getRouteSightByRouteId(r.getId()).getCopyRoute().getCopyRouteStops().get(0).getCopyPlace().equals(p));
//		
//			
//			Customer cust = Customer._createCustomer(-1,"Tal20", "11235", "a@a.com", "Tal",
//					"Shahnov", "055","5495681338665894","07/24", "896");
//			Subscription sub=new Subscription(cust, c, today, 2000, 1999.99, new Date(2028,5,12));
//			cust.addSubscription(sub);
//			cust.saveToDatabase();
//			Customer cust2=Database.getCustomerById(cust.getId());
//			System.out.println(Database.searchCustomer("Tal20", "11235"));
//			System.out.println(cust2.getCopyUnactiveSubscription().size());
//			Date d2=new Date(2028,5,12);
//			
//			System.out.println("Tal's tests start");
//			
//			System.out.println(Database.searchPlaceOfInterest(null, "gAd king", null));
//			
//			cust.deleteFromDatabase();
//			c.deleteFromDatabase();
//			p.deleteFromDatabase();
//			rs.deleteFromDatabase();
//			cdv2.deleteFromDatabase();
//Database.resetAll("Tal", "11235813");
						
			
		}catch (Exception e) {
			System.out.println(e);
		}
		Database.closeConnection();
		System.out.println("Done");
	}
}
