package Game;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;




public class Main extends Application {

    public FXMLLoader loader;
    public static Stage stage;




    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        loader = new FXMLLoader(getClass().getResource("GOL.fxml"));
        Parent root = loader.load();


        Scene scene = new Scene(root);

        stage.setTitle("Game Of Life");
        stage.setScene(scene);
        stage.show();

    }
    
    static public Stage getPrimaryStage() {
        return Main.stage;
    }

    public static void main(String[] args) {
        launch(args);
    }




}
