package Game.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import Game.Model.Boards.*;
import Game.Model.MetaData;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.concurrent.*;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;

/**
 *
 * @author Joachim-Privat
 */
public class Controller implements Initializable {

    // FXML
    public ColorPicker colorPicker;
    public BorderPane BoarderPane;
    public Slider timer;
    public Canvas Canvas;
    public ScrollPane scrollpane;
    public ComboBox<String> RuleDropDown;
    public Label BoardLabel;

    public Button StartStop;
    public Button SaveBoard;
    public Button Clear;
    public Button NewBoard;
    public Button LoadBoard;
    public Button Reset;
    public ToggleButton FitView;

    //Counters
    private int runCount = 1;
    private int aliveCount = 0;
    private double size = 5;

    //Checkers
    private boolean loaded = false;
    private boolean running = false;

    //Objects
    private BoardMaker boardMaker;
    private FileLoader fileLoader;
    private Board board;
    private NextGenThreads nextGenThreads;
    private MetaData metaData;
    private Mouse mouse;

    private Stage stage;
    private GraphicsContext gc;
    private ObservableList<String> ChangeRules = FXCollections.observableArrayList("Game of Life", "No deaths", "Cover");
    private ObservableList<String> ChangeBoard = FXCollections.observableArrayList("Static", "Dynamic");
    private ScheduledService<Void> scheduledService;


    /**
     * Her er det satt opp listeners som reagerer hver gang en slider eller colorpicker endrer verdi.
     */
    private void listeners(){
        timer.valueProperty().addListener((ObservableValue<? extends Number> timerListener, Number oldtime, Number newtime) -> {
            Duration duration = new Duration(1000/newtime.intValue());
            scheduledService.setPeriod(duration);
        });

        colorPicker.valueProperty().addListener((ObservableValue<? extends Color> timerListener, Color oldColor, Color newColor) -> {
            try {
                gc.clearRect(0,0,3000,2000);
                draw_Array();
            }
            catch (Exception e){
                e.getStackTrace();
            }
        });
        Canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,(MouseEvent e) ->{
            if (e.isControlDown()){
                scrollpane.setPannable(true);
            }else {
                scrollpane.setPannable(false);
                int y = (int)(e.getX()/size);
                int x = (int)(e.getY()/size);
                if(board.getCellAliveState(x,y)==1){
                    board.setCellAliveState(x,y,(byte)0);
                    draw_ned(x,y,Color.WHITE);
                }
                else {
                    board.setCellAliveState(x,y,(byte)1);
                    draw_ned(x,y,colorPicker.getValue());
                }
                StartStop.setDisable(false);
                loaded = true;
            }

        });
        Canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,(MouseEvent e) ->{
            if (e.isControlDown()){
                scrollpane.setPannable(true);
            }
            else {

                scrollpane.setPannable(false);
                int y = (int)(e.getX()/size);
                int x = (int)(e.getY()/size);
                try {
                    board.setCellAliveState(x,y,(byte)1);
                    draw_ned(x,y,colorPicker.getValue());
                } catch (Exception el) {
                    System.err.println("Why you out of canvas? " + y + " | "+x );
                }
            }
        });
        stage.addEventHandler(KeyEvent.KEY_PRESSED,(KeyEvent e)->{
            if (e.getCode().equals(KeyCode.ENTER)){
                startStop();
            }else if (e.getCode().equals(KeyCode.DELETE)){
                Clear();
            }else if (e.isControlDown() && e.getCode().equals(KeyCode.N)){
                if (!NewBoard.isDisable()){
                    newBoard();
                }
            }else if(e.isControlDown() && e.getCode().equals(KeyCode.L)){
                if (!LoadBoard.isDisable()){
                    loadBoard();
                }
            }
        });

    }

    public void startStop() {
        if (loaded){
            if (!running){
                running = true;

                    scheduledService.restart();
                LoadBoard.setDisable(true);
                NewBoard.setDisable(true);
                StartStop.setText("Stop");
                StartStop.setTooltip(new Tooltip("Stop"));

            }
            else {
                running=false;

                    scheduledService.cancel();
                LoadBoard.setDisable(false);
                NewBoard.setDisable(false);

                Clear.setDisable(false);
                StartStop.setText("Start");
                StartStop.setTooltip(new Tooltip("Start"));
                stage.setTitle("Game Of Life");
            }
        }

    }
    public void Clear() {
        if (loaded){
            running=false;

            scheduledService.cancel();
            LoadBoard.setDisable(false);
            NewBoard.setDisable(false);

            Clear.setDisable(false);
            StartStop.setText("Start");
            StartStop.setTooltip(new Tooltip("Start"));
            stage.setTitle("Game Of Life");
            stage.setTitle("Game Of Life ");
            showClearBoard();
        }


    }
    public void newBoard() {
        Stage newBoard =new Stage();
        newBoard.initModality(Modality.APPLICATION_MODAL);
        newBoard.setTitle("Load");
            Label label1= new Label("How do you want to load?");
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.setValue("Static");
            comboBox.setItems(ChangeBoard);

            TextField sizeField = new TextField();
            sizeField.setPromptText("Enter Size");
            sizeField.setText("50");

            Button clearBoard = new Button("Clear Board");
            Button randomeBoard = new Button("Randome Board");
            Button cancle = new Button("Cancle");

            clearBoard.setOnAction(event -> {
                setBoardMakerBoard(comboBox);
                int value = Integer.parseInt(sizeField.getText());
                boardMaker.makeClearBoard(value,value);
                loaded(loaded = true);
                newBoard.close();
            });

            randomeBoard.setOnAction(( event) -> {
                setBoardMakerBoard(comboBox);
                int value = Integer.parseInt(sizeField.getText());//sjekke om det er tall eller ikke
                boardMaker.randomPattern(value,value);
                loaded(loaded = true);
                newBoard.close();


            });


            //cancle.setOnAction(event -> newBoard.close());
            cancle.setCancelButton(true);
            randomeBoard.setDefaultButton(true);


            VBox layout= new VBox(20);
            HBox Size = new HBox(10);
            HBox okCancle = new HBox(10);

            Size.getChildren().addAll(sizeField, comboBox);

            okCancle.getChildren().addAll(clearBoard,randomeBoard,cancle);
            okCancle.setAlignment(Pos.BASELINE_RIGHT);

            layout.getChildren().addAll(label1, Size,okCancle);

            layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 150);
        newBoard.setScene(scene1);
        newBoard.showAndWait();

    }

    public void loadBoard() {
        Stage loadBoard =new Stage();
        loadBoard.initModality(Modality.APPLICATION_MODAL);
        loadBoard.setTitle("Load");

            Label label1= new Label("How do you want to load?");

            Button browse = new Button("Browse from Disk");


            TextField urlField = new TextField();
            urlField.setPromptText("Enter URL");


            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.setValue("Dynamic");
            comboBox.setItems(ChangeBoard);

            Button ok = new Button("Load URL");
            Button cancle = new Button("Cancle");

            //ToggleButton toggleButton = new ToggleButton("Place free");
            cancle.setCancelButton(true);
            ok.setDefaultButton(true);



            browse.setOnAction(event -> {

                setBoardMakerBoard(comboBox);

                loaded = fileLoader.ReadFromFile();
                loaded(loaded);
                loadBoard.close();
                if (loaded){
                    BoardLabel.setText(metaData.getAuthor() + ", " + metaData.getName());
                    System.out.println(metaData.getComment());
                }

            });
            ok.setOnAction(event -> {

                setBoardMakerBoard(comboBox);

                if (!urlField.getText().isEmpty()){
                    loaded = fileLoader.ReadFromUrl(urlField.getText());
                    if (loaded){
                        loaded(loaded);
                        loadBoard.close();
                    }else {
                        label1.setText("Not an valid Url.");
                    }



                }
                if (loaded){
                    BoardLabel.setText(metaData.getAuthor() + ", " + metaData.getName());
                    System.out.println(metaData.getComment());
                }
            });
            cancle.setOnAction(event -> {
                loaded=false;
                board= null;
                loadBoard.close();
            });



        VBox layout= new VBox(20);

        HBox url = new HBox(10);
        HBox okCancle = new HBox(10);
        HBox choosers = new HBox(10);
            okCancle.setAlignment(Pos.BASELINE_RIGHT);
            choosers.setAlignment(Pos.CENTER);



            url.getChildren().addAll(urlField,ok);
            url.setAlignment(Pos.CENTER);
            okCancle.getChildren().addAll(comboBox,browse,cancle);
            choosers.getChildren().addAll();

            layout.getChildren().addAll(label1, url, choosers,okCancle);

            layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 200);
        loadBoard.setScene(scene1);
        loadBoard.showAndWait();

    }

    private void setBoardMakerBoard(ComboBox<? extends String> comboBox){
        if (!(comboBox.getValue() == "Static")){
            board = new DynamicBoard();
            //BoardLabel.setText("Dynamic");
        }else {
            board = new StaticBoard();
            //BoardLabel.setText("Static");
        }
        boardMaker.setBoardType(board);

    }


    private void loaded(boolean loaded){
        nextGenThreads.setBoard(board);
        if (loaded){
            gc.clearRect(0,0,Canvas.getWidth(),Canvas.getHeight());
            size = 5;
            updateCanvas();
            draw_Array();
            runCount = 0;
            StartStop.setDisable(false);
        }
        else {
            showClearBoard();
            StartStop.setDisable(true);
        }
    }



    /**
     * Draw array kjører gjennom brettet og bestemmer om det skal tegnes den valgte fargen eller om det skal tegnes hvit.
     * den kaller på draw_ned eller draw, for visning på skjerm.
     * Samtidig så setter den antall celler som er i livet i hver generation.
     */
    private void draw_Array(){
        for (int row = 0; row < board.getRow(); row++) {
            for (int col = 0; col <  board.getColumn() ; col++) {
                if (board.getCellAliveState(row,col)==1){
                    draw_ned(row  , col , colorPicker.getValue());
                    aliveCount ++;
                }
                else {
                    draw_ned(row , col , Color.WHITE);
                }

            }

        }
        


    }

    @Deprecated
    private void draw( int col, int row, Color c) {
        gc = Canvas.getGraphicsContext2D();
        gc.setFill(Color.web("E0E0E0"));
        //gc.setFill(Color.WHITE);
        gc.fillRect(col* (size/10) , row*(size/10), ((size/10)), (size/10));
        gc.setFill(c);
        gc.fillRect((col * (size/10))+1 , (row  * (size/10))+1, ((size/10) -2), (size/10)-2);
    }


    private void draw_ned( int col, int row, Color c) {
        gc.setFill(Color.web("E0E0E0"));
        //gc.setFill(Color.WHITE);
        gc.fillRect(row* (size) , col*(size), ((size)), (size));
        gc.setFill(c);
        gc.fillRect((row * (size))+1 , (col  * (size))+1, ((size))-2, (size)-2);


    }

    private void updateCanvas(){
        if (!FitView.isSelected()){
            if (!(Canvas.getWidth() == board.getRow()*size && Canvas.getHeight() == board.getColumn()*size)){
                Canvas.setWidth(board.getColumn()*size);
                Canvas.setHeight(board.getRow()*size);
            }
        }else{
            gc.clearRect(0,0,600,600);
            Canvas.setWidth(stage.getWidth()-200);
            Canvas.setHeight(stage.getHeight());
            size = (stage.getWidth()/board.getRow())/2;

        }

    }

    /**
     * Generer ett blankt brett for så å tegne det.
     */
    private void showClearBoard(){
        boardMaker.makeClearBoard(200,200);
        updateCanvas();
        draw_Array();
    }

    /**
     * Reset går over verdier som brukeren har endret på og setter de tilbake til standar verdier.
     */
    public void reset(){
        size = 5;
        timer.setValue(60);
        colorPicker.setValue(Color.BLACK);
        try {
            mouse.setZoomValue(0);
            scrollpane.setHvalue(0);
            scrollpane.setVvalue(0);
            Canvas.getTransforms().retainAll();

        }catch (Exception e){
            e.getStackTrace();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = Canvas.getGraphicsContext2D();
        stage = Main.getPrimaryStage();
        colorPicker.setValue(Color.BLACK);

        metaData = new MetaData();
        boardMaker = new BoardMaker(metaData);
        fileLoader = new FileLoader(boardMaker);
        nextGenThreads = new NextGenThreads();

        mouse = new Mouse(Canvas);

        //Clear.setDisable(true);
        StartStop.setDisable(true);

        //scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollpane.setPannable(false);

        RuleDropDown.setValue("Game of Life");
        RuleDropDown.setItems(ChangeRules);
        listeners();
        initializeService();

        board = new StaticBoard();
        board.makeBoard(200,200);
        nextGenThreads.setBoard(board);
        boardMaker.setBoardType(board);
        updateCanvas();
        draw_Array();
    }

    private void initializeService(){
        scheduledService = new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        nextGenThreads.split();
                        nextGenThreads.nextGen(RuleDropDown);
                        Platform.runLater(() -> {
                            updateCanvas();
                            draw_Array();
                            stage.setTitle("Game Of Life | Gen : " + runCount++ + " | Fps : " + (int)(timer.getValue())+ " | Size : " + Math.round(size) + " | Alive : " + aliveCount + " | " + board.getRow() + "x" + board.getColumn());
                            aliveCount = 0;
                        });
                        //board.test();
                        return null;
                    }
                };
            }
        };
        scheduledService.setPeriod(Duration.millis(1000/60));
    }

    public void saveBoard() {
        board.test();
    }

    public void FitToView() {

    }
}
