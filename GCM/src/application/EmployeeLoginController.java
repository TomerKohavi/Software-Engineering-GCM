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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;


public class EmployeeLoginController {
	
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

    void loadPage(String FXMLpage) throws IOException {
        AnchorPane pane = (AnchorPane)FXMLLoader.load((URL)this.getClass().getResource(FXMLpage));
        mainPane.getChildren().setAll(pane);
    }
    
    @FXML
    void goBack(ActionEvent event) throws IOException {
    	loadPage("HomePageScene.fxml");
    }
    
    @FXML
    void customerAccess(ActionEvent event) throws IOException {
    	loadPage("LoginScene.fxml");
    }
    
    @FXML
    void register(ActionEvent event) throws IOException {
    	loadPage("employeeRegisterScene.fxml");
    }

    @FXML
    void getPassword(ActionEvent event) {
    	System.out.println(Password.getText());
    }

    @FXML
    void getUsername(ActionEvent event) {
    	System.out.println(Username.getText());
    }

    @FXML
    void login(ActionEvent event) throws IOException {
    	user = Username.getText();
    	pass = Password.getText();
    	Pair<Integer, Role> login_ans = Connector.client.login(user, pass, true);
    	if (login_ans.getKey() >= 0)
    	{
    		Connector.usr_id = login_ans.getKey();
    		Connector.usr_role = login_ans.getValue();
    		loadPage("HomePageScene.fxml");
    	}
    	else
    	{
    		Username.setText("");
    		Password.setText("");
    		IncorrectText.setOpacity(1);
    		
    	}
    }

}
