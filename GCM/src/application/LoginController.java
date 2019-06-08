package application;

import java.io.IOException;
import java.net.URL;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController {
	
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

    void loadPage(String FXMLpage) throws IOException {
        AnchorPane pane = (AnchorPane)FXMLLoader.load((URL)this.getClass().getResource(FXMLpage));
        mainPane.getChildren().setAll(pane);
    }
    
    @FXML
    void employeeAcessScene(ActionEvent event) throws IOException {
    	loadPage("EmployeeLoginScene.fxml");
    }

    @FXML
    void getPassword(ActionEvent event) {
    	pass = Password.getText();
    }

    @FXML
    void getUsername(ActionEvent event) {
    	user = Username.getText();
    }

    @FXML
    void login(ActionEvent event) throws IOException {
    	user = Username.getText();
    	pass = Password.getText();
    	User loggedUser = Connector.client.login(user, pass, false);
    	if (loggedUser != null)
    	{
    		Connector.user = loggedUser;
    		loadPage("HomePageScene.fxml");
    	}
    	else
    	{
    		Username.setText("");
    		Password.setText("");
    		IncorrectText.setOpacity(1);
    		
    	}
    }
    
    @FXML
    void goBack(ActionEvent event) throws IOException {
    	loadPage("HomePageScene.fxml");
    }
    

    @FXML
    void register(ActionEvent event) throws IOException {
    	loadPage("RegisterScene.fxml");
    }

}
