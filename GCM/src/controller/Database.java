package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;

import javafx.scene.chart.PieChart.Data;
import objectClasses.City;
import objectClasses.CityDataVersion;
import objectClasses.Customer;
import objectClasses.Employee;
import objectClasses.Location;
import objectClasses.Map;
import objectClasses.MapSight;
import objectClasses.OneTimePurchase;
import objectClasses.PlaceOfInterest;
import objectClasses.PlaceOfInterestSight;
import objectClasses.Route;
import objectClasses.RouteSight;
import objectClasses.RouteStop;
import objectClasses.Statistic;
import objectClasses.Subscription;
import objectClasses.Employee.Role;
import objectClasses.PlaceOfInterest.PlaceType;

import java.security.MessageDigest;

//import javax.xml.bind.DatatypeConverter;

/**
 * @author tal20
 *
 */
public class Database {
	static private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	// update USER, PASS and DB URL according to credentials provided by the
	// website:
	// https://remotemysql.com/
	// in future get those hardcoded string into separated config file.
	static private final String DB = "PrtTXnuWoq";
	static private final String DB_URL = "jdbc:mysql://remotemysql.com/" + DB + "?useSSL=NO";
	static private final String USER = "PrtTXnuWoq";
	static private final String PASS = "KOzAI33szl";
	static Connection conn = null;

	/**
	 * @author tal20 This enum maps from integers to their entries on the counter
	 *         table
	 * 
	 */
	public enum Counter {
		PlaceOfInterest(0), User(1), Map(2), Location(3), CityDataVersion(4), Route(5), CityPurchase(6), City(7),
		RouteStop(8), MapSight(9), PlaceOfInterestSight(10), RouteSight(11), Statistic(12);

		private final int value;

		/**
		 * Constructor.
		 * 
		 * @param nv: Integer that hold the value
		 * @return A Counter object.
		 */
		Counter(final int nv) {
			value = nv;
		}

		/**
		 * Translate to int
		 * 
		 * @return Returns the value
		 */
		public int getValue() {
			return value;
		}
	}

	/**
	 * returns all the customers
	 * 
	 * @return List of all the customers
	 */
	public static ArrayList<Customer> getAllCustomers() {
		ArrayList<Integer> ids = searchCustomer(null, null);
		ArrayList<Customer> custs = new ArrayList<Customer>();
		for (int id : ids) {
			Customer c = Database.getCustomerById(id);
			if (c != null)
				custs.add(c);
		}
		return custs;
	}
	
	/**
     * returns all the Cities
     * 
     * @return List of all the cities
     */
    public static ArrayList<City> getAllCities()
    {
        ArrayList<Integer> ids = searchCity(null, null);
        ArrayList<City> cities = new ArrayList<City>();
        for (int id : ids)
        {
            City c = Database.getCityById(id);
            if (c != null)
                cities.add(c);
        }
        return cities;
    }

	/**
	 * @author tal20 This enum maps from full table names and local names.
	 */
	public enum Table {
		PlaceOfInterest("POIs"), Map("Maps"), Route("Routes"), City("Cities"), Customer("Customers"),
		Employee("Employees"), Location("Locations"), RouteStop("RouteStop"), MapSight("MapSights"),
		PlaceOfInterestSight("POISights"), RouteSight("RouteSights"), CityDataVersion("CityDataVersions"),
		Subscription("Subscription"), OneTimePurchase("OneTimePurchase"), Statistic("Statistics");

		private final String url;

		/**
		 * Constructor.
		 * 
		 * @param envUrl
		 */
		Table(final String envUrl) {
			url = envUrl;
		}

		/**
		 * @return the table name
		 */
		public String getValue() {
			return url;
		}
	}

	/**
	 * Create a new database connection.
	 */
	public static void createConnection() {
		try {
			if (conn == null) {
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				System.out.println("connection opening");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * CLose the database connection
	 */
	public static void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
				System.out.println("connection closing");
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * Reset the entire database. Delete all inputs, set counters to 0. Only Tal and
	 * Lior should use this method.
	 * 
	 * @param name of the user
	 * @param pass of the user
	 * @return true if the data base is reset
	 */
	public static boolean resetAll(String name, String pass) {
		try {
			String sql = "SELECT Name FROM Team WHERE Name=? AND Password=?";
			PreparedStatement check = conn.prepareStatement(sql);
			check.setString(1, name);
			check.setString(2, pass);
			ResultSet res = check.executeQuery();
			// check if there is exciting row in table before insert
			if (!res.next())
				return false;
			for (Table table : Table.values()) {
				sql = "DELETE FROM " + table.getValue() + " WHERE TRUE";
				PreparedStatement gt = conn.prepareStatement(sql);
				gt.executeUpdate();
			}

			for (Counter type : Counter.values()) {
				PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=0 WHERE Object=?");
				su.setInt(1, type.getValue());
				su.executeUpdate();
			}
			System.out.println("Finished reset");
			return true;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * initialize the data base
	 * 
	 * @param name of the user permission
	 * @param pass of the user permission
	 */
	public static void initDatabase(String name, String pass) {
		try {
			Database.createConnection();
			// reset
			if (!Database.resetAll(name, pass))
				return;
			// start insert

			// create cities
			// 1
			{
				City c1 = new City("haifa", "The third largest city in Israel. As of 2016, the city is a major seaport "
						+ "located on Israel's Mediterranean coastline in the Bay of Haifa covering 63.7 square kilometres.");
				CityDataVersion cdv = new CityDataVersion(c1, "1.0", 20, 100.9);
				PlaceOfInterest p0 = new PlaceOfInterest(c1.getId(), "University of Haifa",
						PlaceOfInterest.PlaceType.MUSEUM,
						"A public research university on the top of Mount Carmel in Haifa, Israel. "
								+ "The university was founded in 1963 by the mayor of its host city, Abba Hushi,"
								+ " to operate under the academic auspices of the Hebrew University of Jerusalem.",
						false);
				p0.saveToDatabase();
				PlaceOfInterest p1 = new PlaceOfInterest(c1.getId(), "School of Haifa",
						PlaceOfInterest.PlaceType.PUBLIC, "the best shool in the city", false);
				p1.saveToDatabase();
				PlaceOfInterest p2 = new PlaceOfInterest(c1.getId(), "haifa museum of art",
						PlaceOfInterest.PlaceType.MUSEUM, "the biggest meseum in the city", false);
				p2.saveToDatabase();
				PlaceOfInterest p3 = new PlaceOfInterest(c1.getId(), "vivino", PlaceOfInterest.PlaceType.RESTAURANT,
						"Vivino Haifa is located in a magical pine grove in the heart of the city, far from the city’s hustle and bustle. At Vivino Haifa you’ll find a piece of Italian tranquility under the sky in a beautiful courtyard, in our indoor garden or in an interior combining modern design with touches of the rich culture of the boot country.",
						false);
				p3.saveToDatabase();
				PlaceOfInterest p4 = new PlaceOfInterest(c1.getId(), "Hecht Park", PlaceOfInterest.PlaceType.PARK,
						"Hecht Park is the largest stretch of greenery within the urban area of the City of Haifa. Though distinct from its surroundings, it is a continuous layer among the landscape of beaches and municipal open areas stretching between Dado Beach",
						false);
				p4.saveToDatabase();
				PlaceOfInterestSight ps0 = new PlaceOfInterestSight(cdv, p0);
				cdv.addPlaceOfInterestSight(ps0);
				PlaceOfInterestSight ps1 = new PlaceOfInterestSight(cdv, p1);
				cdv.addPlaceOfInterestSight(ps1);
				PlaceOfInterestSight ps2 = new PlaceOfInterestSight(cdv, p2);
				cdv.addPlaceOfInterestSight(ps2);
				PlaceOfInterestSight ps3 = new PlaceOfInterestSight(cdv, p3);
				cdv.addPlaceOfInterestSight(ps3);
				PlaceOfInterestSight ps4 = new PlaceOfInterestSight(cdv, p4);
				cdv.addPlaceOfInterestSight(ps4);
				Map m0 = new Map(c1.getId(), "central city", "first map", "haifa1.png");
				double[] coords0 = { 21.3, 58.7 };
				Location l0 = new Location(m0, p0, coords0);
				m0.addLocation(l0);
				double[] coords1 = { 11.3, 50.2 };
				Location l1 = new Location(m0, p1, coords1);
				m0.addLocation(l1);
				m0.saveToDatabase();
				MapSight ms0 = new MapSight(cdv, m0);
				cdv.addMapSight(ms0);

				Map m1 = new Map(c1.getId(), "Mount Carmel", "second map", "haifa2.png");
				double[] coords2 = { 12.3, 85.7 };
				Location l2 = new Location(m1, p2, coords2);
				m1.addLocation(l2);
				double[] coords3 = { 11.3, 9.2 };
				Location l3 = new Location(m1, p3, coords3);
				m1.addLocation(l3);
				double[] coords4 = { 12.3, 19.2 };
				Location l4 = new Location(m1, p4, coords4);
				m1.addLocation(l4);
				m1.saveToDatabase();
				MapSight ms1 = new MapSight(cdv, m1);
				cdv.addMapSight(ms1);
				Route r = new Route(c1.getId(), "small route");
				RouteStop rstop1 = new RouteStop(r, p0, new Time(1, 25, 0));
				r.addRouteStop(rstop1);
				RouteStop rstop2 = new RouteStop(r, p1, new Time(0, 43, 0));
				r.addRouteStop(rstop2);
				r.saveToDatabase();
				RouteSight rs = new RouteSight(cdv, r, true);
				cdv.addRouteSight(rs);

				Route r1 = new Route(c1.getId(), "big route");
				RouteStop rstop3 = new RouteStop(r1, p2, new Time(1, 12, 0));
				r1.addRouteStop(rstop3);
				RouteStop rstop4 = new RouteStop(r1, p3, new Time(0, 23, 0));
				r1.addRouteStop(rstop4);
				RouteStop rstop5 = new RouteStop(r1, p4, new Time(0, 23, 0));
				r1.addRouteStop(rstop5);
				r1.saveToDatabase();
				RouteSight rs1 = new RouteSight(cdv, r1, true);
				cdv.addRouteSight(rs1);

				c1.addPublishedCityDataVersion(cdv);
				c1.saveToDatabase();
			}
			// 2
			{
				City c1 = new City("Tel aviv",
						"Tel Aviv-Yafo, is the second most populous city in Israel—after Jerusalem—and the most populous city in the conurbation of Gush Dan, Israel's largest metropolitan area. Located on the country's Mediterranean coastline and with a population of 443,939, it is the economic and technological center of the country.");
				CityDataVersion cdv = new CityDataVersion(c1, "1.0", 15, 100.9);
				PlaceOfInterest p0 = new PlaceOfInterest(c1.getId(), "tel aviv carmel market",
						PlaceOfInterest.PlaceType.PUBLIC,
						"The Carmel Market (the Shuk Hacarmel) is the largest market, or shuk, in Tel Aviv. A vibrant marketplace where traders sell everything from clothing to spices, and fruit to electronics, visiting the Carmel Market is a fascinating thing to do in Tel Aviv.",
						false);
				p0.saveToDatabase();
				PlaceOfInterest p1 = new PlaceOfInterest(c1.getId(), "tel aviv port", PlaceOfInterest.PlaceType.PUBLIC,
						"Namal Tel Aviv, the Tel Aviv Port has recently been restored and is now one of the hottest places in town. During the day, the cafes and stores at Namal Tel Aviv (the Tel Aviv Port) the host some of the city’s richest and trendiest",
						false);
				p1.saveToDatabase();
				PlaceOfInterest p2 = new PlaceOfInterest(c1.getId(), "Tel Aviv Museum of Art",
						PlaceOfInterest.PlaceType.MUSEUM,
						"Tel Aviv Museum of Art is a municipal museum, one of Israel's leading artistic and cultural institutions. The museum comprises various departments",
						false);
				p2.saveToDatabase();
				PlaceOfInterest p3 = new PlaceOfInterest(c1.getId(), "tel aviv university",
						PlaceOfInterest.PlaceType.PUBLIC,
						"Tel Aviv University came into being through the dedicated efforts of visionaries who foresaw the need for an additional university in Israel’s rapidly growing central region. In the 1930s, the idea was promoted by then mayor of Tel Aviv, Meir Dizengoff",
						false);
				p3.saveToDatabase();
				PlaceOfInterest p4 = new PlaceOfInterest(c1.getId(), "Neve Tzedek", PlaceOfInterest.PlaceType.PUBLIC,
						"The charming neighborhood of Neve Tzedek is one of the oldest in the city, filled with quaint buildings showcasing both the old Bauhaus buildings in their original form mixed with newer structures and homes.",
						false);
				p4.saveToDatabase();
				PlaceOfInterestSight ps0 = new PlaceOfInterestSight(cdv, p0);
				cdv.addPlaceOfInterestSight(ps0);
				PlaceOfInterestSight ps1 = new PlaceOfInterestSight(cdv, p1);
				cdv.addPlaceOfInterestSight(ps1);
				PlaceOfInterestSight ps2 = new PlaceOfInterestSight(cdv, p2);
				cdv.addPlaceOfInterestSight(ps2);
				PlaceOfInterestSight ps3 = new PlaceOfInterestSight(cdv, p3);
				cdv.addPlaceOfInterestSight(ps3);
				PlaceOfInterestSight ps4 = new PlaceOfInterestSight(cdv, p4);
				cdv.addPlaceOfInterestSight(ps4);
				Map m0 = new Map(c1.getId(), "north city", "first map", "tel_aviv1.png");
				double[] coords0 = { 23.3, 54.7 };
				Location l0 = new Location(m0, p0, coords0);
				m0.addLocation(l0);
				double[] coords1 = { 17.3, 52.2 };
				Location l1 = new Location(m0, p1, coords1);
				m0.addLocation(l1);
				m0.saveToDatabase();
				MapSight ms0 = new MapSight(cdv, m0);
				cdv.addMapSight(ms0);

				Map m1 = new Map(c1.getId(), "downtown", "second map", "tel_aviv2.png");
				double[] coords2 = { 17.3, 35.7 };
				Location l2 = new Location(m1, p2, coords2);
				m1.addLocation(l2);
				double[] coords3 = { 12.3, 6.2 };
				Location l3 = new Location(m1, p3, coords3);
				m1.addLocation(l3);
				double[] coords4 = { 17.3, 13.2 };
				Location l4 = new Location(m1, p4, coords4);
				m1.addLocation(l4);
				m1.saveToDatabase();
				MapSight ms1 = new MapSight(cdv, m1);
				cdv.addMapSight(ms1);
				Route r = new Route(c1.getId(), "small route");
				RouteStop rstop1 = new RouteStop(r, p0, new Time(1, 25, 0));
				r.addRouteStop(rstop1);
				RouteStop rstop2 = new RouteStop(r, p1, new Time(0, 43, 0));
				r.addRouteStop(rstop2);
				r.saveToDatabase();
				RouteSight rs = new RouteSight(cdv, r, true);
				cdv.addRouteSight(rs);

				Route r1 = new Route(c1.getId(), "big route");
				RouteStop rstop3 = new RouteStop(r1, p2, new Time(1, 12, 0));
				r1.addRouteStop(rstop3);
				RouteStop rstop4 = new RouteStop(r1, p3, new Time(0, 23, 0));
				r1.addRouteStop(rstop4);
				RouteStop rstop5 = new RouteStop(r1, p4, new Time(0, 23, 0));
				r1.addRouteStop(rstop5);
				r1.saveToDatabase();
				RouteSight rs1 = new RouteSight(cdv, r1, true);
				cdv.addRouteSight(rs1);

				c1.addPublishedCityDataVersion(cdv);
				c1.saveToDatabase();
			}
			// 3

			City c1 = new City("Jerusalem",
					"Jerusalem is a city in the Middle East, located on a plateau in the Judaean Mountains between the Mediterranean and the Dead Sea. It is one of the oldest cities in the world, and is considered holy to the three major Abrahamic religions—Judaism, Christianity, and Islam.");
			CityDataVersion cdv = new CityDataVersion(c1, "1.0", 10, 122.9);
			PlaceOfInterest p0 = new PlaceOfInterest(c1.getId(), "Western Wall", PlaceOfInterest.PlaceType.HISTORICAL,
					"The Western Wall, or “Wailing Wall”, is the most religious site in the world for the Jewish people. Located in the Old City of Jerusalem, it is the western support wall of the Temple Mount.",
					false);
			p0.saveToDatabase();
			PlaceOfInterest p1 = new PlaceOfInterest(c1.getId(), "Machane Yehuda Market",
					PlaceOfInterest.PlaceType.PUBLIC,
					"The Machane Yehuda Market, or shuk, is the largest market in Jerusalem with over 250 vendors selling everything from fruit and vegetables to specialty foods, and clothing to Judaica. The market is the main ‘traditional’ marketplace of Jerusalem contrasting with the supermarkets that are found across this city, just as any other advanced city",
					false);
			p1.saveToDatabase();
			PlaceOfInterest p2 = new PlaceOfInterest(c1.getId(), "Tower od David", PlaceOfInterest.PlaceType.HISTORICAL,
					"The Tower of David also known as the Jerusalem Citadel, is an ancient citadel located near the Jaffa Gate entrance to western edge of the Old City of Jerusalem.",
					false);
			p2.saveToDatabase();
			PlaceOfInterest p3 = new PlaceOfInterest(c1.getId(), "Jewish Quarter", PlaceOfInterest.PlaceType.HISTORICAL,
					"The Jewish Quarter of Jerusalem’s Old City is one of the four quarters of the walled city. The quarter is home to around 2,000 people and covers about 0.1 square kilometers. It is also the location of many tens of synagogues and yeshivas (places of the study of Jewish texts) and has been almost continually home to Jews since the century 8 BCE.",
					false);
			p3.saveToDatabase();
			PlaceOfInterest p4 = new PlaceOfInterest(c1.getId(), "Old City of Jerusalem",
					PlaceOfInterest.PlaceType.PUBLIC,
					"The Old City of Jerusalem is one of the most intense places on Earth! At the heart of the Jewish, Islamic, and Christian religions, this walled one-kilometer area in the center of Jerusalem is beyond words and cannot be missed.",
					false);
			p4.saveToDatabase();
			PlaceOfInterestSight ps0 = new PlaceOfInterestSight(cdv, p0);
			cdv.addPlaceOfInterestSight(ps0);
			PlaceOfInterestSight ps1 = new PlaceOfInterestSight(cdv, p1);
			cdv.addPlaceOfInterestSight(ps1);
			PlaceOfInterestSight ps2 = new PlaceOfInterestSight(cdv, p2);
			cdv.addPlaceOfInterestSight(ps2);
			PlaceOfInterestSight ps3 = new PlaceOfInterestSight(cdv, p3);
			cdv.addPlaceOfInterestSight(ps3);
			PlaceOfInterestSight ps4 = new PlaceOfInterestSight(cdv, p4);
			cdv.addPlaceOfInterestSight(ps4);
			Map m0 = new Map(c1.getId(), "west city", "first map", "jerusalem1.png");
			double[] coords0 = { 33.3, 45.7 };
			Location l0 = new Location(m0, p0, coords0);
			m0.addLocation(l0);
			double[] coords1 = { 17.3, 28.2 };
			Location l1 = new Location(m0, p1, coords1);
			m0.addLocation(l1);
			m0.saveToDatabase();
			MapSight ms0 = new MapSight(cdv, m0);
			cdv.addMapSight(ms0);

			Map m1 = new Map(c1.getId(), "east city", "second map", "jerusalem2.png");
			double[] coords2 = { 23.3, 49.7 };
			Location l2 = new Location(m1, p2, coords2);
			m1.addLocation(l2);
			double[] coords3 = { 12.3, 16.2 };
			Location l3 = new Location(m1, p3, coords3);
			m1.addLocation(l3);
			double[] coords4 = { 72.3, 13.2 };
			Location l4 = new Location(m1, p4, coords4);
			m1.addLocation(l4);
			m1.saveToDatabase();
			MapSight ms1 = new MapSight(cdv, m1);
			cdv.addMapSight(ms1);
			Route r = new Route(c1.getId(), "small route");
			RouteStop rstop1 = new RouteStop(r, p0, new Time(1, 25, 0));
			r.addRouteStop(rstop1);
			RouteStop rstop2 = new RouteStop(r, p1, new Time(0, 43, 0));
			r.addRouteStop(rstop2);
			r.saveToDatabase();
			RouteSight rs = new RouteSight(cdv, r, true);
			cdv.addRouteSight(rs);

			Route r1 = new Route(c1.getId(), "big route");
			RouteStop rstop3 = new RouteStop(r1, p2, new Time(1, 12, 0));
			r1.addRouteStop(rstop3);
			RouteStop rstop4 = new RouteStop(r1, p3, new Time(0, 23, 0));
			r1.addRouteStop(rstop4);
			RouteStop rstop5 = new RouteStop(r1, p4, new Time(0, 23, 0));
			r1.addRouteStop(rstop5);
			r1.saveToDatabase();
			RouteSight rs1 = new RouteSight(cdv, r1, true);
			cdv.addRouteSight(rs1);

			c1.addPublishedCityDataVersion(cdv);
			c1.saveToDatabase();

			// create Users
			// 1
			{
				Employee e = new Employee("Lior", "Lior_strong1!", "lior@gmail.com", "lior", "wiessman", "0523322726",
						Employee.Role.CEO);
				e.saveToDatabase();
				Customer cust = new Customer("yosi11", "LDCyosiiii!", "yosi@gmail.com", "yosi", "ben asser",
						"0523322123", "5495681338665894", "07/24", "896");
				Subscription sub = new Subscription(cust, c1, new Date(119, 8, 6), 201.8, 199.9, new Date(119, 10, 8));
				cust.addSubscription(sub);

				OneTimePurchase otp = new OneTimePurchase(cust, c1, new Date(119, 8, 6), 20, 19);
				otp.updateToWasDownload();
				cust.addOneTimePurchase(otp);
				cust.saveToDatabase();
			}
			// 2
			{
				Employee e = new Employee("adiel", "adiel1", "statman.adiel@gmail.com", "lior", "statman", "0525952726",
						Employee.Role.REGULAR);
				e.saveToDatabase();
				Customer cust = new Customer("dan", "masterDan%", "dannyf.post@gmail.com", "dan", "feldman",
						"0523325686", "5495655558665894", "01/23", "354");
				Subscription sub = new Subscription(cust, c1, new Date(119, 7, 6), 201.8, 199.9, new Date(119, 9, 8));
				cust.addSubscription(sub);

				OneTimePurchase otp = new OneTimePurchase(cust, c1, new Date(119, 8, 6), 19, 18);
				otp.updateToWasDownload();
				cust.addOneTimePurchase(otp);
				cust.saveToDatabase();
			}

			// 3
			{
				Employee e = new Employee("ben", "benbon&ALAA", "bengordoncshaifa@gmail.com", "ben", "musa",
						"0508322726", Employee.Role.MANAGER);
				e.saveToDatabase();
				Customer cust = new Customer("gadi", "gadiHAVIV!@", "gadi@gmail.com", "gadi", "landau", "0524867726",
						"5495123458665894", "01/25", "891");
				Subscription sub = new Subscription(cust, c1, new Date(119, 7, 6), 53.2, 50.9, new Date(119, 9, 8));
				cust.addSubscription(sub);

				OneTimePurchase otp = new OneTimePurchase(cust, c1, new Date(119, 8, 6), 9, 8);
				otp.updateToWasDownload();
				cust.addOneTimePurchase(otp);
				cust.saveToDatabase();
			}
			// 4
			{
				Employee e = new Employee("sigal", "sigalIsNoob!", "yonatan.sigal11@gmail.com", "yonatan", "sigal",
						"0508322126", Employee.Role.REGULAR);
				e.saveToDatabase();
				Customer cust = new Customer("tomer", "IAmTomer*", "1234tomer@gmail.com", "tomer", "kohavi",
						"0524867726", "5495123458612894", "02/25", "821");
				Subscription sub = new Subscription(cust, c1, new Date(119, 7, 6), 63.2, 50.9, new Date(119, 9, 8));
				cust.addSubscription(sub);

				OneTimePurchase otp = new OneTimePurchase(cust, c1, new Date(119, 7, 6), 19, 8);
				otp.updateToWasDownload();
				cust.addOneTimePurchase(otp);
				cust.saveToDatabase();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Database.closeConnection();
		}
	}

	// generate ID's

	/**
	 * @param type: the table find ID
	 * @return the ID of this table
	 */
	private static int generateId(int type) {
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, type);
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, type);
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * @return the ID of the next user
	 */
	public static int generateIdUser() {
		return generateId(Counter.User.getValue());
	}

	/**
	 * @return the ID of the next POI
	 */
	public static int generateIdPlaceOfInterest() {// first example
		return generateId(Counter.PlaceOfInterest.getValue());

	}

	/**
	 * @return the ID of the next map
	 */
	public static int generateIdMap() {
		return generateId(Counter.Map.getValue());
	}

	/**
	 * @return the ID of the next location
	 */
	public static int generateIdLocation() {
		return generateId(Counter.Location.getValue());
	}

	/**
	 * @return the ID of the next city data version
	 */
	public static int generateIdCityDataVersion() {
		return generateId(Counter.CityDataVersion.getValue());
	}

	/**
	 * @return the ID of the next route
	 */
	public static int generateIdRoute() {
		return generateId(Counter.Route.getValue());
	}

	/**
	 * @return the ID of the next city purchase
	 */
	public static int generateIdCityPurchase() {
		return generateId(Counter.CityPurchase.getValue());
	}

	/**
	 * @return the ID of the next city
	 */
	public static int generateIdCity() {
		return generateId(Counter.City.getValue());
	}

	/**
	 * @return the ID of the next route stop
	 */
	public static int generateIdRouteStop() {
		return generateId(Counter.RouteStop.getValue());
	}

	/**
	 * @return the ID of the next map sight
	 */
	public static int generateIdMapSight() {
		return generateId(Counter.MapSight.getValue());
	}

	/**
	 * @return the ID of the next POI sight
	 */
	public static int generateIdPlaceOfInterestSight() {
		return generateId(Counter.PlaceOfInterestSight.getValue());
	}

	/**
	 * @return the ID of the next route sight
	 */
	public static int generateIdRouteSight() {
		return generateId(Counter.RouteSight.getValue());
	}

	/**
	 * @return the ID of the next statistic
	 */
	public static int generateIdStatistic() {
		return generateId(Counter.Statistic.getValue());
	}

	/**
	 * @param table: the table to search in
	 * @param id: the id to search
	 * @return true if exists, false else.
	 */
	private static boolean exist(String table, int id) {
		try {
			String sql = "SELECT ID FROM " + table + " WHERE ID=?";
			PreparedStatement check = conn.prepareStatement(sql);
			check.setInt(1, id);
			ResultSet res = check.executeQuery();
			// check if there is exciting row in table before insert
			if (!res.next())
				return false;
			return true;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a POI with this ID.
	 */
	private static boolean existPlaceOfInterest(int id) {
		return exist(Table.PlaceOfInterest.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a map with this ID.
	 */
	private static boolean existMap(int id) {
		return exist(Table.Map.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a route with this ID.
	 */
	private static boolean existRoute(int id) {
		return exist(Table.Route.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a city with this ID.
	 */
	private static boolean existCity(int id) {
		return exist(Table.City.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a customer with this ID.
	 */
	private static boolean existCustomer(int id) {
		return exist(Table.Customer.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a employee with this ID.
	 */
	private static boolean existEmployee(int id) {
		return exist(Table.Employee.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a location with this ID.
	 */
	private static boolean existLocation(int id) {
		return exist(Table.Location.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a route stop with this ID.
	 */
	private static boolean existRouteStop(int id) {
		return exist(Table.RouteStop.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a map sight with this ID.
	 */
	private static boolean existMapSight(int id) {
		return exist(Table.MapSight.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a POI sight with this ID.
	 */
	private static boolean existPlaceOfInterestSight(int id) {
		return exist(Table.PlaceOfInterestSight.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a route sight with this ID.
	 */
	private static boolean existRouteSight(int id) {
		return exist(Table.RouteSight.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a city data version with this ID.
	 */
	private static boolean existCityDataVersion(int id) {
		return exist(Table.CityDataVersion.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a subscription with this ID.
	 */
	private static boolean existSubscription(int id) {
		return exist(Table.Subscription.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a one time purchase with this ID.
	 */
	private static boolean existOneTimePurchase(int id) {
		return exist(Table.OneTimePurchase.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a statistic with this ID.
	 */
	private static boolean existStatistic(int id) {
		return exist(Table.Statistic.getValue(), id);
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the place of interest we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean savePlaceOfInterest(PlaceOfInterest p) {
		try {
			if (existPlaceOfInterest(p.getId())) {
				String sql = "UPDATE " + Table.PlaceOfInterest.getValue()
						+ " SET CityID=?, Name=?, Type=?, Description=?, ATD=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getCityId());
				su.setString(2, p.getName());
				su.setInt(3, p.getType().getValue());
				su.setString(4, p.getPlaceDescription());
				su.setBoolean(5, p.isAccessibilityToDisabled());
				su.setInt(6, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.PlaceOfInterest.getValue()
						+ " (ID,CityID, Name, Type, Description, ATD) VALUES (?,?, ?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getCityId());
				su.setString(3, p.getName());
				su.setInt(4, p.getType().getValue());
				su.setString(5, p.getPlaceDescription());
				su.setBoolean(6, p.isAccessibilityToDisabled());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the map we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean saveMap(Map p) {
		try {
			if (existMap(p.getId())) {
				String sql = "UPDATE " + Table.Map.getValue() + " SET Name=?, Info=?, imgURL=?, CityID=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setString(1, p.getName());
				su.setString(2, p.getInfo());
				su.setString(3, p.getImgURL());
				su.setInt(4, p.getCityId());
				su.setInt(5, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.Map.getValue()
						+ " (ID,Name, Info, imgURL, CityID) VALUES (?,?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setString(2, p.getName());
				su.setString(3, p.getInfo());
				su.setString(4, p.getImgURL());
				su.setInt(5, p.getCityId());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the route we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean saveRoute(Route p) {
		try {
			if (existRoute(p.getId())) {
				String sql = "UPDATE " + Table.Route.getValue() + " SET Info=?, NumStops=?, CityID=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setString(1, p.getInfo());
				su.setInt(2, p.getNumStops());
				su.setInt(3, p.getCityId());
				su.setInt(4, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.Route.getValue()
						+ " (ID, Info, NumStops, CityID) VALUES (?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setString(2, p.getInfo());
				su.setInt(3, p.getNumStops());
				su.setInt(4, p.getCityId());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the city we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean saveCity(City p) {
		try {
			if (existCity(p.getId())) {
				String sql = "UPDATE " + Table.City.getValue() + " SET Name=?, Description=?, VersionID=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setString(1, p.getCityName());
				su.setString(2, p.getCityDescription());
				su.setInt(3, p.getPublishedVersionId() == null ? -1 : p.getPublishedVersionId());
				su.setInt(4, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.City.getValue()
						+ " (ID,Name, Description, VersionID) VALUES (?,?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setString(2, p.getCityName());
				su.setString(3, p.getCityDescription());
				su.setInt(4, p.getPublishedVersionId() == null ? -1 : p.getPublishedVersionId());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the customer we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean saveCustomer(Customer p) {
		try {
			if (existCustomer(p.getId())) {
				String sql = "UPDATE " + Table.Customer.getValue()
						+ " SET Username=?, Password=?, Email=?, FirstName=?, LastName=?,"
						+ " PhoneNumber=?, CardNum=?, CVC=?, Exp=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setString(1, p.getUserName());
				su.setString(2, p.getPassword());
				su.setString(3, p.getEmail());
				su.setString(4, p.getFirstName());
				su.setString(5, p.getLastName());
				su.setString(6, p.getPhoneNumber());
				su.setString(7, p.getCreditCardNum());
				su.setString(8, p.getCvc());
				su.setString(9, p.getCreditCardExpires());
				su.setInt(10, p.getId());
				su.executeUpdate();
				return true;
			}
//			else if(!searchCustomer(p.getUserName(), null).isEmpty())
//			{
//				System.out.println("already saved");
//				return false;
//			}
			else {
				String sql = "INSERT INTO " + Table.Customer.getValue() + " "
						+ "(ID,Username, Password, Email, FirstName, LastName, PhoneNumber, CardNum, CVC, Exp) VALUES "
						+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setString(2, p.getUserName());
				su.setString(3, p.getPassword());
				su.setString(4, p.getEmail());
				su.setString(5, p.getFirstName());
				su.setString(6, p.getLastName());
				su.setString(7, p.getPhoneNumber());
				su.setString(8, p.getCreditCardNum());
				su.setString(9, p.getCvc());
				su.setString(10, p.getCreditCardExpires());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the employee we want save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean saveEmployee(Employee p) {
		try {
			if (existEmployee(p.getId())) {
				String sql = "UPDATE " + Table.Employee.getValue() + " Username=?,"
						+ " Password=?, Email=?, FirstName=?, LastName=?, PhoneNumber=?, Role=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setString(1, p.getUserName());
				su.setString(2, p.getPassword());
				su.setString(3, p.getEmail());
				su.setString(4, p.getFirstName());
				su.setString(5, p.getLastName());
				su.setString(6, p.getPhoneNumber());
				su.setInt(7, p.getRole().getValue());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.Employee.getValue()
						+ " (ID,Username, Password, Email, FirstName, LastName, PhoneNumber, Role)"
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setString(2, p.getUserName());
				su.setString(3, p.getPassword());
				su.setString(4, p.getEmail());
				su.setString(5, p.getFirstName());
				su.setString(6, p.getLastName());
				su.setString(7, p.getPhoneNumber());
				su.setInt(8, p.getRole().getValue());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the location we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean _saveLocation(Location p) // friend to Map
	{
		try {
			if (existLocation(p.getId())) {
				String sql = "UPDATE " + Table.Location.getValue() + " SET MapID=?, POIID=?, x=?, y=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getMapId());
				su.setInt(2, p.getPlaceOfInterestId());
				su.setDouble(3, p.getCoordinates()[0]);
				su.setDouble(4, p.getCoordinates()[1]);
				su.setInt(5, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.Location.getValue()
						+ " (ID, MapID, POIID, x, y) VALUES (?, ?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getMapId());
				su.setInt(3, p.getPlaceOfInterestId());
				su.setDouble(4, p.getCoordinates()[0]);
				su.setDouble(5, p.getCoordinates()[1]);
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the route stop we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean _saveRouteStop(RouteStop p)// friend to Route
	{
		try {
			if (existRouteStop(p.getId())) {
				String sql = "UPDATE " + Table.RouteStop.getValue()
						+ " SET RouteID=?, PlaceID=?, NumStops=?, Time=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getRouteId());
				su.setInt(2, p.getPlaceId());
				su.setInt(3, p.getNumStop());
				su.setTime(4, p.getRecommendedTime());
				su.setInt(5, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.RouteStop.getValue()
						+ " (ID,RouteID, PlaceID, NumStops, Time) VALUES (?, ?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getRouteId());
				su.setInt(3, p.getPlaceId());
				su.setInt(4, p.getNumStop());
				su.setTime(5, p.getRecommendedTime());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the map sight we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean _saveMapSight(MapSight p) // friend to MapSight
	{
		try {
			if (existMapSight(p.getId())) {
				String sql = "UPDATE " + Table.MapSight.getValue() + " SET MapID=?, CityDataVersionID=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getMapId());
				su.setInt(2, p.getCityDataVersionId());
				su.setInt(3, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.MapSight.getValue()
						+ " (ID,MapID, CityDataVersionID) VALUES (?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getMapId());
				su.setInt(3, p.getCityDataVersionId());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p place of interest sight we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean _savePlaceOfInterestSight(PlaceOfInterestSight p)// friend to CityDataVersion
	{
		try {
			if (existPlaceOfInterestSight(p.getId())) {
				String sql = "UPDATE " + Table.PlaceOfInterestSight.getValue()
						+ " SET CityDataVersions=?, POIID=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getCityDataVersionId());
				su.setInt(2, p.getPlaceOfInterestId());
				su.setInt(3, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.PlaceOfInterestSight.getValue()
						+ " (ID,CityDataVersions, POIID) VALUES (?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getCityDataVersionId());
				su.setInt(3, p.getPlaceOfInterestId());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the route sight we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean _saveRouteSight(RouteSight p)// friend to CityDataVersion
	{
		try {
			if (existRouteSight(p.getId())) {
				String sql = "UPDATE " + Table.RouteSight.getValue()
						+ " SET CityDataVersions=?, RouteID=?, IsFavorite=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getCityDataVersionId());
				su.setInt(2, p.getRouteId());
				su.setBoolean(3, p.getIsFavorite());
				su.setInt(4, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.RouteSight.getValue()
						+ " (ID,CityDataVersions, RouteID, IsFavorite) VALUES (?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getCityDataVersionId());
				su.setInt(3, p.getRouteId());
				su.setBoolean(4, p.getIsFavorite());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p city data version we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean _saveCityDataVersion(CityDataVersion p)// friend to City
	{
		try {
			if (existCityDataVersion(p.getId())) {
				String sql = "UPDATE " + Table.CityDataVersion.getValue()
						+ " SET CityID=?, VersionName=?, PriceOneTime=?, PricePeriod=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getCityId());
				su.setString(2, p.getVersionName());
				su.setDouble(3, p.getPriceOneTime());
				su.setDouble(4, p.getPricePeriod());
				su.setInt(5, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.CityDataVersion.getValue()
						+ " (ID,CityID, VersionName, PriceOneTime, PricePeriod) VALUES (?, ?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getCityId());
				su.setString(3, p.getVersionName());
				su.setDouble(4, p.getPriceOneTime());
				su.setDouble(5, p.getPricePeriod());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the subscription we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean _saveSubscription(Subscription p) // friend to Customer
	{
		try {
			if (existSubscription(p.getId())) {
				String sql = "UPDATE " + Table.Subscription.getValue()
						+ " SET CityID=?, UserID=?, PurchaseDate=?, FullPrice=?, PricePayed=?, ExpDate=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getCityId());
				su.setInt(2, p.getUserId());
				su.setDate(3, (Date) p.getPurchaseDate()); // fix here - RON
				su.setDouble(4, p.getFullPrice());
				su.setDouble(5, p.getPricePayed());
				su.setDate(6, (Date) p.getExpirationDate()); // fix here - RON
				su.setInt(7, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.Subscription.getValue()
						+ " (ID,CityID, UserID, PurchaseDate, FullPrice, PricePayed, ExpDate) VALUES (?,?,?, ?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getCityId());
				su.setInt(3, p.getUserId());
				su.setDate(4, (Date) p.getPurchaseDate()); // fix here - RON
				su.setDouble(5, p.getFullPrice());
				su.setDouble(6, p.getPricePayed());
				su.setDate(7, (Date) p.getExpirationDate()); // fix here - RON
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the one time purchase we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean _saveOneTimePurchase(OneTimePurchase p) // friend to Customer
	{
		try {
			if (existOneTimePurchase(p.getId())) {
				String sql = "UPDATE " + Table.OneTimePurchase.getValue()
						+ " SET CityID=?, UserID=?, PurchaseDate=?, FullPrice=?, PricePayed=?, WasDownloaded=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getCityId());
				su.setInt(2, p.getUserId());
				su.setDate(3, (Date) p.getPurchaseDate()); // fix here - RON
				su.setDouble(4, p.getFullPrice());
				su.setDouble(5, p.getPricePayed());
				su.setBoolean(6, p.getWasDownload());
				su.setInt(7, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.OneTimePurchase.getValue()
						+ " (ID,CityID, UserID, PurchaseDate, FullPrice, PricePayed, WasDownloaded) VALUES (?,?, ?, ?, ?, ?,?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getCityId());
				su.setInt(3, p.getUserId());
				su.setDate(4, (Date) p.getPurchaseDate()); // fix here - RON
				su.setDouble(5, p.getFullPrice());
				su.setDouble(6, p.getPricePayed());
				su.setBoolean(7, p.getWasDownload());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the statistic we want to save
	 * @return true if an updated was made. false for new element.
	 */
	public static boolean _saveStatistic(Statistic p) {
		try {
			if (existStatistic(p.getId())) {
				String sql = "UPDATE " + Table.Statistic.getValue()
						+ " SET CityID=?, Date=?, NOTP=?, NS=?, NR=?, NV=?, NSD=?, NVP=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getCityId());
				su.setDate(2, (Date) p.getDate());
				su.setInt(3, p.getNumOneTimePurchases());
				su.setInt(4, p.getNumSubscriptions());
				su.setInt(5, p.getNumSubscriptionsRenewal());
				su.setInt(6, p.getNumVisited());
				su.setInt(7, p.getNumSubDownloads());
				su.setBoolean(8, p.isNewVersionPublished());
				su.setInt(9, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.Statistic.getValue()
						+ " (ID, CityID, Date, NOTP, NS, NSR, NV, NSD, NVP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getCityId());
				su.setDate(3, (Date) p.getDate());
				su.setInt(4, p.getNumOneTimePurchases()); // fix here - RON
				su.setInt(5, p.getNumSubscriptions());
				su.setInt(6, p.getNumSubscriptionsRenewal());
				su.setInt(7, p.getNumVisited());
				su.setInt(8, p.getNumSubDownloads());
				su.setBoolean(9, p.isNewVersionPublished());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param table: the table to search in
	 * @param id: the id to delete
	 * @return true if deleted, false else.
	 */
	private static boolean delete(String table, int id) {
		try {
			String sql = "DELETE FROM " + table + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, id);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean deletePlaceOfInterest(int id) {
		return delete(Table.PlaceOfInterest.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean deleteMap(int id) {
		return delete(Table.Map.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean deleteRoute(int id) {
		return delete(Table.Route.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean deleteCity(int id) {
		return delete(Table.City.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean deleteCustomer(int id) {
		return delete(Table.Customer.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean deleteEmployee(int id) {
		return delete(Table.Employee.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean _deleteLocation(int id) {
		return delete(Table.Location.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean _deleteRouteStop(int id) {
		return delete(Table.RouteStop.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean _deleteMapSight(int id) {
		return delete(Table.MapSight.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean _deletePlaceOfInterestSight(int id) {
		return delete(Table.PlaceOfInterestSight.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean _deleteRouteSight(int id) {
		return delete(Table.RouteSight.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean _deleteCityDataVersion(int id) {
		return delete(Table.CityDataVersion.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean _deleteSubscription(int id) {
		return delete(Table.Subscription.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean _deleteOneTimePurchase(int id) {
		return delete(Table.OneTimePurchase.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 */
	public static boolean _deleteStatistic(int id) {
		return delete(Table.Statistic.getValue(), id);
	}

	/**
	 * @param gt: A finished SQL query to run.
	 * @return returns the list of the results.
	 */
	private static ArrayList<Integer> queryToList(PreparedStatement gt) {
		try {
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next())
				IDs.add(res.getInt("ID"));
			return IDs;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it. When searching by
	 * description, we look for a POI such that every word from the query
	 * description is a substring of the POI description.
	 * 
	 * @param placeName        the name of the place we want to search
	 * @param placeDescription the place description we want to search
	 * @param cityId           the id of the city we want to search
	 * @return the result list.
	 */
	public static ArrayList<Integer> searchPlaceOfInterest(String placeName, String placeDescription, Integer cityId) {
		try {
			int counter = 1;
			String[] words = { "" };
			if (placeDescription != null)
				words = placeDescription.split(" ");
			int len = words.length;
			String sql = "SELECT ID FROM " + Table.PlaceOfInterest.getValue() + " WHERE ";
			if (placeName != null)
				sql += "Name=? AND ";
			if (placeDescription != null)
				for (int i = 0; i < len; i++)
					sql += "(Description LIKE ?) AND ";
			if (cityId != null)
				sql += "CityID=? AND ";
			sql = sql.substring(0, sql.length() - 4);

			PreparedStatement gt = conn.prepareStatement(sql);
			if (placeName != null)
				gt.setString(counter++, placeName);

			if (placeDescription != null)
				for (int i = 0; i < len; i++)
					gt.setString(counter++, "%" + words[i] + "%");

			if (cityId != null)
				gt.setInt(counter++, cityId);

			return queryToList(gt);
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityId the id of the city we want to search
	 * @param name   the name we want to search
	 * @param info   the info data we want to search
	 * @param imgURL the image url we want to search
	 * @return : the result list.
	 */
	public static ArrayList<Integer> searchMap(Integer cityId, String name, String info, String imgURL) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.Map.getValue() + " WHERE ";
			if (cityId != null)
				sql += "CityID=? AND ";
			if (name != null)
				sql += "Name=? AND ";
			if (info != null)
				sql += "Info=? AND ";
			if (imgURL != null)
				sql += "imgURL=? AND ";
			sql = sql.substring(0, sql.length() - 4);
			PreparedStatement gt = conn.prepareStatement(sql);
			if (cityId != null)
				gt.setInt(counter++, cityId);

			if (name != null)
				gt.setString(counter++, name);

			if (info != null)
				gt.setString(counter++, info);

			if (imgURL != null)
				gt.setString(counter++, imgURL);

			return queryToList(gt);
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityId the id of the city we want to search
	 * @param info   the info data we want to search
	 * @return the result list.
	 */
	public static ArrayList<Integer> searchRoute(Integer cityId, String info) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.Route.getValue() + " WHERE ";
			if (cityId != null)
				sql += "CityID=? AND ";
			if (info != null)
				sql += "Info=? AND ";
			sql = sql.substring(0, sql.length() - 4);
			PreparedStatement gt = conn.prepareStatement(sql);
			if (cityId != null)
				gt.setInt(counter++, cityId);

			if (info != null)
				gt.setString(counter++, info);

			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it. When searching by
	 * description, we look for a city such that every word from the query
	 * description is in the city description.
	 * 
	 * @param cityName        the city name we want to search
	 * @param cityDescription the city description we want to search
	 * @return the result list.
	 */
	public static ArrayList<Integer> searchCity(String cityName, String cityDescription) {
		try {
			int counter = 1;
			String[] words = { "" };
			if (cityDescription != null)
				words = cityDescription.split(" ");
			int len = words.length;
			String sql = "SELECT ID FROM " + Table.City.getValue() + " WHERE ";
			if (cityName != null)
				sql += "Name=? AND ";
			if (cityDescription != null)
				for (int i = 0; i < len; i++)
					sql += "(Description LIKE ?) AND ";

			sql = sql.substring(0, sql.length() - 4);

			if (cityName == null && cityDescription == null)
				sql = "SELECT ID FROM " + Table.City.getValue() + " WHERE True";

			PreparedStatement gt = conn.prepareStatement(sql);
			if (cityName != null)
				gt.setString(counter++, cityName);

			if (cityDescription != null)
				for (int i = 0; i < len; i++)
					gt.setString(counter++, "%" + words[i] + "%");

			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param userName
	 * @param password
	 * @param          table: user type
	 * @return the result list.
	 */
	private static ArrayList<Integer> searchUser(String userName, String password, String table) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + table + " WHERE ";
			if (userName != null)
				sql += "Username=? AND ";
			if (password != null)
				sql += "Password=? AND ";
			sql = sql.substring(0, sql.length() - 4);

			if (userName == null && password == null)
				sql += "SELECT ID FROM " + table + "WHERE True";

			PreparedStatement gt = conn.prepareStatement(sql);
			if (userName != null)
				gt.setString(counter++, userName);

			if (password != null)
				gt.setString(counter++, password);

			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param userName the user name we want to search
	 * @param password the password we want to search
	 * @return the result list.
	 */
	public static ArrayList<Integer> searchCustomer(String userName, String password) {
		return searchUser(userName, password, Table.Customer.getValue());
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param userName the user name we want to search
	 * @param password the password we want to search
	 * @return the result list.
	 */
	public static ArrayList<Integer> searchEmployee(String userName, String password) {
		return searchUser(userName, password, Table.Employee.getValue());
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param mapId   the map id we want to search
	 * @param placeId the place id we want to search
	 * @return the result list.
	 */
	public static ArrayList<Integer> searchLocation(Integer mapId, Integer placeId) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.Location.getValue() + " WHERE ";
			if (mapId != null)
				sql += "MapID=? AND ";
			if (placeId != null)
				sql += "POIID=? AND ";
			sql = sql.substring(0, sql.length() - 4);

			PreparedStatement gt = conn.prepareStatement(sql);
			if (mapId != null)
				gt.setInt(counter++, mapId);

			if (placeId != null)
				gt.setInt(counter++, placeId);

			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param routeId the route id we want to search
	 * @param placeId the place id we want to search
	 * @param numStop the number stop we want to search
	 * @return the result list.
	 */
	public static ArrayList<Integer> searchRouteStop(Integer routeId, Integer placeId, Integer numStop) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.RouteStop.getValue() + " WHERE ";
			if (routeId != null)
				sql += "RouteID=? AND ";
			if (placeId != null)
				sql += "PlaceID=? AND ";
			if (numStop != null)
				sql += "NumStops=? AND ";
			sql = sql.substring(0, sql.length() - 4);

			PreparedStatement gt = conn.prepareStatement(sql);
			if (routeId != null)
				gt.setInt(counter++, routeId);

			if (placeId != null)
				gt.setInt(counter++, placeId);

			if (numStop != null)
				gt.setInt(counter++, numStop);

			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityDataVersionId the city data version id we want to search
	 * @param mapId             the map id we want to search
	 * @return the result list.
	 */
	public static ArrayList<Integer> searchMapSight(Integer cityDataVersionId, Integer mapId) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.MapSight.getValue() + " WHERE ";
			if (cityDataVersionId != null)
				sql += "CityDataVersionID=? AND ";
			if (mapId != null)
				sql += "MapID=? AND ";
			sql = sql.substring(0, sql.length() - 4);

			PreparedStatement gt = conn.prepareStatement(sql);
			if (cityDataVersionId != null)
				gt.setInt(counter++, cityDataVersionId);

			if (mapId != null)
				gt.setInt(counter++, mapId);

			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityDataVersionId the city data version id we want to search
	 * @param placeId           the place id we want to search
	 * @return the result list.
	 */
	public static ArrayList<Integer> searchPlaceOfInterestSight(Integer cityDataVersionId, Integer placeId) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.PlaceOfInterestSight.getValue() + " WHERE ";
			if (cityDataVersionId != null)
				sql += "CityDataVersions=? AND ";
			if (placeId != null)
				sql += "POIID=? AND ";
			sql = sql.substring(0, sql.length() - 4);

			PreparedStatement gt = conn.prepareStatement(sql);
			if (cityDataVersionId != null)
				gt.setInt(counter++, cityDataVersionId);

			if (placeId != null)
				gt.setInt(counter++, placeId);

			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityDataVersionId the city data version id we want to search
	 * @param routeId           the route id we want to search
	 * @param isFavorite        if we want to search favorite or not
	 * @return the result list.
	 */
	public static ArrayList<Integer> searchRouteSight(Integer cityDataVersionId, Integer routeId, Boolean isFavorite) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.RouteSight.getValue() + " WHERE ";
			if (cityDataVersionId != null)
				sql += "CityDataVersions	=? AND ";
			if (routeId != null)
				sql += "RouteID=? AND ";
			if (isFavorite != null)
				sql += "IsFavorite=? AND ";
			sql = sql.substring(0, sql.length() - 4);

			PreparedStatement gt = conn.prepareStatement(sql);
			if (cityDataVersionId != null)
				gt.setInt(counter++, cityDataVersionId);

			if (routeId != null)
				gt.setInt(counter++, routeId);

			if (isFavorite != null)
				gt.setBoolean(counter++, isFavorite);

			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityId the city id we want to search
	 * @return the result list.
	 */
	public static ArrayList<Integer> searchCityDataVersion(Integer cityId) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.CityDataVersion.getValue() + " WHERE ";
			if (cityId != null)
				sql += "CityID=? AND ";
			sql = sql.substring(0, sql.length() - 4);

			PreparedStatement gt = conn.prepareStatement(sql);
			if (cityId != null)
				gt.setInt(counter++, cityId);

			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param userId the user id we want to search
	 * @param cityId the city id we want to search
	 * @param date   the data we want to search
	 * @param active if we want to search ative or not
	 * @return the result list.
	 */
	public static ArrayList<Integer> searchSubscription(Integer userId, Integer cityId, Date date, Boolean active) {
		try {
			int counter = 1;
			String sql = "SELECT ID, PurchaseDate, ExpDate FROM " + Table.Subscription.getValue() + " WHERE ";
			if (userId != null)
				sql += "UserID=? AND ";
			if (cityId != null)
				sql += "CityID=? AND ";
			if (active != null)
			{
				if (active)
					sql += "(? BETWEEN PurchaseDate AND ExpDate) AND ";
				else
					sql += "(? NOT BETWEEN PurchaseDate AND ExpDate) AND ";
			}
			
			sql = sql.substring(0, sql.length() - 4);

			if (userId == null && cityId == null && date == null && active == null)
				sql = "SELECT ID, PurchaseDate, ExpDate FROM " + Table.Subscription.getValue() + " WHERE True";

			PreparedStatement gt = conn.prepareStatement(sql);
			if (userId != null)
				gt.setInt(counter++, userId);

			if (cityId != null)
				gt.setInt(counter++, cityId);

			if (date != null)
				gt.setDate(counter++, date);

			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param userId       the user id we want to search
	 * @param cityId       the city id we want to search
	 * @param purchaseDate the purchase data we want to search
	 * @param wasDownload  if we want to search download or not
	 * @return the result list.
	 */
	public static ArrayList<Integer> searchOneTimePurchase(Integer userId, Integer cityId, Date purchaseDate,
			Boolean wasDownload) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.OneTimePurchase.getValue() + " WHERE ";
			if (userId != null)
				sql += "UserID=? AND ";
			if (cityId != null)
				sql += "CityID=? AND ";
			if (purchaseDate != null)
				sql += "PurchaseDate=? AND ";
			if (wasDownload != null)
				sql += "WasDownloaded=? AND ";
			sql = sql.substring(0, sql.length() - 4);
			PreparedStatement gt = conn.prepareStatement(sql);
			if (userId != null)
				gt.setInt(counter++, userId);

			if (cityId != null)
				gt.setInt(counter++, cityId);

			if (purchaseDate != null)
				gt.setDate(counter++, purchaseDate);

			if (wasDownload != null)
				gt.setBoolean(counter++, wasDownload);

			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityId   the city id we want to search
	 * @param date     the data we want to search
	 * @param dateFrom the data from we want to search
	 * @param dateEnd  the data end we want to search
	 * @return the result list.
	 * 
	 */
	public static ArrayList<Integer> searchStatistic(Integer cityId, Date date, Date dateFrom, Date dateEnd,
			Boolean newVersionPublished) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.Statistic.getValue() + " WHERE ";
			if (cityId != null)
				sql += "CityDataVersionID=? AND ";

			if (dateFrom != null && dateEnd != null)
				sql += "(Date BETWEEN ? AND ?) AND ";

			else if (date != null)
				sql += "Date=? AND ";

			if (newVersionPublished != null)
				sql += "NVP=? AND ";

			sql = sql.substring(0, sql.length() - 4);

			if (cityId == null && date == null && dateFrom == null && dateEnd == null && newVersionPublished == null)
				sql = "SELECT ID FROM " + Table.Statistic.getValue() + " WHERE True";

			PreparedStatement gt = conn.prepareStatement(sql);

			if (cityId != null)
				gt.setInt(counter++, cityId);

			if (dateFrom != null && dateEnd != null) {
				gt.setDate(counter++, dateFrom);
				gt.setDate(counter++, dateEnd);
			} else if (date != null)
				gt.setDate(counter++, date);

			if (newVersionPublished != null)
				gt.setBoolean(counter++, newVersionPublished);

			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * returns the row with id=id on table.
	 * 
	 * @param table: where to look
	 * @param id: target ID
	 * @return last element from resultset
	 */
	private static ResultSet get(String table, int id) {
		try {
			String sql = "SELECT * FROM " + table + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, id);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return res;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the place of interest we want to get
	 * @return the new object
	 */
	public static PlaceOfInterest getPlaceOfInterestById(int id) {
		try {
			ResultSet res = get(Table.PlaceOfInterest.getValue(), id);
			if (res == null)
				return null;
			return PlaceOfInterest._createPlaceOfInterest(res.getInt("ID"), res.getInt("CityID"), res.getString("Name"),
					PlaceOfInterest.PlaceType.values()[res.getInt("Type")], res.getString("Description"),
					res.getInt("ATD") != 0);
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the map we want to get
	 * @return the new object
	 */
	public static Map getMapById(int id) {
		try {
			ResultSet res = get(Table.Map.getValue(), id);
			if (res == null)
				return null;
			return Map._createMap(res.getInt("ID"), res.getInt("CityID"), res.getString("Name"), res.getString("Info"),
					res.getString("imgURL"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the route we want to get
	 * @return the new object
	 */
	public static Route getRouteById(int id) {
		try {
			ResultSet res = get(Table.Route.getValue(), id);
			if (res == null)
				return null;
			return Route._createRoute(res.getInt("ID"), res.getInt("CityID"), res.getString("Info"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the city we want to get
	 * @return the new object
	 */
	public static City getCityById(int id) {
		try {
			ResultSet res = get(Table.City.getValue(), id);
			if (res == null)
				return null;
			return City._createCity(res.getInt("ID"), res.getString("Name"), res.getString("Description"),
					res.getInt("VersionID") == -1 ? null : res.getInt("VersionID"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the cutomer we want to get
	 * @return the new object
	 */
	public static Customer getCustomerById(int id) {
		try {
			ResultSet res = get(Table.Customer.getValue(), id);
			if (res == null)
				return null;
			return Customer._createCustomer(res.getInt("ID"), res.getString("Username"), res.getString("Password"),
					res.getString("Email"), res.getString("FirstName"), res.getString("LastName"),
					res.getString("PhoneNumber"), res.getString("CardNum"), res.getString("Exp"), res.getString("CVC"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the employee we want to get
	 * @return the new object
	 */
	public static Employee getEmployeeById(int id) {
		try {
			ResultSet res = get(Table.Employee.getValue(), id);
			if (res == null)
				return null;
			return Employee._createEmployee(res.getInt("ID"), res.getString("Username"), res.getString("Password"),
					res.getString("Email"), res.getString("FirstName"), res.getString("LastName"),
					res.getString("PhoneNumber"), Employee.Role.values()[res.getInt("Role")]);
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the location we want to get
	 * @return the new object
	 */
	public static Location _getLocationById(int id) {
		try {
			ResultSet res = get(Table.Location.getValue(), id);
			if (res == null)
				return null;
			double[] coordinates = { res.getInt("x"), res.getInt("y") };
			return Location._createLocation(res.getInt("ID"), res.getInt("MapID"), res.getInt("POIID"), coordinates);
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the route stop we want to get
	 * @return the new object
	 */
	public static RouteStop _getRouteStopById(int id) {
		try {
			ResultSet res = get(Table.RouteStop.getValue(), id);
			if (res == null)
				return null;
			return RouteStop._createRouteStop(res.getInt("ID"), res.getInt("RouteID"), res.getInt("PlaceID"),
					res.getInt("NumStops"), res.getTime("Time"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the map sight we want to get
	 * @return the new object
	 */
	public static MapSight _getMapSightById(int id) {
		try {
			ResultSet res = get(Table.MapSight.getValue(), id);
			if (res == null)
				return null;
			return MapSight._createMapSight(res.getInt("ID"), res.getInt("MapID"), res.getInt("CityDataVersionID"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the place of interest sight we want to get
	 * @return the new object
	 */
	public static PlaceOfInterestSight _getPlaceOfInterestSightById(int id) {
		try {
			ResultSet res = get(Table.PlaceOfInterestSight.getValue(), id);
			if (res == null)
				return null;
			return PlaceOfInterestSight._PlaceOfInterestSight(res.getInt("ID"), res.getInt("CityDataVersions"),
					res.getInt("POIID"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the route sight we want to get
	 * @return the new object
	 */
	public static RouteSight _getRouteSightById(int id) {
		try {
			ResultSet res = get(Table.RouteSight.getValue(), id);
			if (res == null)
				return null;
			return RouteSight._createRouteSight(res.getInt("ID"), res.getInt("CityDataVersions"), res.getInt("RouteID"),
					res.getBoolean("IsFavorite"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the city data version we want to get
	 * @return the new object
	 */
	public static CityDataVersion _getCityDataVersionById(int id) {
		try {
			ResultSet res = get(Table.CityDataVersion.getValue(), id);
			return CityDataVersion._createCityDataVersion(res.getInt("ID"), res.getInt("CityID"),
					res.getString("VersionName"), res.getDouble("PriceOneTime"), res.getDouble("PricePeriod"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the subscription we want to get
	 * @return the new object
	 */
	public static Subscription _getSubscriptionById(int id) {
		try {
			ResultSet res = get(Table.Subscription.getValue(), id);
			return Subscription._createSubscription(res.getInt("ID"), res.getInt("CityID"), res.getInt("UserID"),
					res.getDate("PurchaseDate"), res.getDouble("FullPrice"), res.getDouble("PricePayed"),
					res.getDate("ExpDate"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the one time purchase we want to get
	 * @return the new object
	 */
	public static OneTimePurchase _getOneTimePurchaseById(int id) {
		try {
			ResultSet res = get(Table.OneTimePurchase.getValue(), id);
			return OneTimePurchase._createOneTimePurchase(res.getInt("ID"), res.getInt("CityID"), res.getInt("UserID"),
					res.getDate("PurchaseDate"), res.getDouble("FullPrice"), res.getDouble("PricePayed"),
					res.getBoolean("WasDownloaded"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the statistic we want to get
	 * @return the new object
	 */
	public static Statistic _getStatisticById(int id) {
		try {
			ResultSet res = get(Table.Statistic.getValue(), id);
			return Statistic._createStatistic(res.getInt("ID"), res.getInt("CityID"), res.getDate("Date"),
					res.getInt("NOTP"), res.getInt("NS"), res.getInt("NSR"), res.getInt("NV"), res.getInt("NSD"),
					res.getBoolean("NVP"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize() clean the connection when finished.
	 */
	@Override
	protected void finalize() throws Throwable {
		closeConnection();
	}
}