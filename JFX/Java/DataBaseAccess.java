import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.net.ssl.SSLException;


public class DataBaseAccess {
	static private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	// update USER, PASS and DB URL according to credentials provided by the website:
	// https://remotemysql.com/
	// in future get those hardcoded string into separated config file.
	static private final String DB = "PrtTXnuWoq";
	static private final String DB_URL = "jdbc:mysql://remotemysql.com/"+ DB + "?useSSL=NO";
	static private final String USER = "PrtTXnuWoq";
	static private final String PASS = "KOzAI33szl";
	static Connection conn = null;
	
    public enum Counter
    {
    	PlaceOfInterest(0),
    	User(1),
    	Map(2),
    	Location(3),
    	CityDataVersion(4),
    	Route(5),
    	CityPurchase(6),
    	City(7),
    	RouteStop(8),
    	MapSight(9),
    	PlaceOfInterestSight(10),
    	RouteSight(11),
    	Statistic(12);
        
        private final int value;
        
        Counter(final int nv)
        {
        	value = nv;
        }
        
        public int getValue()
        {
        	return value;
        }
    }
    public enum Table
    {
    	PlaceOfInterest("POIs");
//    	User(1),
//    	Map(2),
//    	Location(3),
//    	CityDataVersion(4),
//    	Route(5),
//    	CityPurchase(6),
//    	City(7),
//    	RouteStop(8),
//    	MapSight(9),
//    	PlaceOfInterestSight(10),
//    	RouteSight(11),
//    	Statistic(12);
     
        private final String url;
     
        Table(final String envUrl) {
            url = envUrl;
        }
     
        public String getValue() {
            return url;
        }
    }
	public static void createConnection()
	{
		try {
			if(conn==null) {
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
    public static void closeConnection()
	{
		try {
			if(conn!=null)
				conn.close();
			return;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
    
    
    
    // generate ID's
    public static int generateIdPlaceOfInterest() 
	{//first example
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.PlaceOfInterest.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter=res.getInt("Counter")+1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.PlaceOfInterest.getValue());
			su.executeUpdate();
			return counter;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return -1;
		}
	}
    public static int generateIdUser() 
	{
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.User.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter=res.getInt("Counter")+1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.User.getValue());
			su.executeUpdate();
			return counter;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return -1;
		}
	}
    public static int generateIdMap() 
	{
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.Map.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter=res.getInt("Counter")+1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.Map.getValue());
			su.executeUpdate();
			return counter;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return -1;
		}
	}
    public static int generateIdLocation() 
	{
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.Location.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter=res.getInt("Counter")+1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.Location.getValue());
			su.executeUpdate();
			return counter;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return -1;
		}
	}
    public static int generateIdCityDataVersion() 
	{
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.CityDataVersion.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter=res.getInt("Counter")+1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.CityDataVersion.getValue());
			su.executeUpdate();
			return counter;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return -1;
		}
	}
    public static int generateIdRoute() 
	{
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.Route.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter=res.getInt("Counter")+1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.Route.getValue());
			su.executeUpdate();
			return counter;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return -1;
		}
	}
    public static int generateIdCityPurchase() 
	{
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.CityPurchase.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter=res.getInt("Counter")+1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.CityPurchase.getValue());
			su.executeUpdate();
			return counter;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return -1;
		}
	}
    public static int generateIdCity() 
	{
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.City.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter=res.getInt("Counter")+1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.City.getValue());
			su.executeUpdate();
			return counter;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return -1;
		}
	}
    public static int generateIdRouteStop()
	{
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.RouteStop.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter=res.getInt("Counter")+1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.RouteStop.getValue());
			su.executeUpdate();
			return counter;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return -1;
		}
	}
    public static int generateIdMapSight()
	{
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.MapSight.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter=res.getInt("Counter")+1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.MapSight.getValue());
			su.executeUpdate();
			return counter;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return -1;
		}
	}
    public static int generateIdPlaceOfInterestSight() 
	{
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.PlaceOfInterestSight.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter=res.getInt("Counter")+1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.PlaceOfInterestSight.getValue());
			su.executeUpdate();
			return counter;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return -1;
		}
	}
    public static int generateIdRouteSight() 
	{
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.RouteSight.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter=res.getInt("Counter")+1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.RouteSight.getValue());
			su.executeUpdate();
			return counter;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return -1;
		}
	}
    public static int generateIdStatistic() 
	{
		try {
			PreparedStatement gt = conn.prepareStatement("SELECT Counter FROM Counters WHERE Object=? ");
			gt.setInt(1, Counter.Statistic.getValue());
			ResultSet res = gt.executeQuery();
			res.last();
			Integer counter=res.getInt("Counter")+1;
			PreparedStatement su = conn.prepareStatement("UPDATE `Counters` SET Counter=? WHERE Object=?");
			su.setInt(1, counter);
			su.setInt(2, Counter.Statistic.getValue());
			su.executeUpdate();
			return counter;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return -1;
		}
	}
    
    
    
	private static boolean existePlaceOfInterest(int id) {
		try {
			String sql="SELECT ID FROM "+ Table.PlaceOfInterest.getValue()+ " WHERE ID=?";
			PreparedStatement check = conn.prepareStatement(sql);
			check.setInt(1, id);
			ResultSet res = check.executeQuery();
			// check if there is exciting row in table before insert
			if(!res.next())
				return false;
			return true;
			} 
		catch (Exception e)
		{
	  	    closeConnection();
			e.printStackTrace();
		}
		return true;
	}
    
    public static boolean savePlaceOfInterest(PlaceOfInterest p)//return true if it's already in the database
	{
		try {
			if(existePlaceOfInterest(p.getId()))
			{
				String sql="UPDATE "+ Table.PlaceOfInterest.getValue()+" SET CityID=?, Name=?, Type=?, Description=?, ATD=? WHERE ID=?";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getCityId());
				su.setString(2, p.getName());
				su.setInt(3, p.getType().getValue());
				su.setString(4,  p.getPlaceDescription());
				su.setBoolean(5, p.isAccessibilityToDisabled());
				su.setInt(6, p.getId());
				su.executeUpdate();
				return true;
			}
			else
			{
				String sql="INSERT INTO "+ Table.PlaceOfInterest.getValue() +" (ID,CityID, Name, Type, Description, ATD) VALUES (?,?, ?, ?, ?, ?)";
				PreparedStatement su = conn.prepareStatement(sql);
				su.setInt(1, p.getId());
				su.setInt(2, p.getCityId());
				su.setString(3, p.getName());
				su.setInt(4, p.getType().getValue());
				su.setString(5,  p.getPlaceDescription());
				su.setBoolean(6, p.isAccessibilityToDisabled());
				su.executeUpdate();
				return false;
			}
		} 
		catch (Exception e)
		{
	  	    closeConnection();
			e.printStackTrace();
		}
		return false;
	}

    public static ArrayList<Integer> searchPlaceOfInterest(String placeName,String placeDescription,Integer cityId)
	{
		try {
			int counter=1;
			String sql="SELECT ID FROM "+Table.PlaceOfInterest.getValue()+" WHERE ";
			if(placeName!=null) 
				sql+="Name=? AND ";
			if(placeDescription!=null) 
				sql+="Description=? AND ";
			if(cityId!=null) 
				sql+="CityID=? AND ";
			sql=sql.substring(0,sql.length()-4);
		    System.out.println(sql);
			PreparedStatement gt = conn.prepareStatement(sql);
			if(placeName!=null) {
				gt.setString(counter, placeName);
				counter+=1;
			}
			if(placeDescription!=null) {
				gt.setString(counter, placeDescription);
				counter+=1;
			}
			if(cityId!=null) {
				gt.setInt(counter, cityId);
				counter+=1;
			}
			ResultSet res = gt.executeQuery();
			ArrayList<Integer> IDs = new ArrayList<>();
			while(res.next())
			{
				int id=res.getInt("ID");
				IDs.add(id);
			}
			return IDs;
		}
		catch (Exception e) {
	  	    closeConnection();
	  	    e.printStackTrace();
			return new ArrayList<>();
		}
	}
    
    public static boolean deletePlaceOfInterest(int placeId) // return true if the item was deleted
	{
		try {
			String sql="DELETE FROM "+Table.PlaceOfInterest.getValue()+" WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			int count = gt.executeUpdate();
			return count!=0;
		}
		catch (Exception e) {
	  	    closeConnection();
			e.printStackTrace();
			return false;
		} 
	}
    
    public static PlaceOfInterest getPlaceOfInterestById(int placeId)
	{
		try {
			String sql="SELECT * FROM "+Table.PlaceOfInterest.getValue()+" WHERE ID=?";
			PreparedStatement gt = conn.prepareStatement(sql);
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();	 
			if(!res.next()) 
				return null;
			res.last();
			int id=res.getInt("ID");
			int cityId=res.getInt("CityID");
			String name=res.getString("Name");
			int int_type=res.getInt("Type");
			PlaceOfInterest.PlaceType type=PlaceOfInterest.PlaceType.values()[int_type];
			String placeDescription=res.getString("Description");
			boolean accessibilityToDisabled=res.getInt("ATD")!=0;
			return PlaceOfInterest._createPlaceOfInterest(id,cityId,name,type,placeDescription,accessibilityToDisabled);
		}
		catch (Exception e) {
	  	    closeConnection();
			e.printStackTrace();
			return null;
		} 
	}
    

    @Override
    protected void finalize() throws Throwable
    {
	      System.out.println("lior is king");
  	      closeConnection();
    }
}
