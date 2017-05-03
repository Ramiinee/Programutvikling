package Game.Controller;



import java.net.URL;
import java.util.ResourceBundle;
import Game.Model.Boards.Board;
import Game.Model.Boards.DynamicBoard;
import Game.Model.Boards.StaticBoard;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

   
    public ColorPicker colorPicker;
    public BorderPane BoarderPane;
    public Slider size;
    public Slider timer;
    public Canvas Canvas;
    public ScrollPane scrollpane;
    public ComboBox RuleDropDown;
    public Label TestLabel;
    

    public Button StartStop;
    public Button SaveBoard;
    public Button Clear;
    public Button NewBoard;
    public Button LoadBoard;
    public Button Reset;


    private Stage stage;
    private GraphicsContext gc;
    private ObservableList<String> ChangeRules = FXCollections.observableArrayList("Game of Life", "No deaths", "Cover");
    private ObservableList<String> ChangeBoard = FXCollections.observableArrayList("Static", "Dynamic");
    private ScheduledService<Void> scheduledService;

    //Counters
    private int runCount = 1;
    private int aliveCount = 0;

    private boolean loaded = false;
    private boolean running = false;


    private BoardMaker boardMaker;
    private FileLoader fileLoader;
    private Board board = null;
    private NextGenThreads nextGenThreads;
    public Mouse mouse;



    /**
     * Her er det satt opp listeners som reagerer hver gang en slider eller colorpicker endrer verdi.
     */
    private void listeners(){
        timer.valueProperty().addListener((ObservableValue<? extends Number> timerListener, Number oldtime, Number newtime) -> {
            Duration duration = new Duration(1000/newtime.intValue());
            scheduledService.setPeriod(duration);
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
                int y = (int)(e.getX()/size.getValue());
                int x = (int)(e.getY()/size.getValue());
                try {
                    board.setCellAliveState(x,y,(byte)1);
                    draw_ned(x,y,colorPicker.getValue());
                } catch (Exception el) {
                    System.err.println("Why you out of canvas? " + y + " | "+x );
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
                size.setDisable(false);
            }
            else {
                running=false;

                    scheduledService.cancel();
                LoadBoard.setDisable(false);
                NewBoard.setDisable(false);
                size.setDisable(true);
                Clear.setDisable(false);
                StartStop.setText("Start");
                StartStop.setTooltip(new Tooltip("Start"));
            }
        }

    }
    public void Clear() {
        if (loaded){
            stage.setTitle("Game Of Life ");
            showClearBoard();
        }

    }
    public void newBoard() {
        Stage popupwindow=new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Load");


        Label label1= new Label("How do you want to load?");

        ComboBox comboBox = new ComboBox();
        comboBox.setValue("Static");
        comboBox.setItems(ChangeBoard);


        TextField sizeField = new TextField();
        sizeField.setPromptText("Enter Size");
        sizeField.setText("200");



        Button clearBoard = new Button("Clear Board");
        Button randomeBoard = new Button("Randome Board");
        Button cancle = new Button("Cancel");

        clearBoard.setOnAction(event -> {
            setBoardMakerBoard(comboBox);
            int value = Integer.parseInt(sizeField.getText());
            boardMaker.makeClearBoard(value,value);
            loaded(loaded = true);
            popupwindow.close();
        });

        randomeBoard.setOnAction(( event) -> {
            setBoardMakerBoard(comboBox);
            int value = Integer.parseInt(sizeField.getText());//sjekke om det er tall eller ikke
            boardMaker.randomPattern(value,value);
            loaded(loaded = true);
            popupwindow.close();


        });


        cancle.setOnAction(event -> {
            popupwindow.close();
        });


        VBox layout= new VBox(20);
        HBox Size = new HBox(10);

        HBox okCancle = new HBox(10);

        Size.getChildren().addAll(sizeField, comboBox);

        okCancle.getChildren().addAll(clearBoard,randomeBoard,cancle);
        okCancle.setAlignment(Pos.BASELINE_RIGHT);

        layout.getChildren().addAll(label1, Size,okCancle);

        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 150);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();

    }

    public void loadBoard(ActionEvent actionEvent) {

        Stage popupwindow=new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Load");
        final ToggleGroup group = new ToggleGroup();

        Label label1= new Label("How do you want to load?");

        RadioButton radioDisk = new RadioButton("From disk");
        radioDisk.setSelected(true);
        radioDisk.setToggleGroup(group);
        TextField fileField = new TextField();
        fileField.setPromptText("Browse from file");
        Button browse = new Button("Browse");

        RadioButton radioUrl = new RadioButton("From disk");
        radioUrl.setToggleGroup(group);
        TextField urlField = new TextField();
        urlField.setPromptText("Enter URL");
        urlField.setDisable(true);

        ComboBox comboBox = new ComboBox();
        comboBox.setValue("Static");
        comboBox.setItems(ChangeBoard);

        Button ok = new Button("Load");
        Button cancle = new Button("Cancle");

        radioDisk.setOnAction(event -> {
            fileField.setDisable(false);
            browse.setDisable(false);
            urlField.setDisable(true);
        });
        radioUrl.setOnAction(event -> {
            fileField.setDisable(true);
            browse.setDisable(true);
            urlField.setDisable(false);
        });


        browse.setOnAction(event -> {
            setBoardMakerBoard(comboBox);
            loaded = fileLoader.ReadFromFile();
            loaded(loaded);
            popupwindow.close();
        });
        ok.setOnAction(event -> {
            setBoardMakerBoard(comboBox);
            if (!urlField.getText().isEmpty()){ //Sjekke om det er en url eller ikke.... mangler.
                loaded = fileLoader.ReadFromUrl(urlField.getText());
                loaded(loaded);
                popupwindow.close();
            }
        });
        cancle.setOnAction(event -> {
            loaded=false;
            board= null;
            popupwindow.close();
        });



        VBox layout= new VBox(20);
        HBox disk = new HBox(10);
        HBox url = new HBox(10);
        HBox okCancle = new HBox(10);
        okCancle.setAlignment(Pos.BASELINE_RIGHT);

        disk.getChildren().addAll(radioDisk,fileField, browse);
        url.getChildren().addAll(radioUrl,urlField);
        okCancle.getChildren().addAll(ok,cancle);

        layout.getChildren().addAll(label1, disk, url, comboBox,okCancle);

        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 200);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();

    }

    private void setBoardMakerBoard(ComboBox comboBox){
        if (!(comboBox.getValue() == "Static")){
            board = new DynamicBoard();
        }else {
            board = new StaticBoard();
        }
        boardMaker.setBoardType(board);

    }


    public void loaded(boolean loaded){
        nextGenThreads.setBoard(board);
        nextGenThreads.split();
        if (loaded){
            gc.clearRect(0,0,Canvas.getWidth(),Canvas.getHeight());
            updateCanvas();
            draw_Array();

            StartStop.setDisable(false);
        }
        else {
            showClearBoard();
            System.out.println(loaded);
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
        gc.fillRect(row* (size.getValue()) , col*(size.getValue()), ((size.getValue())), (size.getValue()));
        gc.setFill(c);
        gc.fillRect((row * (size.getValue()))+1 , (col  * (size.getValue()))+1, ((size.getValue()))-2, (size.getValue())-2);


    }


    private void updateCanvas(){
        if (!(Canvas.getWidth() == board.getRow()*size.getValue() && Canvas.getHeight() == board.getColumn()*size.getValue())){
            Canvas.setWidth(board.getColumn()*size.getValue());
            Canvas.setHeight(board.getRow()*size.getValue());
        }
    }









    /**
     * Generer ett blankt brett for så å tegne det.
     */
    public void showClearBoard(){
        boardMaker.makeClearBoard(200,200);
        updateCanvas();
        draw_Array();
    }


    /**
     * Reset går over verdier som brukeren har endret på og setter de tilbake til standar verdier.
     */
    public void reset(){
        size.setValue(5);
        timer.setValue(60);
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
        gc = Canvas.getGraphicsContext2D();
        stage = Main.getPrimaryStage();
        colorPicker.setValue(Color.BLACK);

        boardMaker = new BoardMaker();
        fileLoader = new FileLoader(boardMaker);
        nextGenThreads = new NextGenThreads();
        mouse = new Mouse(Canvas);

        Clear.setDisable(true);
        StartStop.setDisable(true);

        //scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollpane.setPannable(false);

        RuleDropDown.setValue("Game of Life");
        RuleDropDown.setItems(ChangeRules);
        listeners();
        initializeService();

    }



    private void initializeService(){
        scheduledService = new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        nextGenThreads.nextGen(RuleDropDown);
                        Platform.runLater(() -> {
                            updateCanvas();
                            draw_Array();
                            stage.setTitle("Game Of Life | Gen : " + runCount++ + " | Fps : " + (int)(timer.getValue())+ " | Size : " + Math.round(size.getValue()) + " | Alive : " + aliveCount + " | " + board.getRow() );
                            aliveCount = 0;
                        });
                        return null;
                    }
                };
            }
        };
        Duration duration = Duration.millis(1000/60);
        scheduledService.setPeriod(duration);
    }



public java.awt.Color getAwkColor(javafx.scene.paint.Color fx){

    
java.awt.Color awtColor = new java.awt.Color((float) fx.getRed(),
                                             (float) fx.getGreen(),
                                             (float) fx.getBlue(),
                                             (float) fx.getOpacity());
return awtColor;
}



    public void saveBoard(ActionEvent actionEvent) throws Exception {

        Color c = colorPicker.getValue();
                
      //GifWriter ugh = new GifWriter(getAwkColor(c));
      GifWriter gifWriter = new GifWriter(board, size, Canvas,getAwkColor(c), nextGenThreads, RuleDropDown);
      gifWriter.GifWriter();
      
      scheduledService.cancel();
      Stage dialog = new Stage();

            dialog.initOwner(Main.getPrimaryStage());
            dialog.initModality(Modality.WINDOW_MODAL); 
            dialog.showAndWait();

            
    }

    
    
}
