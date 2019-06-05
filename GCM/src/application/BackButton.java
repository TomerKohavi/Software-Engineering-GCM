package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BackButton {
	
	public static List<Stage>stageval = new ArrayList<Stage>();
	public static List<String>fxmlval = new ArrayList<String>();

    public void back(String instance,Stage stage) throws IOException{
                    Parent parent = FXMLLoader.load(getClass().getResource(instance));
                    Scene scene = new Scene(parent);
                    scene.getStylesheets().add("/css/Style.css");
                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.show();
    }

	@FXML
	public void back() throws IOException {
	    BackButton bb = new BackButton();
	
	    bb.back(fxmlval.get(fxmlval.size() - 1),        
	    stageval.get(stageval.size() - 1));
	    fxmlval.remove(fxmlval.size() - 1);
	    stageval.remove(stageval.size() - 1);
	}
}
