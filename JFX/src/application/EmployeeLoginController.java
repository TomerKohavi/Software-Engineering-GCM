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
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


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
        this.mainPane.getChildren().setAll((Node[])new Node[]{pane});
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
    	if (user.equals(pass))
    	{
    		Connector.usr_id = "1";
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
