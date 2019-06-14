package io_commands;

/**
 * @author sigal
 * treat log off for users 
 */
public class Logoff extends Command {

	/**
	 * log out COnstructor.
	 * @param id id of the user
	 */
	public Logoff(int id) {
		this.logoffID = id;
	}

	public void delete() {
		this.logoffID = null;
	}

	public Integer logoffID;
}
