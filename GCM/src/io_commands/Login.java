package io_commands;

import classes.User;
import javafx.util.Pair;

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
}
