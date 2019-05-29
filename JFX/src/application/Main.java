package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main
extends Application {
    public void start(Stage primaryStage) throws IOException {
//        try {
//            this.localHostPopUp();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    	Connector.mainStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("HomePageScene.fxml"));
        AnchorPane pane = (AnchorPane)loader.load();
        Scene scene = new Scene((Parent)pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("GCM");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

//    public void localHostPopUp() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("HostPortScene.fxml"));
//        Parent root = (Parent)fxmlLoader.load();
//        Stage stage = new Stage();
//        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.setTitle("Host-Port");
//        stage.setScene(new Scene(root));
//        stage.showAndWait();
//    }

    public static void main(String[] args) {
        Main.launch((String[])args);
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    public void stop() throws IOException {
        
    }
}

