package mytunes;

import java.awt.Color;
import java.awt.Paint;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mytunes.gui.util.WindowDecorator;

/**
 * The {@code MyTunes} class is a engine of our application. It runs
 * the whole program and sets the initial view.
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class Beatbone extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("gui/view/LoginView.fxml"));
        
        stage.setTitle("Beatbone");
        Scene scene = new Scene(root); 
        Image icon = new Image(getClass().getResourceAsStream("/mytunes/gui/images/Logo.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        WindowDecorator.showStage(stage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
