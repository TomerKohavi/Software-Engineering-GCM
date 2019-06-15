package io_commands;

import objectClasses.User;

/**
 * @author sigal
 * Fetch customer request from the client to the server
 */
public class FetchCustomer extends Command
{
	/**
	 * The constructor of the request
	 * @param id the customer id
	 */
	public FetchCustomer(int id)
	{
		this.id = id;
	}
	
	/**
	 * delete the request
	 */
	public void delete()
	{
		this.id = null;
	}
	
	public User user;
	public Integer id;
}
