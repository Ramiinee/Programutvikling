package Game.Controller;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;



public class Main extends Application {

    public FXMLLoader loader;
    public static Stage stage;
    public Scene scene;



    /**
     * Tries to open the FXML file.
     * @param primaryStage Main Stage.
     * @throws IOException if FXMLLoader fails.
    */
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        stage = primaryStage;

        stage.getIcons().add(new Image("/Game/Icon.PNG"));

        loader = new FXMLLoader(getClass().getResource("GOL_new.fxml"));
        Parent root = loader.load();
        scene = new Scene(root);


        

        //stage.setResizable(false);
        stage.setTitle("Game Of Life");
        stage.setScene(scene);
        stage.show();


    }

    public static Stage getPrimaryStage() {
        return Main.stage;
    }

    public Scene getScene() {
        return scene;
    }
    /**
     * Launches Game of Life program through the start function.  
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
           
    }





}
