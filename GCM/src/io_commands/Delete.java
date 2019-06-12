package io_commands;

import otherClasses.ClassMustProperties;

/**
 * @author sigal
 * treat delete request from the client and return the result
 */
public class Delete extends Command
{
	/**
	 * @param toDelete object to delete
	 */
	public Delete(ClassMustProperties toDelete)
	{
		this.toDelete = toDelete;
	}
	
	public void delete() {}
	
	public ClassMustProperties toDelete;
}
