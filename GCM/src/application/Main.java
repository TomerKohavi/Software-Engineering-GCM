package application;

import java.io.IOException;

import client.ChatClient;
import common.Console;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application
{
	public void start(Stage primaryStage) throws IOException
	{

		Connector.client = new ChatClient(Connector.LOCAL_HOST, Connector.PORT, new Console());
		Connector.mainStage = primaryStage;
		FXMLLoader loader = new FXMLLoader(((Object) ((Object) this)).getClass().getResource("HomePageScene.fxml"));
		AnchorPane pane = (AnchorPane) loader.load();
		Scene scene = new Scene((Parent) pane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("GCM");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args)
	{
		Main.launch((String[]) args);
	}

	@FXML
	public void exitApplication(ActionEvent event)
	{
		Platform.exit();
	}
	
	@Override
	public void stop() throws IOException
	{
		System.out.println("quitting");
		Connector.client.logoff();
		Connector.client.quit();
	}
}
