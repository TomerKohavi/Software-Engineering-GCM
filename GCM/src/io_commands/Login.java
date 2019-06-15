package io_commands;

import javafx.util.Pair;
import objectClasses.User;
import server.EchoServer.LoginRegisterResult;

/**
 * @author sigal
 * treat login of user and return the result to the client
 */
public class Login extends Command {

	/**
	 * Constructor.
	 * @param name the name of the user
	 * @param pass the password of the user
	 * @param isEmployee if the user is employee or not
	 */
	public Login(String name, String pass, Boolean isEmployee)
	{
		super();
		this.name = name;
		this.pass = pass;
		this.isEmployee = isEmployee;
		this.loggedUser = null;
	}	
	
	/**
	 * delete the request
	 */
	public void delete()
	{
		name = pass = null;
		isEmployee = null;		
	}
	
	public String name, pass;
	public User loggedUser;
	public Boolean isEmployee;
	public LoginRegisterResult loginResult;
}
