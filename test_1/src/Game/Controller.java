package Game;



import java.net.URL;
import java.util.ResourceBundle;

import Game.DynamicBoard;
import Game.StaticBoard;
import javafx.animation.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;

/**
 *
 * @author Joachim-Privat
 */
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
    public ScrollPane scrollpane;
    public TextField Url_Text;
    public Button Url_button;
    public ComboBox RuleDropDown;
    public CheckBox dynamic;


    private Stage stage;
    private GraphicsContext gc;
    private ObservableList<String> ChangeRules = FXCollections.observableArrayList("Game of Life", "No deaths", "Cover");

    //Counters
    private int playCount = 0;
    private int stopCount = 0;
    private int runCount = 1;
    private int aliveCount = 0;

    private boolean loaded = false;
    private StaticBoard staticBoard;
    private BoardMaker boardMaker;
    private FileConverter fileConverter;
    private DynamicBoard dynamicBoard;


    public Mouse mouse;

    private double timing = 120;
    private double StartTimer = timing;
    /**
     * Her styres hvert frame, og hva som skal skje i det framet.
     */
    private Timeline timeline = new Timeline( new KeyFrame(Duration.millis(timing), e -> {
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,2000,2000);


        if (dynamic.isSelected()){
            if(RuleDropDown.getValue() == "Game of Life"){
                dynamicBoard.nextGeneration();
            }
            else if(RuleDropDown.getValue() == "No deaths"){

            }
            else if(RuleDropDown.getValue() == "Cover"){

            }
        }else {
            if(RuleDropDown.getValue() == "Game of Life"){
                staticBoard.nextGeneration();
            }
            else if(RuleDropDown.getValue() == "No deaths"){
                staticBoard.noDeadCellsRule();
            }
            else if(RuleDropDown.getValue() == "Cover"){
                staticBoard.slowlyCover();
            }
        }


        draw_Array();

        stage.setTitle("Game Of Life | Gen : " + runCount++ + " | Fps : " + Math.round((1000/(StartTimer/timing) )) + " | Size : " + Math.round(size.getValue()) + " | Alive : " + aliveCount );
        aliveCount = 0;
    }
    ));

    /**
     * Her er det satt opp listeners som reagerer hver gang en slider eller colorpicker endrer verdi.
     */
    private void listeners(){
        timer.valueProperty().addListener((ObservableValue<? extends Number> timerListener, Number oldtime, Number newtime) -> {
            timing = newtime.intValue();
            timeline.setRate(timing);
        });
        size.valueProperty().addListener((ObservableValue<? extends Number> timerListener, Number oldtime, Number newtime) -> {
            try {
                gc.clearRect(0,0,2000,2000);
                draw_Array();
            }
            catch (Exception e){
                System.out.println(e);
            }
        });
        colorPicker.valueProperty().addListener((ObservableValue<? extends Color> timerListener, Color oldColor, Color newColor) -> {
            try {
                gc.clearRect(0,0,3000,2000);
                draw_Array();
            }
            catch (Exception e){
                System.out.println(e);
            }
        });
        Canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,(MouseEvent e) ->{
            if (e.isControlDown()){
                scrollpane.setPannable(true);
            }else {
                scrollpane.setPannable(false);
                int y = (int)(e.getX()/size.getValue());
                int x = (int)(e.getY()/size.getValue());
                if(staticBoard.getBoard()[x][y]==1){
                    staticBoard.getBoard()[x][y] = 0;
                    draw_ned(x,y,Color.WHITE);
                }
                else {
                    staticBoard.getBoard()[x][y] = 1;
                    draw_ned(x,y,colorPicker.getValue());
                }
                Start.setDisable(false);
                stopCount = 0;
                loaded = true;
            }

        });
        Canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,(MouseEvent e) ->{
            if (e.isControlDown()){
                scrollpane.setPannable(true);
            }
            else {
                scrollpane.setPannable(false);
                int y = (int)(e.getX()/size.getValue());
                int x = (int)(e.getY()/size.getValue());
                try {
                    staticBoard.getBoard()[x][y] = 1;

                    draw_ned(x,y,colorPicker.getValue());
                } catch (Exception el) {
                    System.err.println("Why you out of canvas?");
                }
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

                Load.setDisable(true);
                Random.setDisable(true);
                //make_board.randomPattern();
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
            dynamic.setSelected(false);
            remove_Array();
            timeline.stop();
            playCount = 0;
            runCount = 0;
            Stop.setText("Stop");
            Load.setDisable(false);
            Random.setDisable(false);
            stage.setTitle("Game Of Life ");
            Start.setDisable(true);
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


    /**
     * Draw array kjører gjennom brettet og bestemmer om det skal tegnes den valgte fargen eller om det skal tegnes hvit.
     * den kaller på draw_ned eller draw, for visning på skjerm.
     * Samtidig så setter den antall celler som er i livet i hver generation.
     */
    private void draw_Array(){
        if (dynamic.isSelected()){
            for (int i = 0; i < dynamicBoard.getCurrentGeneration().size(); i++) {
                for (int j = 0; j <  dynamicBoard.getCurrentGeneration().get(i).size() ; j++) {
                    if (dynamicBoard.getCurrentGeneration().get(i).get(j)==1){
                        draw_ned( i  , j , colorPicker.getValue());
                    }
                    else {
                        draw_ned(i , j , Color.WHITE);
                    }
                }

            }
        }
        else {
            for (int i = 0; i < staticBoard.getBoard().length; i++) {
                for (int j = 0; j < staticBoard.getBoard()[i].length ; j++) {
                    if (staticBoard.getBoard()[i][j] == 1){
                        draw_ned( i  , j , colorPicker.getValue());
                        aliveCount ++;
                    }
                    else {
                        draw_ned(i , j , Color.WHITE);
                    }
                }
            }
        }

    }



    /**
     * Blir kalt på når boardet skal bli klart. Den kjører over alt og setter alle verdier til å være hvite.
     */
    @FXML private void remove_Array() {

        for (int col = 0; col < staticBoard.getBoard().length; col++) {
            for (int row = 0; row < staticBoard.getBoard()[col].length ; row++) {
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
        //gc.setFill(Color.web("E0E0E0"));
        //gc.setFill(Color.WHITE);
        //gc.fillRect(row* (size.getValue()) , col*(size.getValue()), ((size.getValue())), (size.getValue()));
        gc.setFill(c);
        gc.fillRect((row * (size.getValue()))+1 , (col  * (size.getValue()))+1, ((size.getValue()))-2, (size.getValue())-2);


    }


    /**
     * Load setter i gang en rekke andre funksjoner nedover linja, samt fler sjekker for å se at brettet blir lastet inn riktig.
     * Her må alt før ha gått greit for at vi skal få klar beskjed til å kunne kjøre.
     */
    public void load() {
        Stage popupwindow=new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Load");
        Label label1= new Label("How do you want to load?");
        TextField textField = new TextField();
        Button button1= new Button("Load from selected Url");
        Button button2= new Button("Load from file");
        button1.setOnAction(e -> {
            if (!textField.getText().isEmpty()){
                loaded = fileConverter.ReadFromUrl(textField.getText());
                loaded(loaded);
                popupwindow.close();
            }else {
                label1.setText("There is a error in your Url");
            }
        });
        button2.setOnAction(e -> {
            loadAction();
            popupwindow.close();
        });

        VBox layout= new VBox(15);
        layout.getChildren().addAll(label1,textField, button1, button2);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 200, 200);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();

    }
    public void loadAction(){
        draw_Array();
        gc.clearRect(0,0,600,600);
        stopCount = 0;
        Stop.setDisable(true);
        loaded = fileConverter.FromFileToBoard();
        loaded(loaded);
    }
    public void loaded(boolean loaded){
        if (loaded){
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



        stopCount = 0;
        Stop.setDisable(true);
        //boardMaker.randomPattern();
        draw_Array();
        loaded = true;
        Start.setDisable(false);


    }

    /**
     * Generer ett blankt brett for så å tegne det.
     */
    public void showClearBoard(){
        boardMaker.makeClearBoard(); // vurderer å endre alle over til å bare hente info fra Board classen. ER redundent å lagre alt i Controller når vi har tilgang til det i Board.
        draw_Array();
    }


    /**
     * Reset går over verdier som brukeren har endret på og setter de tilbake til standar verdier.
     */
    public void reset(){

        size.setValue(5);
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
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        timing =timer.getValue();
        staticBoard = new StaticBoard();
        boardMaker = new BoardMaker(staticBoard);
        fileConverter = new FileConverter(staticBoard,boardMaker);
        gc = Canvas.getGraphicsContext2D();

        stage = Main.getPrimaryStage();
        colorPicker.setValue(Color.BLACK);


        boardMaker.makeClearBoard();

        Stop.setDisable(true);
        Start.setDisable(true);
        //scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollpane.setPannable(false);

        RuleDropDown.setValue("Game of Life");
        RuleDropDown.setItems(ChangeRules);

        mouse = new Mouse(Canvas, staticBoard,scrollpane);
        mouse.scroll();
        listeners();
        showClearBoard();
        dynamicBoard = new DynamicBoard(200,200);
        System.out.println(dynamicBoard.getColumn()+ " | "+dynamicBoard.getRow());



    }


}
