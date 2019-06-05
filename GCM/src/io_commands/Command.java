package io_commands;
import java.io.Serializable;

public abstract class Command implements Serializable {

	public Command() {}
	
	public abstract void delete(); // delete all sent info; leave only answer to caller
	
	protected static final long serialVersionUID = 1L;
}
