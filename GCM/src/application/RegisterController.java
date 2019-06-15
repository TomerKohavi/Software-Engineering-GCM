package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import objectClasses.User;
import otherClasses.Pair;
import server.EchoServer.LoginRegisterResult;
import controller.RegCheck;

/**
 * @author tomer
 * register controller that connect the UI register to the client
 */
public class RegisterController
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

	@FXML // fx:id="CreditCardNumber"
	private JFXTextField CreditCardNumber; // Value injected by FXMLLoader

	@FXML // fx:id="ExperationMonth"
	private JFXComboBox<Integer> ExperationMonth; // Value injected by FXMLLoader

	@FXML // fx:id="ExperationYear"
	private JFXComboBox<Integer> ExperationYear; // Value injected by FXMLLoader

	@FXML // fx:id="CSV"
	private JFXTextField CVC; // Value injected by FXMLLoader

	/**
	 * initialize variables
	 */
	public void initialize()
	{
		ExperationMonth.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

		ArrayList<Integer> yearList = new ArrayList<Integer>();
		for (int i = 19; i < 100; i++)
			yearList.add(2000 + i);

		ExperationYear.getItems().addAll(yearList);
	}

	/**
	 * user ask for load page
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
	 * Loads login page.
	 * @param event user click on login scene
	 * @throws IOException cannot load the login scene page
	 */
	@FXML
	void loginScene(ActionEvent event) throws IOException
	{
		loadPage("LoginScene.fxml");
	}

	/**
	 * Handles register events.
	 * @param event user click on register
	 * @throws IOException cannot register
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
		String creditCard = CreditCardNumber.getText(); // need to implement
		Integer expM = ExperationMonth.getValue();
		Integer expY = ExperationYear.getValue();
		String cvv = CVC.getText();
		String errorMsg = RegCheck.isValidCustomer(usr, pass, first, last, emailAdd, phoneNumber, creditCard, expY, expM ,cvv)
				.getValue();
		if (!errorMsg.equals("All Good"))
		{
			Connector.errorMsg = errorMsg;
			openNewPage("ErrorScene.fxml");
//			IncorrectText.setText(errorMsg);
//			IncorrectText.setOpacity(1);
		}
		else
		{
			String expires = (expM < 10 ? "0" : "") + expM + "/" + (expY - 2000);
			IncorrectText.setOpacity(0);
			Pair<User, LoginRegisterResult> regResult = Connector.client.register(usr, pass, first, last, emailAdd,
					phoneNumber, null, creditCard, expires, cvv, false);
			if (regResult.a != null && regResult.b == LoginRegisterResult.Success)
			{
				Connector.user = regResult.a;
				loadPage("HomePageScene.fxml");
			}
			else
			{
				Connector.errorMsg = regResult.b.getValue();
				openNewPage("ErrorScene.fxml");
//				IncorrectText.setText(regResult.b.getValue());
//				IncorrecIncorrectTexttText.setOpacity(1);
			}
		}
	}
}
