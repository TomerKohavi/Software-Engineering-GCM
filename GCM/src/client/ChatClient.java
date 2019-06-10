// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import otherClasses.ClassMustProperties;
import otherClasses.Pair;
import server.EchoServer.LoginRegisterResult;
import common.*;
import common.Console;
import io_commands.*;
import objectClasses.City;
import objectClasses.Customer;
import objectClasses.User;
import objectClasses.Employee.Role;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import application.Connector;

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

	CustomersRequest custReq;

	Semaphore semaphore;
	private AllCitiesRequest cityReq;

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

	public Pair<User, LoginRegisterResult> login(String uname, String pass, boolean isEmployee) throws IOException
	{
		try
		{
			sendToServer(new Login(uname, pass, isEmployee));
			this.semaphore.acquire();
			this.user = this.login.loggedUser;
			System.out.println("login " + this.user.getUserName());
			return new Pair<User, LoginRegisterResult>(this.user, this.login.loginResult);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Pair<User, LoginRegisterResult> register(String username, String password, String firstName, String lastName, String email,
			String phone, Role role, String ccard, String expires, String cvv, boolean isEmployee) throws IOException
	{
		sendToServer(new Register(username, password, firstName, lastName, email, phone, role, ccard, expires, cvv,
				isEmployee));
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
		return new Pair<User, LoginRegisterResult>(this.reg.user, this.reg.regResult);
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
		imTr.readImageFromFile();
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

	public ArrayList<Customer> customersRquest() throws IOException
	{
		sendToServer(new CustomersRequest());
		try
		{
			this.semaphore.acquire();
		}
		catch (InterruptedException e)
		{
			System.err.println("semaphore");
			e.printStackTrace();
		}
		return this.custReq.custList;
	}

	public ArrayList<Pair<String, Integer>> allCitiesRequest() throws IOException
	{
		sendToServer(new AllCitiesRequest());
		try
		{
			this.semaphore.acquire();
		}
		catch (InterruptedException e)
		{
			System.err.println("semaphore");
			e.printStackTrace();
		}
		return this.cityReq.cityList;

	}
	
	public void update(ClassMustProperties object) throws IOException
	{
		sendToServer(object);
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
		else if (msg instanceof CustomersRequest)
		{
			this.custReq = (CustomersRequest) msg;
			this.semaphore.release();
		}
		else if (msg instanceof AllCitiesRequest)
		{
			this.cityReq = (AllCitiesRequest) msg;
			this.semaphore.release();
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
		System.out.println(client.login("a", "a", false).a.getId());
		client.logoff();
	}
}
// End of ChatClient class
