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
import controller.InformationSystem.Ops;
import io_commands.*;
import objectClasses.City;
import objectClasses.Customer;
import objectClasses.User;
import objectClasses.Employee.Role;
import objectClasses.MapSight;
import objectClasses.PlaceOfInterest.PlaceType;
import objectClasses.PlaceOfInterestSight;
import objectClasses.RouteSight;
import objectClasses.RouteStop;
import objectClasses.Statistic;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import com.sun.xml.internal.bind.v2.runtime.Name;

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

	CreateRouteStops cstops;

	Semaphore semaphore;

	Statboi statboi;
	
	FetchSights fs;
	
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

	/**
	 *  active semaphore 
	 */
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
	public BufferedImage fetchImage(String pathname) throws IOException
	{
		sendToServer(new ImageTransfer(pathname, null, true));
		this.semAcquire();
		BufferedImage im = this.imTr.getImage();
		this.imTr = null;
		return im;
	}

	/**
	 * @param pathname the path of the image we want to save
	 * @throws IOException problem with the image
	 */
	public void sendImage(String readpath, String writepath) throws IOException
	{
		ImageTransfer imTr = new ImageTransfer(readpath, writepath, false);
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

	/**
	 * @param cityId the id of the city that contained the map
	 * @param name the name of the map
	 * @param info the data of the city
	 * @param imgURL the path of the image
	 * @param cdvId city data version id of the map
	 * @return map sight object of the new map
	 * @throws IOException if there is problem with the semaphore
	 */
	public MapSight createMap(int cityId, String name, String info, String imgURL, int cdvId) throws IOException
	{
		sendToServer(new CreateMap(cityId, name, info, imgURL, cdvId));
		this.semAcquire();
		return this.cmap.mapS;
	}

	/**
	 * @param cityId the id of the city that contained the point of interest
	 * @param name the name of the point of interest
	 * @param type which type the point of interest
	 * @param placeDescription the description of the point of interest
	 * @param accessibilityToDisabled if the point of interest is accessibility 
	 * @param cdvId city data version id of the point of interest
	 * @return point of interest sight object of the new point of interest
	 * @throws IOException if there is problem with the semaphore
	 */
	public PlaceOfInterestSight createPOI(int cityId, String name, PlaceType type, String placeDescription,
			boolean accessibilityToDisabled, int cdvId) throws IOException
	{
		sendToServer(new CreatePOI(cityId, name, type, placeDescription, accessibilityToDisabled, cdvId));
		this.semAcquire();
		return this.cpoi.poiS;
	}

	/**
	 * @param cityId the id of the city that contained the route
	 * @param info the info of the route
	 * @param cdvId city data version id of the route
	 * @return route sight object of the new route
	 * @throws IOException if there is problem with the semaphore
	 */
	public RouteSight createRoute(int cityId, String name, String info, int cdvId) throws IOException
	{
		sendToServer(new CreateRoute(cityId, name, info, cdvId));
		this.semAcquire();
		return this.croute.routeS;
	}

	/**
	 * @param newStopList list of route stop to create
	 * @return list if the ids of the new route stops
	 * @throws IOException if there is problem with the semaphore
	 */
	public ArrayList<Integer> createRouteStops(ArrayList<RouteStop> newStopList) throws IOException
	{
		sendToServer(new CreateRouteStops(newStopList));
		this.semAcquire();
		return this.cstops.idList;
	}

	/**
	 * @param object object to delete
	 * @throws IOException if there is problem with the delete
	 */
	public void deleteObject(ClassMustProperties object) throws IOException
	{
		sendToServer(new Delete(object));
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

	/**
	 * get statistics from specific city and time
	 * @param cityId the city id we want to get the statistic
	 * @param from from when the statistics is taken
	 * @param end until when the statistics is taken
	 * @return statistics object with the relevant data
	 * @throws IOException if there is problem with the semaphore
	 */
	public Statistic getStatistics(Integer cityId, Date from, Date end) throws IOException
	{
		sendToServer(new Statboi(cityId, from, end));
		this.semAcquire();
		return this.statboi.statboi;
	}
	
	/**
	 * @param cityId the city id we want to add the statistics to here
	 * @param op with  kind of statistics to add 
	 * @throws IOException if there is any problem with the insert
	 */
	public void addStat(Integer cityId, Ops op) throws IOException
	{
		sendToServer(new AddStat(cityId, op));
	}
	
	public void addStat(Integer cityId, Integer numMaps) throws IOException
	{
		sendToServer(new AddStat(cityId, numMaps));
	}
	
	public ArrayList<?> fetchSights(int cdvId, Class<?> sightType) throws IOException
	{
		sendToServer(new FetchSights(cdvId, sightType));
		this.semAcquire();
		return fs.sightList;
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
		else if (msg instanceof CreateRouteStops)
			this.cstops = (CreateRouteStops) msg;
		else if (msg instanceof Statboi)
			this.statboi = (Statboi) msg;
		else if (msg instanceof FetchSights)
			this.fs = (FetchSights) msg;
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
}
// End of ChatClient class
