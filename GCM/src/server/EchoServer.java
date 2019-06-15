package server;

import client.ChatClient;

import java.awt.image.BufferedImage;

// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.sql.SQLException;
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
import objectClasses.CityDataVersion;
import objectClasses.CityPurchase;
import objectClasses.Customer;
import objectClasses.Employee;
import objectClasses.User;
import objectClasses.Employee.Role;
import objectClasses.Location;
import objectClasses.PlaceOfInterest.PlaceType;
import objectClasses.PlaceOfInterestSight;
import objectClasses.Route;
import objectClasses.RouteSight;
import objectClasses.RouteStop;
import objectClasses.Subscription;
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
	 * @throws IOException can be thrown due to session opening
	 * @throws SQLException if the access to database failed
	 * @throws ClassNotFoundException cannot find the class
	 */
	public EchoServer(int port, ChatIF serverUI) throws IOException, ClassNotFoundException, SQLException
	{
		super(port);
		this.serverUI = serverUI;
		Database.createConnection();
		this.loggedList = new ArrayList<Integer>();
	}

	/**
	 * @param login login request
	 * @return login result
	 * @throws SQLException if the access to database failed
	 */
	private Login handleLogin(Login login) throws SQLException
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
	private void handleLogoff(Logoff logoff)
	{
		System.out.println("logoff " + logoff.logoffID + " " + this.loggedList.remove(logoff.logoffID));
	}

	/**
	 * @param reg the register request
	 * @return the register result
	 * @throws SQLException if the access to database failed
	 */
	private Register handleRegister(Register reg) throws SQLException
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
	private Search handleSearch(Search s)
	{
		System.out.println("search: " + s.cityName + "|" + s.cityInfo + "|" + s.poiName + "|" + s.poiInfo);
		s.searchResult = SearchCatalog.SearchCity(s.cityName, s.cityInfo, s.poiName, s.poiInfo);
		return s;
	}

	/**
	 * @param user the user request we want to handle
	 * @throws SQLException if the access to database failed
	 */
	private void handleUpdateUser(User user) throws SQLException
	{
		System.out.print("update " + user.getUserName());
		if (user instanceof Customer)
			Database.saveCustomer((Customer) user);
		else
			Database.saveEmployee((Employee) user);
	}

	/**
	 * @param r the resubscribe inputs
	 */
	private void handleResubscribe(Resub r)
	{
		Subscription._Resubscribe(r.subAlmostEnd, r.newFullPrice, r.newPayedPrice);
	}

	/**
	 * @param cr the customer request we want to handle
	 * @return the result of the request
	 * @throws SQLException if the access to database failed
	 */
	private CustomersRequest handleUsersRequest(CustomersRequest cr) throws SQLException
	{
		System.out.println("customers list request");
		cr.custList = Database.getAllCustomers();
		return cr;
	}

	/**
	 * @param cityReq the get all city request
	 * @return the result of the request
	 * @throws SQLException if the access to database failed
	 */
	private AllCitiesRequest handleCityRequest(AllCitiesRequest cityReq) throws SQLException
	{
		System.out.println("cities list request");
		cityReq.cityList = Database.getAllCitiesNameId();
		return cityReq;

	}

	/**
	 * @param update update request handle
	 */
	private void handleUpdate(Update update)
	{
		System.out.println("update " + update.toUpdate.getClass().toString());
		update.toUpdate.saveToDatabase();
	}

	/**
	 * @param cmap map creation that come from the client
	 * @return create map object the sent to the client
	 */
	private CreateMap handleMapCreation(CreateMap cmap)
	{
		Map map = new Map(cmap.cityId, cmap.name, cmap.info, cmap.imgURL);
		map.saveToDatabase();

		MapSight mapS = new MapSight(cmap.cdvId, map);
		mapS.saveToDatabase();

		cmap.mapS = mapS;
		return cmap;
	}

	/**
	 * @param cpoi point of interest creation that come from the client
	 * @return create point of interest object the sent to the client
	 */
	private CreatePOI handlePOICreation(CreatePOI cpoi)
	{
		PlaceOfInterest poi = new PlaceOfInterest(cpoi.cityId, cpoi.name, cpoi.type, cpoi.placeDescription,
				cpoi.accessibilityToDisabled);
		poi.saveToDatabase();

		PlaceOfInterestSight poiS = new PlaceOfInterestSight(cpoi.cdvId, poi);
		poiS.saveToDatabase();
		System.out.println(poi.getId() + " " + poiS.getId());

		cpoi.poiS = poiS;
		return cpoi;
	}

	/**
	 * @param croute route creation that come from the client
	 * @return create route object the sent to the client
	 */
	private CreateRoute handleRouteCreation(CreateRoute croute)
	{
		Route route = new Route(croute.cityId, croute.name, croute.info, croute.isFav);
		route.saveToDatabase();

		RouteSight routeS = new RouteSight(croute.cdvId, route);
		routeS.saveToDatabase();

		croute.routeS = routeS;
		return croute;
	}

	/**
	 * @param cstops route stop creation that come from the client
	 * @return create route stop object the sent to the client
	 * @throws SQLException if the access to database failed
	 */
	private CreateRouteStops handleRouteStopsCreation(CreateRouteStops cstops) throws SQLException
	{
		cstops.idList = new ArrayList<Integer>();
		System.out.print("create route stops");
		for (RouteStop stop : cstops.stopList)
		{
			if (stop.getId() == -1)
				stop._setId(Database.generateIdRouteStop());
			cstops.idList.add(stop.getId());
			stop.saveToDatabase();
		}
		System.out.println(" ids " + cstops.idList);
		return cstops;
	}

	/**
	 * handle delete request from the client
	 * 
	 * @param del the delete request from the client
	 */
	private void handleDelete(Delete del)
	{
		System.out.println("delete " + del.toDelete.getClass().toString() + " " + del.toDelete.getId());
		del.toDelete.deleteFromDatabase();
	}

	/**
	 * handle add statistics request from the client
	 * 
	 * @param stat the statistics request from the client
	 */
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
		case NumMaps:
			if (stat.numMaps != null)
				InformationSystem.setNumMaps(stat.cityId, stat.numMaps);
			break;
		}
	}

	/**
	 * handle fetch sights request from the client
	 * 
	 * @param fs the fetch sights request from the client
	 * @return the fetch sights result that return to the client
	 */
	private FetchSights handleFetchSights(FetchSights fs)
	{
		System.out.println("fetch " + fs.sightType.toString());
		if (fs.sightType.isAssignableFrom(MapSight.class))
			fs.sightList = CityDataVersion._generateMapSights(fs.cdvId);
		else if (fs.sightType.isAssignableFrom(PlaceOfInterestSight.class))
			fs.sightList = CityDataVersion._generatePlaceSights(fs.cdvId);
		else if (fs.sightType.isAssignableFrom(RouteSight.class))
			fs.sightList = CityDataVersion._generateRouteSights(fs.cdvId);
		else
			System.err.println("Wrong class type");
		return fs;
	}

	/**
	 * handle purchase request from the client
	 * 
	 * @param cp the city purchase request from the client
	 * @throws SQLException if the access to database failed
	 */
	private void handlePurchase(CityPurchase cp) throws SQLException
	{
		System.out.println("purchase " + cp.getCityName());
		cp._setId(Database.generateIdCityPurchase());
		cp.saveToDatabase();
	}

	/**
	 * handle fetch customer request from the client
	 * 
	 * @param fc the fetch customer request from the client
	 * @return the fetch customer result that return to the client
	 * @throws SQLException if the access to database failed
	 */
	private FetchCustomer handleFetchUser(FetchCustomer fc) throws SQLException
	{
		System.out.println("fetch customer " + fc.id);
		fc.user = Database.getCustomerById(fc.id);
		return fc;
	}

	/**
	 * handle create location request from the client
	 * 
	 * @param clocs the location request
	 * @return the result of the location request to the client
	 * @throws SQLException if the access to database failed
	 */
	private CreateLocations handleLocationsCreation(CreateLocations clocs) throws SQLException
	{
		clocs.idList = new ArrayList<Integer>();
		System.out.println("create locations");
		for (Location loc : clocs.locList)
		{
			if (loc.getId() == -1)
				loc._setId(Database.generateIdRouteStop());
			clocs.idList.add(loc.getId());
			loc.saveToDatabase();
		}
		System.out.println(" ids " + clocs.idList);
		return clocs;
	}

	/**
	 * handle create city request from the client
	 * 
	 * @param clocs the city request
	 * @return the result of the city request to the client
	 */
	private CreateCity handleCityCreation(CreateCity ccity)
	{
		ccity.city = new City(ccity.name, ccity.info, ccity.priceOneTime, ccity.pricePeriod);
		CityDataVersion cdv = new CityDataVersion(ccity.city, ccity.name = "1.0");
		ccity.city.addUnpublishedCityDataVersion(cdv);
		ccity.city.saveToDatabase();
		return ccity;
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
					System.out.println("image request " + imTr.readpath);
					imTr.readImageFromFile();
					client.sendToClient(imTr);
				}
				else
				{
					System.out.println("image save " + imTr.writepath);
					imTr.saveImage();
				}
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
			else if (msg instanceof CreateLocations)
				client.sendToClient(handleLocationsCreation((CreateLocations) msg));
			else if (msg instanceof Delete)
				handleDelete((Delete) msg);
			else if (msg instanceof Statboi)
				client.sendToClient(handleStatistics((Statboi) msg));
			else if (msg instanceof AddStat)
				handleAddStat((AddStat) msg);
			else if (msg instanceof FetchSights)
				client.sendToClient(handleFetchSights((FetchSights) msg));
			else if (msg instanceof CityPurchase)
				handlePurchase((CityPurchase) msg);
			else if (msg instanceof FetchCustomer)
				client.sendToClient(handleFetchUser((FetchCustomer) msg));
			else if (msg instanceof CreateCity)
				client.sendToClient(handleCityCreation((CreateCity) msg));
			else if (msg instanceof Resub)
				handleResubscribe((Resub) msg);
			else
				System.out.println(msg.getClass().toString() + '\n' + msg.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			try
			{
				client.sendToClient(e);
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
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
	 * handle statistics request from the client and return the result
	 * 
	 * @param statboi the statistics request from the client
	 * @return statistics result to the client
	 */
	public Statboi handleStatistics(Statboi statboi)
	{
		statboi.statboi = InformationSystem.getRangeSumStatistics(statboi.cityId, statboi.from, statboi.end);
		return statboi;
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
// End of EchoServer class
