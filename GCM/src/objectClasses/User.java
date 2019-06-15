package objectClasses;

import java.io.Serializable;
import java.sql.SQLException;

import controller.Database;

/**
 * Abstract class of general user object
 * @author Ron Cohen
 *
 */
@SuppressWarnings("serial")
public abstract class User implements Serializable {

	private int id;
	private String userName;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private String phoneNumber;

	/**
	 * This is a private constructor of user object
	 * 
	 * @param id the user id
	 * @param userName the user user name
	 * @param password the user password
	 * @param email the user email address
	 * @param firstName the user first name
	 * @param lastName the user last name
	 * @param phoneNumber the user phone number
	 */
	protected User(int id, String userName, String password, String email, String firstName, String lastName,
			String phoneNumber) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
	}

	/**
	 * This is the normal public constructor for user object
	 * 
	 * @param userName the user user name
	 * @param password the user password
	 * @param email the user email address
	 * @param firstName the user first name
	 * @param lastName the user last name
	 * @param phoneNumber the user phone number
	 * @throws SQLException if the access to database failed
	 */
	public User(String userName, String password, String email, String firstName, String lastName, String phoneNumber) throws SQLException {
		this.id = Database.generateIdUser();
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Returns the user email address
	 * 
	 * @return the user email address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the user first name
	 * 
	 * @return the user first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns the user last name
	 * 
	 * @return the user last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns the user phone number
	 * 
	 * @return the user phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Returns the user id
	 * 
	 * @return the user id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the user user name
	 * 
	 * @return the user user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Returns the user password
	 * 
	 * @return the user password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the user password
	 * 
	 * @param password the new user password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Sets the user email address
	 * 
	 * @param email the new user email address
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets the user first name
	 * 
	 * @param firstName the new user first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Sets the user last name
	 * 
	 * @param lastName the new user last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Sets the user phone number
	 * 
	 * @param phoneNumber the new user phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Sets the user user name
	 * 
	 * @param userName the new user user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}