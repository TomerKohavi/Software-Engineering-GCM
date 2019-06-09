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
			
			
			
			Customer c=Database.getCustomerById(2);
			System.out.println(c.getCopyActiveSubscription().size());
			ArrayList<Integer> ids = Database.searchSubscription(null, null, today, false);
			System.out.println(ids.size());
			//InformationSystem.newVersionWasPublished(3);
			//Downloader.downloadReportsUpdateVersions("downloadReportsUpdateVersions.txt");
			
			
			long finish = System.currentTimeMillis();
			long timeElapsed = (finish - start)/1000;
			System.out.println("took "+timeElapsed+" sec");
		}
		catch (Exception e) {
			System.out.println(e);
		}
		Database.closeConnection();
	}
}
