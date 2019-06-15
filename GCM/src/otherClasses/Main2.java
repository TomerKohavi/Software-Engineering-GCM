package otherClasses;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

import controller.*;
import objectClasses.*;

public class Main2 {
	public static void main(String [] args)
	{
		Database.createConnection();
		try 
		{
			//Date today = new Date(Calendar.getInstance().getTime().getTime());
			System.out.println(new Date(119,1,14));
			/*Customer cust = new Customer("t", "t", "t@gmail.com", "t", "t t", "0523002100", "0095681338665894", "07/24",
					"000");
			
			Database.saveSecuredCustomer(cust);
			
			Customer res = Database.getCustomerById(cust.getId());
			System.out.println(res.getPassword());
			
			System.out.println(Database.searchSecuredCustomer("t", "s"));
			
			cust.deleteFromDatabase();*/
//			Date today = new Date(Calendar.getInstance().getTime().getTime());
//			long start = System.currentTimeMillis();
//			System.out.println("ss");
//			
//			Time time = new Time(4, 5, 4);
//			RouteStop rs = RouteStop._createRouteStop(1244, 1444, 2414, "a", 2242, time);
//			rs.saveToDatabase();
//			System.out.println(time);
//			RouteStop rs2 = Database._getRouteStopById(1244);
//			System.out.println(rs2.getRecommendedTime());
//			
//			/*Customer c=Database.getCustomerById(2);
//			System.out.println(c.getCopyActiveSubscription().get(0).getCityId());
//			ArrayList<Integer> ids = Database.searchSubscription(2, null, today, true);
//			System.out.println(ids.size());*/
//			//System.out.println(Database._getSubscriptionById(ids.get(0)).getCityId());
//			//InformationSystem.newVersionWasPublished(2);
//			//InformationSystem.newVersionWasPublished(1);
//			//Downloader.downloadReportsUpdateVersions("downloadReportsUpdateVersions.txt");
//			
//			
//			long finish = System.currentTimeMillis();
//			long timeElapsed = (finish - start)/1000;
//			System.out.println("took "+timeElapsed+" sec");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Database.closeConnection();
	}
}
