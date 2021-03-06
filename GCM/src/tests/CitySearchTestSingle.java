package tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.junit.*;

import controller.Database;
import controller.SearchCatalog;
import objectClasses.City;
import objectClasses.CityDataVersion;
import objectClasses.PlaceOfInterest;
import objectClasses.PlaceOfInterestSight;

/**
 * @author ron
 * @author tal JUnit for test single city search
 */
public class CitySearchTestSingle
{

	public static City c1;
	public static City c2;
	public static City c3;
	public static City c4;
	public static PlaceOfInterest p1;
	public static PlaceOfInterest p2;
	public static PlaceOfInterest p3;
	public static PlaceOfInterest p4;
	public static PlaceOfInterest p5;
	public static PlaceOfInterest p6;

	/**
	 * Add point of interest to the city
	 * 
	 * @param c       the city we test
	 * @param name    the name of the city
	 * @param info    the city info
	 * @param publish if the city is published
	 * @return the place of interest that add to the city
	 * @throws SQLException if the access to database failed
	 */
	private static PlaceOfInterest createAndAddPOI(City c, String name, String info, boolean publish)
			throws SQLException
	{
		Random rand = new Random();
		int randomNum = rand.nextInt(100);
		CityDataVersion cdv;
		if (!publish)
		{
			if (c.getCopyUnpublishedVersions().size() == 0)
			{
				cdv = new CityDataVersion(c, randomNum + ".0");
				c.addUnpublishedCityDataVersion(cdv);
			}
			else
				cdv = c.getCopyUnpublishedVersions().get(0);

		}
		else
		{
			cdv = c.getPublishedVersion();
			if (cdv == null)
			{
				cdv = new CityDataVersion(c, randomNum + ".2");
				c._addPublishedCityDataVersion(cdv);
			}
		}
		PlaceOfInterest p = new PlaceOfInterest(c.getId(), name, PlaceOfInterest.PlaceType.HISTORICAL, info, true);
		PlaceOfInterestSight ps = new PlaceOfInterestSight(cdv.getId(), p);
		ps.saveToDatabase();
		p.saveToDatabase();
		return p;
	}

	/**
	 * init the data base
	 * 
	 * @throws SQLException if the access to database failed
	 */
	@BeforeClass
	public static void initDatabaseConnection() throws SQLException
	{
		// open connection
		Database.createConnection();
		// create cities
		Random rand = new Random();
		int randomNum = rand.nextInt(9999);
		c1 = new City("Haifa" + randomNum, "very boring city", 100, 200);
		p1 = createAndAddPOI(c1, "Tomer", "just a simple description", true);
		p2 = createAndAddPOI(c1, "Boby", "house blue in the sea", false);
		c1.saveToDatabase();
		c2 = new City("TelAviv" + randomNum, "exciting city in the middle of israel", 100, 200);
		p3 = createAndAddPOI(c2, "Tal", "error 404 description not found", false);
		p4 = createAndAddPOI(c2, "Tomer", "tree growing by the river", false);
		c2.saveToDatabase();
		c3 = new City("New York" + randomNum, "very boring city", 100, 200);
		p5 = createAndAddPOI(c3, "Sigal", "just a simple description", false);
		p6 = createAndAddPOI(c3, "Ron", "random sentance", true);
		c3.saveToDatabase();
		c4 = new City("New York" + randomNum, "not that bad", 100, 200);
		c4.saveToDatabase();
	}

	/**
	 * Test if the search by city is working good
	 * 
	 * @throws SQLException if the access to database failed
	 */
	@Test
	public void testSearchByCity() throws SQLException
	{
		// search according to City name
		ArrayList<City> searchResult1 = SearchCatalog.SearchCity(c1.getCityName(), null, null, null, true);
		assertTrue(searchResult1.contains(c1));
		assertFalse(searchResult1.contains(c2));
		assertFalse(searchResult1.contains(c3));
		assertFalse(searchResult1.contains(c4));
		// search according to City description
		ArrayList<City> searchResult2 = SearchCatalog.SearchCity(null, "very boring city", null, null, true);
		assertTrue(searchResult2.contains(c1));
		assertFalse(searchResult2.contains(c2));
		assertTrue(searchResult2.contains(c3));
		assertFalse(searchResult2.contains(c4));
		// search according to name and description of city
		ArrayList<City> searchResult3 = SearchCatalog.SearchCity(c3.getCityName(), "very boring city", null, null,
				true);
		assertFalse(searchResult3.contains(c1));
		assertFalse(searchResult3.contains(c2));
		assertTrue(searchResult3.contains(c3));
		assertFalse(searchResult3.contains(c4));
	}

	/**
	 * Test if the search by point of interest is working good
	 * 
	 * @throws SQLException if the access to database failed
	 */
	@Test
	public void testSearchByPOI() throws SQLException
	{
		// search according to Place name
		ArrayList<City> searchResult1 = SearchCatalog.SearchCity(null, null, "Tomer", null, true);
		assertTrue(searchResult1.contains(c1));
		assertTrue(searchResult1.contains(c2));
		assertFalse(searchResult1.contains(c3));
		// search according to Place description
		ArrayList<City> searchResult2 = SearchCatalog.SearchCity(null, null, null, "just a simple description", true);
		assertTrue(searchResult2.contains(c1));
		assertFalse(searchResult2.contains(c2));
		assertTrue(searchResult2.contains(c3));
		// search according to name and description of Place
		ArrayList<City> searchResult3 = SearchCatalog.SearchCity(null, null, "Tomer", "just a simple description",
				true);
		assertTrue(searchResult3.contains(c1));
		assertFalse(searchResult3.contains(c2));
		assertFalse(searchResult3.contains(c3));
	}

	/**
	 * Test if the search by point of interest and city is working good
	 * 
	 * @throws SQLException if the access to database failed
	 */
	@Test
	public void testSearchByBoth() throws SQLException
	{
		// search according to Place name
		ArrayList<City> searchResult1 = SearchCatalog.SearchCity(c1.getCityName(), null, "Tomer", null, true);
		assertTrue(searchResult1.contains(c1));
		assertFalse(searchResult1.contains(c2));
		assertFalse(searchResult1.contains(c3));
		// search according to Place description
		ArrayList<City> searchResult2 = SearchCatalog.SearchCity(null, "very boring city", null,
				"just a simple description", true);
		assertTrue(searchResult2.contains(c1));
		assertFalse(searchResult2.contains(c2));
		assertTrue(searchResult2.contains(c3));
		// search according to name and description of Place
		ArrayList<City> searchResult3 = SearchCatalog.SearchCity(null, "exciting city in the middle of israel", "Tomer",
				"just a simple description", true);
		assertFalse(searchResult3.contains(c1));
		assertFalse(searchResult3.contains(c2));
		assertFalse(searchResult3.contains(c3));
	}

	/**
	 * Close the connection to the database
	 * 
	 * @throws SQLException if the access to database failed
	 */
	@AfterClass
	public static void closeDatabaseConnection() throws SQLException
	{
		// delete cities
		c1.deleteFromDatabase();
		c2.deleteFromDatabase();
		c3.deleteFromDatabase();
		c4.deleteFromDatabase();
		p1.deleteFromDatabase();
		p2.deleteFromDatabase();
		p3.deleteFromDatabase();
		p4.deleteFromDatabase();
		p5.deleteFromDatabase();
		p6.deleteFromDatabase();
		// close connection
		Database.closeConnection();
	}

}