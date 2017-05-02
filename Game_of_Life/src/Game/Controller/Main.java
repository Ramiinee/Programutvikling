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




    @Override
    public void start(Stage primaryStage) throws Exception {


        stage = primaryStage;
        
        Image Icon = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1c/Game_of_life_fpento.svg/2000px-Game_of_life_fpento.svg.png");
        primaryStage.getIcons().add(Icon);
        
        
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

    public static void main(String[] args) {

        launch(args);
    }





}
