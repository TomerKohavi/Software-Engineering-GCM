package application;

import java.io.IOException;
import java.net.URL;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import classes.Employee.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EmployeeRegisterController {
	
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

    void loadPage(String FXMLpage) throws IOException {
        AnchorPane pane = (AnchorPane)FXMLLoader.load((URL)this.getClass().getResource(FXMLpage));
        mainPane.getChildren().setAll(pane);
    }
    
    @FXML
    void loginScene(ActionEvent event) throws IOException {
    	loadPage("EmployeeLoginScene.fxml");
    }

    @FXML
    void register(ActionEvent event) throws IOException, InterruptedException {
    	usr = Username.getText();
    	pass = Password.getText();
    	first = FirstName.getText();
    	last = LastName.getText();
    	emailAdd = Email.getText();
    	phoneNumber = Phone.getText();
    	Role role = Role.REGULAR; // TODO GET FROM UI
    	if (usr.equals("") || pass.equals("") || first.equals("") || last.equals("") || emailAdd.equals("") || phoneNumber.equals(""))
    	{
    		IncorrectText.setText("Please fill all of the above");
    		IncorrectText.setOpacity(1);
    	}
    	else
    	{
    		IncorrectText.setOpacity(0);
    		Connector.user = Connector.client.register(usr, pass, first, last, emailAdd, phoneNumber, role, true);
    		loadPage("HomePageScene.fxml");
    	}

    }
    
}
