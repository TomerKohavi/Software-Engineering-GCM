package application;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import objectClasses.OneTimePurchase;
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
		ObservableList<OneTimePurchase> otpL = FXCollections.observableArrayList(Connector.selectedCustomer.getCopyOneTimePurchase());
		ObservableList<Subscription> acL = FXCollections.observableArrayList(Connector.selectedCustomer.getCopyActiveSubscription());
		ObservableList<Subscription> usL = FXCollections.observableArrayList(Connector.selectedCustomer.getCopyUnactiveSubscription());
		
		
		TableColumn<OneTimePurchase, String> oneCityColumn = new TableColumn<>("City");
		oneCityColumn.setMinWidth(150);
		oneCityColumn.setCellValueFactory(new PropertyValueFactory<>("cityName"));
		
		TableColumn<OneTimePurchase, Double> onePriceColumn = new TableColumn<>("Price");
		onePriceColumn.setMinWidth(50);
		onePriceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePayed"));
		
		TableColumn<OneTimePurchase, Double> oneDateColumn = new TableColumn<>("Date");
		oneDateColumn.setMinWidth(100);
		oneDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
		
		
		TableColumn<Subscription, String> cityColumn = new TableColumn<>("City");
		oneCityColumn.setMinWidth(150);
		oneCityColumn.setCellValueFactory(new PropertyValueFactory<>("cityName"));
		
		TableColumn<Subscription, Double> priceColumn = new TableColumn<>("Price");
		onePriceColumn.setMinWidth(50);
		onePriceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePayed"));
		
		TableColumn<Subscription, Double> dateColumn = new TableColumn<>("Date");
		dateColumn.setMinWidth(100);
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
		
		TableColumn<Subscription, Integer> monthColumn = new TableColumn<>("Months");
		monthColumn.setMinWidth(60);
		monthColumn.setCellValueFactory(new PropertyValueFactory<>("numMonth"));

		
		Active.setItems(acL);
		
		Active.getColumns().clear();
		Active.getColumns().addAll(cityColumn, priceColumn, dateColumn, monthColumn);
		
		PastSub.setItems(usL);
		
		PastSub.getColumns().clear();
		PastSub.getColumns().addAll(cityColumn, priceColumn, dateColumn, monthColumn);
		
		PastOneTime.setItems(otpL);
		
		PastOneTime.getColumns().clear();
		PastOneTime.getColumns().addAll(oneCityColumn, onePriceColumn, oneDateColumn);
    	
//    	for (OneTimePurchase otp : otpL)
//    	{
//    		otp.getPricePayed();
//    		otp.getPurchaseDate();
//    		otp.getCityName();
//    	}
//    	
//    	for (Subscription sub : acL)
//    	{
//    		sub.getExpirationDate();
//    		sub.getPricePayed();
//    		sub.getNumMonths();
//    		sub.getCityName();
//    	}
//    	
//    	for (Subscription sub : usL)
//    	{
//    		sub.getExpirationDate();
//    		sub.getPricePayed();
//    		sub.getNumMonths();
//    		sub.getCityName();
//    	}
    }
    
    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }

}
