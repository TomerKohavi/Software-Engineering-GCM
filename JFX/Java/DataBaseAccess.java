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
	static private final String DB_URL = "jdbc:mysql://remotemysql.com/"+ DB + "?useSSL=false";
	static private final String USER = "PrtTXnuWoq";
	static private final String PASS = "KOzAI33szl";
	static Connection conn = null;
	static Connection conn2 = null;

	static Statement stmt = null;
	
	public DataBaseAccess()
	{
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			conn2 = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
    public static boolean savePlaceOfInterest(PlaceOfInterest p)//return if it's already in the database
	{
		try {
			PreparedStatement check = conn2.prepareStatement("SELECT from POIs WHERE CityID=?, Name=?,"
					+ " Type=?, Description=?, ATD=?");
			check.setInt(1, p.getId());
			check.setString(2, p.getName());
			check.setInt(3, p.getType().getValue());
			check.setString(4,  p.getPlaceDescription());
			check.setBoolean(5, p.isAccessibilityToDisabled());
			ResultSet x = check.executeQuery();
			if(!x.next())
				return false;
			
			PreparedStatement su = conn2.prepareStatement("INSERT INTO POIs (CityID, Name, Type, Description, ATD) VALUES "
					+ "(?, ?, ?, ?, ?)");
			su.setInt(1, p.getId());
			su.setString(2, p.getName());
			su.setInt(3, p.getType().getValue());
			su.setString(4,  p.getPlaceDescription());
			su.setBoolean(5, p.isAccessibilityToDisabled());
			su.executeUpdate();
			} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

    public static ArrayList<Integer> searchPlaceOfInterest(String placeName,String placeDescription,Integer cityId)
	{
		try {
			PreparedStatement gt = conn2.prepareStatement("SELECT ID FROM `POIs` WHERE Name=? "
					+ "AND Description=? AND CityID=?");
			gt.setString(1, placeName);
			gt.setString(2, placeDescription);
			gt.setInt(3, cityId);
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
			e.printStackTrace();
			return null;
		}
	}
    
    public static boolean deletePlaceOfInterest(int placeId) 
	{
		try {
			PreparedStatement gt = conn2.prepareStatement("DELETE FROM `POIs` WHERE ID=?");
			gt.setInt(1, placeId);
			gt.executeQuery();
			int count = gt.executeUpdate();
			return count!=0;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
    
    public static PlaceOfInterest getPlaceOfInterestById(int placeId)
	{
		try {
			PreparedStatement gt = conn2.prepareStatement("SELECT * FROM `POIs` WHERE ID=?");
			gt.setInt(1, placeId);
			ResultSet res = gt.executeQuery();
			res.last();
			//int id,int cityId, String name, PlaceType type, String placeDescription, boolean accessibilityToDisabled
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
			e.printStackTrace();
			return null;
		} 
	}
    
    public void close()
	{
		try {
			conn.close();
			conn2.close();
			stmt.close();
			return;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static void main(String[] args) throws SSLException {
		System.out.println("dd");

	}
	
}
