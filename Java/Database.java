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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void closeConnection() {
		try {
			if (conn != null)
				conn.close();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	// generate ID's

	public static int generateIdUser() {
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.User.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.User.getValue());
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	public static int generateIdPlaceOfInterest() {// first example
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.PlaceOfInterest.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.PlaceOfInterest.getValue());
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	public static int generateIdMap() {
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.Map.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.Map.getValue());
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	public static int generateIdLocation() {
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.Location.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.Location.getValue());
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	public static int generateIdCityDataVersion() {
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.CityDataVersion.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.CityDataVersion.getValue());
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	public static int generateIdRoute() {
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.Route.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.Route.getValue());
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	public static int generateIdCityPurchase() {
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.CityPurchase.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.CityPurchase.getValue());
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	public static int generateIdCity() {
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.City.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.City.getValue());
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	public static int generateIdRouteStop() {
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.RouteStop.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.RouteStop.getValue());
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	public static int generateIdMapSight() {
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.MapSight.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.MapSight.getValue());
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	public static int generateIdPlaceOfInterestSight() {
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.PlaceOfInterestSight.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.PlaceOfInterestSight.getValue());
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	public static int generateIdRouteSight() {
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.RouteSight.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.RouteSight.getValue());
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	public static int generateIdStatistic() {
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.Statistic.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter = res.getInt("Counter") + 1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.Statistic.getValue());
			su.executeUpdate();
			return counter;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return -1;
		}
	}

	private static boolean existPlaceOfInterest(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.PlaceOfInterest.getValue() + " WHERE ID=?";
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

	private static boolean existMap(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.Map.getValue() + " WHERE ID=?";
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

	private static boolean existRoute(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.Route.getValue() + " WHERE ID=?";
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

	private static boolean existCity(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.City.getValue() + " WHERE ID=?";
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

	private static boolean existCustomer(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.Customer.getValue() + " WHERE ID=?";
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

	private static boolean existEmployee(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.Employee.getValue() + " WHERE ID=?";
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

	private static boolean existLocation(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.Location.getValue() + " WHERE ID=?";
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

	private static boolean existRouteStop(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.RouteStop.getValue() + " WHERE ID=?";
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

	private static boolean existMapSight(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.MapSight.getValue() + " WHERE ID=?";
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

	private static boolean existPlaceOfInterestSight(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.PlaceOfInterestSight.getValue() + " WHERE ID=?";
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

	private static boolean existRouteSight(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.RouteSight.getValue() + " WHERE ID=?";
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

	private static boolean existCityDataVersion(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.CityDataVersion.getValue() + " WHERE ID=?";
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

	private static boolean existSubscription(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.Subscription.getValue() + " WHERE ID=?";
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

	private static boolean existOneTimePurchase(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.OneTimePurchase.getValue() + " WHERE ID=?";
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

	private static boolean existStatistic(int id) {
		try {
			String sql = "SELECT ID FROM " + Table.Statistic.getValue() + " WHERE ID=?";
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
				String sql = "UPDATE " + Table.Route.getValue() + " SET Info=?, ATD=?, NumStops=?, CityID=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setString(1, p.getInfo());
				su.setBoolean(2, p.isAcceptabilityToDisabled());
				su.setInt(3, p.getNumStops());
				su.setInt(4, p.getCityId());
				su.setInt(5, p.getId());
				su.executeUpdate();
				return true;
			} else {
				String sql = "INSERT INTO " + Table.Route.getValue()
						+ " (ID,Info, ATD, NumStops, CityID) VALUES (?,?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setString(2, p.getInfo());
				su.setBoolean(3, p.isAcceptabilityToDisabled());
				su.setInt(4, p.getNumStops());
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

	public static boolean saveCity(City p) {
		try {
			if (existCity(p.getId())) {
				String sql = "UPDATE " + Table.City.getValue() + " SET Name=?, Description=?, VersionID=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setString(1, p.getCityName());
				su.setString(2, p.getCityDescription());
				su.setInt(3, p.getPublishedVersionId());
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
				su.setInt(4, p.getPublishedVersionId());
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
						+ " (ID,CityDataVersions, RouteID) VALUES (?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getCityDataVersionId());
				su.setInt(3, p.getRouteId());
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

	public static boolean deletePlaceOfInterest(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.PlaceOfInterest.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteMap(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.Map.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteRoute(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.Route.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteCity(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.City.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteCustomer(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.Customer.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteEmployee(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.Employee.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean _deleteLocation(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.Location.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean _deleteRouteStop(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.RouteStop.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean _deleteMapSight(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.MapSight.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean _deletePlaceOfInterestSight(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.PlaceOfInterestSight.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean _deleteRouteSight(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.RouteSight.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean _deleteCityDataVersion(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.CityDataVersion.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean _deleteSubscription(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.Subscription.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean _deleteOneTimePurchase(int placeId) // return true if the item was deleted
	{
		try {
			String sql = "DELETE FROM " + Table.OneTimePurchase.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean _deleteStatistic(int statisticId) {
		try {
			String sql = "DELETE FROM " + Table.Statistic.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, statisticId);
			int count = gt.executeUpdate();
			return count != 0;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return false;
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
			if (placeName != null) {
				gt.setString(counter, placeName);
				counter += 1;
			}
			if (placeDescription != null) {
				gt.setString(counter, placeDescription);
				counter += 1;
			}
			if (cityId != null) {
				gt.setInt(counter, cityId);
				counter += 1;
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
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
			if (cityId != null) {
				gt.setInt(counter, cityId);
				counter += 1;
			}
			if (name != null) {
				gt.setString(counter, name);
				counter += 1;
			}
			if (info != null) {
				gt.setString(counter, info);
				counter += 1;
			}
			if (imgURL != null) {
				gt.setString(counter, imgURL);
				counter += 1;
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static ArrayList<Integer> searchRoute(Integer cityId, String info, Boolean acceptabilityToDisabled) {
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.Route.getValue() + " WHERE ";
			if (cityId != null)
				sql += "CityID=? AND ";
			if (info != null)
				sql += "Info=? AND ";
			if (acceptabilityToDisabled != null)
				sql += "ATD=? AND ";
			sql = sql.substring(0, sql.length() - 4);
			PreparedStatement gt = conn.prepareStatement(sql);
			if (cityId != null) {
				gt.setInt(counter, cityId);
				counter += 1;
			}
			if (info != null) {
				gt.setString(counter, info);
				counter += 1;
			}
			if (acceptabilityToDisabled != null) {
				gt.setBoolean(counter, acceptabilityToDisabled);
				counter += 1;
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
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
			if (cityName != null) {
				gt.setString(counter, cityName);
				counter += 1;
			}
			if (cityDescription != null) {
				gt.setString(counter, cityDescription);
				counter += 1;
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
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
			if (userName != null) {
				gt.setString(counter++, userName);
			}
			if (password != null) {
				gt.setString(counter++, password);
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
				System.out.println(res.getString("Username"));

			}
			return IDs;
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
			if (mapId != null) {
				gt.setInt(counter, mapId);
				counter += 1;
			}
			if (placeId != null) {
				gt.setInt(counter, placeId);
				counter += 1;
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
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
			if (routeId != null) {
				gt.setInt(counter, routeId);
				counter += 1;
			}
			if (placeId != null) {
				gt.setInt(counter, placeId);
				counter += 1;
			}
			if (numStop != null) {
				gt.setInt(counter, numStop);
				counter += 1;
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
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
			if (cityDataVersionId != null) {
				gt.setInt(counter, cityDataVersionId);
				counter += 1;
			}
			if (mapId != null) {
				gt.setInt(counter, mapId);
				counter += 1;
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
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
			if (cityDataVersionId != null) {
				gt.setInt(counter, cityDataVersionId);
				counter += 1;
			}
			if (placeId != null) {
				gt.setInt(counter, placeId);
				counter += 1;
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
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
			if (cityDataVersionId != null) {
				gt.setInt(counter, cityDataVersionId);
				counter += 1;
			}
			if (routeId != null) {
				gt.setInt(counter, routeId);
				counter += 1;
			}
			if (isFavorite != null) {
				gt.setBoolean(counter, isFavorite);
				counter += 1;
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
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
			if (cityId != null) {
				gt.setInt(counter, cityId);
				counter += 1;
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static ArrayList<Integer> searchSubscription(Integer userId, Integer cityId, Date purchaseDate, Date date,
			Boolean afterDate) // fix this - RON
	{
		try {
			int counter = 1;
			String sql = "SELECT ID FROM " + Table.Subscription.getValue() + " WHERE ";
			if (userId != null)
				sql += "UserID=? AND ";
			if (cityId != null)
				sql += "CityID=? AND ";
			if (purchaseDate != null)
				sql += "PurchaseDate=? AND ";
			if (date != null)
				sql += "ExpDate=? AND ";
			if (afterDate != null)
				sql += "CityID=? AND ";
			sql = sql.substring(0, sql.length() - 4);

			PreparedStatement gt = conn.prepareStatement(sql);
			if (userId != null) {
				gt.setInt(counter, userId);
				counter += 1;
			}
			if (cityId != null) {
				gt.setInt(counter, cityId);
				counter += 1;
			}
			if (purchaseDate != null) {
				gt.setDate(counter, purchaseDate);
				counter += 1;
			}
			if (date != null) {
				gt.setDate(counter, date);
				counter += 1;
			}
			if (afterDate != null) {
				gt.setBoolean(counter, afterDate);
				counter += 1;
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
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
			if (userId != null) {
				gt.setInt(counter, userId);
				counter += 1;
			}
			if (cityId != null) {
				gt.setInt(counter, cityId);
				counter += 1;
			}
			if (purchaseDate != null) {
				gt.setDate(counter, purchaseDate);
				counter += 1;
			}
			if (wasDownload != null) {
				gt.setBoolean(counter, wasDownload);
				counter += 1;
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
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

			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while (res.next()) {
				int id = res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static PlaceOfInterest getPlaceOfInterestById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.PlaceOfInterest.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return PlaceOfInterest._createPlaceOfInterest(res.getInt("ID"), res.getInt("CityID"), res.getString("Name"),
					PlaceOfInterest.PlaceType.values()[res.getInt("Type")], res.getString("Description"),
					res.getInt("ATD") != 0);
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static Map getMapById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.Map.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return Map._createMap(res.getInt("ID"), res.getInt("CityID"), res.getString("Name"), res.getString("Info"),
					res.getString("imgURL"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static Route getRouteById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.Route.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return Route._createRoute(res.getInt("ID"), res.getInt("CityID"), res.getString("Info"),
					res.getBoolean("ATD"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static City getCityById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.City.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return City._createCity(res.getInt("ID"), res.getString("Name"), res.getString("Description"),
					res.getInt("VersionID"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static Customer getCustomerById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.Customer.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return Customer._createCustomer(res.getInt("ID"), res.getString("Username"), res.getString("Password"),
					res.getString("Email"), res.getString("FirstName"), res.getString("LastName"),
					res.getString("PhoneNumber"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static Employee getEmployeeById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.Employee.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return Employee._createEmployee(res.getInt("ID"), res.getString("Username"), res.getString("Password"),
					res.getString("Email"), res.getString("FirstName"), res.getString("LastName"),
					res.getString("PhoneNumber"), Employee.Role.values()[res.getInt("Role")]);
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static Location _getLocationById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.Location.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			double[] coordinates = { res.getInt("x"), res.getInt("y") };
			return Location._createLocation(res.getInt("ID"), res.getInt("MapID"), res.getInt("POIID"), coordinates);
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static RouteStop _getRouteStopById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.RouteStop.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return RouteStop._createRouteStop(res.getInt("ID"), res.getInt("RouteID"), res.getInt("PlaceID"),
					res.getInt("NumStops"), res.getTime("Time"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static MapSight _getMapSightById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.MapSight.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return MapSight._createMapSight(res.getInt("ID"), res.getInt("MapID"), res.getInt("CityDataVersionID"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static PlaceOfInterestSight _getPlaceOfInterestSightById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.PlaceOfInterestSight.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return PlaceOfInterestSight._PlaceOfInterestSight(res.getInt("ID"), res.getInt("CityDataVersions"),
					res.getInt("POIID"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static RouteSight _getRouteSightById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.RouteSight.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return RouteSight._createRouteSight(res.getInt("ID"), res.getInt("CityDataVersions"), res.getInt("RouteID"),
					res.getBoolean("IsFavorite"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static CityDataVersion _getCityDataVersionById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.CityDataVersion.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return CityDataVersion._createCityDataVersion(res.getInt("ID"), res.getInt("CityID"),
					res.getString("VersionName"), res.getDouble("PriceOneTime"), res.getDouble("PricePeriod"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static Subscription _getSubscriptionById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.Subscription.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return Subscription._createSubscription(res.getInt("ID"), res.getInt("CityID"), res.getInt("UserID"),
					res.getDate("PurchaseDate"), res.getDouble("FullPrice"), res.getDouble("PricePayed"),
					res.getDate("ExpDate"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static OneTimePurchase _getOneTimePurchaseById(int placeId) {
		try {
			String sql = "SELECT * FROM " + Table.OneTimePurchase.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
			return OneTimePurchase._createOneTimePurchase(res.getInt("ID"), res.getInt("CityID"), res.getInt("UserID"),
					res.getDate("PurchaseDate"), res.getDouble("FullPrice"), res.getDouble("PricePayed"),
					res.getBoolean("WasDownloaded"));
		} catch (Exception e) {
			closeConnection();
			e.printStackTrace();
			return null;
		}
	}

	public static Statistic _getStatisticById(int statisticId) {
		try {
			String sql = "SELECT * FROM " + Table.Statistic.getValue() + " WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, statisticId);
			ResultSet res = gt.executeQuery();
			if (!res.next())
				return null;
			res.last();
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