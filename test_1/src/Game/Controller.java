package Game;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller implements Initializable {

    // FXML
    public Button Stop;
    public Button Start;
    public Label Label;
    public BorderPane BoarderPane;
    public Slider size;
    public Slider timer;
    public Canvas Canvas;

    public Stage stage;
    private GraphicsContext gc;


    //Counters
    private int playCount = 0;
    private int stopCount = 0;
    private int runCount = 1;
    private int neighbors;

    //Board
    public Board make_board;
    public int[][] board;
    public int[][] nextgeneration;
    
    public double timing = 120;
    public double startTime = timing;
    
    private Timeline timeline = new Timeline( new KeyFrame(Duration.millis(timing), e -> {
         gc.setFill(Color.PINK);
        gc.fillRect(0,0,1500,1500);
        draw_Array();
        nextGen();
        timerlistener();
 
        
        
        stage.setTitle("Game Of Life : " + runCount++ + " | FPS : " + Math.round(1000/(startTime/timing)) + " | " + timing);
        
    }
    ));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(location);
        gc = Canvas.getGraphicsContext2D();

        stage = Main.getPrimaryStage();
        make_board = new Board();
       timing = timer.getValue();

        Stop.setDisable(true);
        
   
        
    }
    
    
    public void timerlistener(){
          timer.valueProperty().addListener((ObservableValue<? extends Number> timerlistener, Number oldtime, Number newtime) -> {
              System.out.println(timing);
              timing = newtime.intValue();
          timeline.setRate(timing);
        });
    }

    public void startButton(){
        timing = timer.getValue();
        
        
        if(playCount == 0){
            make_board.randomPattern();
            board = make_board.getBoard();
            timeline();
            Stop.setDisable(false);
        }
        timeline.play();
        stopCount = 0;
        playCount++;
        Start.setDisable(true);
        Stop.setText("Stop");
    }

    public void stopButton(){
        Start.setDisable(false);
        if(stopCount == 0){
            timeline.stop();
            stopCount++;
            Stop.setText("Clear");

        }
        else{
            remove_Array();
            timeline.stop();
            playCount = 0;
            runCount = 0;
            Stop.setText("Stop");
            Stop.setDisable(true);
            stage.setTitle("Game Of Life ");
            gc.setFill(Color.WHITE);
            gc.fillRect(0,0,1500,1500);
        }
    }

    private void timeline(){
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
    }

    private void draw_Array(){
        for (int i = 0; i < make_board.getKolonner(); i++) {
            for (int j = 0; j < make_board.getRader() ; j++) {
                if (board[i][j] == 1){
                    draw( i , j, Color.BLACK);
                }
                if (board[i][j] == 0){
                    draw(i, j, Color.WHITE);
                }
            }
        }
    }

    private void nextGen(){
        nextgeneration = new int[make_board.getKolonner()][make_board.getRader()];
        for (int a = 0; a < make_board.getKolonner(); a++) {
            for (int b = 0; b < make_board.getRader(); b++) {
                neighbors = 0;
                if (a != 0 && b !=0){
                    if (board[a-1][b-1] == 1)
                        neighbors++;
                }
                if (b != 0){
                    if (board[a][b-1] == 1)
                        neighbors++;
                }
                try {
                    if (a != board[a].length -1 && b != 0 ){
                        if (board[a+1][b-1] == 1)
                            neighbors++;
                    }
                }catch (IndexOutOfBoundsException  e){

                }
                if (a != 0){
                    if (board[a-1][b]   == 1)
                        neighbors++;
                }
                try {
                    if (a != board[a].length -1){
                        if (board[a+1][b]   == 1)
                            neighbors++;
                    }
                }catch (Exception e) {

                }
                if(a != 0 && b != board[b].length -1){
                    if (board[a-1][b+1] == 1)
                        neighbors++;
                }
                if(b != board[b].length -1){
                    if (board[a][b+1] == 1)
                        neighbors++;
                }
                try {
                    if(a != board[a].length - 1 && b != board[b].length -1){
                        if (board[a+1][b+1] == 1)
                            neighbors++;
                    }
                } catch (IndexOutOfBoundsException  e) {

                }

                //----------------------------

                if(neighbors <  2) {
                    nextgeneration[a][b] = 0;
                }
                else if (neighbors >  3) {
                    nextgeneration[a][b] = 0;
                }
                else if (neighbors == 3) {
                    nextgeneration[a][b] = 1;
                }
                else {
                    nextgeneration[a][b] = board[a][b];
                }
            }
        }

        board = nextgeneration;
    }

    @FXML private void remove_Array() {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length ; j++) {

                draw( i , j, Color.WHITE);
            }
        }
    }

    private void draw( int x, int y, Color c) {
        gc.setFill(Color.PINK);
        gc.fillRect(x* (size.getValue()/10) , y*(size.getValue()/10), ((size.getValue()/10) -1), (size.getValue()/10)-1);
        gc.setFill(c);
        gc.fillRect(x* (size.getValue()/10) , y*(size.getValue()/10), ((size.getValue()/10) -1), (size.getValue()/10)-1);


    }


}
