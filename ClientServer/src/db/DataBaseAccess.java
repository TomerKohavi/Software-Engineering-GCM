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
	
	public int display(String uname)
	{
		try {
			PreparedStatement disp = this.conn2.prepareStatement("Select NoP, Username FROM Prototype WHERE Username = ?");
			disp.setString(1, uname);
			ResultSet res = disp.executeQuery();
			res.last();
			return res.getInt("NoP");
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
		return -1;
	}

	public void inc(String uname)
	{
		try {
			PreparedStatement increment = this.conn2.prepareStatement("UPDATE Prototype SET NoP = NoP + 1 WHERE Username = ?");
			increment.setString(1, uname);
			increment.executeUpdate();
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

	public void changeTo(String uname, int num)
	{
		try {
			PreparedStatement increment = this.conn2.prepareStatement("UPDATE Prototype SET NoP = " + num + " WHERE Username = ?");
			increment.setString(1, uname);
			increment.executeUpdate();
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
	
	public int getType(String uname)
	{
		try {
			PreparedStatement gt = this.conn2.prepareStatement("Select Type, Username FROM Prototype WHERE Username = ?");
			gt.setString(1, uname);
			ResultSet res = gt.executeQuery();
			res.last();
			return res.getInt("Type");
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
		return -1;
	}

	public boolean signUp(String uname, String pass, int type)
	{
		try {
			PreparedStatement su = this.conn2.prepareStatement("INSERT INTO Prototype VALUES (?, 0, ?, ?)");
			su.setString(1, uname);
			su.setString(2, pass);
			su.setInt(3, type);
			su.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
			return false;
		}
	}
	
	public boolean signIn(String uname, String pass)
	{
		try {
			PreparedStatement si = this.conn2.prepareStatement("Select Password, Username FROM Prototype WHERE Username = ?");
			si.setString(1, uname);
			ResultSet res = si.executeQuery();
			res.last();
			return res.getString("Password").equals(pass);
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
		return false;
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
		DataBaseAccess dba = new DataBaseAccess();
		System.out.println(dba.display("ceo"));
		dba.getType("ceo");
//		System.out.println(dba.getType("ceo"));
//		System.out.println(dba.signUp("Sigal1", "BrawlStars", 2));		
		dba.close();
	}
	
}
