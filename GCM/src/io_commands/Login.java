package io_commands;

import javafx.util.Pair;
import objectClasses.User;
import server.EchoServer.LoginRegisterResult;

public class Login extends Command {

	public Login(String name, String pass, Boolean isEmployee)
	{
		super();
		this.name = name;
		this.pass = pass;
		this.isEmployee = isEmployee;
		this.loggedUser = null;
	}	
	
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
