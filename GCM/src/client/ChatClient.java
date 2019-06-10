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
import objectClasses.MapSight;
import objectClasses.PlaceOfInterest.PlaceType;
import objectClasses.PlaceOfInterestSight;
import objectClasses.RouteSight;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import application.Connector;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author sigal
 */
/**
 * @author user
 *
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

	User user;

	Login login;

	Register reg;

	ImageTransfer imTr;

	Search search;

	CustomersRequest custReq;

	AllCitiesRequest cityReq;

	CreateMap cmap;

	CreatePOI cpoi;

	CreateRoute croute;

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

		this.semaphore = new Semaphore(0);
	}

	public void semAcquire()
	{
		try
		{
			this.semaphore.acquire();
		}
		catch (InterruptedException e)
		{
			System.err.println("semaphore");
			e.printStackTrace();
		}
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
		sendToServer(new Login(uname, pass, isEmployee));
		this.semAcquire();
		this.user = this.login.loggedUser;
		if (this.user != null)
			System.out.println("login " + this.user.getUserName());
		else
			System.out.println("login null");
		return new Pair<User, LoginRegisterResult>(this.user, this.login.loginResult);
	}

	/**
	 * @param username   the user name of the new user
	 * @param password   the password of the new user
	 * @param firstName  the firstName of the new user
	 * @param lastName   the lastName of the new user
	 * @param email      the email of the new user
	 * @param phone      the phone number of the new user
	 * @param role       the user role(employee/manger) of the new user
	 * @param ccard      the credit card of the new user
	 * @param expires    the credit card expires of the new user
	 * @param cvv        the user credit card cvv of the new user
	 * @param isEmployee if the new user if employee or not
	 * @return the user and the result of the request
	 * @throws IOException thrown when cannot register
	 */
	public Pair<User, LoginRegisterResult> register(String username, String password, String firstName, String lastName,
			String email, String phone, Role role, String ccard, String expires, String cvv, boolean isEmployee)
			throws IOException
	{
		sendToServer(new Register(username, password, firstName, lastName, email, phone, role, ccard, expires, cvv,
				isEmployee));
		this.semAcquire();
		this.user = this.reg.user;
		return new Pair<User, LoginRegisterResult>(this.reg.user, this.reg.regResult);
	}

	/**
	 * log off the user
	 * 
	 * @throws IOException when cannot log off
	 */
	public void logoff() throws IOException
	{
		if (this.user != null)
		{
			sendToServer(new Logoff(this.user.getId()));
			this.user = null;
		}
	}

	/**
	 * @param pathname the path of the image
	 * @return image
	 * @throws IOException thrown when cannot load the image from server
	 */
	public BufferedImage getImage(String pathname) throws IOException
	{
		sendToServer(new ImageTransfer(pathname, true));
		this.semAcquire();
		BufferedImage im = this.imTr.getImage();
		this.imTr = null;
		return im;
	}

	/**
	 * @param pathname the path of the image we want to save
	 * @throws IOException problem with the image
	 */
	public void sendImage(String pathname) throws IOException
	{
		ImageTransfer imTr = new ImageTransfer(pathname, false);
		imTr.readImageFromFile();
		sendToServer(imTr);
	}

	/**
	 * @param cityName the name of city we want to search
	 * @param cityInfo the info of city we want to search
	 * @param poiName  the name of point of interest we want to search
	 * @param poiInfo  the info of point of interest we want to search
	 * @return array list with the city we searched from the server
	 * @throws IOException if we cannot search the city
	 */
	public ArrayList<City> search(String cityName, String cityInfo, String poiName, String poiInfo) throws IOException
	{
		sendToServer(new Search(cityName, cityInfo, poiName, poiInfo));
		this.semAcquire();
		ArrayList<City> cityList = this.search.searchResult;
		this.search = null;
		return cityList;
	}

	/**
	 * @return send to the server request for the customers
	 * @throws IOException error in the request
	 */
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

	/**
	 * @return return all the cities from the server
	 * @throws IOException cannot get the cities from the server
	 */
	public ArrayList<Pair<String, Integer>> allCitiesRequest() throws IOException
	{
		sendToServer(new AllCitiesRequest());
		this.semAcquire();
		return this.cityReq.cityList;
	}

	public MapSight createMap(int cityId, String name, String info, String imgURL, int cdvId) throws IOException
	{
		sendToServer(new CreateMap(cityId, name, info, imgURL, cdvId));
		this.semAcquire();
		return this.cmap.mapS;
	}

	public PlaceOfInterestSight createPOI(int cityId, String name, PlaceType type, String placeDescription,
			boolean accessibilityToDisabled, int cdvId) throws IOException
	{
		sendToServer(new CreatePOI(cityId, name, type, placeDescription, accessibilityToDisabled, cdvId));
		this.semAcquire();
		return this.cpoi.poiS;
	}

	public RouteSight createRoute(int cityId, String info, int cdvId) throws IOException
	{
		sendToServer(new CreateRoute(cityId, info, cdvId));
		this.semAcquire();
		return this.croute.routeS;
	}

	/**
	 * @param object update every object that is not user
	 * @throws IOException cannot update
	 */
	public void update(ClassMustProperties object) throws IOException
	{
		sendToServer(new Update(object));
	}

	/**
	 * @param update user
	 * @throws IOException cannot update
	 */
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
			this.login = (Login) msg;
		else if (msg instanceof Register)
			this.reg = (Register) msg;
		else if (msg instanceof ImageTransfer)
			this.imTr = (ImageTransfer) msg;
		else if (msg instanceof Search)
			this.search = (Search) msg;
		else if (msg instanceof CustomersRequest)
			this.custReq = (CustomersRequest) msg;
		else if (msg instanceof AllCitiesRequest)
			this.cityReq = (AllCitiesRequest) msg;
		else if (msg instanceof CreateMap)
			this.cmap = (CreateMap) msg;
		else if (msg instanceof CreatePOI)
			this.cpoi = (CreatePOI) msg;
		else if (msg instanceof CreateRoute)
			this.croute = (CreateRoute) msg;

		if (msg instanceof Command)
			this.semaphore.release();
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
		ChatClient client = new ChatClient("localhost", 5555, new Console());
		client.createMap(1234, "sigmap", "mapsig", "oof.png", 4321);
	}
}
// End of ChatClient class
