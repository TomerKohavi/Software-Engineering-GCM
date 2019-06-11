package io_commands;

import otherClasses.ClassMustProperties;

public class Delete extends Command
{
	public Delete(ClassMustProperties toDelete)
	{
		this.toDelete = toDelete;
	}
	
	public void delete() {}
	
	public ClassMustProperties toDelete;
}
