package application;

import java.io.IOException;
import java.net.URL;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import controller.RegCheck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import objectClasses.User;
import objectClasses.Employee.Role;
import otherClasses.Pair;
import server.EchoServer.LoginRegisterResult;

public class EmployeeRegisterController
{

	String usr, pass, first, last, emailAdd, phoneNumber;

	@FXML // fx:id="mainPane"
	private AnchorPane mainPane; // Value injected by FXMLLoader

	@FXML // fx:id="Username"
	private JFXTextField Username; // Value injected by FXMLLoader

	@FXML // fx:id="Password"
	private JFXPasswordField Password; // Value injected by FXMLLoader

	@FXML // fx:id="Register"
	private JFXButton Register; // Value injected by FXMLLoader

	@FXML // fx:id="Back"
	private JFXButton Back; // Value injected by FXMLLoader

	@FXML // fx:id="IncorrectText"
	private Text IncorrectText; // Value injected by FXMLLoader

	@FXML // fx:id="FirstName"
	private JFXTextField FirstName; // Value injected by FXMLLoader

	@FXML // fx:id="LastName"
	private JFXTextField LastName; // Value injected by FXMLLoader

	@FXML // fx:id="Email"
	private JFXTextField Email; // Value injected by FXMLLoader

	@FXML // fx:id="Phone"
	private JFXTextField Phone; // Value injected by FXMLLoader

	@FXML // fx:id="RoleBox"
	private JFXComboBox<String> RoleBox; // Value injected by FXMLLoader

	public void initialize()
	{
		RoleBox.getItems().addAll("Regular", "Manager", "CEO");
	}

	void loadPage(String FXMLpage) throws IOException
	{
		AnchorPane pane = (AnchorPane) FXMLLoader.load((URL) this.getClass().getResource(FXMLpage));
		mainPane.getChildren().setAll(pane);
	}

	@FXML
	void loginScene(ActionEvent event) throws IOException
	{
		loadPage("EmployeeLoginScene.fxml");
	}

	@FXML
	void register(ActionEvent event) throws IOException
	{
		usr = Username.getText();
		pass = Password.getText();
		first = FirstName.getText();
		last = LastName.getText();
		emailAdd = Email.getText();
		phoneNumber = Phone.getText();
		Role role = RoleBox.getValue().equals("Regular") ? Role.REGULAR
				: (RoleBox.getValue().equals("Manager") ? Role.MANAGER : Role.CEO);
		String errorMsg = RegCheck.isValidUser(usr, pass, first, last, emailAdd, phoneNumber).getValue();
		if (!errorMsg.equals("All Good"))
		{
			IncorrectText.setText(errorMsg);
			IncorrectText.setOpacity(1);
		}
		else
		{
			IncorrectText.setOpacity(0);
			Pair<User, LoginRegisterResult> regResult = Connector.client.register(usr, pass, first, last, emailAdd,
					phoneNumber, role, null, null, null, true);
			if (regResult.a != null && regResult.b == LoginRegisterResult.Success)
			{
				Connector.user = regResult.a;
				loadPage("HomePageScene.fxml");
			}
			else
			{
				IncorrectText.setText(regResult.b.getValue());
				IncorrectText.setOpacity(1);
			}
		}
	}
}
