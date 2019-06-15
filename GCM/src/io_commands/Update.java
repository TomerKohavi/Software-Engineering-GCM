package io_commands;

import otherClasses.ClassMustProperties;

/**
 * @author sigal
 * treat update request from the server to the client
 */
public class Update extends Command
{
	/**
	 * @param toUpdate object to update
	 */
	public Update(ClassMustProperties toUpdate)
	{
		this.toUpdate = toUpdate;
	}
	
	/**
	 * delete the request
	 */
	public void delete() {}
	
	public ClassMustProperties toUpdate;
}
