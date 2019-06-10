package io_commands;

import otherClasses.ClassMustProperties;

public class Update extends Command
{
	public Update(ClassMustProperties toUpdate)
	{
		this.toUpdate = toUpdate;
	}
	
	public void delete() {}
	
	public ClassMustProperties toUpdate;
}
