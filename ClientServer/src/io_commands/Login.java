package io_commands;

public class Login extends Command {

	public Login(String name, String pass)
	{
		super();
		this.name = name;
		this.pass = pass;
		this.correct = false;
		this.type = 0;
	}
	
	public String toString()
	{
		return name + '|' + pass + "--" + correct + "-" + type;
	}
	
	public String name, pass;
	public boolean correct;
	public int type;
}
