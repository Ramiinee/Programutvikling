package Game;



import java.net.URL;
import java.util.ResourceBundle;


import Game.StaticBoard;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

    
    ObservableList<String> ChangeRules = FXCollections.observableArrayList("game of life", "No deaths", "Cover");
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
    @FXML
    public ComboBox RuleDropDown;

    public ComboBox changerules;
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

        if(RuleDropDown.getValue() == "game of life"){
        nextGeneration();
        }
        else if(RuleDropDown.getValue() == "No deaths"){
            nodeadcellsrule();
        }
        else if(RuleDropDown.getValue() == "Cover"){
            slowlycover();
        }
        
        
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
    
    public void nodeadcellsrule(){
        nextGeneration = new byte[board.length][board[0].length];
        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length; row++) {
                int neighbors = countNeighbor(col,row);

                if (board[col][row] == 0 && (neighbors == 3)) {
                    nextGeneration[col][row] = 1;
                }
                else {
                    nextGeneration[col][row] = board[col][row];
                }

            }
        }
        board = nextGeneration;
    }

    public void nextGeneration(){
        nextGeneration = new byte[board.length][board[0].length];
        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length; row++) {
                int neighbors = countNeighbor(col,row);
                if((board[col][row] == 1) && (neighbors <  2)) {
                    nextGeneration[col][row] = 0;
                }
                else if ((board[col][row] == 1) && (neighbors >  3)) {
                    nextGeneration[col][row] = 0;
                }
                else if (board[col][row] == 0 && (neighbors == 3)) {
                    nextGeneration[col][row] = 1;
                }
                else {
                    nextGeneration[col][row] = board[col][row];
                }

            }
            
        }
        board = nextGeneration;
    }
     public void slowlycover(){
        nextGeneration = new byte[board.length][board[0].length];
        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length; row++) {
                int neighbors = countNeighbor(col,row);
                if((board[col][row] == 1)) {
                    nextGeneration[col][row] = 1;
                }
                else if ((neighbors >  3)) {
                    nextGeneration[col][row] = 1;
                }
                else if ((neighbors == 3)) {
                    nextGeneration[col][row] = 0;
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

        if (board[(col-1+board.length)%board.length][(row-1+board[row].length)%board[row].length] == 1){ neighbors++;}
        if (board[(col-1+board.length)%board.length][row] == 1){ neighbors++;}
        if (board[(col-1+board.length)%board.length][(row+1)%board[row].length] == 1){ neighbors++;}
        if (board[col][(row-1+board[row].length)%board[row].length] == 1){ neighbors++;}
        if (board[col][(row+1)%board[row].length] == 1){ neighbors++;}
        if (board[(col+1)%board.length][(row-1+board[row].length)%board[row].length] == 1){ neighbors++;}
        if (board[(col+1)%board.length][row] == 1){ neighbors++;}
        if (board[(col+1)%board.length][(row+1)%board[row].length] == 1){ neighbors++;}
        return neighbors;
    
    }

    @FXML private void remove_Array() {

        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length ; row++) {
                draw_ned(col , row, Color.WHITE);
            }
        }
    }



    private void draw_ned( int col, int row, Color c) {
        gc.setFill(Color.web("E0E0E0"));
        //gc.setFill(Color.WHITE);
        gc.fillRect(row* (size.getValue())-1 , col*(size.getValue())-1, ((size.getValue()))-1, (size.getValue())-1);
        gc.setFill(c);
        gc.fillRect((row * (size.getValue()))-1 , (col  * (size.getValue()))-1, ((size.getValue()))-1, (size.getValue())-1);


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
        size.setValue(400);
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
        timing =(timer.getValue());
        staticBoard = new StaticBoard();
        boardMaker = new BoardMaker(staticBoard);
        fileConverter = new FileConverter();
        gc = Canvas.getGraphicsContext2D();

        stage = Main.getPrimaryStage();
        colorPicker.setValue(Color.BLACK);

        //showClearBoard();

        Stop.setDisable(true);
        Start.setDisable(true);
        
        RuleDropDown.setValue("game of life");
        RuleDropDown.setItems(ChangeRules);
       
        


        /* scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollpane.setPannable(true);*/

        //SceneGestures sceneGestures = new SceneGestures();
        //scrollpane.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

      
        /*
        
        mouse = new Mouse(Canvas);
        mouse.scroll();
        */
        
        
        /***** Mouse onClick logic ******
		  Changes the location in the array on mouseclick and draws a new box
		 */
		Canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,(MouseEvent e) ->{
			int y = (int)(e.getX()/size.getValue());
			int x = (int)(e.getY()/size.getValue());
			if(board[x][y]==1){
				board[x][y] = 0;
				draw_ned(x,y,Color.WHITE);
			}
			else { 
				board[x][y] = 1;
				draw_ned(x,y,colorPicker.getValue());
			}
		});

                	Canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,(MouseEvent e) ->{
			int y = (int)(e.getX()/size.getValue());
			int x = (int)(e.getY()/size.getValue());
			
				board[x][y] = 1;

				draw_ned(x,y,colorPicker.getValue());
			
		});
        
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
