package controller;

import java.sql.Connection;
import java.time.LocalDate;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

//import javafx.scene.chart.PieChart.Data;
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
import otherClasses.DatabaseException;
import otherClasses.Pair;
import objectClasses.Employee.Role;
import objectClasses.PlaceOfInterest.PlaceType;

import java.security.MessageDigest;

//import javax.xml.bind.DatatypeConverter;

/**
 * class that handles the management of the database
 * 
 * @author tal20
 * @author Lior Weissman
 */
public class Database
{
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
	 * @author Lior Weissman
	 * 
	 */
	public enum Counter
	{
		PlaceOfInterest(0), User(1), Map(2), Location(3), CityDataVersion(4), Route(5), CityPurchase(6), City(7),
		RouteStop(8), MapSight(9), PlaceOfInterestSight(10), RouteSight(11), Statistic(12);

		private final int value;

		/**
		 * Constructor.
		 * 
		 * @param nv: Integer that hold the value
		 * @return A Counter object.
		 */
		Counter(final int nv)
		{
			value = nv;
		}

		/**
		 * Translate to int
		 * 
		 * @return Returns the value
		 */
		public int getValue()
		{
			return value;
		}
	}

	/**
	 * change the default constructor to private, this class cannot be created as
	 * object.
	 */
	private Database()
	{

	}

	/**
	 * returns all the customers
	 * 
	 * @return List of all the customers
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Customer> getAllCustomers() throws SQLException
	{
		ArrayList<Integer> ids = searchCustomer(null, null);
		ArrayList<Customer> custs = new ArrayList<Customer>();
		for (int id : ids)
		{
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
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Pair<String, Integer>> getAllCitiesNameId() throws SQLException
	{
		ArrayList<Integer> ids = searchCity(null, null);
		ArrayList<Pair<String, Integer>> list = new ArrayList<Pair<String, Integer>>();
		for (int id : ids)
		{
			String cName = Database.getCityNameById(id);
			if (cName == null)
				continue;
			list.add(new Pair<>(cName, id));
		}
		return list;
	}

	/**
	 * Return list of customers subscribes right now to the city
	 * 
	 * @param cityId the id of the city
	 * @return list of customers
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Customer> getCustomersSubscribesToCity(int cityId) throws SQLException
	{
		LocalDate today = LocalDate.now();

		// find relevant subs
		ArrayList<Integer> subsIds = Database.searchSubscription(null, cityId, today, true);
		// find relevant customers
		Set<Integer> customersIds = new HashSet<Integer>();
		for (int subsId : subsIds)
		{
			Subscription s = Database._getSubscriptionById(subsId);
			if (s == null)
				continue;
			customersIds.add(s.getUserId());
		}
		// load them
		ArrayList<Customer> customers = new ArrayList<Customer>();
		for (int id : customersIds)
		{
			Customer c = Database.getCustomerById(id);
			if (c == null)
				continue;
			customers.add(c);
		}
		return customers;
	}

	/**
	 * @author tal20 This enum maps from full table names and local names.
	 */
	public enum Table
	{
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
		Table(final String envUrl)
		{
			url = envUrl;
		}

		/**
		 * @return the table name
		 */
		public String getValue()
		{
			return url;
		}
	}

	/**
	 * Create a new database connection.
	 * 
	 * @throws SQLException if the access to database failed
	 */
	public static void createConnection() throws SQLException
	{

		if (conn == null)
		{
			try
			{
				Class.forName(JDBC_DRIVER);
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("connection opening");
		}
	}

	/**
	 * CLose the database connection
	 * 
	 * @throws SQLException if the access to database failed
	 */
	public static void closeConnection() throws SQLException
	{
		if (conn != null)
		{
			conn.close();
			conn = null;
			System.out.println("connection closing");
		}
	}

	/**
	 * Reset the entire database. Delete all inputs, set counters to 0. Only Tal and
	 * Lior should use this method.
	 * 
	 * @param name of the user
	 * @param pass of the user
	 * @return true if the data base is reset
	 * @throws SQLException if the access to database failed
	 */
	public static boolean resetAll(String name, String pass) throws SQLException
	{
		String sql = "SELECT Name FROM Team WHERE Name=? AND Password=?";
		PreparedStatement check = conn.prepareStatement(sql);
		check.setString(1, name);
		check.setString(2, pass);
		ResultSet res = check.executeQuery();
		// check if there is exciting row in table before insert
		if (!res.next())
			return false;
		for (Table table : Table.values())
		{
			sql = "DELETE FROM " + table.getValue() + " WHERE TRUE";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.executeUpdate();
		}

		for (Counter type : Counter.values())
		{
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=0 WHERE Object=?");
			su.setInt(1, type.getValue());
			su.executeUpdate();
		}
		System.out.println("Finished reset");
		return true;
	}

	/**
	 * // generate ID's
	 * 
	 * /**
	 * 
	 * @param type: the table find ID
	 * @return the ID of this table
	 * @throws SQLException if the access to database failed
	 */
	private static int generateId(int type) throws SQLException
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
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
	}

	/**
	 * @return the ID of the next user
	 * @throws SQLException if the access to database failed
	 */
	public static int generateIdUser() throws SQLException
	{
		return generateId(Counter.User.getValue());
	}

	/**
	 * @return the ID of the next POI
	 * @throws SQLException if the access to database failed
	 */
	public static int generateIdPlaceOfInterest() throws SQLException
	{// first example
		return generateId(Counter.PlaceOfInterest.getValue());

	}

	/**
	 * @return the ID of the next map
	 * @throws SQLException if the access to database failed
	 */
	public static int generateIdMap() throws SQLException
	{
		return generateId(Counter.Map.getValue());
	}

	/**
	 * @return the ID of the next location
	 * @throws SQLException if the access to database failed
	 */
	public static int generateIdLocation() throws SQLException
	{
		return generateId(Counter.Location.getValue());
	}

	/**
	 * @return the ID of the next city data version
	 * @throws SQLException if the access to database failed
	 */
	public static int generateIdCityDataVersion() throws SQLException
	{
		return generateId(Counter.CityDataVersion.getValue());
	}

	/**
	 * @return the ID of the next route
	 * @throws SQLException if the access to database failed
	 */
	public static int generateIdRoute() throws SQLException
	{
		return generateId(Counter.Route.getValue());
	}

	/**
	 * @return the ID of the next city purchase
	 * @throws SQLException if the access to database failed
	 */
	public static int generateIdCityPurchase() throws SQLException
	{
		return generateId(Counter.CityPurchase.getValue());
	}

	/**
	 * @return the ID of the next city
	 * @throws SQLException if the access to database failed
	 */
	public static int generateIdCity() throws SQLException
	{
		return generateId(Counter.City.getValue());
	}

	/**
	 * @return the ID of the next route stop
	 * @throws SQLException if the access to database failed
	 */
	public static int generateIdRouteStop() throws SQLException
	{
		return generateId(Counter.RouteStop.getValue());
	}

	/**
	 * @return the ID of the next map sight
	 * @throws SQLException if the access to database failed
	 */
	public static int generateIdMapSight() throws SQLException
	{
		return generateId(Counter.MapSight.getValue());
	}

	/**
	 * @return the ID of the next POI sight
	 * @throws SQLException if the access to database failed
	 */
	public static int generateIdPlaceOfInterestSight() throws SQLException
	{
		return generateId(Counter.PlaceOfInterestSight.getValue());
	}

	/**
	 * @return the ID of the next route sight
	 * @throws SQLException if the access to database failed
	 */
	public static int generateIdRouteSight() throws SQLException
	{
		return generateId(Counter.RouteSight.getValue());
	}

	/**
	 * @return the ID of the next statistic
	 * @throws SQLException if the access to database failed
	 */
	public static int generateIdStatistic() throws SQLException
	{
		return generateId(Counter.Statistic.getValue());
	}

	/**
	 * @param table: the table to search in
	 * @param id:    the id to search
	 * @return true if exists, false else.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean exist(String table, int id) throws SQLException
	{

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		String sql = "SELECT ID FROM " + table + " WHERE ID=?";
		PreparedStatement check = conn.prepareStatement(sql);
		check.setInt(1, id);
		ResultSet res = check.executeQuery();
		// check if there is exciting row in table before insert
		if (!res.next())
			return false;
		return true;

	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a POI with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existPlaceOfInterest(int id) throws SQLException
	{
		return exist(Table.PlaceOfInterest.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a map with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existMap(int id) throws SQLException
	{
		return exist(Table.Map.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a route with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existRoute(int id) throws SQLException
	{
		return exist(Table.Route.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a city with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existCity(int id) throws SQLException
	{
		return exist(Table.City.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a customer with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existCustomer(int id) throws SQLException
	{
		return exist(Table.Customer.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a employee with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existEmployee(int id) throws SQLException
	{
		return exist(Table.Employee.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a location with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existLocation(int id) throws SQLException
	{
		return exist(Table.Location.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a route stop with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existRouteStop(int id) throws SQLException
	{
		return exist(Table.RouteStop.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a map sight with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existMapSight(int id) throws SQLException
	{
		return exist(Table.MapSight.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a POI sight with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existPlaceOfInterestSight(int id) throws SQLException
	{
		return exist(Table.PlaceOfInterestSight.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a route sight with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existRouteSight(int id) throws SQLException
	{
		return exist(Table.RouteSight.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a city data version with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existCityDataVersion(int id) throws SQLException
	{
		return exist(Table.CityDataVersion.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a subscription with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existSubscription(int id) throws SQLException
	{
		return exist(Table.Subscription.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a one time purchase with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existOneTimePurchase(int id) throws SQLException
	{
		return exist(Table.OneTimePurchase.getValue(), id);
	}

	/**
	 * checks if there is an entry on the table with this ID.
	 * 
	 * @param id: id to search for
	 * @return whether there is a statistic with this ID.
	 * @throws SQLException if the access to database failed
	 */
	private static boolean existStatistic(int id) throws SQLException
	{
		return exist(Table.Statistic.getValue(), id);
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the place of interest we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean savePlaceOfInterest(PlaceOfInterest p) throws SQLException
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existPlaceOfInterest(p.getId()))
		{
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
		}
		else
		{
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
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the map we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean saveMap(Map p) throws SQLException
	{
		if (conn == null)
		{
			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existMap(p.getId()))
		{
			String sql = "UPDATE " + Table.Map.getValue() + " SET Name=?, Info=?, imgURL=?, CityID=? WHERE ID=?";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setString(1, p.getName());
			su.setString(2, p.getInfo());
			su.setString(3, p.getImgURL());
			su.setInt(4, p.getCityId());
			su.setInt(5, p.getId());
			su.executeUpdate();
			return true;
		}
		else
		{
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
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the route we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean saveRoute(Route p) throws SQLException
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existRoute(p.getId()))
		{
			String sql = "UPDATE " + Table.Route.getValue()
					+ " SET Info=?, NumStops=?, CityID=?, Name=?, IsFavorite=? WHERE ID=?";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setString(1, p.getInfo());
			su.setInt(2, p.getNumStops());
			su.setInt(3, p.getCityId());
			su.setString(4, p.getName());
			su.setInt(5, p.getId());
			su.setBoolean(6, p.getIsFavorite());
			su.executeUpdate();
			return true;
		}
		else
		{
			String sql = "INSERT INTO " + Table.Route.getValue()
					+ " (ID, Info, NumStops, CityID, Name, IsFavorite) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getId());
			su.setString(2, p.getInfo());
			su.setInt(3, p.getNumStops());
			su.setInt(4, p.getCityId());
			su.setString(5, p.getName());
			su.setBoolean(6, p.getIsFavorite());
			su.executeUpdate();
			return false;
		}
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the city we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean saveCity(City p) throws SQLException
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existCity(p.getId()))
		{
			String sql = "UPDATE " + Table.City.getValue()
					+ " SET Name=?, Description=?, VersionID=?, MNtP=?, PriceOneTime=?, PricePeriod=?, CEONTAP=?, TBPriceOneTime=?, TBPricePeriod=?  WHERE ID=?";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setString(1, p.getCityName());
			su.setString(2, p.getCityDescription());
			su.setInt(3, p.getPublishedVersionId() == null ? -1 : p.getPublishedVersionId());
			su.setBoolean(4, p.getManagerNeedsToPublish());
			su.setDouble(5, p.getPriceOneTime());
			su.setDouble(6, p.getPricePeriod());
			su.setBoolean(7, p.isCeoNeedsToApprovePrices());
			su.setDouble(8, p.getToBePriceOneTime());
			su.setDouble(9, p.getToBePricePeriod());
			su.setInt(10, p.getId());
			su.executeUpdate();
			return true;
		}
		else
		{
			String sql = "INSERT INTO " + Table.City.getValue()
					+ " (ID,Name, Description, VersionID, MNtP,PriceOneTime, PricePeriod, CEONTAP, TBPriceOneTime, TBPricePeriod) VALUES (?,?, ?, ?, ?,?,?,?,?,?)";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getId());
			su.setString(2, p.getCityName());
			su.setString(3, p.getCityDescription());
			su.setInt(4, p.getPublishedVersionId() == null ? -1 : p.getPublishedVersionId());
			su.setBoolean(5, p.getManagerNeedsToPublish());
			su.setDouble(6, p.getPriceOneTime());
			su.setDouble(7, p.getPricePeriod());
			su.setBoolean(8, p.isCeoNeedsToApprovePrices());
			su.setDouble(9, p.getToBePriceOneTime());
			su.setDouble(10, p.getToBePricePeriod());
			su.executeUpdate();
			return false;
		}
	}

	/**
	 * Save secured customer to the data base
	 * 
	 * @param p the customer
	 * @return if the customer is secured or not
	 * @throws SQLException if the access to database failed
	 */
	public static boolean saveCustomer(Customer p) throws SQLException
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existCustomer(p.getId()))
		{
			String sql = "UPDATE " + Table.Customer.getValue()
					+ " SET Username=?, Password=MD5(?), Email=?, FirstName=?, LastName=?,"
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
		else
		{
			String sql = "INSERT INTO " + Table.Customer.getValue() + " "
					+ "(ID,Username, Password, Email, FirstName, LastName, PhoneNumber, CardNum, CVC, Exp) VALUES "
					+ "(?, ?, MD5(?), ?, ?, ?, ?, ?, ?, ?)";
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
	}

	/**
	 * Search user secured
	 * 
	 * @param userName the user name
	 * @param password the password
	 * @param table    the table
	 * @return list of ids of secured users
	 * @throws SQLException if the access to database failed
	 */
	private static ArrayList<Integer> searchUser(String userName, String password, String table)
			throws SQLException
	{
		if (conn == null)
		{
			System.err.println("No connection found");
			throw new DatabaseException();
		}
		int counter = 1;
		String sql = "SELECT ID FROM " + table + " WHERE ";
		if (userName != null)
			sql += "Username=? AND ";
		if (password != null)
			sql += "Password=MD5(?) AND ";
		sql = sql.substring(0, sql.length() - 4);

		if (userName == null && password == null)
			sql = "SELECT ID FROM " + table + " WHERE True";

		PreparedStatement gt = conn.prepareStatement(sql);
		if (userName != null)
			gt.setString(counter++, userName);

		if (password != null)
			gt.setString(counter++, password);

		return queryToList(gt);
	}

	/**
	 * Search secured customer
	 * 
	 * @param userName the user name
	 * @param password the password
	 * @return list of ids of the secured customers
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchCustomer(String userName, String password) throws SQLException
	{
		return searchUser(userName, password, Table.Customer.getValue());
	}

	/**
	 * Search secured employee
	 * 
	 * @param userName the user name
	 * @param password the password
	 * @return list of ids of the secured employees
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchEmployee(String userName, String password) throws SQLException
	{
		return searchUser(userName, password, Table.Employee.getValue());
	}
	
	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the employee we want save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean saveEmployee(Employee p) throws SQLException
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existEmployee(p.getId()))
		{
			String sql = "UPDATE " + Table.Employee.getValue() + "SET Username=?,"
					+ " Password=MD5(?), Email=?, FirstName=?, LastName=?, PhoneNumber=?, Role=? WHERE ID=?";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setString(1, p.getUserName());
			su.setString(2, p.getPassword());
			su.setString(3, p.getEmail());
			su.setString(4, p.getFirstName());
			su.setString(5, p.getLastName());
			su.setString(6, p.getPhoneNumber());
			su.setInt(7, p.getRole().getValue());
			su.setInt(8, p.getId());
			su.executeUpdate();
			return true;
		}
		else ////
		{
			String sql = "INSERT INTO " + Table.Employee.getValue()
					+ " (ID,Username, Password, Email, FirstName, LastName, PhoneNumber, Role)"
					+ " VALUES (?, ?, MD5(?), ?, ?, ?, ?, ?)";
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
	}

	
	/**
	 * saves a new non secured instance to the database.
	 * 
	 * @param p the customer we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean saveNonSecuredCustomer(Customer p) throws SQLException
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existCustomer(p.getId()))
		{
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
		else
		{
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
	}

	
	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the location we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _saveLocation(Location p) throws SQLException // friend to Map
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existLocation(p.getId()))
		{
			String sql = "UPDATE " + Table.Location.getValue() + " SET MapID=?, POIID=?, x=?, y=? WHERE ID=?";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getMapId());
			su.setInt(2, p.getPlaceOfInterestId());
			su.setDouble(3, p.getCoordinates()[0]);
			su.setDouble(4, p.getCoordinates()[1]);
			su.setInt(5, p.getId());
			su.executeUpdate();
			return true;
		}
		else
		{
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
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the route stop we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _saveRouteStop(RouteStop p) throws SQLException// friend to Route
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existRouteStop(p.getId()))
		{
			String sql = "UPDATE " + Table.RouteStop.getValue()
					+ " SET RouteID=?, PlaceID=?, NumStops=?, Time=? WHERE ID=?";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getRouteId());
			su.setInt(2, p.getPlaceId());
			su.setInt(3, p.getNumStop());
			su.setTime(4, java.sql.Time.valueOf(p.getRecommendedTime()));
			su.setInt(5, p.getId());
			su.executeUpdate();
			return true;
		}
		else
		{
			String sql = "INSERT INTO " + Table.RouteStop.getValue()
					+ " (ID,RouteID, PlaceID, NumStops, Time) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getId());
			su.setInt(2, p.getRouteId());
			su.setInt(3, p.getPlaceId());
			su.setInt(4, p.getNumStop());
			su.setTime(5, java.sql.Time.valueOf(p.getRecommendedTime()));
			su.executeUpdate();
			return false;
		}
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the map sight we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _saveMapSight(MapSight p) throws SQLException // friend to MapSight
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existMapSight(p.getId()))
		{
			String sql = "UPDATE " + Table.MapSight.getValue() + " SET MapID=?, CityDataVersionID=? WHERE ID=?";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getMapId());
			su.setInt(2, p.getCityDataVersionId());
			su.setInt(3, p.getId());
			su.executeUpdate();
			return true;
		}
		else
		{
			String sql = "INSERT INTO " + Table.MapSight.getValue() + " (ID,MapID, CityDataVersionID) VALUES (?, ?, ?)";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getId());
			su.setInt(2, p.getMapId());
			su.setInt(3, p.getCityDataVersionId());
			su.executeUpdate();
			return false;
		}
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p place of interest sight we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _savePlaceOfInterestSight(PlaceOfInterestSight p) throws SQLException// friend to
																								// CityDataVersion
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existPlaceOfInterestSight(p.getId()))
		{
			String sql = "UPDATE " + Table.PlaceOfInterestSight.getValue()
					+ " SET CityDataVersions=?, POIID=? WHERE ID=?";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getCityDataVersionId());
			su.setInt(2, p.getPlaceOfInterestId());
			su.setInt(3, p.getId());
			su.executeUpdate();
			return true;
		}
		else
		{
			String sql = "INSERT INTO " + Table.PlaceOfInterestSight.getValue()
					+ " (ID,CityDataVersions, POIID) VALUES (?, ?, ?)";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getId());
			su.setInt(2, p.getCityDataVersionId());
			su.setInt(3, p.getPlaceOfInterestId());
			su.executeUpdate();
			return false;
		}
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the route sight we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _saveRouteSight(RouteSight p) throws SQLException// friend to CityDataVersion
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existRouteSight(p.getId()))
		{
			String sql = "UPDATE " + Table.RouteSight.getValue() + " SET CityDataVersions=?, RouteID=?  WHERE ID=?";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getCityDataVersionId());
			su.setInt(2, p.getRouteId());
			su.setInt(4, p.getId());
			su.executeUpdate();
			return true;
		}
		else
		{
			String sql = "INSERT INTO " + Table.RouteSight.getValue()
					+ " (ID,CityDataVersions, RouteID) VALUES (?, ?, ?)";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getId());
			su.setInt(2, p.getCityDataVersionId());
			su.setInt(3, p.getRouteId());
			su.executeUpdate();
			return false;
		}
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p city data version we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _saveCityDataVersion(CityDataVersion p) throws SQLException// friend to City
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existCityDataVersion(p.getId()))
		{
			String sql = "UPDATE " + Table.CityDataVersion.getValue() + " SET CityID=?, VersionName=? WHERE ID=?";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getCityId());
			su.setString(2, p.getVersionName());
			su.setInt(3, p.getId());
			su.executeUpdate();
			return true;
		}
		else
		{
			String sql = "INSERT INTO " + Table.CityDataVersion.getValue()
					+ " (ID,CityID, VersionName) VALUES (?, ?, ?)";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getId());
			su.setInt(2, p.getCityId());
			su.setString(3, p.getVersionName());
			su.executeUpdate();
			return false;
		}
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the subscription we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _saveSubscription(Subscription p) throws SQLException // friend to Customer
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existSubscription(p.getId()))
		{
			String sql = "UPDATE " + Table.Subscription.getValue()
					+ " SET CityID=?, UserID=?, PurchaseDate=?, FullPrice=?, PricePayed=?, ExpDate=? WHERE ID=?";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getCityId());
			su.setInt(2, p.getUserId());
			su.setDate(3, java.sql.Date.valueOf(p.getPurchaseDate().plusDays(1))); // fix here - RON
			su.setDouble(4, p.getFullPrice());
			su.setDouble(5, p.getPricePayed());
			su.setDate(6, java.sql.Date.valueOf(p.getExpirationDate().plusDays(1))); // fix here - RON
			su.setInt(7, p.getId());
			su.executeUpdate();
			return true;
		}
		else
		{
			String sql = "INSERT INTO " + Table.Subscription.getValue()
					+ " (ID,CityID, UserID, PurchaseDate, FullPrice, PricePayed, ExpDate) VALUES (?,?,?, ?, ?, ?, ?)";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getId());
			su.setInt(2, p.getCityId());
			su.setInt(3, p.getUserId());
			su.setDate(4, java.sql.Date.valueOf(p.getPurchaseDate().plusDays(1))); // fix here - RON
			su.setDouble(5, p.getFullPrice());
			su.setDouble(6, p.getPricePayed());
			su.setDate(7, java.sql.Date.valueOf(p.getExpirationDate().plusDays(1))); // fix here - RON
			su.executeUpdate();
			return false;
		}
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the one time purchase we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _saveOneTimePurchase(OneTimePurchase p) throws SQLException // friend to Customer
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existOneTimePurchase(p.getId()))
		{
			String sql = "UPDATE " + Table.OneTimePurchase.getValue()
					+ " SET CityID=?, UserID=?, PurchaseDate=?, FullPrice=?, PricePayed=?, WasDownloaded=? WHERE ID=?";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getCityId());
			su.setInt(2, p.getUserId());
			su.setDate(3, java.sql.Date.valueOf(p.getPurchaseDate().plusDays(1))); // fix here - RON
			su.setDouble(4, p.getFullPrice());
			su.setDouble(5, p.getPricePayed());
			su.setBoolean(6, p.getWasDownload());
			su.setInt(7, p.getId());
			su.executeUpdate();
			return true;
		}
		else
		{
			String sql = "INSERT INTO " + Table.OneTimePurchase.getValue()
					+ " (ID,CityID, UserID, PurchaseDate, FullPrice, PricePayed, WasDownloaded) VALUES (?,?, ?, ?, ?, ?,?)";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getId());
			su.setInt(2, p.getCityId());
			su.setInt(3, p.getUserId());
			su.setDate(4, java.sql.Date.valueOf(p.getPurchaseDate().plusDays(1))); // fix here - RON
			su.setDouble(5, p.getFullPrice());
			su.setDouble(6, p.getPricePayed());
			su.setBoolean(7, p.getWasDownload());
			su.executeUpdate();
			return false;
		}
	}

	/**
	 * saves a new instance to the database.
	 * 
	 * @param p the statistic we want to save
	 * @return true if an updated was made. false for new element.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _saveStatistic(Statistic p) throws SQLException
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		if (existStatistic(p.getId()))
		{
			String sql = "UPDATE " + Table.Statistic.getValue()
					+ " SET CityID=?, Date=?, NOTP=?, NS=?, NSR=?, NV=?, NSD=?, NVP=?, NumMaps=? WHERE ID=?";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getCityId());
			su.setDate(2, java.sql.Date.valueOf(p.getDate().plusDays(1)));
			su.setInt(3, p.getNumOneTimePurchases());
			su.setInt(4, p.getNumSubscriptions());
			su.setInt(5, p.getNumSubscriptionsRenewal());
			su.setInt(6, p.getNumVisited());
			su.setInt(7, p.getNumSubDownloads());
			su.setBoolean(8, p.isNewVersionPublished());
			su.setInt(9, p.getNumMaps());
			su.setInt(10, p.getId());
			su.executeUpdate();
			return true;
		}
		else
		{
			String sql = "INSERT INTO " + Table.Statistic.getValue()
					+ " (ID, CityID, Date, NOTP, NS, NSR, NV, NSD, NVP, NumMaps) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement su = conn.prepareStatement(sql);
			su.setInt(1, p.getId());
			su.setInt(2, p.getCityId());
			su.setDate(3, java.sql.Date.valueOf(p.getDate().plusDays(1)));
			su.setInt(4, p.getNumOneTimePurchases());
			su.setInt(5, p.getNumSubscriptions());
			su.setInt(6, p.getNumSubscriptionsRenewal());
			su.setInt(7, p.getNumVisited());
			su.setInt(8, p.getNumSubDownloads());
			su.setBoolean(9, p.isNewVersionPublished());
			su.setInt(10, p.getNumMaps());
			su.executeUpdate();
			return false;
		}
	}

	/**
	 * @param table: the table to search in
	 * @param id:    the id to delete
	 * @return true if deleted, false else.
	 */
	private static boolean delete(String table, int id) throws SQLException
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		String sql = "DELETE FROM " + table + " WHERE ID=?";
		PreparedStatement gt = conn.prepareStatement(sql);
		gt.setInt(1, id);
		int count = gt.executeUpdate();
		return count != 0;
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean deletePlaceOfInterest(int id) throws SQLException
	{
		return delete(Table.PlaceOfInterest.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean deleteMap(int id) throws SQLException
	{
		return delete(Table.Map.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean deleteRoute(int id) throws SQLException
	{
		return delete(Table.Route.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean deleteCity(int id) throws SQLException
	{
		return delete(Table.City.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean deleteCustomer(int id) throws SQLException
	{
		return delete(Table.Customer.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean deleteEmployee(int id) throws SQLException
	{
		return delete(Table.Employee.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _deleteLocation(int id) throws SQLException
	{
		return delete(Table.Location.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _deleteRouteStop(int id) throws SQLException
	{
		return delete(Table.RouteStop.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _deleteMapSight(int id) throws SQLException
	{
		return delete(Table.MapSight.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _deletePlaceOfInterestSight(int id) throws SQLException
	{
		return delete(Table.PlaceOfInterestSight.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _deleteRouteSight(int id) throws SQLException
	{
		return delete(Table.RouteSight.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _deleteCityDataVersion(int id) throws SQLException
	{
		return delete(Table.CityDataVersion.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _deleteSubscription(int id) throws SQLException
	{
		return delete(Table.Subscription.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _deleteOneTimePurchase(int id) throws SQLException
	{
		return delete(Table.OneTimePurchase.getValue(), id);
	}

	/**
	 * @param id the id to delete
	 * @return true if deleted, false else.
	 * @throws SQLException if the access to database failed
	 */
	public static boolean _deleteStatistic(int id) throws SQLException
	{
		return delete(Table.Statistic.getValue(), id);
	}

	/**
	 * @param gt: A finished SQL query to run.
	 * @return returns the list of the results.
	 * @throws SQLException if the access to database failed
	 */
	private static ArrayList<Integer> queryToList(PreparedStatement gt) throws SQLException
	{
		ResultSet res = gt.executeQuery();
		ArrayList<Integer> IDs = new ArrayList<>();
		while (res.next())
			IDs.add(res.getInt("ID"));
		return IDs;
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
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchPlaceOfInterest(String placeName, String placeDescription, Integer cityId)
			throws SQLException
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		int counter = 1;
		String[] words = { "" };
		if (placeDescription != null)
			words = placeDescription.split(" ");
		int len = words.length;
		String sql = "SELECT ID FROM " + Table.PlaceOfInterest.getValue() + " WHERE ";
		if (placeName != null)
			sql += "(Name LIKE ?) AND ";
		if (placeDescription != null)
			for (int i = 0; i < len; i++)
				sql += "(Description LIKE ?) AND ";
		if (cityId != null)
			sql += "CityID=? AND ";
		sql = sql.substring(0, sql.length() - 4);

		PreparedStatement gt = conn.prepareStatement(sql);
		if (placeName != null)
			gt.setString(counter++, "%" + placeName + "%");

		if (placeDescription != null)
			for (int i = 0; i < len; i++)
				gt.setString(counter++, "%" + words[i] + "%");

		if (cityId != null)
			gt.setInt(counter++, cityId);

		return queryToList(gt);
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityId the id of the city we want to search
	 * @param name   the name we want to search
	 * @param info   the info data we want to search
	 * @param imgURL the image url we want to search
	 * @return : the result list.
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchMap(Integer cityId, String name, String info, String imgURL)
			throws SQLException
	{
		int counter = 1;

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
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
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityId the id of the city we want to search
	 * @param info   the info data we want to search
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchRoute(Integer cityId, String info) throws SQLException
	{
		int counter = 1;

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
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
	}

	/**
	 * search function. if a parameter is null, we ignore it. When searching by
	 * description, we look for a city such that every word from the query
	 * description is in the city description.
	 * 
	 * @param cityName        the city name we want to search
	 * @param cityDescription the city description we want to search
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchCity(String cityName, String cityDescription) throws SQLException
	{
		int counter = 1;

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		String[] words = { "" };
		if (cityDescription != null)
			words = cityDescription.split(" ");
		int len = words.length;
		String sql = "SELECT ID FROM " + Table.City.getValue() + " WHERE ";
		if (cityName != null)
			sql += "(Name LIKE ?) AND ";
		if (cityDescription != null)
			for (int i = 0; i < len; i++)
				sql += "(Description LIKE ?) AND ";

		sql = sql.substring(0, sql.length() - 4);

		if (cityName == null && cityDescription == null)
			sql = "SELECT ID FROM " + Table.City.getValue() + " WHERE True";

		PreparedStatement gt = conn.prepareStatement(sql);
		if (cityName != null)
			gt.setString(counter++, "%" + cityName + "%");

		if (cityDescription != null)
			for (int i = 0; i < len; i++)
				gt.setString(counter++, "%" + words[i] + "%");

		return queryToList(gt);

	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param userName
	 * @param password
	 * @param table:   user type
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 */
	private static ArrayList<Integer> searchNonSecuredUser(String userName, String password, String table) throws SQLException
	{

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		int counter = 1;
		String sql = "SELECT ID FROM " + table + " WHERE ";
		if (userName != null)
			sql += "Username=? AND ";
		if (password != null)
			sql += "Password=? AND ";
		sql = sql.substring(0, sql.length() - 4);

		if (userName == null && password == null)
			sql = "SELECT ID FROM " + table + " WHERE True";

		PreparedStatement gt = conn.prepareStatement(sql);
		if (userName != null)
			gt.setString(counter++, userName);

		if (password != null)
			gt.setString(counter++, password);

		return queryToList(gt);

	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param userName the user name we want to search
	 * @param password the password we want to search
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchNonSecuredCustomer(String userName, String password) throws SQLException
	{
		return searchNonSecuredUser(userName, password, Table.Customer.getValue());
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param userName the user name we want to search
	 * @param password the password we want to search
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchNonSecuredEmployee(String userName, String password) throws SQLException
	{
		return searchUser(userName, password, Table.Employee.getValue());
	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param mapId   the map id we want to search
	 * @param placeId the place id we want to search
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchLocation(Integer mapId, Integer placeId) throws SQLException
	{

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
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

	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param routeId the route id we want to search
	 * @param placeId the place id we want to search
	 * @param numStop the number stop we want to search
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchRouteStop(Integer routeId, Integer placeId, Integer numStop)
			throws SQLException
	{

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
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

	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityDataVersionId the city data version id we want to search
	 * @param mapId             the map id we want to search
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchMapSight(Integer cityDataVersionId, Integer mapId) throws SQLException
	{

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
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

	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityDataVersionId the city data version id we want to search
	 * @param placeId           the place id we want to search
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchPlaceOfInterestSight(Integer cityDataVersionId, Integer placeId)
			throws SQLException
	{

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
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

	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityDataVersionId the city data version id we want to search
	 * @param routeId           the route id we want to search
	 * @param isFavorite        if we want to search favorite or not
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchRouteSight(Integer cityDataVersionId, Integer routeId, Boolean isFavorite)
			throws SQLException
	{

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
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

	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityId the city id we want to search
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchCityDataVersion(Integer cityId) throws SQLException
	{

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		int counter = 1;
		String sql = "SELECT ID FROM " + Table.CityDataVersion.getValue() + " WHERE ";
		if (cityId != null)
			sql += "CityID=? AND ";
		sql = sql.substring(0, sql.length() - 4);

		PreparedStatement gt = conn.prepareStatement(sql);
		if (cityId != null)
			gt.setInt(counter++, cityId);

		return queryToList(gt);

	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param userId the user id we want to search
	 * @param cityId the city id we want to search
	 * @param date   the data we want to search
	 * @param active if we want to search active or not
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchSubscription(Integer userId, Integer cityId, LocalDate date, Boolean active)
			throws SQLException
	{

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
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
			gt.setDate(counter++, java.sql.Date.valueOf(date.plusDays(1)));

		return queryToList(gt);

	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param userId       the user id we want to search
	 * @param cityId       the city id we want to search
	 * @param purchaseDate the purchase data we want to search
	 * @param wasDownload  if we want to search download or not
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 */
	public static ArrayList<Integer> searchOneTimePurchase(Integer userId, Integer cityId, LocalDate purchaseDate,
			Boolean wasDownload) throws SQLException
	{

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
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
			gt.setDate(counter++, java.sql.Date.valueOf(purchaseDate.plusDays(1)));

		if (wasDownload != null)
			gt.setBoolean(counter++, wasDownload);

		return queryToList(gt);

	}

	/**
	 * search function. if a parameter is null, we ignore it.
	 * 
	 * @param cityId              the city id we want to search
	 * @param date                the data we want to search
	 * @param dateFrom            the data from we want to search
	 * @param dateEnd             the data end we want to search
	 * @param newVersionPublished the new version published we want to search
	 * @param validNumMaps        the number of valid maps we want to search
	 * @return the result list.
	 * @throws SQLException if the access to database failed
	 * 
	 */
	public static ArrayList<Integer> searchStatistic(Integer cityId, LocalDate date, LocalDate dateFrom,
			LocalDate dateEnd, Boolean newVersionPublished, Boolean validNumMaps) throws SQLException
	{
		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		int counter = 1;
		String sql = "SELECT ID FROM " + Table.Statistic.getValue() + " WHERE ";
		if (cityId != null)
			sql += "CityId=? AND ";

		if (dateFrom != null)
			sql += "(Date >= ?) AND ";

		if (dateEnd != null)
			sql += "(Date <= ?) AND ";

		if (date != null)
			sql += "Date=? AND ";

		if (newVersionPublished != null)
			sql += "NVP=? AND ";

		if (validNumMaps != null)
			sql += "NumMaps>=0 AND ";

		sql = sql.substring(0, sql.length() - 4);

		if (cityId == null && date == null && dateFrom == null && dateEnd == null && newVersionPublished == null
				&& validNumMaps == null)
			sql = "SELECT ID FROM " + Table.Statistic.getValue() + " WHERE True";

		PreparedStatement gt = conn.prepareStatement(sql);

		if (cityId != null)
			gt.setInt(counter++, cityId);

		if (dateFrom != null)
			gt.setDate(counter++, java.sql.Date.valueOf(dateFrom.plusDays(1)));

		if (dateEnd != null)
			gt.setDate(counter++, java.sql.Date.valueOf(dateEnd.plusDays(1)));

		if (date != null)
			gt.setDate(counter++, java.sql.Date.valueOf(date.plusDays(1)));

		if (newVersionPublished != null)
			gt.setBoolean(counter++, newVersionPublished);

		return queryToList(gt);
	}

	/**
	 * returns the row with id=id on table.
	 * 
	 * @param table: where to look
	 * @param id:    target ID
	 * @return last element from result set
	 * @throws SQLException if the access to database failed
	 */
	private static ResultSet get(String table, int id) throws SQLException
	{

		if (conn == null)
		{

			System.err.println("No connection found");
			throw new DatabaseException();
		}
		String sql = "SELECT * FROM " + table + " WHERE ID=?";
		PreparedStatement gt = conn.prepareStatement(sql);
		gt.setInt(1, id);
		ResultSet res = gt.executeQuery();
		if (!res.next())
			return null;
		res.last();
		return res;

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the place of interest we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static PlaceOfInterest getPlaceOfInterestById(int id) throws SQLException
	{

		ResultSet res = get(Table.PlaceOfInterest.getValue(), id);
		if (res == null)
			return null;
		return PlaceOfInterest._createPlaceOfInterest(res.getInt("ID"), res.getInt("CityID"), res.getString("Name"),
				PlaceOfInterest.PlaceType.values()[res.getInt("Type")], res.getString("Description"),
				res.getInt("ATD") != 0);

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the map we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static Map getMapById(int id) throws SQLException
	{

		ResultSet res = get(Table.Map.getValue(), id);
		if (res == null)
			return null;
		return Map._createMap(res.getInt("ID"), res.getInt("CityID"), res.getString("Name"), res.getString("Info"),
				res.getString("imgURL"));

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the route we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static Route getRouteById(int id) throws SQLException
	{

		ResultSet res = get(Table.Route.getValue(), id);
		if (res == null)
			return null;
		return Route._createRoute(res.getInt("ID"), res.getInt("CityID"), res.getString("Name"), res.getString("Info"),
				res.getBoolean("IsFavorite"));

	}

	/**
	 * return the city name with the corresponding id
	 * 
	 * @param id the id of the city we want to get
	 * @return the name of the city
	 * @throws SQLException if the access to database failed
	 */
	public static String getCityNameById(int id) throws SQLException
	{

		ResultSet res = get(Table.City.getValue(), id);
		if (res == null)
			return null;
		return res.getString("Name");

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the city we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static City getCityById(int id) throws SQLException
	{

		ResultSet res = get(Table.City.getValue(), id);
		if (res == null)
			return null;
		return City._createCity(res.getInt("ID"), res.getString("Name"), res.getString("Description"),
				res.getInt("VersionID") == -1 ? null : res.getInt("VersionID"), res.getBoolean("MNtP"),
				res.getDouble("PriceOneTime"), res.getDouble("PricePeriod"), res.getBoolean("CEONTAP"),
				res.getDouble("TBPriceOneTime"), res.getDouble("TBPricePeriod"));

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the cutomer we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static Customer getCustomerById(int id) throws SQLException
	{

		ResultSet res = get(Table.Customer.getValue(), id);
		if (res == null)
			return null;
		return Customer._createCustomer(res.getInt("ID"), res.getString("Username"), res.getString("Password"),
				res.getString("Email"), res.getString("FirstName"), res.getString("LastName"),
				res.getString("PhoneNumber"), res.getString("CardNum"), res.getString("Exp"), res.getString("CVC"));

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the employee we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static Employee getEmployeeById(int id) throws SQLException
	{

		ResultSet res = get(Table.Employee.getValue(), id);
		if (res == null)
			return null;
		return Employee._createEmployee(res.getInt("ID"), res.getString("Username"), res.getString("Password"),
				res.getString("Email"), res.getString("FirstName"), res.getString("LastName"),
				res.getString("PhoneNumber"), Employee.Role.values()[res.getInt("Role")]);

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the location we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static Location _getLocationById(int id) throws SQLException
	{

		ResultSet res = get(Table.Location.getValue(), id);
		if (res == null)
			return null;
		double[] coordinates = { res.getInt("x"), res.getInt("y") };
		return Location._createLocation(res.getInt("ID"), res.getInt("MapID"), res.getInt("POIID"), coordinates);

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the route stop we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static RouteStop _getRouteStopById(int id) throws SQLException
	{

		ResultSet res = get(Table.RouteStop.getValue(), id);
		if (res == null)
			return null;
		return RouteStop._createRouteStop(res.getInt("ID"), res.getInt("RouteID"), res.getInt("PlaceID"),
				res.getInt("NumStops"), res.getTime("Time").toLocalTime());

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the map sight we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static MapSight _getMapSightById(int id) throws SQLException
	{

		ResultSet res = get(Table.MapSight.getValue(), id);
		if (res == null)
			return null;
		return MapSight._createMapSight(res.getInt("ID"), res.getInt("MapID"), res.getInt("CityDataVersionID"));

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the place of interest sight we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static PlaceOfInterestSight _getPlaceOfInterestSightById(int id) throws SQLException
	{

		ResultSet res = get(Table.PlaceOfInterestSight.getValue(), id);
		if (res == null)
			return null;
		return PlaceOfInterestSight._PlaceOfInterestSight(res.getInt("ID"), res.getInt("CityDataVersions"),
				res.getInt("POIID"));

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the route sight we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static RouteSight _getRouteSightById(int id) throws SQLException
	{

		ResultSet res = get(Table.RouteSight.getValue(), id);
		if (res == null)
			return null;
		return RouteSight._createRouteSight(res.getInt("ID"), res.getInt("CityDataVersions"), res.getInt("RouteID"));

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the city data version we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static CityDataVersion _getCityDataVersionById(int id) throws SQLException
	{

		ResultSet res = get(Table.CityDataVersion.getValue(), id);
		return CityDataVersion._createCityDataVersion(res.getInt("ID"), res.getInt("CityID"),
				res.getString("VersionName"));

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the subscription we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static Subscription _getSubscriptionById(int id) throws SQLException
	{

		ResultSet res = get(Table.Subscription.getValue(), id);
//			if (res == null) System.out.println("why god why");
		return Subscription._createSubscription(res.getInt("ID"), res.getInt("CityID"), res.getInt("UserID"),
				res.getDate("PurchaseDate").toLocalDate(), res.getDouble("FullPrice"), res.getDouble("PricePayed"),
				res.getDate("ExpDate").toLocalDate());

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the one time purchase we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static OneTimePurchase _getOneTimePurchaseById(int id) throws SQLException
	{

		ResultSet res = get(Table.OneTimePurchase.getValue(), id);
		return OneTimePurchase._createOneTimePurchase(res.getInt("ID"), res.getInt("CityID"), res.getInt("UserID"),
				res.getDate("PurchaseDate").toLocalDate(), res.getDouble("FullPrice"), res.getDouble("PricePayed"),
				res.getBoolean("WasDownloaded"));

	}

	/**
	 * builds an object from the entry with this id.
	 * 
	 * @param id the id of the statistic we want to get
	 * @return the new object
	 * @throws SQLException if the access to database failed
	 */
	public static Statistic _getStatisticById(int id) throws SQLException
	{
		ResultSet res = get(Table.Statistic.getValue(), id);
		return Statistic._createStatistic(res.getInt("ID"), res.getInt("CityID"), res.getDate("Date").toLocalDate(),
				res.getInt("NOTP"), res.getInt("NS"), res.getInt("NSR"), res.getInt("NV"), res.getInt("NSD"),
				res.getBoolean("NVP"), res.getInt("NumMaps"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize() clean the connection when finished.
	 */
	@Override
	protected void finalize() throws Throwable
	{
		closeConnection();
	}
}