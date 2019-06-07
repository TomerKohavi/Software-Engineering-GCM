package io_commands;

public class Logoff extends Command {

	public Logoff(int id) {
		this.logoffID = id;
	}

	public void delete() {
		this.logoffID = -1;
	}

	public int logoffID;
}
