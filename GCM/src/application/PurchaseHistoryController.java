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


    @SuppressWarnings("unchecked")
	public void initialize()
    {	
		ObservableList<OneTimePurchase> otpL = FXCollections.observableArrayList(Connector.selectedCustomer.getCopyOneTimePurchase());
		ObservableList<Subscription> acL = FXCollections.observableArrayList(Connector.selectedCustomer.getCopyActiveSubscription());
		ObservableList<Subscription> usL = FXCollections.observableArrayList(Connector.selectedCustomer.getCopyUnactiveSubscription());
		
		
		TableColumn<OneTimePurchase, String> oneCityColumn = new TableColumn<>("City");
		oneCityColumn.setMinWidth(228);
		oneCityColumn.setCellValueFactory(new PropertyValueFactory<>("cityName"));
		
		TableColumn<OneTimePurchase, Double> onePriceColumn = new TableColumn<>("Price");
		onePriceColumn.setMinWidth(50);
		onePriceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePayed"));
		
		TableColumn<OneTimePurchase, Double> oneDateColumn = new TableColumn<>("Date");
		oneDateColumn.setMinWidth(80);
		oneDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
		
		
		TableColumn<Subscription, String> cityColumn = new TableColumn<>("City");
		cityColumn.setMinWidth(167);
		cityColumn.setCellValueFactory(new PropertyValueFactory<>("cityName"));
		
		TableColumn<Subscription, Double> priceColumn = new TableColumn<>("Price");
		priceColumn.setMinWidth(50);
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePayed"));
		
		TableColumn<Subscription, Double> dateColumn = new TableColumn<>("Date");
		dateColumn.setMinWidth(80);
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
		
		TableColumn<Subscription, Integer> monthColumn = new TableColumn<>("Months");
		monthColumn.setMinWidth(60);
		monthColumn.setCellValueFactory(new PropertyValueFactory<>("numMonth"));
		
		
		TableColumn<Subscription, String> pastCityColumn = new TableColumn<>("City");
		pastCityColumn.setMinWidth(167);
		pastCityColumn.setCellValueFactory(new PropertyValueFactory<>("cityName"));
		
		TableColumn<Subscription, Double> pastPriceColumn = new TableColumn<>("Price");
		pastPriceColumn.setMinWidth(50);
		pastPriceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePayed"));
		
		TableColumn<Subscription, Double> pastDateColumn = new TableColumn<>("Date");
		pastDateColumn.setMinWidth(80);
		pastDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
		
		TableColumn<Subscription, Integer> pastMonthColumn = new TableColumn<>("Months");
		pastMonthColumn.setMinWidth(60);
		pastMonthColumn.setCellValueFactory(new PropertyValueFactory<>("numMonth"));

		
		Active.setItems(acL);
		
		Active.getColumns().clear();
		Active.getColumns().addAll(cityColumn, priceColumn, dateColumn, monthColumn);
		
		PastSub.setItems(usL);
		
		PastSub.getColumns().clear();
		PastSub.getColumns().addAll(pastCityColumn, pastPriceColumn, pastDateColumn, pastMonthColumn);
		
		PastOneTime.setItems(otpL);
		
		PastOneTime.getColumns().clear();
		PastOneTime.getColumns().addAll(oneCityColumn, onePriceColumn, oneDateColumn);
		
    }
    
    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }

}
