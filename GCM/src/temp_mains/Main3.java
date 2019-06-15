package temp_mains;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import controller.*;
import objectClasses.*;

public class Main3 {
	public static void main(String [] args)
	{
		Database.createConnection();
		try 
		{
			LocalDate today = LocalDate.now();
			long start = System.currentTimeMillis();
			
			
			
			Subscription s=Database._getSubscriptionById(7);
			if(s==null)
				System.out.println("nullllllllllllll");
			else
				System.out.println("num months: "+s.getNumMonths());
			
			
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
