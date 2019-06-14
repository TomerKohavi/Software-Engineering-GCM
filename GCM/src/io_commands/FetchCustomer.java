package io_commands;

import objectClasses.User;

public class FetchCustomer extends Command
{
	public FetchCustomer(int id)
	{
		this.id = id;
	}
	
	public void delete()
	{
		this.id = null;
	}
	
	public User user;
	public Integer id;
}
