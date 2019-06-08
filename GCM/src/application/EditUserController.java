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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditUserController
{

	String usr, pass, first, last, emailAdd, phoneNumber;

	@FXML // fx:id="mainPane"
	private AnchorPane mainPane; // Value injected by FXMLLoader

	@FXML // fx:id="Username"
	private JFXTextField Username; // Value injected by FXMLLoader

	@FXML // fx:id="Password"
	private JFXPasswordField Password; // Value injected by FXMLLoader

	@FXML // fx:id="Register"
	private JFXButton ApplyChangesButton; // Value injected by FXMLLoader

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

	public void initialize()
	{
		Username.setText(Connector.user.getUserName());
		Password.setText(Connector.user.getPassword());
		FirstName.setText(Connector.user.getFirstName());
		LastName.setText(Connector.user.getLastName());
		Email.setText(Connector.user.getEmail());
		Phone.setText(Connector.user.getPhoneNumber());
	}

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

	@FXML
	void applyChanges(ActionEvent event) throws IOException
	{
		if (true)
		{ // check that all of the inputs are valid
			Connector.user.setUserName(Username.getText());
			Connector.user.setPassword(Password.getText());
			Connector.user.setFirstName(FirstName.getText());
			Connector.user.setLastName(LastName.getText());
			Connector.user.setEmail(Email.getText());
			Connector.user.setPhoneNumber(Phone.getText());
			Connector.client.updateUser(Connector.user);
			mainPane.getScene().getWindow().hide();
		}
		else
			IncorrectText.setVisible(true);

	}

	@FXML
	void viewPurchaseHistory(ActionEvent event) throws IOException
	{
		openNewPage("PurchaseHistoryScene.fxml");
	}

	@FXML
	void goBack(ActionEvent event) throws IOException
	{
		mainPane.getScene().getWindow().hide();
	}

}
