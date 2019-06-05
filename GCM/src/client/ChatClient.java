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

import application.Connector;
import classes.Employee.Role;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {
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
	int userID;

	boolean registerIDready;
	Register reg;

	boolean imageReady;
	ImageTransfer imTr;
	// Constructors ****************************************************
	private Role userRole;

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();
		this.loginID = "USER";
		sendToServer("#login USER");

		this.userID = -1;
		this.loginReady = false;
		this.registerIDready = false;
	}

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param loginID  The user ID.
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

//	public ChatClient(String loginID, String host, int port, ChatIF clientUI) throws IOException {
//		super(host, port); // Call the superclass constructor
//		this.clientUI = clientUI;
//		this.loginID = loginID;
//		openConnection();
//		sendToServer("#login " + loginID);
//	}

	public Pair<Integer, Role> login(String uname, String pass, boolean isEmployee) {
		try {
			sendToServer(new Login(uname, pass, isEmployee));
			for (int i = 0; !this.loginReady; i++)
				clientUI.display("login " + i);
			this.loginReady = false;
			this.userID = this.login.id;
			this.userRole = this.login.role;
			this.login = null;
			return new Pair<>(this.userID, this.userRole);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int register(String username, String password, String firstName, String lastName, String email,
			String phone, Role role, boolean isEmployee) {
		try {
			sendToServer(new Register(username, password, firstName, lastName, email, phone, role, isEmployee));
			for (int i = 0; !this.registerIDready; i++)
				clientUI.display("register " + i);
			this.registerIDready = false;
			if (this.reg.username.equals(username))
			{
				this.userID = this.reg.id;
				return this.reg.id;
			}
			else
				return -1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public void logoff() {
		this.userID = -1;
	}

	public BufferedImage getImage(String pathname) {
		try {
			sendToServer(new ImageTransfer(pathname, true));
			for (int i = 0; !this.imageReady; i++)
				clientUI.display("image " + i);
			this.imageReady = false;

			this.imTr.saveImage("C:\\Users\\yonat\\Documents\\cmeme.png");

			BufferedImage im = this.imTr.getImage();
			this.imTr = null;
			return im;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void sendImage(String pathname) {
		try {
			ImageTransfer imTr = new ImageTransfer(pathname, false);
			imTr.loadImage();
			sendToServer(imTr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Instance methods ***********************************************
	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {

		if (msg instanceof Login) {
			this.login = (Login) msg;
			this.loginReady = true;
		} else if (msg instanceof Register) {
			this.reg = (Register) msg;
			this.registerIDready = true;
		} else if (msg instanceof ImageTransfer) {
			this.imTr = (ImageTransfer) msg;
			this.imageReady = true;
		}

		else
			clientUI.display(msg.toString());
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {
		// detect commands
		if (message.charAt(0) == '#') {
			runCommand(message);
		} else {
			try {
				sendToServer(message);
			} catch (IOException e) {
				clientUI.display("Could not send message to server.  Terminating client.");
				quit();
			}
		}
	}

	/**
	 * This method executes client commands. Benjamin Bergman, Oct 22, 2009
	 *
	 * @param message string from the client console
	 */
	private void runCommand(String message) {
		// a bunch of ifs
		if (message.equalsIgnoreCase("#quit")) {
			quit();
		} else if (message.equalsIgnoreCase("#logoff")) {
			try {
				closeConnection();
			} catch (IOException e) {
			}
			clientUI.display("You have logged off.");
		} else if (message.toLowerCase().startsWith("#setport")) {
			// requires the command, followed by a space, then the port number
			try {
				int newPort = Integer.parseInt(message.substring(9));
				setPort(newPort);
				// error checking for syntax a possible addition
				clientUI.display("Port changed to " + getPort());
			} catch (Exception e) {
				System.out.println("Unexpected error while setting client port!");
			}
		} else if (message.toLowerCase().startsWith("#sethost")) {
			setHost(message.substring(9));
			clientUI.display("Host changed to " + getHost());
		} else if (message.toLowerCase().startsWith("#login")) {
			if (isConnected()) {
				clientUI.display("You must logout before you can login.");
				return;
			}
			// login
			// if no id, login anonymous
			loginID = message.substring(7);
			try {
				openConnection();
				sendToServer("#login " + loginID);
			} catch (Exception e) {
				clientUI.display("Connection could not be established.");
			}
		} else if (message.equalsIgnoreCase("#gethost")) {
			clientUI.display("Current host: " + getHost());
		} else if (message.equalsIgnoreCase("#getport")) {
			clientUI.display("Current port: " + Integer.toString(getPort()));
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
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
	protected void connectionException(Exception exception) {
		clientUI.display("The connection to the Server (" + getHost() + ", " + getPort() + ") has been disconnected");
	}

	public static void main(String[] args) throws IOException {
		ChatClient client = new ChatClient(Connector.LOCAL_HOST, Connector.PORT, new Console());
		System.out.println(client.getImage("C:\\Users\\yonat\\Documents\\meme.png"));
	}
}
// End of ChatClient class
