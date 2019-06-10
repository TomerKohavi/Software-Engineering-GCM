package application;

import java.sql.Time;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import objectClasses.OneTimePurchase;
import objectClasses.RouteStop;
import objectClasses.Subscription;

public class PurchaseHistoryController {

	@FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="Back"
    private JFXButton Back; // Value injected by FXMLLoader

    @FXML // fx:id="Active"
    private TableView<Subscription> Active; // Value injected by FXMLLoader

    @FXML // fx:id="PastSub"
    private TableView<Subscription> PastSub; // Value injected by FXMLLoader

    @FXML // fx:id="SubOneTime"
    private TableView<OneTimePurchase> PastOneTime; // Value injected by FXMLLoader


    public void initialize()
    {
    	ArrayList<OneTimePurchase> otpL = Connector.selectedCustomer.getCopyOneTimePurchase();
    	ArrayList<Subscription> acL = Connector.selectedCustomer.getCopyActiveSubscription();
    	ArrayList<Subscription> usL = Connector.selectedCustomer.getCopyUnactiveSubscription();
    	
//		ObservableList<Subscription> subs = FXCollections.observableArrayList(acL);
//		
//		TableColumn<Subscription, String> cityColumn = new TableColumn<>("City");
//		cityColumn.setMinWidth(100);
//		cityColumn.setCellValueFactory(new PropertyValueFactory<>("tempPlaceName"));
//		
//		TableColumn<Subscription, Double> priceColumn = new TableColumn<>("Price");
//		priceColumn.setMinWidth(100);
//		priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePayed"));
//		
//		TableColumn<Subscription, Double> dateColumn = new TableColumn<>("Date");
//		dateColumn.setMinWidth(100);
//		dateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
//		
//		TableColumn<Subscription, Integer> monthColumn = new TableColumn<>("Months");
//		monthColumn.setMinWidth(60);
//		monthColumn.setCellValueFactory(new PropertyValueFactory<>("numMonth"));
//		
//		Active.setItems(subs);
//		
//		Active.getColumns().clear();
//		Active.getColumns().addAll(cityColumn, priceColumn, dateColumn, monthColumn);
		
		
		ObservableList<OneTimePurchase> otp = FXCollections.observableArrayList(otpL);
		
		TableColumn<OneTimePurchase, String> cityColumn = new TableColumn<>("City");
		cityColumn.setMinWidth(100);
		cityColumn.setCellValueFactory(new PropertyValueFactory<>("tempPlaceName"));
		
		TableColumn<OneTimePurchase, Double> priceColumn = new TableColumn<>("Price");
		priceColumn.setMinWidth(100);
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePayed"));
		
		TableColumn<OneTimePurchase, Double> dateColumn = new TableColumn<>("Date");
		dateColumn.setMinWidth(100);
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));

		Active.setItems(subs);
		
		Active.getColumns().clear();
		Active.getColumns().addAll(cityColumn, priceColumn, dateColumn, monthColumn);
    	
//    	for (OneTimePurchase otp : otpL)
//    	{
//    		otp.getPricePayed();
//    		otp.getPurchaseDate();
//    		otp.getId();
//    	}
//    	
//    	for (Subscription sub : acL)
//    	{
//    		sub.getExpirationDate();
//    		sub.getPricePayed();
//    		sub.getNumMonths();
//    		sub.getCityId();
//    	}
//    	
//    	for (Subscription sub : usL)
//    	{
//    		sub.getExpirationDate();
//    		sub.getPricePayed();
//    		sub.getNumMonths();
//    		sub.getCityId();
//    	}
    }
    
    @FXML
    void goBack(ActionEvent event) {
    	
    }

}
