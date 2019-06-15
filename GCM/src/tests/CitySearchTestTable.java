package tests;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import controller.Database;
import controller.SearchCatalog;
import objectClasses.City;
import objectClasses.CityDataVersion;
import objectClasses.PlaceOfInterest;
import objectClasses.PlaceOfInterestSight;
 
/**
 * @author ron
 * @author tal
 * JUnit for search city
 */
@RunWith(Parameterized.class)
public class CitySearchTestTable {
	
			@Parameters(name = "{index}: search({0},{1},{2},{3},{4})= [{5},{6},{7},{8}]")
		    public static Iterable<Object[]> data() {
		        return Arrays.asList(new Object[][] { 
		        		//cityName, cityDescription, placeName, placeDescription, useUnpublished,		expectedC1, expectedC2, expectedC3, expectedC4
		                 { "Haifa",null , null ,null , false ,											true , false, false, false },
		                 { null ,"the second most populous city in Israel" , null ,null , false ,		false , true, false, false },
		                 { null ,null , null ,"One of the biggest" , true ,								false , false, true, true },
		                 { null ,null , null ,"One of the biggest" , false ,							false , false, true, false },
		                 { "Tel Aviv" ,null , "Dizengoff Circle" ,null , false ,						false , true, false, false },
		                 { null ,null , "Brooklyn Bridge" ,null , false ,								false , false, true, false },
		                 { null ,"The City" , null ,"Israel" , false ,									true , true, false, false },
		                 { null ,null , null ,"area" , false ,											false , true, true, false },
		                 { null ,null , null ,"area Israel" , false ,									false , true, false, false },
		                 { "Jerusalem" ,null , null ,"wall" , false ,									false , false, false, true },
		                 { null ,"popular" , null ,"river" , false ,									false , false, true, false },
		                 { "Tel Aviv" ,null , "Jaffa" ,null , true ,									false , true, false, false },
		                 { "Tel Aviv" ,null , "Jaffa" ,null , false ,									false , false, false, false },
		                
		           });
		    }
		    
		    @Parameter(0)
		    public String cityName;
		    @Parameter(1)
		    public String cityDescription;
		    @Parameter(2)
		    public String placeName;
		    @Parameter(3)
			public String placeDescription;
		    @Parameter(4)
			public boolean useUnpublished;
		    @Parameter(5)
			public boolean expectedC1;
		    @Parameter(6)
			public boolean expectedC2;
		    @Parameter(7)
			public boolean expectedC3;
		    @Parameter(8)
			public boolean expectedC4;
			
		    
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
			public static PlaceOfInterest p7;
			public static PlaceOfInterest p8;
			public static PlaceOfInterest p9;
			public static PlaceOfInterest p10;
          
			/**
			 * Add point of interest to city
			 * @param c the city to add
			 * @param name the point of interest name
			 * @param info the point of interest info
			 * @param publish the point of interest publish
			 * @return point of interest that created and added
			 */
			private static PlaceOfInterest createAndAddPOI(City c,String name,String info,boolean publish)
            {
		    	Random rand = new Random();
        		int randomNum=rand.nextInt(100);
		    	CityDataVersion cdv;
        		if(!publish)
        			{
        				if(c.getCopyUnpublishedVersions().size()==0)
        					{
        						cdv=new CityDataVersion(c, randomNum+".0");
        						c.addUnpublishedCityDataVersion(cdv);
        					}
        				else
        					cdv=c.getCopyUnpublishedVersions().get(0);
        				
        			}
        		else
        			{
        				cdv=c.getPublishedVersion();
        				if(cdv==null)
        				{
        					cdv=new CityDataVersion(c, randomNum+".2");
        					c.addPublishedCityDataVersion(cdv);
        				}
        			}
        		PlaceOfInterest p=new PlaceOfInterest(c.getId(), name, PlaceOfInterest.PlaceType.HISTORICAL, info, true);
        		PlaceOfInterestSight ps=new PlaceOfInterestSight(cdv.getId(), p);
        		ps.saveToDatabase();
        		p.saveToDatabase();
        		return p;
            }
          
            /**
             * Init database connection
             */
            @BeforeClass
            public static void initDatabaseConnection() {
            	//open connection
            	Database.createConnection();
            	//create cities
        		c1=new City("Haifa", "The third largest city in Israel. As of 2016, the city is a major seaport located on Israel's Mediterranean coastline in the Bay of Haifa covering 63.7 square kilometres.",100, 200);
        		p1=createAndAddPOI(c1,"University of Haifa","A public research university on the top of Mount Carmel in Haifa, Israel. The university was founded in 1963 by the mayor of its host city, Abba Hushi, to operate under the academic auspices of the Hebrew University of Jerusalem.",true);
        		p2=createAndAddPOI(c1,"Technion","A public research university in Haifa, Israel. Established in 1912 during the Ottoman Empire and more than 35 years before the State of Israel, the Technion is the oldest university in the country and is ranked the best university in Israel and in the whole of the Middle East in the Shanghai Ranking.",false);
        		c1.saveToDatabase();
        		c2=new City("Tel Aviv", "The city is the second most populous city in Israel — after Jerusalem—and the most populous city in the conurbation of Gush Dan, Israel's largest metropolitan area.",100, 200);
        		p3=createAndAddPOI(c2,"Jaffa","A short walk south along the coast from downtown Tel Aviv brings you to the old Arab port town of Jaffa, with its preserved acropolis.",false);
        		p4=createAndAddPOI(c2,"Yemenite Quarter","One of Tel Aviv's most atmospheric neighbourhoods, the Yemenite Quarter is full of meandering alleyways lined by old-style architecture that has withstood the area's gentrification.",true);
        		p9=createAndAddPOI(c2,"Dizengoff Circle","The hub of Tel Aviv is this central plaza, laid out on two levels with a raised area for pedestrians above the carriageway and topped by the peculiar modern-art Fire and Water Fountain, designed by Israel artist Yaacov Agam. ",true);
        		c2.saveToDatabase();
        		c3=new City("New York", "The City of New York, usually called either New York City (NYC) or simply New York (NY), is the most popular city in the United States.",100, 200);
        		p5=createAndAddPOI(c3,"Empire State Building","It's practically impossible to imagine the sparkling New York skyline without the iconic Empire State Building.",false);
        		p6=createAndAddPOI(c3,"Brooklyn Bridge","No mere river crossing, this span is an elegant reminder of New York’s history of architectural innovation.",true);
        		p10=createAndAddPOI(c3,"Central Park","To feel truly out of the city, head to the 38-acre wilderness area on the west side of the park known as the Ramble. It is One of the biggest known to man. ",true);
        		c3.saveToDatabase();
        		c4=new City("Jerusalem", "Jerusalem, of course, is not only the capital of Israel, but also one of the world’s most famous cities.",100, 200);
        		p7=createAndAddPOI(c4,"Machane Yehuda","One of the biggest markets are the life blood of any major city, and Machane Yehuda market is no exception.",false);
        		p8=createAndAddPOI(c4,"Western Wall","The Western “Wailing” Wall, otherwise known as HaKotel in Hebrew, is one of the absolute must-sees on any visit to Jerusalem.",true);
        		c4.saveToDatabase();
            }
            
            /**
             * Test the search
             */
            @Test
        	public void testSearchByParms() {
        		//search according to City name
        		ArrayList<City> searchResult=SearchCatalog.SearchCity(cityName, cityDescription, placeName, placeDescription, useUnpublished);
        		assertEquals(expectedC1,searchResult.contains(c1));
        		assertEquals(expectedC2,searchResult.contains(c2));
        		assertEquals(expectedC3,searchResult.contains(c3));
        		assertEquals(expectedC4,searchResult.contains(c4));
        	}
            
           
            /**
             * Close database connection
             */
            @AfterClass 
            public static void closeDatabaseConnection() {
            	//delete cities
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
        		p7.deleteFromDatabase();
        		p8.deleteFromDatabase();
        		p9.deleteFromDatabase();
        		p10.deleteFromDatabase();
        		//close connection
        		Database.closeConnection();
          }
 
}