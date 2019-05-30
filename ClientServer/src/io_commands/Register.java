package io_commands;

public class Register extends Command {
	public Register(String username, String password, String firstName, String lastName, String email, String phone)
	{
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.id = -1;
	}
	
	public String username, password, firstName, lastName, email, phone;
	public int id;
}
