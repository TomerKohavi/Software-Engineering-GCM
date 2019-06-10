package otherClasses;

import java.sql.Date;
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
			Date today = new Date(Calendar.getInstance().getTime().getTime());
			long start = System.currentTimeMillis();
			
			
			
			City c=Database.getCityById(1);
			Route r=c.getCopyPublishedVersion().getRouteSightById(1).getCopyRoute();
			System.out.println(r==null);
			System.out.println(r.isAcceptabilityToDisabled());
			
			
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
