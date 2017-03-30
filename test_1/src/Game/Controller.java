package Game;



import java.net.URL;
import java.util.ResourceBundle;



import javafx.animation.*;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Joachim-Privat
 */
public class Controller implements Initializable {

    // FXML

    /**
     *
     */
    public Button Stop;

    /**
     *
     */
    public Button Start;

    /**
     *
     */
    public Button reset;

    /**
     *
     */
    public Button Load;

    /**
     *
     */
    public Button Random;

    /**
     *
     */
    public ColorPicker colorPicker;

    /**
     *
     */
    public BorderPane BoarderPane;

    /**
     *
     */
    public Slider size;

    /**
     *
     */
    public Slider timer;

    /**
     *
     */
    public Canvas Canvas;

    /**
     *
     */
    public Stage stage;

    /**
     *
     */
    public ScrollPane scrollpane;


    private GraphicsContext gc;


    //Counters
    private int playCount = 0;
    private int stopCount = 0;
    private int runCount = 1;
    private int aliveCount = 0;

    /**
     *
     */
    public boolean loaded = false;

    /**
     *
     */
    public StaticBoard staticBoard;

    /**
     *
     */
    public BoardMaker boardMaker;

    /**
     *
     */
    public FileConverter fileConverter;

    /**
     *
     */
    public Mouse mouse;

    //Board

    /**
     *
     */

    public byte[][] board;

    /**
     *
     */
    public byte[][] nextGeneration;


    private double timing = 120;
    private double StartTimer = timing;
    /**
     * Her styres hvert frame, og hva som skal skje i det framet.
     */
    private Timeline timeline = new Timeline( new KeyFrame(Duration.millis(timing), e -> {
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,2000,2000);

        nextGeneration();
        draw_Array();



        stage.setTitle("Game Of Life | Gen : " + runCount++ + " | Fps : " + Math.round((1000/(StartTimer/timing) )) + " | Size : " + Math.round(size.getValue()) + " | Alive : " + aliveCount );
        aliveCount = 0;
    }
    ));


    /**
     * Her er det satt opp listeners som reagerer hver gang en slider eller colorpicker endrer verdi.
     */
    public void listeners(){
        timer.valueProperty().addListener((ObservableValue<? extends Number> timerlistener, Number oldtime, Number newtime) -> {
            timing = newtime.intValue();
            timeline.setRate(timing);
        });
        size.valueProperty().addListener((ObservableValue<? extends Number> timerlistener, Number oldtime, Number newtime) -> {
            try {
                draw_Array();
            }
            catch (Exception e){

            }
        });
        colorPicker.valueProperty().addListener((ObservableValue<? extends Color> timerlistener, Color oldColor, Color newColor) -> {
            try {
                draw_Array();
            }
            catch (Exception e){

            }
        });

    }


    /**
     * Når start knappen i guiet blir trykket på kaller den på denne funksjonen.
     * Den setter i gang videre prosesser som henter board og setter i gang animasjonen.
     * Start kan ikke kjørers med mindre man har vært innom load eller randomboard.
     */
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

    /**
     * Når stopp knappen i guiet blir trykket på kaller den på denne funksjonen.
     * Den stopper animasjonen og setter knappen klar for å cleare canvaset. Ved trykk 2 så cleres brettet.
     */
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


    /**
     * Draw array kjører gjennom brettet og bestemmer om det skal tegnes den valgte fargen eller om det skal tegnes hvit.
     * den kaller på draw_ned eller draw, for visning på skjerm.
     * Samtidig så setter den antall celler som er i livet i hver generation.
     */
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

    /**
     * NextGeneration styrer hvordan neste genereasjon skal se ut. Med hjelp av countNeighbor så settes neste generasjons
     * brett til rett verdi.
     */
    private void nextGeneration(){
        nextGeneration = new byte[board.length][board[0].length];
        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length ; row++) {
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

    /**
     * countNeighbor avgjør for hver enkelt celle om den skal få leve eller ikke. Reglene styres her inne.
     * @param col
     * @param row
     * row and col controll the position of the cell.
     * @return amount of neighbors per cell
     */
    private int countNeighbor(int col, int row){
        int neighbors = 0;
        if (col != 0 && row !=0){
            if (board[col-1][row-1] == 1)
                neighbors++;
        }
        if (row != 0){
            if (board[col][row-1] == 1)
                neighbors++;
        }
        try {
            if (col != board[col].length -1 && row != 0 ){
                if (board[col+1][row-1] == 1)
                    neighbors++;
            }
        }catch (IndexOutOfBoundsException  e){


        }
        if (col != 0){
            if (board[col-1][row]   == 1)
                neighbors++;
        }
        try {
            if (col != board[col].length -1){
                if (board[col+1][row]   == 1)
                    neighbors++;
            }
        }catch (Exception e) {

        }
        if(col != 0 && row != board[row].length -1){
            if (board[col-1][row+1] == 1)
                neighbors++;
        }
        if(row != board[row].length -1){
            if (board[col][row+1] == 1)
                neighbors++;
        }
        try {
            if(col != board[col].length - 1 && row != board[row].length -1){
                if (board[col+1][row+1] == 1)
                    neighbors++;
            }
        } catch (IndexOutOfBoundsException  e) {
        }
        return neighbors;
    }

    /**
     * Blir kalt på når boardet skal bli klart. Den kjører over alt og setter alle verdier til å være hvite.
     */
    @FXML private void remove_Array() {

        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length ; row++) {
                draw_ned(col , row, Color.WHITE);
            }
        }
    }

    /**
     * Draw får posisjonen til celle opp i mot resten og tegner den så proposjonalt ut på skjermen.
     * @param col
     * @param row
     * @param c
     */
    private void draw( int col, int row, Color c) {
        gc = Canvas.getGraphicsContext2D();
        gc.setFill(Color.web("E0E0E0"));
        //gc.setFill(Color.WHITE);
        gc.fillRect(col* (size.getValue()/10) , row*(size.getValue()/10), ((size.getValue()/10)), (size.getValue()/10));
        gc.setFill(c);
        gc.fillRect((col * (size.getValue()/10))+1 , (row  * (size.getValue()/10))+1, ((size.getValue()/10) -2), (size.getValue()/10)-2);


    }

    /**
     * Draw får posisjonen til celle opp i mot resten og tegner den så proposjonalt ut på skjermen.
     * @param col
     * @param row
     * @param c
     */
    private void draw_ned( int col, int row, Color c) {
        gc.setFill(Color.web("E0E0E0"));
        //gc.setFill(Color.WHITE);
        gc.fillRect(row* (size.getValue()/20) , col*(size.getValue()/20), ((size.getValue()/20)), (size.getValue()/20));
        gc.setFill(c);
        gc.fillRect((row * (size.getValue()/20))+1 , (col  * (size.getValue()/20))+1, ((size.getValue()/20) -2), (size.getValue()/20)-2);


    }


    /**
     * Load setter i gang en rekke andre funksjoner nedover linja, samt fler sjekker for å se at brettet blir lastet inn riktig.
     * Her må alt før ha gått greit for at vi skal få klar beskjed til å kunne kjøre.
     */
    public void load() {
        boardMaker.makeClearBoard();
        board = staticBoard.getBoard();
        draw_Array();
        gc.clearRect(0,0,600,600);
        stopCount = 0;
        Stop.setDisable(true);
        loaded = fileConverter.FromFileToBoard();
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

    /**
     * RandomBoard setter i gang flere funksjoner som generer ett random brett som så blir vist.
     */
    public void RandomBoard() {

        boardMaker.makeClearBoard();
        board = staticBoard.getBoard();
        draw_Array();
        stopCount = 0;
        Stop.setDisable(true);
        boardMaker.randomPattern();
        board = staticBoard.getBoard();
        draw_Array();
        loaded = true;
        Start.setDisable(false);
        System.out.println(board.length);

    }

    /**
     * Generer ett blankt brett for så å tegne det.
     */
    public void showClearBoard(){

        boardMaker.makeClearBoard(); // vurderer å endre alle over til å bare hente info fra Board classen. ER redundent å lagre alt i Controller når vi har tilgang til det i Board.
        board = staticBoard.getBoard();
        draw_Array();
    }

    /**
     *
     * @return
     */
    public Canvas getCanvas() {
        return Canvas;
    }

    /**
     * Reset går over verdier som brukeren har endret på og setter de tilbake til standar verdier.
     */
    public void reset(){
        size.setValue(100);
        timer.setValue(1);
        colorPicker.setValue(Color.BLACK);
        try {
            mouse.setZoomValue(0);
            scrollpane.setHvalue(0);
            scrollpane.setVvalue(0);
            Canvas.getTransforms().retainAll();

        }catch (Exception e){
            System.out.println(e);
        }

    }

    /**
     * Her blir objekter laget og regler for hvordan guiet skal oppføre seg strammet inn.
     * Det setter også i gang listners og mouse input.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        staticBoard = new StaticBoard();
        boardMaker = new BoardMaker(staticBoard);
        fileConverter = new FileConverter(staticBoard,boardMaker);
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


        mouse = new Mouse(Canvas, staticBoard);
        mouse.scroll();
        listeners();

    }

    /**
     *
     * @return
     */
    public Slider getSize() {
        return size;
    }

    /**
     *
     * @param size
     */
    public void setSize(Slider size) {
        this.size = size;
    }

    /**
     *
     * @param scrollEvent
     */
    public void onScrollEventHandler(ScrollEvent scrollEvent) {
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
