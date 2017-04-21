package Game.Controller;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;



public class Main extends Application {

    public FXMLLoader loader;
    public static Stage stage;
    public Scene scene;




    @Override
    public void start(Stage primaryStage) throws Exception {


        stage = primaryStage;


        loader = new FXMLLoader(getClass().getResource("GOL.fxml"));
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

    public static void main(String[] args) {

        launch(args);
    }





}
