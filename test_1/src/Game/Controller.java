package Game;


import java.net.URL;
import java.util.ResourceBundle;


import javafx.animation.*;
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
    public Button reset;
    public Button Load;
    public Button Random;
    public ColorPicker colorPicker;
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
    private int aliveCount = 0;

    public boolean loaded = false;


    //Board
    public Board make_board;
    public byte[][] board;
    public byte[][] nextGeneration;


    private double timing = 120;
    private double StartTimer = timing;
    private Timeline timeline = new Timeline( new KeyFrame(Duration.millis(timing), e -> {
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,1050,700);
        draw_Array();
        nextGeneration();
        timerlistener();

        stage.setTitle("Game Of Life | Gen : " + runCount++ + " | Fps : " + Math.round((1000/(StartTimer/timing) )) + " | Size : " + Math.round(size.getValue()) + " | Alive : " + aliveCount );
        aliveCount = 0;
    }
    ));

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        gc = Canvas.getGraphicsContext2D();
        gc.fillText("Load board",225,250);
        stage = Main.getPrimaryStage();
        colorPicker.setValue(Color.BLACK);
        make_board = new Board();

        Stop.setDisable(true);
        Start.setDisable(true);


    }
    public void timerlistener(){
        timer.valueProperty().addListener((ObservableValue<? extends Number> timerlistener, Number oldtime, Number newtime) -> {
            timing = newtime.intValue();
            timeline.setRate(timing);
        });
    }

    public void startButton(){
        if (loaded) {
            if (playCount == 0) {
                board = make_board.getBoard();
                Load.setDisable(true);
                Random.setDisable(true);
                //make_board.randomPattern();

                timeline();
                Stop.setDisable(false);
            }
            timeline.play();
            stopCount = 0;
            playCount++;
            Start.setDisable(true);
            size.setDisable(false);
            Stop.setText("Stop");
            Stop.setTooltip(new Tooltip("Stop"));
        }
    }

    public void stopButton(){
        Start.setDisable(false);
        //size.setDisable(true);
        if(stopCount == 0){
            timeline.stop();
            stopCount++;
            Stop.setText("Clear");
            Stop.setTooltip(new Tooltip("Clear"));

        }
        else if( stopCount==1){
            remove_Array();
            timeline.stop();
            playCount = 0;
            runCount = 0;
            Stop.setText("Stop");
            Load.setDisable(false);
            Random.setDisable(false);
            stage.setTitle("Game Of Life ");
            gc.setFill(Color.WHITE);
            gc.fillRect(0,0,1500,1500);
            // satt inn else if, for å slippe å måtte flytte musen for å lukket programmet... ja, jeg er lat.
            // når ferdig fjern if( stopCount==1) og stopcount + else under. uncomment stop.
            //Stop.setDisable(true);
            stopCount++;
        }
        else {
            stage.close();
        }
    }

    private void timeline(){
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
    }

    public void resetSlider(){
        size.setValue(100);
        timer.setValue(1);
        colorPicker.setValue(Color.BLACK);
    }

    private void draw_Array(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length ; j++) {
                if (board[i][j] == 1){
                    draw( i  , j , colorPicker.getValue());
                    aliveCount ++;

                }
                else {
                    draw(i , j , Color.WHITE);
                }
            }
        }
    }

    private void nextGeneration(){
        nextGeneration = new byte[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length ; j++) {
                int neighbors = countNeighbor(i,j);
                if(neighbors <  2) {
                    nextGeneration[i][j] = 0;
                }
                else if (neighbors >  3) {
                    nextGeneration[i][j] = 0;
                }
                else if (neighbors == 3) {
                    nextGeneration[i][j] = 1;
                }
                else {
                    nextGeneration[i][j] = board[i][j];
                }

            }
        }
        board = nextGeneration;
    }
    private int countNeighbor(int i, int j){
        int neighbors = 0;
        if (i != 0 && j !=0){
            if (board[i-1][j-1] == 1)
                neighbors++;
        }
        if (j != 0){
            if (board[i][j-1] == 1)
                neighbors++;
        }
        try {
            if (i != board[i].length -1 && j != 0 ){
                if (board[i+1][j-1] == 1)
                    neighbors++;
            }
        }catch (IndexOutOfBoundsException  e){

        }
        if (i != 0){
            if (board[i-1][j]   == 1)
                neighbors++;
        }
        try {
            if (i != board[i].length -1){
                if (board[i+1][j]   == 1)
                    neighbors++;
            }
        }catch (Exception e) {

        }
        if(i != 0 && j != board[j].length -1){
            if (board[i-1][j+1] == 1)
                neighbors++;
        }
        if(j != board[j].length -1){
            if (board[i][j+1] == 1)
                neighbors++;
        }
        try {
            if(i != board[i].length - 1 && j != board[j].length -1){
                if (board[i+1][j+1] == 1)
                    neighbors++;
            }
        } catch (IndexOutOfBoundsException  e) {
        }
        return neighbors;
    }

    @FXML private void remove_Array() {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length ; j++) {
                draw( i , j, Color.WHITE);
            }
        }
    }

    private void draw( int x, int y, Color c) {
        gc.setFill(Color.web("E0E0E0"));
        //gc.setFill(Color.WHITE);
        gc.fillRect(x* (size.getValue()/10) , y*(size.getValue()/10), ((size.getValue()/10)), (size.getValue()/10));
        gc.setFill(c);
        gc.fillRect((x * (size.getValue()/10))+1 , (y  * (size.getValue()/10))+1, ((size.getValue()/10) -2), (size.getValue()/10)-2);


    }
    private void draw_ned( int x, int y, Color c) {
        gc.setFill(Color.web("E0E0E0"));
        //gc.setFill(Color.WHITE);
        gc.fillRect(y* (size.getValue()/10) , x*(size.getValue()/10), ((size.getValue()/10)), (size.getValue()/10));
        gc.setFill(c);
        gc.fillRect((y * (size.getValue()/10))+1 , (x  * (size.getValue()/10))+1, ((size.getValue()/10) -2), (size.getValue()/10)-2);


    }


    public void load() {
        make_board.copyFileBufferedIO();

        loaded = true;
        Start.setDisable(false);


    }

    public void RandomBoard() {
        make_board.randomPattern();
        loaded = true;
        Start.setDisable(false);
    }
}
