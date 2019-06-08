import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.*;
 
public class CitySearchTestSingle {
	
			public City c;
          
            @BeforeClass
            public static void initDatabaseConnection() {
            	Database.createConnection();
            }
            
            @Test
        	public void testSearchByCity() {
            	/*Random rand = new Random();
        		int randomNum=rand.nextInt(9999);
        		//create cities
        		City c1=new City("Haifa"+randomNum, "very boring city");
        		c1.saveToDatabase();
        		City c2=new City("TelAviv"+randomNum, "exciting city in the middle of israel");
        		c2.saveToDatabase();
        		City c3=new City("New York"+randomNum, "very boring city");
        		c3.saveToDatabase();
        		City c4=new City("New York"+randomNum, "not that bad");
        		c4.saveToDatabase();
        		//search according to City name
        		ArrayList<City> searchResult1=SearchCatalog.SearchCity("Haifa"+randomNum, null, null, null, true);
        		assertTrue(searchResult1.contains(c1));
        		assertFalse(searchResult1.contains(c2));
        		assertFalse(searchResult1.contains(c3));
        		assertFalse(searchResult1.contains(c4));
        		//search according to City description
        		ArrayList<City> searchResult2=SearchCatalog.SearchCity(null, "very boring city", null, null, true);
        		assertTrue(searchResult2.contains(c1));
        		assertFalse(searchResult2.contains(c2));
        		assertTrue(searchResult2.contains(c3));
        		assertFalse(searchResult2.contains(c4));
        		//search according to name and description of city
        		ArrayList<City> searchResult3=SearchCatalog.SearchCity("New York"+randomNum, "very boring city", null, null, true);
        		assertFalse(searchResult3.contains(c1));
        		assertFalse(searchResult3.contains(c2));
        		assertTrue(searchResult3.contains(c3));
        		assertFalse(searchResult3.contains(c4));
        		//delete cities
        		c1.deleteFromDatabase();
        		c2.deleteFromDatabase();
        		c3.deleteFromDatabase();
        		c4.deleteFromDatabase();*/
        	}
            
            private PlaceOfInterest createAndAddPOI(City c,String name,String info)
            {
            	Random rand = new Random();
        		int randomNum=rand.nextInt(9999);
        		CityDataVersion cdv=new CityDataVersion(c, "1.0", 100, 200);
        		if(randomNum%2==0)
        			c.addPublishedCityDataVersion(cdv);
        		else
        			c.addUnpublishedCityDataVersion(cdv);
        		PlaceOfInterest p=new PlaceOfInterest(c.getId(), name, PlaceOfInterest.PlaceType.HISTORICAL, info, true);
        		PlaceOfInterestSight ps=new PlaceOfInterestSight(cdv, p);
        		cdv.addPlaceOfInterestSight(ps);
        		p.saveToDatabase();
        		return p;
            }
            
            @Test
        	public void testSearchByPOI() {
            	/*Random rand = new Random();
        		int randomNum=rand.nextInt(9999);
        		//create cities
        		City c1=new City("Haifa"+randomNum, "very boring city");
        		PlaceOfInterest p1=createAndAddPOI(c1,"Tomer","just a simple description");
        		PlaceOfInterest p2=createAndAddPOI(c1,"Boby","house blue in the sea");
        		c1.saveToDatabase();
        		City c2=new City("TelAviv"+randomNum, "exciting city in the middle of israel");
        		PlaceOfInterest p3=createAndAddPOI(c2,"Tal","error 404 description not found");
        		PlaceOfInterest p4=createAndAddPOI(c2,"Tomer","tree growing by the river");
        		c2.saveToDatabase();
        		City c3=new City("New York"+randomNum, "very boring city");
        		PlaceOfInterest p5=createAndAddPOI(c3,"Sigal","just a simple description");
        		PlaceOfInterest p6=createAndAddPOI(c3,"Ron","random sentance");
        		c3.saveToDatabase();
        		//search according to Place name
        		ArrayList<City> searchResult1=SearchCatalog.SearchCity(null, null, "Tomer", null, true);
        		assertTrue(searchResult1.contains(c1));
        		assertTrue(searchResult1.contains(c2));
        		assertFalse(searchResult1.contains(c3));
        		//search according to Place description
        		ArrayList<City> searchResult2=SearchCatalog.SearchCity(null, null, null, "just a simple description", true);
        		assertTrue(searchResult2.contains(c1));
        		assertFalse(searchResult2.contains(c2));
        		assertTrue(searchResult2.contains(c3));
        		//search according to name and description of Place
        		ArrayList<City> searchResult3=SearchCatalog.SearchCity(null, null,"Tomer", "just a simple description", true);
        		assertTrue(searchResult3.contains(c1));
        		assertFalse(searchResult3.contains(c2));
        		assertFalse(searchResult3.contains(c3));
        		//check with delete
        		p1.deleteFromDatabase();
        		ArrayList<City> searchResult4=SearchCatalog.SearchCity(null, null, "Tomer", null, true);
        		assertFalse(searchResult4.contains(c1));
        		assertTrue(searchResult4.contains(c2));
        		assertFalse(searchResult4.contains(c3));
        		//delete cities
        		c1.deleteFromDatabase();
        		c2.deleteFromDatabase();
        		c3.deleteFromDatabase();
        		p2.deleteFromDatabase();
        		p3.deleteFromDatabase();
        		p4.deleteFromDatabase();
        		p5.deleteFromDatabase();
        		p6.deleteFromDatabase();*/
        	}
            
            
            @Test
        	public void testSearchByBoth() {
            	Random rand = new Random();
        		int randomNum=rand.nextInt(9999);
        		//create cities
        		City c1=new City("Haifa"+randomNum, "very boring city");
        		PlaceOfInterest p1=createAndAddPOI(c1,"Tomer","just a simple description");
        		PlaceOfInterest p2=createAndAddPOI(c1,"Boby","house blue in the sea");
        		c1.saveToDatabase();
        		City c2=new City("TelAviv"+randomNum, "exciting city in the middle of israel");
        		PlaceOfInterest p3=createAndAddPOI(c2,"Tal","error 404 description not found");
        		PlaceOfInterest p4=createAndAddPOI(c2,"Tomer","tree growing by the river");
        		c2.saveToDatabase();
        		City c3=new City("New York"+randomNum, "very boring city");
        		PlaceOfInterest p5=createAndAddPOI(c3,"Sigal","just a simple description");
        		PlaceOfInterest p6=createAndAddPOI(c3,"Ron","random sentance");
        		c3.saveToDatabase();
        		//search according to Place name
        		ArrayList<City> searchResult1=SearchCatalog.SearchCity("Haifa"+randomNum, null, "Tomer", null, true);
        		assertTrue(searchResult1.contains(c1));
        		assertFalse(searchResult1.contains(c2));
        		assertFalse(searchResult1.contains(c3));
        		//search according to Place description
        		ArrayList<City> searchResult2=SearchCatalog.SearchCity(null, "very boring city", null, "just a simple description", true);
        		assertTrue(searchResult2.contains(c1));
        		assertFalse(searchResult2.contains(c2));
        		assertTrue(searchResult2.contains(c3));
        		//search according to name and description of Place
        		ArrayList<City> searchResult3=SearchCatalog.SearchCity(null, "exciting city in the middle of israel","Tomer", "just a simple description", true);
        		assertFalse(searchResult3.contains(c1));
        		assertFalse(searchResult3.contains(c2));
        		assertFalse(searchResult3.contains(c3));
        		//check with delete
        		p1.deleteFromDatabase();
        		ArrayList<City> searchResult4=SearchCatalog.SearchCity("TelAviv"+randomNum, "exciting city in the middle of israel", "Tomer", null, true);
        		assertFalse(searchResult4.contains(c1));
        		assertTrue(searchResult4.contains(c2));
        		assertFalse(searchResult4.contains(c3));
        		//delete cities
        		c1.deleteFromDatabase();
        		c2.deleteFromDatabase();
        		c3.deleteFromDatabase();
        		p2.deleteFromDatabase();
        		p3.deleteFromDatabase();
        		p4.deleteFromDatabase();
        		p5.deleteFromDatabase();
        		p6.deleteFromDatabase();
        	}
            
            @AfterClass 
            public static void closeDatabaseConnection() {
            	Database.closeConnection();
          }
 
}