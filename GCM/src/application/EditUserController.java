package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import controller.RegCheck;
import controller.RegCheck.Res;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objectClasses.Customer;

/**
 * @author tomer
 * edit the user detiels from the UI
 */
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

	@FXML // fx:id="CreditCardNumber"
	private JFXTextField CreditCardNumber; // Value injected by FXMLLoader

	@FXML // fx:id="ExperationMonth"
	private JFXComboBox<Integer> ExperationMonth; // Value injected by FXMLLoader

	@FXML // fx:id="ExperationYear"
	private JFXComboBox<Integer> ExperationYear; // Value injected by FXMLLoader

	@FXML // fx:id="CSV"
	private JFXTextField CVC; // Value injected by FXMLLoader
	
    @FXML // fx:id="ViewPurchaseHistoryButton"
    private JFXButton ViewPurchaseHistoryButton; // Value injected by FXMLLoader

    /**
     * initialize the variables
     */
	public void initialize()
	{
		ExperationMonth.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

		ArrayList<Integer> yearList = new ArrayList<Integer>();
		for (int i = 19; i < 100; i++)
			yearList.add(2000 + i);

		ExperationYear.getItems().addAll(yearList);

		Username.setText(Connector.user.getUserName());
//		Password.setText(Connector.user.getPassword());
		Password.setText("");
		FirstName.setText(Connector.user.getFirstName());
		LastName.setText(Connector.user.getLastName());
		Email.setText(Connector.user.getEmail());
		Phone.setText(Connector.user.getPhoneNumber());
		if (Connector.user instanceof Customer)
		{
			Customer cust = (Customer) Connector.user;
			CreditCardNumber.setText(cust.getCreditCardNum()); // need to implement
			ExperationMonth.setValue(Integer.valueOf(cust.getCreditCardExpires().substring(0, 2)));
			ExperationYear.setValue(Integer.valueOf(cust.getCreditCardExpires().substring(3, 5)) + 2000);
			CVC.setText(cust.getCvc());
		}
		else
		{
			CreditCardNumber.setVisible(false);
			ExperationMonth.setVisible(false);
			ExperationYear.setVisible(false);
			CVC.setVisible(false);
			ViewPurchaseHistoryButton.setVisible(false);
		}
	}

	/**
	 * Tries opening a new page.
	 * @param FXMLpage the page we want to open
	 * @throws IOException if we can't open the page
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
	 * Applies the changes the user requested.
	 * @param event user click on change 
	 * @throws IOException there is problem with those changes
	 */
	@FXML
	void applyChanges(ActionEvent event) throws IOException
	{
		String uname = Username.getText();
		String pass = Password.getText();
		String fname = FirstName.getText();
		String lname = LastName.getText();
		String email = Email.getText();
		String phone = Phone.getText();
		String check = RegCheck.isValidUser(uname, pass, fname, lname, email, phone).getValue();
		if (check.equals("All Good"))
		{ // check that all of the inputs are valid
			if (Connector.user instanceof Customer)
			{
				Customer cust = (Customer) Connector.user;
				String ccard = CreditCardNumber.getText();
				String cvc = CVC.getText();

				Integer expM = ExperationMonth.getValue();
				Integer expY = ExperationYear.getValue();

				check = RegCheck.isValidCustomer(uname, pass, fname, lname, email, phone, ccard, expY, expM, cvc).getValue();
				if (check.equals("All Good"))
				{
					Connector.user.setUserName(uname);
					Connector.user.setPassword(pass);
					Connector.user.setFirstName(fname);
					Connector.user.setLastName(lname);
					Connector.user.setEmail(email);
					Connector.user.setPhoneNumber(phone);
					cust.setCreditCardNum(ccard);
					cust.setCreditCardExpires((expM < 10 ? "0" : "") + expM + "/" + (expY - 2000));
					cust.setCvc(cvc);
					Connector.client.updateUser(Connector.user);
					mainPane.getScene().getWindow().hide();
				}
				else
				{
					Connector.errorMsg = check;
					openNewPage("ErrorScene.fxml");
//					IncorrectText.setText(check);
//					IncorrectText.setVisible(true);
				}
			}
			else
			{
				Connector.user.setUserName(uname);
				Connector.user.setPassword(pass);
				Connector.user.setFirstName(fname);
				Connector.user.setLastName(lname);
				Connector.user.setEmail(email);
				Connector.user.setPhoneNumber(phone);
				Connector.client.updateUser(Connector.user);
				mainPane.getScene().getWindow().hide();
			}
		}
		else
		{
			Connector.errorMsg = check;
			openNewPage("ErrorScene.fxml");
//			IncorrectText.setText(check);
//			IncorrectText.setVisible(true);
		}
	}


	/**
	 * Displays the purchase history.
	 * @param event user click on watch history
	 * @throws IOException can't get to the history
	 */
	@FXML
	void viewPurchaseHistory(ActionEvent event) throws IOException
	{
		Connector.user = Connector.client.fetchUser(Connector.user.getId());
		Connector.selectedCustomer = (Customer) Connector.user;
		openNewPage("PurchaseHistoryScene.fxml");
	}

    /**
     * go to the previous page
     * @param event user click go previous page
     */
	@FXML
	void goBack(ActionEvent event) throws IOException
	{
		mainPane.getScene().getWindow().hide();
	}

}
