package io_commands;

import classes.Employee.Role;

public class Login extends Command {

	public Login(String name, String pass, boolean isEmployee)
	{
		super();
		this.name = name;
		this.pass = pass;
		this.isEmployee = isEmployee;
		this.id = -1;
		this.role = null;
	}
	
	public String toString()
	{
		return name + '|' + pass + "--" + id;
	}
	
	public void delete()
	{
		name = pass = null;
		isEmployee = null;
	}
	
	public String name, pass;
	public int id;
	public Boolean isEmployee;
	public Role role;
}
