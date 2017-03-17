package Game;


import java.net.URL;
import java.util.ResourceBundle;

import Game.Board;
import Game.StaticBoard;
import java.util.ArrayList;
import java.util.List;
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
    public Button Dynstart;
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

    public StaticBoard staticBoard = new StaticBoard();
    public DynamicBoard dynamicBoard = new DynamicBoard();
    public BoardMaker boardMaker = new BoardMaker(staticBoard);
    public DynamicBoardMaker dynboardMaker = new DynamicBoardMaker(dynamicBoard);
    public FileConverter fileConverter = new FileConverter();
    

    //Board

    public byte[][] board;
    public byte[][] nextGeneration;
    ArrayList<List<Byte>> arraylist = new ArrayList<>();
    ArrayList<List<Byte>> dynnextgen = new ArrayList<>();

    private double timing = 120;
    private double StartTimer = timing;
    private Timeline timeline = new Timeline( new KeyFrame(Duration.millis(timing), e -> {
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,1050,700);

        nextGeneration();
        draw_Array();

        timerlistener();

        stage.setTitle("Game Of Life | Gen : " + runCount++ + " | Fps : " + Math.round((1000/(StartTimer/timing) )) + " | Size : " + Math.round(size.getValue()) + " | Alive : " + aliveCount );
        aliveCount = 0;
    }
    ));

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        gc = Canvas.getGraphicsContext2D();

        stage = Main.getPrimaryStage();
        colorPicker.setValue(Color.BLACK);

        //showClearBoard();

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
            Dynstart.setDisable(true);
            Stop.setText("Stop");
            Stop.setTooltip(new Tooltip("Stop"));
        }
    }
    
    

    public void stopButton(){
        Start.setDisable(false);
        Dynstart.setDisable(false);
        //size.setDisable(true);
        if(stopCount == 0){
            timeline.stop();
            Dyntimeline.stop();
            stopCount++;
            Stop.setText("Clear");
            Stop.setTooltip(new Tooltip("Clear"));

        }
        else if( stopCount==1){
            remove_Array();
            timeline.stop();
            Dyntimeline.stop();
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

    public void resetSlider(){
        size.setValue(100);
        timer.setValue(1);
        colorPicker.setValue(Color.BLACK);
    }
   
    private void draw_Array(){
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

    private void nextGeneration(){
        nextGeneration = new byte[board.length][board[0].length];
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
            System.out.println("indexoutofbound når de når nederst i høyre hjørnet. ");
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
        gc = Canvas.getGraphicsContext2D();
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
        boardMaker.makeClearBoard();
        dynboardMaker.makeClearBoard();
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
        dynboardMaker.makeClearBoard();
        board = staticBoard.getBoard();
        draw_Array();
        stopCount = 0;
        Stop.setDisable(true);
        boardMaker.randomPattern(staticBoard);
        dynboardMaker.randomPattern(dynamicBoard);
        board = staticBoard.getBoard();
        draw_Array();
        loaded = true;
        Start.setDisable(false);
        System.out.println(board.length);

    }
    public void showClearBoard(){

        boardMaker.makeClearBoard();
        dynboardMaker.makeClearBoard();// vurderer å endre alle over til å bare hente info fra Board classen. ER redundent å lagre alt i Controller når vi har tilgang til det i Board.
        board = staticBoard.getBoard();
        draw_Array();
    }

    
    
    // isrjgisjsorigjsorigjsogijosrigjosrigjosigjosrigjsrogijsroijoigjsosigjosgijrsoz
   
     private Timeline Dyntimeline = new Timeline( new KeyFrame(Duration.millis(timing), e -> {
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,1050,700);

        DynNextGen();
        draw_Array();

        timerlistener();

        stage.setTitle("Game Of Life | Gen : " + runCount++ + " | Fps : " + Math.round((1000/(StartTimer/timing) )) + " | Size : " + Math.round(size.getValue()) + " | Alive : " + aliveCount );
        aliveCount = 0;
    }
    ));
    
     private void dyntimeline(){
        Dyntimeline.setCycleCount(Timeline.INDEFINITE);
        Dyntimeline.setAutoReverse(false);
    }
    
public void DynStartButton(){
        if (loaded) {
            if (playCount == 0) {
                arraylist = dynamicBoard.getarrayList();
                Load.setDisable(true);
                Random.setDisable(true);
                //make_board.randomPattern();

                dyntimeline();
                Stop.setDisable(false);
            }
            Dyntimeline.play();
            stopCount = 0;
            playCount++;
            Start.setDisable(true);
            size.setDisable(false);
            Stop.setText("Stop");
            Stop.setTooltip(new Tooltip("Stop"));
        }
    }
        private void DynNextGen(){
            
        dynnextgen = new ArrayList<List<Byte>>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length ; j++) {
                int neighbors = 0;
                
                Byte nu = 0;
                Byte en = 1;
                Byte same = board[i][j];
                if(neighbors <  2) {
                   dynnextgen.get(i).set(j, nu);
                }
                else if (neighbors >  3) {
                   dynnextgen.get(i).set(j, nu); 
                }
                else if (neighbors == 3) {
                   dynnextgen.get(i).set(j, en);
                }
                else {
                   dynnextgen.get(i).set(j, same);
                }

            }
        }
        board = nextGeneration;
    }
        private int countfordynamic(int i, int j){
        int neighbors = 0;
        

        
        
        for( int y = 0; y < board.length; y++){
            arraylist.add(new ArrayList<Byte>());
            for(int u = 0; u < board[y].length;  u++){
                
            Byte tall = board[y][u];
            arraylist.get(y).set(u, tall);         
        }

        }
        
        
        
            if (arraylist.get(i).get(j) == 1)
                neighbors++;
        
            else if (arraylist.get(i).get(j-1) == 1)
                neighbors++;
        
            else if (arraylist.get(i+1).get(j-1) == 1)
                    neighbors++;

            else if (arraylist.get(i-1).get(j)   == 1)
                neighbors++;

            else if (arraylist.get(i+1).get(j)   == 1)
                    neighbors++;

            else if (arraylist.get(i-1).get(j+1) == 1)
                neighbors++;
 
            else if (arraylist.get(i).get(j+1) == 1)
                neighbors++;
   
            else if(arraylist.get(i+1).get(j+1) == 1)
                    neighbors++;
           

        return neighbors;
    }


}
