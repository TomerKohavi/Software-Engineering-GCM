// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;
import ocsf.client.*;
import common.*;
import io_commands.*;
import javafx.scene.layout.Region;
import javafx.util.Pair;
import java.io.*;
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

	
	
	boolean loginReady = false;
	Login login;
	
	boolean registerIDready = false;
	Register reg;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host
	 *            The server to connect to.
	 * @param port
	 *            The port number to connect on.
	 * @param clientUI
	 *            The interface type variable.
	 */

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();
		this.loginID = "ANONYMOUS";
		sendToServer("#login ANONYMOUS");
	}

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param loginID
	 *            The user ID.
	 * @param host
	 *            The server to connect to.
	 * @param port
	 *            The port number to connect on.
	 * @param clientUI
	 *            The interface type variable.
	 */

	public ChatClient(String loginID, String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		this.loginID = loginID;
		openConnection();
		sendToServer("#login " + loginID);
	}

	public Pair<Boolean, Integer> login(String uname, String pass)
	{
		try {
			sendToServer(new Login(uname, pass));
			for (int i = 0; !this.loginReady; i++) clientUI.display(i);
			this.loginReady = false;
			if (this.login.name.equals(uname))
			{
				Pair<Boolean, Integer> ret = new Pair<>(this.login.correct, this.login.type);
				this.login = null;
				return ret;
			}
			else
			{
				this.login = null;
				return new Pair<>(false, -1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Pair<>(false, -1);
	}
	
	public int register(String username, String password, String firstName, String lastName, String email, String phone)
	{
		try {
			sendToServer(new Register(username, password, firstName, lastName, email, phone));
			for (int i = 0; !this.registerIDready; i++) clientUI.display(i);
			this.registerIDready = false;
			if (this.reg.username.equals(username))
				return this.reg.id;
			else
				return -1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	// Instance methods ***********************************************
	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg
	 *            The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		
		if (msg instanceof Login)
		{
			this.login = (Login) msg;
			this.loginReady = true;
		}
		else if (msg instanceof Register)
		{
			this.reg = (Register) msg;
			this.registerIDready = true;
		}
		else
			clientUI.display(msg.toString());
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message
	 *            The message from the UI.
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
	 * @param message
	 *            string from the client console
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
		else if (message.toLowerCase().startsWith("#signin")) {
			String[] uname_pwd = message.substring(8).split("\\s+");
			if (uname_pwd.length != 2)
				clientUI.display("wrong number of parameters; #signin [username] [password]");
			else
				clientUI.display(this.login(uname_pwd[0], uname_pwd[1]));
		}
		else if (message.equalsIgnoreCase("#img")) {
			try {
				sendToServer(new ImageTransfer());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
//		else if (message.toLowerCase().startsWith("#signup")) {
//			String[] uname_pwd_type = message.substring(8).split("\\s+");
//			if (uname_pwd_type.length != 2 && uname_pwd_type.length != 3)
//				clientUI.display("wrong number of parameters; #signup [username] [password] [[type]]");
//			else
//				try {
//					int type;
//					try {
//						type = Integer.valueOf(uname_pwd_type[2]);
//					} catch (IndexOutOfBoundsException e) {
//						type = 1;
//					}
//					
//					sendToServer("#signup " + uname_pwd_type[0] + " " + secure(uname_pwd_type[1]) + " " + type + (this.signedin ? " " + this.signedID : ""));
//				} catch (IOException e) {
//					clientUI.display("Could not send message to server.  Terminating client.");
//					quit();
//				}
//		}
//		else if (message.equalsIgnoreCase("#display")
//				|| message.equalsIgnoreCase("#increment")
//				|| message.toLowerCase().startsWith("#update")) {
//			try {
//				if (this.signedin)
//					sendToServer(message + ' ' + this.signedID);
//				else
//					clientUI.display("Not connected, can't perform action");
//			} catch (IOException e) {
//			}
//		}
//		else if (message.toLowerCase().startsWith("#display")) {
//			try {
//				sendToServer(message);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		else if (message.equalsIgnoreCase("#signed"))
//			clientUI.display((this.signedin ? "logged in: " + this.signedID : "no user logged in"));
//		else if (message.equalsIgnoreCase("#signoff"))
//		{
//			this.signedID = "";
//			this.signedin = false;
//		}

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
	 * Overrides method in <code>AbstractClient</code>. Added by Benjamin
	 * Bergman, Oct 22, 2009.
	 *
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
		clientUI.display("The connection to the Server (" + getHost() + ", " + getPort() + ") has been disconnected");
	}
}
// End of ChatClient class
