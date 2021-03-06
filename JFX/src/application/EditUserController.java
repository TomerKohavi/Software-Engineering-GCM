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

public class EditUserController {
	
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
    private JFXTextField CSV; // Value injected by FXMLLoader
    
    public void initialize() {
    	ExperationMonth.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    	
    	ArrayList<Integer> yearList = new ArrayList<Integer>();
    	for (int i=19; i < 100; i++)
    		yearList.add(2000 + i);
    	
    	ExperationYear.getItems().addAll(yearList);
    	
    	Username.setText("");
    	Password.setText("");
    	FirstName.setText("");
    	LastName.setText("");
    	Email.setText("");
    	Phone.setText("");
    	CreditCardNumber.setText("");
    	ExperationMonth.setValue(1);
    	ExperationYear.setValue(2019);
    	CSV.setText("");
    }
    
    void openNewPage(String FXMLpage) throws IOException {
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
    void applyChanges(ActionEvent event) throws IOException {
    	if (true) { // check that all of the inputs are valid
    		// send to server updated info 
    		// change the info in this running program
    		mainPane.getScene().getWindow().hide();
    	}
    	else
    		IncorrectText.setVisible(true);
    	
    }

    @FXML
    void viewPurchaseHistory(ActionEvent event) throws IOException {
    	openNewPage("PurchaseHistoryScene.fxml");
    }
    
    @FXML
    void goBack(ActionEvent event) throws IOException {
    	mainPane.getScene().getWindow().hide();
    }
    
}
