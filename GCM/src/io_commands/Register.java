package io_commands;

import objectClasses.User;
import objectClasses.Employee.Role;

public class Register extends Command {
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
}
