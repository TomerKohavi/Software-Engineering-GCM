package application;

import java.io.FileInputStream;
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
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application
{

	public void start(Stage primaryStage) throws IOException
	{
		// TODO PLEASE divide to packages, will be much easier plus George will like it
		// more.
		// TODO Gui for reconnection
		Connector.client = new ChatClient(Connector.SERVER_HOST, Connector.PORT, new Console());
		Connector.mainStage = primaryStage;
		FXMLLoader loader = new FXMLLoader(((Object) ((Object) this)).getClass().getResource("HomePageScene.fxml"));
		AnchorPane pane = (AnchorPane) loader.load();
		Scene scene = new Scene((Parent) pane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("GCM");
		primaryStage.getIcons().add(new Image(new FileInputStream("Pics\\GCM Logo_WB.png")));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args)
	{
		Main.launch((String[]) args);
		try
		{
			Connector.SERVER_HOST = args[0];
		}
		catch (IndexOutOfBoundsException e)
		{
			Connector.SERVER_HOST = "localhost";
		}
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
