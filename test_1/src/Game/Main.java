package Game;
import javafx.application.Application;
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
        stage.setResizable(false);
        stage.setTitle("Game Of Life");
        stage.setScene(scene);
        stage.show();






    }
    private void setPrimaryStage(Stage stage) {
        Main.stage = stage;
    }

    static public Stage getPrimaryStage() {
        return Main.stage;
    }

    public static void main(String[] args) {

        launch(args);
    }




}
