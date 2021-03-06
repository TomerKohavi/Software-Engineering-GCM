package application;

import java.io.IOException;
import java.net.URL;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import objectClasses.User;
import otherClasses.Pair;
import server.EchoServer.LoginRegisterResult;

/**
 * @author tomer
 * treat login page and connect to the client
 */
public class LoginController
{

	private String user = "";
	private String pass = "";

	@FXML // fx:id="mainPane"
	private AnchorPane mainPane; // Value injected by FXMLLoader

	@FXML // fx:id="Username"
	private JFXTextField Username; // Value injected by FXMLLoader

	@FXML // fx:id="Password"
	private JFXPasswordField Password; // Value injected by FXMLLoader

	@FXML // fx:id="LoginButton"
	private JFXButton LoginButton; // Value injected by FXMLLoader

	@FXML // fx:id="EmployeeAccess"
	private JFXButton EmployeeAccess; // Value injected by FXMLLoader

	@FXML // fx:id="IncorrectText"
	private Text IncorrectText; // Value injected by FXMLLoader

	@FXML // fx:id="RegisterHL"
	private Hyperlink RegisterHL; // Value injected by FXMLLoader

	@FXML // fx:id="Back"
	private JFXButton Back; // Value injected by FXMLLoader

	/**
	 * Loads generic FXML page.
	 * @param FXMLpage the FXML page 
	 * @throws IOException can be thrown by FXMLLoader
	 */
	void loadPage(String FXMLpage) throws IOException
	{
		AnchorPane pane = (AnchorPane) FXMLLoader.load((URL) this.getClass().getResource(FXMLpage));
		mainPane.getChildren().setAll(pane);
	}

	/**
	 * Handles Employee access scene.
	 * @param event the FXML event we are working on
	 * @throws IOException  can be thrown by loadPage
	 */
	@FXML
	void employeeAcessScene(ActionEvent event) throws IOException
	{
		loadPage("EmployeeLoginScene.fxml");
	}

	/**
	 * Gets password from textbox and saves locally.
	 * @param event the FXML event we are working on
	 */
	@FXML
	void getPassword(ActionEvent event)
	{
		pass = Password.getText();
	}

	/**
	 * Gets username from textbox and saves locally.
	 * @param event the FXML event we are working on
	 */
	@FXML
	void getUsername(ActionEvent event)
	{
		user = Username.getText();
	}

	/**
	 * Handles login try.
	 * @param event the FXML event we are working on
	 * @throws IOException can be thrown by clentLogin
	 */
	@FXML
	void login(ActionEvent event) throws IOException
	{
		user = Username.getText();
		pass = Password.getText();
		Pair<User, LoginRegisterResult> loginResult = Connector.client.login(user, pass, false);
		if (loginResult.b == LoginRegisterResult.Success && loginResult.a != null)
		{
			Connector.user = loginResult.a;
			loadPage("HomePageScene.fxml");
		}
		else
		{
			Username.setText("");
			Password.setText("");
			IncorrectText.setText(loginResult.b.getValue());
			IncorrectText.setOpacity(1);
		}
	}
	/**
	 * Go back to the homepage.
	 * @param event the FXML event we are working on
	 * @throws IOException can be thrown by loadPage
	 */
	@FXML
	void goBack(ActionEvent event) throws IOException
	{
		loadPage("HomePageScene.fxml");
	}

	/**
	 * Moves to register scene.
	 * @param event the FXML event we are working on
	 * @throws IOException can be thrown by loadPage
	 */
	@FXML
	void register(ActionEvent event) throws IOException
	{
		loadPage("RegisterScene.fxml");
	}

}
