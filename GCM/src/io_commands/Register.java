package io_commands;

import objectClasses.User;
import objectClasses.Employee.Role;
import server.EchoServer.LoginRegisterResult;

/**
 * @author sigal
 * send register request to the server and return the result to the client
 */
public class Register extends Command {
	/**
	 * Constructor
	 * @param username the user name of the user
	 * @param password the password of the user
	 * @param firstName the firstName of the user
	 * @param lastName the lastName of the user
	 * @param email the email of the user
	 * @param phone the phone of the user
	 * @param role the role of the employee(if it an employee)
	 * @param ccard the credit card of the user
	 * @param expires the expires of the user credit card
	 * @param cvv the cvv of the user credit card
	 * @param isEmployee if the user is employee or not
	 */
	public Register(String username, String password, String firstName, String lastName, String email, String phone, Role role, String ccard, String expires, String cvv, boolean isEmployee)
	{
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.user = null;
		this.role = role;
		this.ccard = ccard;
		this.expires = expires;
		this.cvv = cvv;
		this.isEmployee = isEmployee;
	}
	
	public void delete()
	{
		username = password = firstName = lastName = email = phone = null;
		isEmployee = null;
		role = null;
	}
	
	public String username, password, firstName, lastName, email, phone, ccard, expires, cvv;
	public User user;
	public Role role;
	public Boolean isEmployee;
	public LoginRegisterResult regResult;
}
