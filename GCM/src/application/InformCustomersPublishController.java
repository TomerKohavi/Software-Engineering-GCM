/**
 * Sample Skeleton for 'InformCustomersPublishScene.fxml' Controller Class
 */

package application;

import com.jfoenix.controls.JFXButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import objectClasses.Customer;
import objectClasses.OneTimePurchase;

/**
 * @author tomer
 * Display all the customers with published version 
 */
public class InformCustomersPublishController {

    @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="Back"
    private JFXButton Back; // Value injected by FXMLLoader

    @FXML // fx:id="CityText"
    private Text CityText; // Value injected by FXMLLoader

    @FXML // fx:id="CustomersTable"
    private TableView<Customer> CustomersTable; // Value injected by FXMLLoader
    
    /**
     * initialize variables
     */
    @SuppressWarnings("unchecked")
	void initialize ()
    {
    	ObservableList<Customer> customers = FXCollections.observableArrayList(Connector.getCustomersSubscibeToCity());
    	
    	TableColumn<Customer, String> username = new TableColumn<>("Username");
//    	username.setMinWidth(228);
//    	username.setMaxWidth(228);
    	username.setCellValueFactory(new PropertyValueFactory<>("userName"));
		
		TableColumn<Customer, String> email = new TableColumn<>("Email");
//		username.setMinWidth(50);
//		username.setMaxWidth(50);
		username.setCellValueFactory(new PropertyValueFactory<>("email"));
		
		CustomersTable.setItems(customers);
		
		CustomersTable.getColumns().clear();
		CustomersTable.getColumns().addAll(username, email);
    }

	/**
	 * go to the previous page
	 * 
	 * @param event user click go previous page
	 */
    @FXML
    void goBack(ActionEvent event) {

    }

}
