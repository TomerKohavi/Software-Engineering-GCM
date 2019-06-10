package io_commands;
import java.io.Serializable;

/**
 * @author sigal
 * base class for data transfer
 */
public abstract class Command implements Serializable {

	/**
	 * Constructor
	 */
	public Command() {}
	
	/**
	 * delete all query data, leaving only results
	 */
	public abstract void delete(); // delete all sent info; leave only answer to caller
	
	protected static final long serialVersionUID = 1L;
}
