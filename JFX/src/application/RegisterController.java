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
import javafx.stage.Stage;

public class RegisterController {
	
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
    private JFXTextField CSV; // Value injected by FXMLLoader

    
    public void initialize () {
    	ExperationMonth.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    	
    	ArrayList<Integer> yearList = new ArrayList<Integer>();
    	for (int i=19; i < 100; i++)
    		yearList.add(2000 + i);
    	
    	ExperationYear.getItems().addAll(yearList);
    }
    
    void loadPage(String FXMLpage) throws IOException {
        AnchorPane pane = (AnchorPane)FXMLLoader.load((URL)this.getClass().getResource(FXMLpage));
        mainPane.getChildren().setAll(pane);
    }
    
    @FXML
    void loginScene(ActionEvent event) throws IOException {
    	loadPage("LoginScene.fxml");
    }

    @FXML
    void register(ActionEvent event) throws IOException {
    	usr = Username.getText();
    	pass = Password.getText();
    	first = FirstName.getText();
    	last = LastName.getText();
    	emailAdd = Email.getText();
    	phoneNumber = Phone.getText();
    	CreditCardNumber.getText();
    	ExperationMonth.getValue();
    	ExperationYear.getValue();
    	CSV.getText();
    	if (usr.equals("") || pass.equals("") || first.equals("") || last.equals("") || emailAdd.equals("") || phoneNumber.equals(""))
    	{
    		IncorrectText.setText("Please fill all of the above");
    		IncorrectText.setOpacity(1);
    	}
    	else
    	{
    		IncorrectText.setOpacity(0);
    		Connector.usr_id = 1;
    		loadPage("HomePageScene.fxml");
    	}
    }
    
}
