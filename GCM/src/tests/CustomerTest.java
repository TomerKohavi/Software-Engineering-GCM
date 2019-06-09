package tests;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.Random;

import org.junit.*;

import classes.City;
import classes.Customer;
import classes.Database;
import classes.OneTimePurchase;
import classes.Subscription;
 
public class CustomerTest {
          
            @BeforeClass
            public static void initDatabaseConnection() {
            	Database.createConnection();
            }
            
            @Test
        	public void testSubscription() {
        		Random rand = new Random();
        		Date today = new Date(Calendar.getInstance().getTime().getTime());
        		int randomNum=rand.nextInt(9999);
        		//create citys
        		City c=new City("Haifa"+randomNum, "boring city");
        		c.saveToDatabase();
        		City c2=new City("TelAviv"+randomNum, "less boring city");
        		c2.saveToDatabase();
        		//create customer
        		Customer cust = new Customer("Tal"+randomNum, "11235", "a@a.com"+randomNum, "Tal", "Shahnov", "055","5495681338665894","07/24", "896");
        		cust.saveToDatabase();
        		//check not exist subscription
        		assertFalse(cust.canViewCityWithSubscription(c.getId()));
        		//check good subscription
        		Subscription sub=new Subscription(cust, c, today, 2000, 1999.99, new Date(2028,5,12));
        		cust.addSubscription(sub);
        		cust.saveToDatabase();
        		cust=Database.getCustomerById(cust.getId());
        		assertTrue(cust.canViewCityWithSubscription(c.getId()));
        		//check bad subscription
        		Subscription sub2=new Subscription(cust, c2, new Date(100,5,12), 2000, 1999.99, new Date(100,20,12));
        		Subscription sub3=new Subscription(cust, c2, new Date(122,5,12), 2000, 1999.99, new Date(122,20,12));
        		cust.addSubscription(sub2);
        		cust.addSubscription(sub3);
        		cust.saveToDatabase();
        		cust=Database.getCustomerById(cust.getId());
        		assertFalse(cust.canViewCityWithSubscription(c2.getId()));
        		//delete from database
        		cust.deleteFromDatabase();
        		c.deleteFromDatabase();
        		c2.deleteFromDatabase();
        	}
            
        	@Test
        	public void testOneTimePurchase() {
        		Random rand = new Random();
        		Date today = new Date(Calendar.getInstance().getTime().getTime());
        		Database.createConnection();
        		//create citys
        		City c=new City("Haifa", "boring city");
        		c.saveToDatabase();
        		//create customer
        		int randomNum=rand.nextInt(9999);
        		Customer cust = new Customer("Tal"+randomNum, "11235", "a@a.com"+randomNum, "Tal", "Shahnov", "055","5495681338665894","07/24", "896");
        		cust.saveToDatabase();
        		//check not exist subscription
        		assertEquals(null, cust.getActiveOneTimePurchaseByCity(c.getId()));
        		//check good otp
        		OneTimePurchase otp=new OneTimePurchase(cust, c, today, 100.99, 2000);
        		cust.addOneTimePurchase(otp);
        		cust.saveToDatabase();
        		cust=Database.getCustomerById(cust.getId());
        		assertNotEquals(null, cust.getActiveOneTimePurchaseByCity(c.getId())!=null);
        		//change otp to bad
        		otp.updateToWasDownload(); //because we reloaded the cust from database the otp is a copy and not referance
        		otp.saveToDatabase();
        		cust.reloadTempsFromDatabase();
        		assertEquals(null,cust.getActiveOneTimePurchaseByCity(c.getId()));
        		//delete from database
        		cust.deleteFromDatabase();
        		c.deleteFromDatabase();
        	}
            
            @AfterClass 
            public static void closeDatabaseConnection() {
            	Database.closeConnection();
          }
 
}