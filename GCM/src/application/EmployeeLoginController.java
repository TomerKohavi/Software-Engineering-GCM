package application;

import java.io.IOException;
import java.net.URL;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
 * controller for employee to connect UI to client
 */
public class EmployeeLoginController
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

	@FXML // fx:id="Back"
	private JFXButton Back; // Value injected by FXMLLoader

	@FXML // fx:id="IncorrectText"
	private Text IncorrectText; // Value injected by FXMLLoader

	@FXML // fx:id="RegisterHL"
	private Hyperlink RegisterHL; // Value injected by FXMLLoader

	/**
	 * @param FXMLpage the fxml page
	 * @throws IOException if there is a error in load the fxml
	 */
	void loadPage(String FXMLpage) throws IOException
	{
		AnchorPane pane = (AnchorPane) FXMLLoader.load((URL) this.getClass().getResource(FXMLpage));
		mainPane.getChildren().setAll(pane);
	}

    /**
     * go to the previous page
     * @param event user click go previous page
     */
	@FXML
	void goBack(ActionEvent event) throws IOException
	{
		loadPage("HomePageScene.fxml");
	}

	
	/**
	 * @param event the customer ask for access to page
	 * @throws IOException cannot load page
	 */
	@FXML
	void customerAccess(ActionEvent event) throws IOException
	{
		loadPage("LoginScene.fxml");
	}

	/**
	 * @param event user click on register
	 * @throws IOException cannot loag the register page
	 */
	@FXML
	void register(ActionEvent event) throws IOException
	{
		loadPage("employeeRegisterScene.fxml");
	}

	/**
	 * get user passward
	 * @param event user click on get password button
	 */
	@FXML
	void getPassword(ActionEvent event)
	{
		System.out.println(Password.getText());
	}

	/**
	 * get user name
	 * @param event user click on get user name button
	 */
	@FXML
	void getUsername(ActionEvent event)
	{
		System.out.println(Username.getText());
	}

	/**
	 * @param event user click on login
	 * @throws IOException cannot login
	 */
	@FXML
	void login(ActionEvent event) throws IOException
	{
		user = Username.getText();
		pass = Password.getText();
		Pair<User, LoginRegisterResult> loginResult = Connector.client.login(user, pass, true);
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

}
