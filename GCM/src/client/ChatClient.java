// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import common.Console;
import io_commands.*;
import javafx.util.Pair;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import application.Connector;
import classes.City;
import classes.Employee.Role;
import classes.User;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	/**
	 * The Login ID of the user.
	 */
	String loginID;

	boolean loginReady;
	Login login;
	User user;

	boolean registerIDready;
	Register reg;

	boolean imageReady;
	ImageTransfer imTr;

	boolean searchReady;
	Search search;

	Semaphore semaphore;

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException
	{
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();
		this.loginID = "USER";
		sendToServer("#login USER");

		this.loginReady = false;
		this.registerIDready = false;
		this.searchReady = false;
		this.semaphore = new Semaphore(0);
	}

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param loginID  The user ID.
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public User login(String uname, String pass, boolean isEmployee) throws IOException
	{
		try
		{
			sendToServer(new Login(uname, pass, isEmployee));
			this.semaphore.acquire();
			this.user = this.login.loggedUser;
			this.login = null;
			System.out.println("login " + this.user);
			return this.user;
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public User register(String username, String password, String firstName, String lastName, String email,
			String phone, Role role, boolean isEmployee) throws IOException
	{
		sendToServer(new Register(username, password, firstName, lastName, email, phone, role, isEmployee));
		try
		{
			this.semaphore.acquire();
		}
		catch (InterruptedException e)
		{
			System.err.println("semaphore");
			e.printStackTrace();
		}
		this.registerIDready = false;
		this.user = this.reg.user;
		return this.reg.user;
	}

	public void logoff() throws IOException
	{
		sendToServer(new Logoff(this.user.getId()));
		this.user = null;
	}

	public BufferedImage getImage(String pathname) throws IOException
	{
		sendToServer(new ImageTransfer(pathname, true));
		for (int i = 0; !this.imageReady; i++)
			clientUI.display("image " + i);
		this.imageReady = false;
		BufferedImage im = this.imTr.getImage();
		this.imTr = null;
		return im;
	}

	public void sendImage(String pathname) throws IOException
	{
		ImageTransfer imTr = new ImageTransfer(pathname, false);
		imTr.loadImage();
		sendToServer(imTr);
	}

	public ArrayList<City> search(String cityName, String cityInfo, String poiName, String poiInfo) throws IOException
	{
		sendToServer(new Search(cityName, cityInfo, poiName, poiInfo));
		for (int i = 0; !this.searchReady; i++)
			clientUI.display("search " + i);
		this.searchReady = false;
		ArrayList<City> cityList = this.search.searchResult;
		this.search = null;
		return cityList;
	}

	public void updateUser(User user) throws IOException
	{
		sendToServer(user);
	}

	// Instance methods ***********************************************
	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg)
	{

		if (msg instanceof Login)
		{
			this.login = (Login) msg;
			clientUI.display(this.login.name);
			this.semaphore.release();
		}
		else if (msg instanceof Register)
		{
			this.reg = (Register) msg;
			this.semaphore.release();
		}
		else if (msg instanceof ImageTransfer)
		{
			this.imTr = (ImageTransfer) msg;
			this.imageReady = true;
		}
		else if (msg instanceof Search)
		{
			this.search = (Search) msg;
			this.searchReady = true;
		}

		else
			clientUI.display(msg.toString());
	}

	/**
	 * This method terminates the client.
	 */
	public void quit()
	{
		try
		{
			closeConnection();
		}
		catch (IOException e)
		{
		}
		System.exit(0);
	}

	/**
	 * Reacts to a closed connection while waiting for messages from the server.
	 * Overrides method in <code>AbstractClient</code>. Added by Benjamin Bergman,
	 * Oct 22, 2009.
	 *
	 * @param exception the exception raised.
	 */
	protected void connectionException(Exception exception)
	{
		exception.printStackTrace();
		clientUI.display("The connection to the Server (" + getHost() + ", " + getPort() + ") has been disconnected");
	}

	public static void main(String[] args) throws IOException
	{
		ChatClient client = new ChatClient(Connector.LOCAL_HOST, Connector.PORT, new Console());
		System.out.println(client.login("a", "a", false).getId());
		client.logoff();
	}
}
// End of ChatClient class
