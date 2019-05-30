
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;
import common.*;
import db.DataBaseAccess;
import io_commands.*;
import javafx.util.Pair;

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
public class EchoServer extends AbstractServer {
	// Class variables *************************************************
	
	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	public final static int USER = 1;
	public static final int WORKER = 2;
	final public static int MANAGER = 3;
	public final static int CEO = 4;
	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF serverUI;
	DataBaseAccess dba;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port
	 *            The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port
	 *            The port number to connect on.
	 * @param serverUI
	 *            The interface type variable.
	 */
	
	public EchoServer(int port, ChatIF serverUI) throws IOException {
		super(port);
		this.dba = new DataBaseAccess();
		this.serverUI = serverUI;
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof Login)
		{
			Login login = (Login) msg;
			login.correct = true; // dba.signIn(login.name, login.pass);
			login.type = 10; //dba.getType(login.name);
			try {
				client.sendToClient(login);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (msg instanceof Register)
		{
			Register reg = (Register) msg;
			// TODO insert SIGNUP
			reg.id = 0;
			try {
				client.sendToClient(reg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (msg instanceof ImageTransfer)
			try {
				client.sendToClient("imageeeeeeeeeeeeeeee");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else if (msg.toString().startsWith("#login ")) {
			if (client.getInfo("loginID") != null) {
				try {
					client.sendToClient("You are already logged in.");
				} catch (IOException e) {
				}
				return;
			}
			client.setInfo("loginID", msg.toString().substring(7));
		}
//		else if (msg.toString().toLowerCase().startsWith("#signin"))
//		{
//			String[] uname_pwd = msg.toString().substring(8).split("\\s+");
//			boolean signedin = dba.signIn(uname_pwd[0], uname_pwd[1]);
//			try {
//				client.sendToClient(signedin ? "successful sign in" : "wrong username or password");
//				if (signedin || !signedin) // TODO CHANGED
//					client.sendToClient(new Login(uname_pwd[0], ""));
////					client.sendToClient(new Pair<String, Boolean>(uname_pwd[0], true));
//			} catch (IOException e) {
//			}
//		}
		else if (msg.toString().toLowerCase().startsWith("#display")) {
			try {
				String uname = msg.toString().substring(9);
				System.out.println(uname);
				client.sendToClient("current number of purchases: " + dba.display(uname));
			} catch (IOException e) {
				System.out.println("err");
			}
		}
		else if (msg.toString().toLowerCase().startsWith("#increment")) {
			try {
				String uname = msg.toString().substring(11);
				dba.inc(uname);
				client.sendToClient("Done increment.");
			} catch (IOException e) {
			}
		}	
		else if (msg.toString().toLowerCase().startsWith("#update")) {
			try {
				String[] input = msg.toString().substring(8).split("\\s+");
				String uname = input[1];
				Integer newval = Integer.valueOf(input[0]);
				System.out.println(uname + ' ' + newval);
				dba.changeTo(uname, newval);
				client.sendToClient("Done update.");
			} catch (IOException e) {
			}
		}
		else if (msg.toString().toLowerCase().startsWith("#signup")) {
			try {
				String[] input = msg.toString().substring(8).split("\\s+");
				String uname = input[0];
				String passwd = input[1];
				int type;
				type = Integer.valueOf(input[2]);
				String callerUname;
				try {
					callerUname = input[3];
				}
				catch (IndexOutOfBoundsException e)
				{
					callerUname = null;
				}
				if ((callerUname == null || dba.getType(callerUname) < CEO) && type > USER)
					client.sendToClient("Only CEO can sign new workers");
				else if (type >= CEO)
					client.sendToClient("Can't have another CEO or higher");
				else if (type < USER)
					client.sendToClient("type must be positive");
				else {
					dba.signUp(uname, passwd, type);
					client.sendToClient("profile " + uname + " has been created");
				}
			} catch (IOException e) {
			}
		} else {
			if (client.getInfo("loginID") == null) {
				try {
					client.sendToClient("You need to login before you can chat.");
					client.close();
				} catch (IOException e) {
				}
				return;
			}
			System.out.println("Message received: " + msg.toString()  + " from \"" + client.getInfo("loginID") + "\" " + client);
		}
		
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message
	 *            The message from the UI
	 */
	public void handleMessageFromServerUI(String message) {
		if (message.charAt(0) == '#') {
			runCommand(message);
		} else {
			// send message to clients
			serverUI.display(message);
			this.sendToAllClients("SERVER MSG> " + message);
		}
	}

	/**
	 * This method executes server commands.
	 *
	 * @param message
	 *            String from the server console.
	 */
	private void runCommand(String message) {
		// run commands
		// a series of if statements

		if (message.equalsIgnoreCase("#quit")) {
			quit();
		} else if (message.equalsIgnoreCase("#stop")) {
			stopListening();
		} else if (message.equalsIgnoreCase("#close")) {
			try {
				close();
			} catch (IOException e) {
			}
		} else if (message.toLowerCase().startsWith("#setport")) {
			if (getNumberOfClients() == 0 && !isListening()) {
				// If there are no connected clients and we are not
				// listening for new ones, assume server closed.
				// A more exact way to determine this was not obvious and
				// time was limited.
				int newPort = Integer.parseInt(message.substring(9));
				setPort(newPort);
				// error checking should be added
				serverUI.display("Server port changed to " + getPort());
			} else {
				serverUI.display("The server is not closed. Port cannot be changed.");
			}
		} else if (message.equalsIgnoreCase("#start")) {
			if (!isListening()) {
				try {
					listen();
				} catch (Exception ex) {
					serverUI.display("Error - Could not listen for clients!");
				}
			} else {
				serverUI.display("The server is already listening for clients.");
			}
		} else if (message.equalsIgnoreCase("#getport")) {
			serverUI.display("Currently port: " + Integer.toString(getPort()));
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * stops listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	/**
	 * Run when new clients are connected. Implemented by Benjamin Bergman, Oct
	 * 22, 2009.
	 *
	 * @param client
	 *            the connection connected to the client
	 */
	protected void clientConnected(ConnectionToClient client) {
		// display on server and clients that the client has connected.
		String msg = "A Client has connected";
		System.out.println(msg);
		this.sendToAllClients(msg);
	}

	/**
	 * Run when clients disconnect. Implemented by Benjamin Bergman, Oct 22,
	 * 2009
	 *
	 * @param client
	 *            the connection with the client
	 */
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		// display on server and clients when a user disconnects
		String msg = client.getInfo("loginID").toString() + " has disconnected";

		System.out.println(msg);
		this.sendToAllClients(msg);
	}

	/**
	 * Run when a client suddenly disconnects. Implemented by Benjamin Bergman,
	 * Oct 22, 2009
	 *
	 * @param client
	 *            the client that raised the exception
	 * @param Throwable
	 *            the exception thrown
	 */
	synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
		String msg = client.getInfo("loginID").toString() + " has disconnected";

		System.out.println(msg);
		this.sendToAllClients(msg);
	}

	/**
	 * This method terminates the server.
	 */
	public void quit() {
		try {
			close();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there
	 * is no UI in this phase).
	 *
	 * @param args[0]
	 *            The port number to listen on. Defaults to 5555 if no argument
	 *            is entered.
	 */
	public static void main(String[] args) {
		int port; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
// End of EchoServer class
