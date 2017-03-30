package Game;



import java.net.URL;
import java.util.ResourceBundle;


import Game.StaticBoard;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
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
    public BorderPane BoarderPane;
    public Slider size;
    public Slider timer;
    public Canvas Canvas;


    public Stage stage;
    public ScrollPane scrollpane;


    private GraphicsContext gc;


    //Counters
    private int playCount = 0;
    private int stopCount = 0;
    private int runCount = 1;
    private int aliveCount = 0;

    public boolean loaded = false;

    public StaticBoard staticBoard;
    public BoardMaker boardMaker;
    public FileConverter fileConverter;
    public Mouse mouse;

    //Board

    public byte[][] board;
    public byte[][] nextGeneration;


    private double timing = 120;
    private double StartTimer = timing;
    private Timeline timeline = new Timeline( new KeyFrame(Duration.millis(timing), e -> {
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,2000,2000);

        nextGeneration();
        draw_Array();

        timerlistener();

        stage.setTitle("Game Of Life | Gen : " + runCount++ + " | Fps : " + Math.round((1000/(StartTimer/timing) )) + " | Size : " + Math.round(size.getValue()) + " | Alive : " + aliveCount );
        aliveCount = 0;
    }
    ));


    public void timerlistener(){
        timer.valueProperty().addListener((ObservableValue<? extends Number> timerlistener, Number oldtime, Number newtime) -> {
            timing = newtime.intValue();
            timeline.setRate(timing);
        });
    }
    
    //----for testing--------------------
    
      @Override
    public String toString(){
        
     String RowMajor = new String();

        for (int a = 0; a < board.length; a++) {
       for (int b = 0; b < board[a].length; b++) {
          
           RowMajor += board[a][b];
       }
        }

        return RowMajor;
    }

 public void setBoard(byte[][] gameBoard){
    board = gameBoard;  
 }
 public int getAlivecount(){
     return aliveCount;
 }

 //----------------------------------------
 
 
    public void startButton(){
        if (loaded) {
            if (playCount == 0) {
                board = staticBoard.getBoard();
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
            showClearBoard();

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



    public void draw_Array(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length ; j++) {
                if (board[i][j] == 1){
                    draw_ned( i  , j , colorPicker.getValue());
                    aliveCount ++;

                }
                else {
                    draw_ned(i , j , Color.WHITE);
                }
            }
        }
    }

    public void nextGeneration(){
        nextGeneration = new byte[board.length][board[0].length];
        for (int col = 1; col < board.length-1; col++) {
            for (int row = 1; row < board[col].length -1 ; row++) {
                int neighbors = countNeighbor(col,row);
                if(neighbors <  2) {
                    nextGeneration[col][row] = 0;
                }
                else if (neighbors >  3) {
                    nextGeneration[col][row] = 0;
                }
                else if (neighbors == 3) {
                    nextGeneration[col][row] = 1;
                }
                else {
                    nextGeneration[col][row] = board[col][row];
                }

            }
        }
        board = nextGeneration;
    }
    private int countNeighbor(int col, int row){
        int neighbors = 0;
        for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        neighbors += board[col+i][row+j];
      }
    }
        neighbors -= board[col][row];
        return neighbors;
    }

    @FXML private void remove_Array() {

        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length ; row++) {
                draw_ned(col , row, Color.WHITE);
            }
        }
    }

    private void draw( int col, int row, Color c) {
        gc = Canvas.getGraphicsContext2D();
        gc.setFill(Color.web("E0E0E0"));
        //gc.setFill(Color.WHITE);
        gc.fillRect(col* (size.getValue()/10) , row*(size.getValue()/10), ((size.getValue()/10)), (size.getValue()/10));
        gc.setFill(c);
        gc.fillRect((col * (size.getValue()/10))+1 , (row  * (size.getValue()/10))+1, ((size.getValue()/10) -2), (size.getValue()/10)-2);


    }
    private void draw_ned( int col, int row, Color c) {
        gc.setFill(Color.web("E0E0E0"));
        //gc.setFill(Color.WHITE);
        gc.fillRect(row* (size.getValue()/20) , col*(size.getValue()/20), ((size.getValue()/20)), (size.getValue()/20));
        gc.setFill(c);
        gc.fillRect((row * (size.getValue()/20))+1 , (col  * (size.getValue()/20))+1, ((size.getValue()/20) -2), (size.getValue()/20)-2);


    }


    public void load() {
        boardMaker.makeClearBoard();
        board = staticBoard.getBoard();
        draw_Array();
        gc.clearRect(0,0,600,600);
        stopCount = 0;
        Stop.setDisable(true);
        loaded = fileConverter.FromFileToBoard(staticBoard,boardMaker);
        if (loaded){
            board = staticBoard.getBoard();

            draw_Array();

            Start.setDisable(false);
        }
        else {
            showClearBoard();
            System.out.println(loaded);
        }
    }

    public void RandomBoard() {

        boardMaker.makeClearBoard();
        board = staticBoard.getBoard();
        draw_Array();
        stopCount = 0;
        Stop.setDisable(true);
        boardMaker.randomPattern(staticBoard);
        board = staticBoard.getBoard();
        draw_Array();
        loaded = true;
        Start.setDisable(false);
        System.out.println(board.length);

    }
    public void showClearBoard(){

        boardMaker.makeClearBoard(); // vurderer å endre alle over til å bare hente info fra Board classen. ER redundent å lagre alt i Controller når vi har tilgang til det i Board.
        board = staticBoard.getBoard();
        draw_Array();
    }

    public javafx.scene.canvas.Canvas getCanvas() {
        return Canvas;
    }
    public void resetSlider(){
        size.setValue(100);
        timer.setValue(1);
        colorPicker.setValue(Color.BLACK);
        try {

            Canvas.getTransforms().retainAll();

        }catch (Exception e){
            System.out.println(e);
        }

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        staticBoard = new StaticBoard();
        boardMaker = new BoardMaker(staticBoard);
        fileConverter = new FileConverter();
        gc = Canvas.getGraphicsContext2D();

        stage = Main.getPrimaryStage();
        colorPicker.setValue(Color.BLACK);

        //showClearBoard();

        Stop.setDisable(true);
        Start.setDisable(true);


        scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollpane.setPannable(true);


        //SceneGestures sceneGestures = new SceneGestures();
        //scrollpane.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        System.out.println(scrollpane.getHvalue());
        System.out.println(scrollpane.getVvalue());

        System.out.println(Canvas.getTransforms());

        mouse = new Mouse(Canvas);
        mouse.scroll();

    }
    /*
    public class SceneGestures {

        public EventHandler<ScrollEvent> getOnScrollEventHandler() {
            return onScrollEventHandler;
        }


        private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

            @Override
            public void handle(ScrollEvent event) {


                System.out.println(event.getX());
                double test = (event.getX() +event.getY())/2;
                size.setValue(test);

            }
        };



    }
    */
    public void onScrollEventHandler(ScrollEvent scrollEvent) {
/*
        if (scrollEvent.getDeltaY() > 0){
            System.out.println("inn");
        }
        else System.out.println("ut");


        System.out.println(scrollEvent.getX());
        System.out.println(scrollEvent.getY());
        */
    }

}
