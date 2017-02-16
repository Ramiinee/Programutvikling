package test_1;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;



public class Main extends Application {

    public FXMLLoader loader;


	@Override
	public void start(Stage primaryStage) throws Exception {
		
            loader = new FXMLLoader(getClass().getResource("GOL.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);

            primaryStage.setTitle("Game of Life");
            primaryStage.setScene(scene);
            primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}




}
