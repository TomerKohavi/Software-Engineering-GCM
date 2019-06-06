import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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

	public enum Counter {
		PlaceOfInterest(0), User(1), Map(2), Location(3), CityDataVersion(4), Route(5), CityPurchase(6), City(7),
		RouteStop(8), MapSight(9), PlaceOfInterestSight(10), RouteSight(11), Statistic(12);

		private final int value;

		Counter(final int nv) {
			value = nv;
		}

		public int getValue() {
			return value;
		}
	}

	public enum Table {
		PlaceOfInterest("POIs"), Map("Maps"), Route("Routes"), City("Cities"), Customer("Customers"),
		Employee("Employees"), Location("Locations"), RouteStop("RouteStop"), MapSight("MapSights"),
		PlaceOfInterestSight("POISights"), RouteSight("RouteSights"), CityDataVersion("CityDataVersions"),
		Subscription("Subscription"), OneTimePurchase("OneTimePurchase"), Statistic("Statistics");

		private final String url;

		Table(final String envUrl) {
			url = envUrl;
		}

		public String getValue() {
			return url;
		}
	}

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

	public static void closeConnection() {
		try {
			if (conn != null)
				{
				conn.close();
				System.out.println("connection closing");
				}
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	// generate ID's

	private static int generateId(int type)
	{
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
	
	public static int generateIdUser() {
		return generateId(Counter.User.getValue());
	}

	public static int generateIdPlaceOfInterest() {// first example
		return generateId(Counter.PlaceOfInterest.getValue());

	}

	public static int generateIdMap() {
		return generateId(Counter.Map.getValue());
	}

	public static int generateIdLocation() {
		return generateId(Counter.Location.getValue());
	}

	public static int generateIdCityDataVersion() {
		return generateId(Counter.CityDataVersion.getValue());
	}

	public static int generateIdRoute() {
		return generateId(Counter.Route.getValue());
	}

	public static int generateIdCityPurchase() {
		return generateId(Counter.CityPurchase.getValue());
	}

	public static int generateIdCity() {
		return generateId(Counter.City.getValue());
	}

	public static int generateIdRouteStop() {
		return generateId(Counter.RouteStop.getValue());
	}

	public static int generateIdMapSight() {
		return generateId(Counter.MapSight.getValue());
	}

	public static int generateIdPlaceOfInterestSight() {
		return generateId(Counter.PlaceOfInterestSight.getValue());
	}

	public static int generateIdRouteSight() {
		return generateId(Counter.RouteSight.getValue());
	}

	public static int generateIdStatistic() {
		return generateId(Counter.Statistic.getValue());
	}

	private static boolean exist(String table, int id)
	{
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
	
	private static boolean existPlaceOfInterest(int id) {
		return exist(Table.PlaceOfInterest.getValue(), id);
	}

	private static boolean existMap(int id) {
		return exist(Table.Map.getValue(), id);
	}

	private static boolean existRoute(int id) {
		return exist(Table.Route.getValue(), id);
	}

	private static boolean existCity(int id) {
		return exist(Table.City.getValue(), id);
	}

	private static boolean existCustomer(int id) {
		return exist(Table.Customer.getValue(), id);
	}

	private static boolean existEmployee(int id) {
		return exist(Table.Employee.getValue(), id);
	}

	private static boolean existLocation(int id) {
		return exist(Table.Employee.getValue(), id);
	}

	private static boolean existRouteStop(int id) {
		return exist(Table.RouteStop.getValue(), id);
	}

	private static boolean existMapSight(int id) {
		return exist(Table.MapSight.getValue(), id);
	}

	private static boolean existPlaceOfInterestSight(int id) {
		return exist(Table.PlaceOfInterestSight.getValue(), id);
	}

	private static boolean existRouteSight(int id) {
		return exist(Table.RouteSight.getValue(), id);
	}

	private static boolean existCityDataVersion(int id) {
		return exist(Table.CityDataVersion.getValue(), id);
	}

	private static boolean existSubscription(int id) {
		return exist(Table.Subscription.getValue(), id);
	}

	private static boolean existOneTimePurchase(int id) {
		return exist(Table.OneTimePurchase.getValue(), id);
	}

	private static boolean existStatistic(int id) {
		return exist(Table.Statistic.getValue(), id);
	}

	public static boolean savePlaceOfInterest(PlaceOfInterest p)// return true if it's already in the database
	{
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

	public static boolean saveCustomer(Customer p) {
		try {
			if (existCustomer(p.getId())) {
				String sql = "UPDATE " + Table.Customer.getValue()
						+ " SET Username=?, Password=?, Email=?, FirstName=?, LastName=?,"
						+ " PhoneNumber=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setString(1, p.getUserName());
				su.setString(2, p.getPassword());
				su.setString(3, p.getEmail());
				su.setString(4, p.getFirstName());
				su.setString(5, p.getLastName());
				su.setString(6, p.getPhoneNumber());
				su.setInt(7, p.getId());
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
						+ "(ID,Username, Password, Email, FirstName, LastName, PhoneNumber) VALUES "
						+ "(?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setString(2, p.getUserName());
				su.setString(3, p.getPassword());
				su.setString(4, p.getEmail());
				su.setString(5, p.getFirstName());
				su.setString(6, p.getLastName());
				su.setString(7, p.getPhoneNumber());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

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
						+ " (ID,Username, Password, Email, FirstName, LastName)" + " VALUES (?, ?, ?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setString(2, p.getUserName());
				su.setString(3, p.getPassword());
				su.setString(4, p.getEmail());
				su.setString(5, p.getFirstName());
				su.setString(6, p.getLastName());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	public static boolean _saveLocation(Location p) // friend to Map
	{
		try {
			if (existLocation(p.getId())) {
				String sql = "UPDATE " + Table.Location.getValue() + " SET ID=?, MapID=?, POIID=?, x=?, y=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getMapId());
				su.setInt(3, p.getPlaceOfInterestId());
				su.setDouble(4, p.getCoordinates()[0]);
				su.setDouble(5, p.getCoordinates()[1]);
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
				su.setLong(4, p.getRecommendedTime().getTime()); // fix here - RON
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
				su.setLong(5, p.getRecommendedTime().getTime());// fix here - RON
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

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

	public static boolean _saveStatistic(Statistic p) {
		try {
			if (existStatistic(p.getId())) {
				String sql = "UPDATE " + Table.OneTimePurchase.getValue()
						+ " SET CityID=?, Date=?, NOTP=?, NS=?, NR=?, NV=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getCityId());
				su.setDate(2, (Date) p.getDate());
				su.setInt(3, p.getNumOneTimePurchases()); // fix here - RON
				su.setInt(4, p.getNumSubscriptions());
				su.setInt(5, p.getNumSubscriptionsRenewal());
				su.setInt(6, p.getNumVisited());
				su.setInt(7, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.OneTimePurchase.getValue()
						+ " (ID,CityID, Date, NOTP, NS, NSR, NV) VALUES (?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getCityId());
				su.setDate(3, (Date) p.getDate());
				su.setInt(4, p.getNumOneTimePurchases()); // fix here - RON
				su.setInt(5, p.getNumSubscriptions());
				su.setInt(6, p.getNumSubscriptionsRenewal());
				su.setInt(7, p.getNumVisited());
				su.executeUpdate();
				return false;
			}
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
		}
		return false;
	}

	private static boolean delete(String table, int id)
	{
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

	public static boolean deletePlaceOfInterest(int id) // return true if the item was deleted
	{
		return delete(Table.PlaceOfInterest.getValue(), id);
	}

	public static boolean deleteMap(int id) // return true if the item was deleted
	{
		return delete(Table.Map.getValue(), id);
	}

	public static boolean deleteRoute(int id) // return true if the item was deleted
	{
		return delete(Table.Route.getValue(), id);
	}

	public static boolean deleteCity(int id) // return true if the item was deleted
	{
		return delete(Table.City.getValue(), id);
	}

	public static boolean deleteCustomer(int id) // return true if the item was deleted
	{
		return delete(Table.Customer.getValue(), id);
	}

	public static boolean deleteEmployee(int id) // return true if the item was deleted
	{
		return delete(Table.Employee.getValue(), id);
	}

	public static boolean _deleteLocation(int id) // return true if the item was deleted
	{
		return delete(Table.Location.getValue(), id);
	}

	public static boolean _deleteRouteStop(int id) // return true if the item was deleted
	{
		return delete(Table.RouteStop.getValue(), id);
	}

	public static boolean _deleteMapSight(int id) // return true if the item was deleted
	{
		return delete(Table.MapSight.getValue(), id);
	}

	public static boolean _deletePlaceOfInterestSight(int id) // return true if the item was deleted
	{
		return delete(Table.PlaceOfInterestSight.getValue(), id);
	}

	public static boolean _deleteRouteSight(int id) // return true if the item was deleted
	{
		return delete(Table.RouteSight.getValue(), id);
	}

	public static boolean _deleteCityDataVersion(int id) // return true if the item was deleted
	{
		return delete(Table.CityDataVersion.getValue(), id);
	}

	public static boolean _deleteSubscription(int id) // return true if tid)he item was deleted
	{
		return delete(Table.Subscription.getValue(), id);
	}

	public static boolean _deleteOneTimePurchase(int id) // return true if the item was deleted
	{
		return delete(Table.OneTimePurchase.getValue(), id);
	}

	public static boolean _deleteStatistic(int id) {
		return delete(Table.Statistic.getValue(), id);
	}

	private static ArrayList<Integer> queryToList(PreparedStatement gt){
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

	public static ArrayList<Integer> searchPlaceOfInterest(String placeName, String placeDescription, Integer cityId) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.PlaceOfInterest.getValue() + " WHERE ";
			if (placeName != null)
				sql += "Name=? AND ";
			if (placeDescription != null)
				sql += "Description=? AND ";
			if (cityId != null)
				sql += "CityID=? AND ";
			sql = sql.substring(0, sql.length() - 4);

			PreparedStatement gt = conn.prepareStatement(sql);
			if (placeName != null) 
				gt.setString(counter++, placeName);
			
			if (placeDescription != null) 
				gt.setString(counter++, placeDescription);
				
			if (cityId != null) 
				gt.setInt(counter++, cityId);
				
			return queryToList(gt);
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

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

	public static ArrayList<Integer> searchCity(String cityName, String cityDescription) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.City.getValue() + " WHERE ";
			if (cityName != null)
				sql += "Name=? AND ";
			if (cityDescription != null)
				sql += "Description=? AND ";
			sql = sql.substring(0, sql.length() - 4);

			PreparedStatement gt = conn.prepareStatement(sql);
			if (cityName != null) 
				gt.setString(counter++, cityName);

			if (cityDescription != null)
				gt.setString(counter++, cityDescription);
				
			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	private static ArrayList<Integer> searchUser(String userName, String password, String table) {
		try {
			int counter = 1;
			String sql = "SELECT ID, Username, Password FROM " + table + " WHERE ";
			if (userName != null)
				sql += "Username=? AND ";
			if (password != null)
				sql += "Password=? AND ";
			sql = sql.substring(0, sql.length() - 4);
			System.out.println(sql);
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

	public static ArrayList<Integer> searchCustomer(String userName, String password) {
		return searchUser(userName, password, Table.Customer.getValue());
	}

	public static ArrayList<Integer> searchEmployee(String userName, String password) {
		return searchUser(userName, password, Table.Employee.getValue());
	}

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

	public static ArrayList<Integer> searchSubscription(Integer userId, Integer cityId, Date date,
			Boolean active) // fix this - RON
	{
		try {
			int counter = 1;
			String sql = "SELECT ID, PurchaseDate, ExpDate FROM " + Table.Subscription.getValue() + " WHERE ";
			if (userId != null)
				sql += "UserID=? AND ";
			if (cityId != null)
				sql += "CityID=? AND ";
			if (active)
				sql += "(? BETWEEN PurchaseDate AND ExpDate) AND";
			else
				sql += "(? NOT BETWEEN PurchaseDate AND ExpDate) AND";
			sql = sql.substring(0, sql.length() - 4);

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

	public static ArrayList<Integer> searchStatistic(Integer cityId, Date date, Date dateFrom, Date dateEnd) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.MapSight.getValue() + " WHERE ";
			if (cityId != null)
				sql += "CityDataVersionID=? AND ";

			if (dateFrom != null && dateEnd != null)
				sql += "(Date BETWEEN ? AND ?) AND";

			else if (date != null)
				sql += "Date=? AND ";

			sql = sql.substring(0, sql.length() - 4);
			PreparedStatement gt = conn.prepareStatement(sql);

			if (cityId != null)
				gt.setInt(counter++, cityId);

			if (dateFrom != null && dateEnd != null) {
				gt.setDate(counter++, dateFrom);
				gt.setDate(counter++, dateEnd);
			} else if (date != null)
				gt.setDate(counter++, date);

			return queryToList(gt);

		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	private static ResultSet get(String table, int id)
	{
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
	
	public static PlaceOfInterest getPlaceOfInterestById(int id) {
		try {
			ResultSet res = get(Table.PlaceOfInterest.getValue(), id);
			if(res == null) return null;
			return PlaceOfInterest._createPlaceOfInterest(res.getInt("ID"), res.getInt("CityID"), res.getString("Name"),
					PlaceOfInterest.PlaceType.values()[res.getInt("Type")], res.getString("Description"),
					res.getInt("ATD") != 0);
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static Map getMapById(int id) {
		try {
			ResultSet res = get(Table.Map.getValue(), id);
			if(res == null) return null;
			return Map._createMap(res.getInt("ID"), res.getInt("CityID"), res.getString("Name"), res.getString("Info"),
					res.getString("imgURL"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static Route getRouteById(int id) {
		try {
			ResultSet res = get(Table.Route.getValue(), id);
			if(res == null) return null;
			return Route._createRoute(res.getInt("ID"), res.getInt("CityID"), res.getString("Info"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static City getCityById(int id) {
		try {
			ResultSet res = get(Table.City.getValue(), id);
			if(res == null) return null;
			return City._createCity(res.getInt("ID"), res.getString("Name"), res.getString("Description"),
					res.getInt("VersionID") == -1 ? null : res.getInt("VersionID") );
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static Customer getCustomerById(int id) {
		try {
			ResultSet res = get(Table.Customer.getValue(), id);
			if(res == null) return null;
			return Customer._createCustomer(res.getInt("ID"), res.getString("Username"), res.getString("Password"),
					res.getString("Email"), res.getString("FirstName"), res.getString("LastName"),
					res.getString("PhoneNumber"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static Employee getEmployeeById(int id) {
		try {
			ResultSet res = get(Table.Employee.getValue(), id);
			if(res == null) return null;
			return Employee._createEmployee(res.getInt("ID"), res.getString("Username"), res.getString("Password"),
					res.getString("Email"), res.getString("FirstName"), res.getString("LastName"),
					res.getString("PhoneNumber"), Employee.Role.values()[res.getInt("Role")]);
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static Location _getLocationById(int id) {
		try {
			ResultSet res = get(Table.Location.getValue(), id);
			if(res == null) return null;
			double[] coordinates = { res.getInt("x"), res.getInt("y") };
			return Location._createLocation(res.getInt("ID"), res.getInt("MapID"), res.getInt("POIID"), coordinates);
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static RouteStop _getRouteStopById(int id) {
		try {
			ResultSet res = get(Table.PlaceOfInterest.getValue(), id);
			if(res == null) return null;
			return RouteStop._createRouteStop(res.getInt("ID"), res.getInt("RouteID"), res.getInt("PlaceID"),
					res.getInt("NumStops"), res.getTime("Time"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static MapSight _getMapSightById(int id) {
		try {
			ResultSet res = get(Table.MapSight.getValue(), id);
			if(res == null) return null;
			return MapSight._createMapSight(res.getInt("ID"), res.getInt("MapID"), res.getInt("CityDataVersionID"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static PlaceOfInterestSight _getPlaceOfInterestSightById(int id) {
		try {
			ResultSet res = get(Table.PlaceOfInterestSight.getValue(), id);
			if(res == null) return null;
			return PlaceOfInterestSight._PlaceOfInterestSight(res.getInt("ID"), res.getInt("CityDataVersions"),
					res.getInt("POIID"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static RouteSight _getRouteSightById(int id) {
		try {
			ResultSet res = get(Table.RouteSight.getValue(), id);
			if(res == null) return null;
			return RouteSight._createRouteSight(res.getInt("ID"), res.getInt("CityDataVersions"), res.getInt("RouteID"),
					res.getBoolean("IsFavorite"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

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

	public static Statistic _getStatisticById(int id) {
		try {
			ResultSet res = get(Table.Statistic.getValue(), id);
			return Statistic._createStatistic(res.getInt("ID"), res.getInt("CityID"), res.getDate("Date"),
					res.getInt("NOTP"), res.getInt("NS"), res.getInt("NSR"), res.getInt("NV"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		System.out.println("lior is king");
		closeConnection();
	}
}