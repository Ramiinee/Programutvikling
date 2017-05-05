package Game.Controller;
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
 * makes a site out of an fxml. 
 * @param primaryStage
 * @throws Exception 
 */
    @Override
    public void start(Stage primaryStage) throws Exception {
        
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
 * launch game of life program. 
 * @param args 
 */
    public static void main(String[] args) {

        launch(args);
        
        
    }





}
