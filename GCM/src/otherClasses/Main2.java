package otherClasses;

import java.sql.Date;
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
			Date today = new Date(Calendar.getInstance().getTime().getTime());
			long start = System.currentTimeMillis();
			
			
			
			/*Customer c=Database.getCustomerById(2);
			System.out.println(c.getCopyActiveSubscription().get(0).getCityId());
			ArrayList<Integer> ids = Database.searchSubscription(2, null, today, true);
			System.out.println(ids.size());*/
			//System.out.println(Database._getSubscriptionById(ids.get(0)).getCityId());
			//InformationSystem.newVersionWasPublished(2);
			//InformationSystem.newVersionWasPublished(1);
			Downloader.downloadReportsUpdateVersions("downloadReportsUpdateVersions.txt");
			
			
			long finish = System.currentTimeMillis();
			long timeElapsed = (finish - start)/1000;
			System.out.println("took "+timeElapsed+" sec");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Database.closeConnection();
	}
}
