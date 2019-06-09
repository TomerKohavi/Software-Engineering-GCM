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
import controller.SearchCatalog;
import io_commands.*;
import javafx.scene.chart.PieChart.Data;
import objectClasses.Customer;
import objectClasses.Employee;
import objectClasses.User;
import objectClasses.Employee.Role;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer
{
	// Class variables *************************************************

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

	public Login handleLogin(Login login)
	{
		System.out.print("login " + login.name + " ");
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
				login.loggedUser = Database.getCustomerById(id);
				this.loggedList.add(login.loggedUser.getId());
			}
			else
				login.loggedUser = null;
		}

		if (login.loggedUser != null)
			System.out.println("success");
		else
			System.out.println("failure");
		login.delete();
		return login;
	}

	public void handleLogoff(Logoff logoff)
	{
		System.out.println("logoff " + logoff.logoffID + " " + this.loggedList.remove(logoff.logoffID));
	}

	public Register handleRegister(Register reg)
	{
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
		}
		this.loggedList.add(reg.user.getId());
		reg.delete();
		return reg;
	}

	public Search handleSearch(Search s)
	{
		s.searchResult = SearchCatalog.SearchCity(s.cityName, s.cityInfo, s.poiName, s.poiInfo);
		return s;
	}

	public void handleUpdateUser(User user)
	{
		System.out.print("update " + user.getId());
		if (user instanceof Customer)
			Database.saveCustomer((Customer) user);
		else
			Database.saveEmployee((Employee) user);
	}
	
	public CustomersRequest handleUsersRequest(CustomersRequest cr)
	{
		cr.custList = Database.getAllCustomers();
		return cr;
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
			{
				System.out.println("login sending");
				client.sendToClient(handleLogin((Login) msg));
				System.out.println("login sent");
			}
			else if (msg instanceof Logoff)
			{
				handleLogoff((Logoff) msg);
			}
			else if (msg instanceof Register)
			{
				System.out.println("reg sending");
				client.sendToClient(handleRegister((Register) msg));
				System.out.println("reg sent");

			}
			else if (msg instanceof ImageTransfer)
			{
				ImageTransfer imTr = (ImageTransfer) msg;
				if (imTr.requested)
				{
					imTr.loadImage();
					client.sendToClient(imTr);

				}
				else
					imTr.saveImage("C:\\Users\\yonat\\Pictures\\mememe.png");
			}
			else if (msg instanceof Search)
			{
				client.sendToClient(handleSearch((Search) msg));
			}
			else if (msg instanceof User)
			{
				handleUpdateUser((User) msg);
			}
			else if (msg instanceof CustomersRequest)
			{
				client.sendToClient(handleUsersRequest((CustomersRequest) msg)); 
			}
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
		{
			quit();
		}
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

	public static void main(String[] args) throws IOException
	{
		Database.createConnection();
		System.out.println(Database.searchCustomer(null, null));
		Database.closeConnection();
	}
}
// End of EchoServer class
