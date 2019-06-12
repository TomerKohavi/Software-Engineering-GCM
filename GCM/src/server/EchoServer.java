package server;

import client.ChatClient;

import java.awt.image.BufferedImage;

// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.ArrayList;

import javax.imageio.*;

import application.Connector;
import ocsf.server.*;
import otherClasses.*;
import common.*;
import common.Console;
import controller.Database;
import controller.InformationSystem;
import controller.SearchCatalog;
import io_commands.*;
import javafx.scene.chart.PieChart.Data;
import objectClasses.City;
import objectClasses.Customer;
import objectClasses.Employee;
import objectClasses.User;
import objectClasses.Employee.Role;
import objectClasses.PlaceOfInterest.PlaceType;
import objectClasses.PlaceOfInterestSight;
import objectClasses.Route;
import objectClasses.RouteSight;
import objectClasses.RouteStop;
import objectClasses.Map;
import objectClasses.MapSight;
import objectClasses.PlaceOfInterest;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author sigal
 */
public class EchoServer extends AbstractServer
{
	// Class variables *************************************************
	public enum LoginRegisterResult
	{
		unameOrPass("Wrong username or password"), alredyLogged("User already logged in"), Success("Success"),
		usernameTaken("Username already in use");

		LoginRegisterResult(String res)
		{
			this.res = res;
		}

		public String getValue()
		{
			return this.res;
		}

		String res;
	}

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF serverUI;
	// Constructors ****************************************************

	ArrayList<Integer> loggedList;

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port     The port number to connect on.
	 * @param serverUI The interface type variable.
	 */

	public EchoServer(int port, ChatIF serverUI) throws IOException
	{
		super(port);
		this.serverUI = serverUI;
		Database.createConnection();
		this.loggedList = new ArrayList<Integer>();
	}

	/**
	 * @param login login request
	 * @return login result
	 */
	public Login handleLogin(Login login)
	{
		System.out.println("login " + login.name + " ");
		ArrayList<Integer> list;
		if (login.isEmployee)
			list = Database.searchEmployee(login.name, login.pass);
		else
			list = Database.searchCustomer(login.name, login.pass);
		if (!list.isEmpty())
		{
			int id = list.get(0);
			if (!this.loggedList.contains(id))
			{
				if (login.isEmployee)
					login.loggedUser = Database.getEmployeeById(id);
				else
					login.loggedUser = Database.getCustomerById(id);
				this.loggedList.add(login.loggedUser.getId());
				login.loginResult = LoginRegisterResult.Success;
			}
			else
			{
				login.loggedUser = null;
				login.loginResult = LoginRegisterResult.alredyLogged;
			}
		}
		else
		{
			login.loginResult = LoginRegisterResult.unameOrPass;
		}
		if (login.loggedUser != null)
			System.out.println("success");
		else
			System.out.println("failure");
		login.delete();
		return login;
	}

	/**
	 * @param logoff the log off request
	 */
	public void handleLogoff(Logoff logoff)
	{
		System.out.println("logoff " + logoff.logoffID + " " + this.loggedList.remove(logoff.logoffID));
	}

	/**
	 * @param reg the register request
	 * @return the register result
	 */
	public Register handleRegister(Register reg)
	{
		System.out.println("register " + reg.username);
		if (Database.searchCustomer(reg.username, null).isEmpty()
				&& Database.searchEmployee(reg.username, null).isEmpty())
		{
			if (reg.isEmployee)
			{
				Employee emp = new Employee(reg.username, reg.password, reg.email, reg.firstName, reg.lastName,
						reg.phone, reg.role);
				Database.saveEmployee(emp);
				reg.user = emp;
			}
			else
			{
				Customer cust = new Customer(reg.username, reg.password, reg.email, reg.firstName, reg.lastName,
						reg.phone, reg.ccard, reg.expires, reg.cvv);
				Database.saveCustomer(cust);
				reg.user = cust;
			}
			this.loggedList.add(reg.user.getId());
			reg.regResult = LoginRegisterResult.Success;
		}
		else
		{
			reg.regResult = LoginRegisterResult.usernameTaken;
		}
		reg.delete();
		return reg;
	}

	/**
	 * @param s the search request
	 * @return the search result
	 */
	public Search handleSearch(Search s)
	{
		System.out.println("search: " + s.cityName + "|" + s.cityInfo + "|" + s.poiName + "|" + s.poiInfo);
		s.searchResult = SearchCatalog.SearchCity(s.cityName, s.cityInfo, s.poiName, s.poiInfo);
		return s;
	}

	/**
	 * @param user the user request we want to handle
	 */
	public void handleUpdateUser(User user)
	{
		System.out.print("update " + user.getUserName());
		if (user instanceof Customer)
			Database.saveCustomer((Customer) user);
		else
			Database.saveEmployee((Employee) user);
	}

	/**
	 * @param cr the customer request we want to handle
	 * @return the result of the request
	 */
	public CustomersRequest handleUsersRequest(CustomersRequest cr)
	{
		System.out.println("customers list request");
		cr.custList = Database.getAllCustomers();
		return cr;
	}

	/**
	 * @param cityReq the get all city request
	 * @return the result of the request
	 */
	public AllCitiesRequest handleCityRequest(AllCitiesRequest cityReq)
	{
		System.out.println("cities list request");
		cityReq.cityList = Database.getAllCitiesNameId();
		return cityReq;

	}

	/**
	 * @param update update request handle
	 */
	public void handleUpdate(Update update)
	{
		System.out.println("update " + update.toUpdate.getClass().toString());
		update.toUpdate.saveToDatabase();
	}

	public CreateMap handleMapCreation(CreateMap cmap)
	{
		Map map = new Map(cmap.cityId, cmap.name, cmap.info, cmap.imgURL);
		map.saveToDatabase();

		MapSight mapS = new MapSight(cmap.cdvId, map);
		mapS.saveToDatabase();

		cmap.mapS = mapS;
		return cmap;
	}

	public CreatePOI handlePOICreation(CreatePOI cpoi)
	{
		PlaceOfInterest poi = new PlaceOfInterest(cpoi.cityId, cpoi.name, cpoi.type, cpoi.placeDescription,
				cpoi.accessibilityToDisabled);
		poi.saveToDatabase();

		PlaceOfInterestSight poiS = new PlaceOfInterestSight(cpoi.cdvId, poi);
		poiS.saveToDatabase();

		cpoi.poiS = poiS;
		return cpoi;
	}

	public CreateRoute handleRouteCreation(CreateRoute croute)
	{
		Route route = new Route(croute.cityId, croute.info);
		route.saveToDatabase();

		RouteSight routeS = new RouteSight(croute.cdvId, route, false);// sigal look at this false. I didn't know how to
																		// treat this
		routeS.saveToDatabase();

		croute.routeS = routeS;
		return croute;
	}

	public CreateRouteStops handleRouteStopsCreation(CreateRouteStops cstops)
	{
		cstops.idList = new ArrayList<Integer>();
		for (RouteStop stop : cstops.stopList)
		{
			if (stop.getId() == -1)
				stop._setId(Database.generateIdRouteStop());
			cstops.idList.add(stop.getId());
			System.out.println(stop.getId());
			stop.saveToDatabase();
		}
		return cstops;
	}

	public void handleDelete(Delete del)
	{
		System.out.println("delete " + del.toDelete.getClass().toString() + " " + del.toDelete.getId());
		del.toDelete.deleteFromDatabase();
	}
	

	private void handleAddStat(AddStat stat)
	{
		System.out.println("add stat " + stat.op);
		switch (stat.op)
		{
		case OneTimePurcahse:
			InformationSystem.addOneTimePurchase(stat.cityId);
			break;
		case Subscription:
			InformationSystem.addSubscription(stat.cityId);
			break;
		case SubRenewal:
			InformationSystem.addSubscriptionRenewal(stat.cityId);
			break;
		case Visit:
			InformationSystem.addVisit(stat.cityId);
			break;
		case SubDownload:
			InformationSystem.addSubDownload(stat.cityId);
			break;
		case VersionPublish:
			InformationSystem.newVersionWasPublished(stat.cityId);
			break;
		}
	}

	
	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client)
	{
		try
		{
			if (msg instanceof Login)
				client.sendToClient(handleLogin((Login) msg));
			else if (msg instanceof Logoff)
				handleLogoff((Logoff) msg);
			else if (msg instanceof Register)
				client.sendToClient(handleRegister((Register) msg));
			else if (msg instanceof ImageTransfer)
			{
				ImageTransfer imTr = (ImageTransfer) msg;
				if (imTr.requested)
				{
					imTr.readImageFromFile();
					client.sendToClient(imTr);
				}
				else
					imTr.saveImage("C:\\Users\\yonat\\Pictures\\mememe.png");
			}
			else if (msg instanceof Search)
				client.sendToClient(handleSearch((Search) msg));
			else if (msg instanceof CustomersRequest)
				client.sendToClient(handleUsersRequest((CustomersRequest) msg));
			else if (msg instanceof AllCitiesRequest)
				client.sendToClient(handleCityRequest((AllCitiesRequest) msg));
			else if (msg instanceof User)
				handleUpdateUser((User) msg);
			else if (msg instanceof Update)
				handleUpdate((Update) msg);
			else if (msg instanceof CreateMap)
				client.sendToClient(handleMapCreation((CreateMap) msg));
			else if (msg instanceof CreatePOI)
				client.sendToClient(handlePOICreation((CreatePOI) msg));
			else if (msg instanceof CreateRoute)
				client.sendToClient(handleRouteCreation((CreateRoute) msg));
			else if (msg instanceof CreateRouteStops)
				client.sendToClient(handleRouteStopsCreation((CreateRouteStops) msg));
			else if (msg instanceof Delete)
				handleDelete((Delete) msg);
			else if (msg instanceof Statboi)
				client.sendToClient(handleStatistics((Statboi) msg));
			else if (msg instanceof AddStat)
				handleAddStat((AddStat) msg);
			else
				System.out.println(msg.getClass().toString() + '\n' + msg.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI
	 */
	public void handleMessageFromServerUI(String message)
	{
		if (message.charAt(0) == '#')
		{
			runCommand(message);
		}
		else
		{
			// send message to clients
			serverUI.display(message);
			this.sendToAllClients("SERVER MSG> " + message);
		}
	}

	/**
	 * This method executes server commands.
	 *
	 * @param message String from the server console.
	 */
	private void runCommand(String message)
	{
		// run commands
		// a series of if statements
		if (message.equalsIgnoreCase("#display"))
			this.serverUI.display(loggedList);
		else if (message.equalsIgnoreCase("#quit"))
			quit();
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted()
	{
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped()
	{
		System.out.println("Server has stopped listening for connections.");
	}

	/**
	 * Run when new clients are connected. Implemented by Benjamin Bergman, Oct 22,
	 * 2009.
	 *
	 * @param client the connection connected to the client
	 */
	protected void clientConnected(ConnectionToClient client)
	{
		// display on server and clients that the client has connected.
		String msg = "A Client has connected";
		System.out.println(msg);
		this.sendToAllClients(msg);
	}

	/**
	 * Run when clients disconnect. Implemented by Benjamin Bergman, Oct 22, 2009
	 *
	 * @param client the connection with the client
	 */
	synchronized protected void clientDisconnected(ConnectionToClient client)
	{
		// display on server and clients when a user disconnects
		String msg = client.getInfo("loginID").toString() + " has disconnected";

		System.out.println(msg);
		this.sendToAllClients(msg);
	}

	public Statboi handleStatistics(Statboi statboi)
	{
		statboi.statboi = InformationSystem.getRangeSumStatistics(statboi.cityId, statboi.from, statboi.end);
		return statboi;
	}

	/**
	 * Run when a client suddenly disconnects. Implemented by Benjamin Bergman, Oct
	 * 22, 2009
	 *
	 * @param client    the client that raised the exception
	 * @param Throwable the exception thrown
	 */
	synchronized protected void clientException(ConnectionToClient client, Throwable exception)
	{
		System.err.println("EXCEPTION");
		String msg = client.getInfo("loginID").toString() + " has disconnected";

		System.out.println(msg);
		this.sendToAllClients(msg);
	}

	/**
	 * This method terminates the server.
	 */
	public void quit()
	{
		try
		{
			Database.closeConnection();
			close();
		}
		catch (IOException e)
		{
		}
		System.exit(0);
	}
}
// End of EchoServer class
