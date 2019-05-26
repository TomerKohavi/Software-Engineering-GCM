package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	Connection conn = null;
	Connection conn2 = null;

	Statement stmt = null;
	
	public DataBaseAccess()
	{
		try {
			Class.forName(JDBC_DRIVER);
			this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
			this.conn2 = DriverManager.getConnection(DB_URL, USER, PASS);
			this.stmt = this.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		}
		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
            System.out.println("SQLState: " + se.getSQLState());
            System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (this.stmt != null)
					this.stmt.close();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		
	}
	
    public static boolean savePlaceOfInterest(PlaceOfInterest p)//return if it's already in the database
	{
		try {
			PreparedStatement check = this.conn2.prepareStatement("SELECT from POIs WHERE CityID=?, Name=?,"
					+ " Type=?, Description=?, ATD=?");
			check.setInt(1, p.cityId);
			check.setString(2, p.name);
			check.setInt(3, p.type);
			check.setString(4,  p.placeDescription);
			check.setBoolean(5, p.accessibilityToDisabled);
			check.executeUpdate();
			if(!check.next())
				return false;
			
			PreparedStatement su = this.conn2.prepareStatement("INSERT INTO POIs (CityID, Name, Type, Description, ATD) VALUES "
					+ "(?, ?, ?, ?, ?)");
			su.setInt(1, p.cityId);
			su.setString(2, p.name);
			su.setInt(3, p.type);
			su.setString(4,  p.placeDescription);
			su.setBoolean(5, p.accessibilityToDisabled);
			su.executeUpdate();
			} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

		
	public void close()
	{
		try {
			this.conn.close();
			this.conn2.close();
			this.stmt.close();
			return;
		}
		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
            System.out.println("SQLState: " + se.getSQLState());
            System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (this.stmt != null)
					this.stmt.close();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return;
	}
	
	public static void main(String[] args) throws SSLException {
		System.out.println("dd");

	}
	
}
