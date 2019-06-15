package tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Random;

import org.junit.*;

import controller.Database;
import objectClasses.City;
import objectClasses.Customer;
import objectClasses.OneTimePurchase;
import objectClasses.Subscription;

/**
 * @author ron
 * @author tal JUnit for customer test
 */
public class CustomerTest
{

	@BeforeClass
	public static void initDatabaseConnection() throws SQLException
	{
		Database.createConnection();
	}

	/**
	 * Check if the customer subscription is fine
	 * 
	 * @throws SQLException connection error
	 */
	@Test
	public void testSubscription() throws SQLException
	{
		Random rand = new Random();
		LocalDate today = LocalDate.now();
		int randomNum = rand.nextInt(9999);
		// create citys
		City c = new City("Haifa" + randomNum, "boring city", 100, 200);
		c.saveToDatabase();
		City c2 = new City("TelAviv" + randomNum, "less boring city", 100, 200);
		c2.saveToDatabase();
		// create customer
		Customer cust = new Customer("Tal" + randomNum, "11235", "a@a.com" + randomNum, "Tal", "Shahnov", "055",
				"5495681338665894", "07/24", "896");
		cust.saveToDatabase();
		// check not exist subscription
		assertFalse(cust.canViewCityWithSubscription(c.getId()));
		// check good subscription
		Subscription sub = new Subscription(cust, c.getId(), today, 2000, 1999.99, LocalDate.of(2028, 5, 12));
		cust.addSubscription(sub);
		cust.saveToDatabase();
		cust = Database.getCustomerById(cust.getId());
		assertTrue(cust.canViewCityWithSubscription(c.getId()));
		// check bad subscription
		Subscription sub2 = new Subscription(cust, c2.getId(), LocalDate.of(100, 5, 12), 2000, 1999.99,
				LocalDate.of(100, 20, 12));
		Subscription sub3 = new Subscription(cust, c2.getId(), LocalDate.of(122, 5, 12), 2000, 1999.99,
				LocalDate.of(122, 20, 12));
		cust.addSubscription(sub2);
		cust.addSubscription(sub3);
		cust.saveToDatabase();
		cust = Database.getCustomerById(cust.getId());
		assertFalse(cust.canViewCityWithSubscription(c2.getId()));
		// delete from database
		cust.deleteFromDatabase();
		c.deleteFromDatabase();
		c2.deleteFromDatabase();
	}

	/**
	 * Check if the customer one time purchase is fine
	 * 
	 * @throws SQLException connection error
	 */
	@Test
	public void testOneTimePurchase() throws SQLException
	{
		Random rand = new Random();
		LocalDate today = LocalDate.now();
		Database.createConnection();
		// create citys
		City c = new City("Haifa", "boring city", 100, 200);
		c.saveToDatabase();
		// create customer
		int randomNum = rand.nextInt(9999);
		Customer cust = new Customer("Tal" + randomNum, "11235", "a@a.com" + randomNum, "Tal", "Shahnov", "055",
				"5495681338665894", "07/24", "896");
		cust.saveToDatabase();
		// check not exist subscription
		assertEquals(null, cust.getActiveOneTimePurchaseByCity(c.getId()));
		// check good otp
		OneTimePurchase otp = new OneTimePurchase(cust, c.getId(), today, 100.99, 2000);
		cust.addOneTimePurchase(otp);
		cust.saveToDatabase();
		cust = Database.getCustomerById(cust.getId());
		assertNotEquals(null, cust.getActiveOneTimePurchaseByCity(c.getId()) != null);
		// change otp to bad
		otp.updateToWasDownload(); // because we reloaded the cust from database the otp is a copy and not
									// referance
		otp.saveToDatabase();
		cust.reloadTempsFromDatabase();
		assertEquals(null, cust.getActiveOneTimePurchaseByCity(c.getId()));
		// delete from database
		cust.deleteFromDatabase();
		c.deleteFromDatabase();
	}

	/**
	 * Close database connection
	 * 
	 * @throws SQLException connection error
	 */
	@AfterClass
	public static void closeDatabaseConnection() throws SQLException
	{
		Database.closeConnection();
	}

}