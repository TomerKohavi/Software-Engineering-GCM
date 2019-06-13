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
import javafx.stage.Modality;
import javafx.stage.Stage;
import objectClasses.User;
import objectClasses.Employee.Role;
import otherClasses.Pair;
import server.EchoServer.LoginRegisterResult;

/**
 * @author tomer
 * controller that connect employee register UI with the client
 */
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

	/**
	 * initialize variables
	 */
	public void initialize()
	{
		RoleBox.getItems().addAll("Regular", "Manager", "CEO");
	}

	/**
	 * @param FXMLpage the page we want to load
	 * @throws IOException cannot load the page
	 */
	void loadPage(String FXMLpage) throws IOException
	{
		AnchorPane pane = (AnchorPane) FXMLLoader.load((URL) this.getClass().getResource(FXMLpage));
		mainPane.getChildren().setAll(pane);
	}
	
	/**
	 * open new page
	 * @param FXMLpage the page we want to open
	 * @throws IOException cannot open the page
	 */
	void openNewPage(String FXMLpage) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLpage));
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(mainPane.getScene().getWindow());
		stage.setScene(new Scene((Parent) loader.load()));
		stage.setResizable(false);

		// showAndWait will block execution until the window closes...
		stage.showAndWait();
	}

	/**
	 * @param event load the employee login scene
	 * @throws IOException
	 */
	@FXML
	void loginScene(ActionEvent event) throws IOException
	{
		loadPage("EmployeeLoginScene.fxml");
	}

	/**
	 * @param event user click on register
	 * @throws IOException cannot registe the user
	 */
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
			Connector.errorMsg = errorMsg;
			openNewPage("ErrorScene.fxml");
//			IncorrectText.setText(errorMsg);
//			IncorrectText.setOpacity(1);
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
				Connector.errorMsg = errorMsg;
				openNewPage("ErrorScene.fxml");
//				IncorrectText.setText(regResult.b.getValue());
//				IncorrectText.setOpacity(1);
			}
		}
	}
}
