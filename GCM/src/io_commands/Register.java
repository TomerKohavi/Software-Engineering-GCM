package io_commands;

import classes.Employee.Role;

public class Register extends Command {
	public Register(String username, String password, String firstName, String lastName, String email, String phone, Role role, boolean isEmployee)
	{
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.id = -1;
		this.role = role;
		this.isEmployee = isEmployee;
	}
	
	public void delete()
	{
		username = password = firstName = lastName = email = phone = null;
		isEmployee = null;
		role = null;
	}
	
	public String username, password, firstName, lastName, email, phone;
	public int id;
	public Role role;
	public Boolean isEmployee;
}
