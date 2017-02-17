package Game;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller implements Initializable {


    public Button Stop;
    public Button Start;
    public Label Label;
    public Stage stage;
    public BorderPane BoarderPane;
    public Slider timer;


    private int playCount = 0;
    private int stopCount = 0;
    private int runCount = 1;
    private int neighbors;

    private double timing = 120;

    private Timeline timeline = new Timeline( new KeyFrame(Duration.millis(timing), e -> {

        draw_Array();
        nextGen();
        stage.setTitle("Game Of Life : " + runCount++ + " | Timing : " + timing);
    }
    ));


    @FXML
    private Canvas Canvas;

    private GraphicsContext gc;


    // sette en random størrelse på disse?
    public int kolonner = 105;
    public int rader = 70;

    public int[][] board = new int[kolonner][rader];
    public int[][] nextgeneration;




    public void startButton(){
        if(playCount == 0){
            randomPattern();
            timeline();
            Stop.setDisable(false);


        }
        timeline.play();
        stopCount = 0;
        playCount++;
    }

    public void stopButton(){
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
            //stage.close();
        }
    }

    public void randomPattern(){
        for (int i = 0; i < kolonner; i++) {
            for (int j = 0; j < rader ; j++) {
                board[i][j] = (Math.random()<0.5)?0:1;
            }
        }
    }


    private void timeline(){
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
    }

    private void draw_Array(){
        for (int i = 0; i < kolonner; i++) {
            for (int j = 0; j < rader ; j++) {
                if (board[i][j] == 1){
                    draw( i , j, Color.BLACK);
                }
                if (board[i][j] == 0){
                    draw(i, j, Color.WHITE);
                }
            }
        }
        // ----------------------------
    }

    private void nextGen(){
        nextgeneration = new int[kolonner][rader];
        for (int a = 0; a < board.length; a++) {
            for (int b = 0; b < board[a].length; b++) {
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
        gc.setFill(c);
        gc.fillRect(x*10 , y*10, 9, 9);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = Canvas.getGraphicsContext2D();
        Stop.setDisable(true);
        stage = Main.getPrimaryStage();

    }




}
