package objectClasses;

import java.io.Serializable;

import controller.Database;
import otherClasses.ClassMustProperties;

/**
 * @author ron
 * TODO: fix 
 */
@SuppressWarnings("serial")
public class Employee extends User implements ClassMustProperties, Serializable {
	/**
	 * @author ron
	 * TODO: fix
	 */
	public enum Role {
		REGULAR(0), MANAGER(1), CEO(2);

		private final int value;

		/**
		 * create the role object
		 * @param nv the value of the role
		 */
		Role(final int nv) {
			value = nv;
		}

		/**
		 * returns the value of the role
		 * @return the value of the role
		 */
		public int getValue() {
			return value;
		}
	}

	private Role role;

	/**
	 * This is a private constructor of employee object
	 * 
	 * @param id the id of the employee
	 * @param userName the user name of the employee
	 * @param password the password of the employee
	 * @param email the email of the employee
	 * @param firstName the first name of the employee
	 * @param lastName the last name of the employee
	 * @param phoneNumber the phone number of the employee
	 * @param role the role of the employee
	 */
	private Employee(int id, String userName, String password, String email, String firstName, String lastName,
			String phoneNumber, Role role) {
		super(id, userName, password, email, firstName, lastName, phoneNumber);
		this.role = role;
	}

	/**
	 * This function create employee object according to all the inputs (supposed to be
	 * used only in Database)
	 * 
	 * @param id the id of the employee
	 * @param userName the user name of the employee
	 * @param password the password of the employee
	 * @param email the email of the employee
	 * @param firstName the first name of the employee
	 * @param lastName the last name of the employee
	 * @param phoneNumber the phone number of the employee
	 * @param role the role of the employee
	 * @return the new employee object
	 */
	public static Employee _createEmployee(int id, String userName, String password, String email, String firstName,
			String lastName, String phoneNumber, Role role) { // friend to Database
		return new Employee(id, userName, password, email, firstName, lastName, phoneNumber, role);
	}

	/**
	 * This is the normal public constructor for employee object
	 * 
	 * @param userName the user name of the employee
	 * @param password the password of the employee
	 * @param email the email of the employee
	 * @param firstName the first name of the employee
	 * @param lastName the last name of the employee
	 * @param phoneNumber the phone number of the employee
	 * @param role the role of the employee
	 */
	public Employee(String userName, String password, String email, String firstName, String lastName,
			String phoneNumber, Role role) {
		super(userName, password, email, firstName, lastName, phoneNumber);
		this.role = role;
	}

	/**
	 * This is a protected constructor of employee
	 * 
	 * @param id the id of the employee
	 * @param userName the user name of the employee
	 * @param password the password of the employee
	 * @param email the email of the employee
	 * @param firstName the first name of the employee
	 * @param lastName the last name of the employee
	 * @param phoneNumber the phone number of the employee
	 */
	protected Employee(int id, String userName, String password, String email, String firstName, String lastName,
			String phoneNumber) {
		super(id, userName, password, email, firstName, lastName, phoneNumber);
	}

	public void saveToDatabase() {
		Database.saveEmployee(this);
	}

	public void deleteFromDatabase() {
		Database.deleteEmployee(super.getId());
	}

	public void reloadTempsFromDatabase() {
	}

	/**
	 * Return the role of the employee
	 * 
	 * @return the role of the employee
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Sets the role of the employee
	 * 
	 * @param role the role of the employee
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Employee && ((Employee) o).getId() == this.getId();
	}
}