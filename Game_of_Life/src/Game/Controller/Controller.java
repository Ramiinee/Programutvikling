package Game.Controller;




import java.net.URL;
import java.util.ResourceBundle;
import Game.Model.Boards.Board;
import Game.Model.Boards.DynamicBoard;
import Game.Model.Boards.StaticBoard;
import Game.Model.MetaData;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.*;
import javafx.util.Duration;
import javax.swing.JFileChooser;

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
    public ToggleButton Grid;

    //Counters
    private int runCount = 1;
    private int aliveCount = 0;
    private double size = 5;
    private double zoom_fac = 1.05;
    private double zoomValue = 0;

    //Checkers
    private boolean loaded = false;
    private boolean running = false;

    //Objects
    private BoardMaker boardMaker;
    private FileLoader fileLoader;
    private Board board;
    private NextGenThreads nextGenThreads;
    private MetaData metaData;


    private Stage stage;
    private GraphicsContext gc;
    private ObservableList<String> ChangeRules = FXCollections.observableArrayList("Game of Life", "No deaths", "Cover");
    private ObservableList<String> ChangeBoard = FXCollections.observableArrayList("Static", "Dynamic");
    private ScheduledService<Void> scheduledService;
    private boolean run = false;
    private String place;
    

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
                    draw(x,y,Color.WHITE);
                }
                else {
                    board.setCellAliveState(x,y,(byte)1);
                    draw(x,y,colorPicker.getValue());
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
                    draw(x,y,colorPicker.getValue());
                } catch (Exception el) {
                    System.err.println("Why you out of canvas? " + y + " | "+x );
                }
            }
        });
        stage.addEventHandler(KeyEvent.KEY_PRESSED,(KeyEvent e)->{
            if (e.getCode().equals(KeyCode.ENTER)){
                startStop();
            }
            if(e.isControlDown() && e.getCode().equals(KeyCode.N)){
                newBoard();
            }
            if(e.isControlDown() && e.getCode().equals(KeyCode.S)){
                saveBoard();
            }


            else if (e.getCode().equals(KeyCode.DELETE)){
                Clear();
            }
        });
        Canvas.setOnScroll((ScrollEvent event) -> {
             
            Scale newScale = new Scale();
            newScale.setPivotX(event.getX());
            newScale.setPivotY(event.getY());

            if (zoomValue < 30 && zoomValue > -30){
                if (event.getDeltaY() > 0){
                    zoomValue++;
                    newScale.setX( Canvas.getScaleX() * zoom_fac );
                    newScale.setY( Canvas.getScaleY() * zoom_fac );
                                }else {
                    zoomValue--;
                    newScale.setX( Canvas.getScaleX() / zoom_fac );
                    newScale.setY( Canvas.getScaleY() / zoom_fac );

                }
            }
            else {
                if (zoomValue >= 30&& event.getDeltaY() < 0) {
                    zoomValue--;
                    newScale.setX(Canvas.getScaleX() / zoom_fac);
                    newScale.setY(Canvas.getScaleY() / zoom_fac);
                } else if (zoomValue <= -30 && event.getDeltaY() > 0) {
                    zoomValue++;
                    newScale.setX(Canvas.getScaleX() * zoom_fac);
                    newScale.setY(Canvas.getScaleY() * zoom_fac);
                }
            }

            Canvas.getTransforms().add(newScale);
            event.consume();
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
            Button randomBoard = new Button("Randome Board");
            Button cancel = new Button("Cancle");

            clearBoard.setOnAction(event -> {
                setBoardMakerBoard(comboBox);
                int value = Integer.parseInt(sizeField.getText());
                boardMaker.makeClearBoard(value,value);
                loaded(loaded = true);
                newBoard.close();
                BoardLabel.setText("");
            });

            randomBoard.setOnAction(( event) -> {
                setBoardMakerBoard(comboBox);
                int value = Integer.parseInt(sizeField.getText());//sjekke om det er tall eller ikke
                boardMaker.randomBoard(value,value);
                loaded(loaded = true);
                newBoard.close();
                BoardLabel.setText("");

            });


            cancel.setOnAction(event -> newBoard.close());
            cancel.setCancelButton(true);
            randomBoard.setDefaultButton(true);


            VBox layout= new VBox(20);
            HBox Size = new HBox(10);
            HBox okCancel = new HBox(10);

            Size.getChildren().addAll(sizeField, comboBox);

            okCancel.getChildren().addAll(clearBoard,randomBoard,cancel);
            okCancel.setAlignment(Pos.BASELINE_RIGHT);

            layout.getChildren().addAll(label1, Size,okCancel);

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
            Button cancel = new Button("Cancel");

            //ToggleButton toggleButton = new ToggleButton("Place free");
            cancel.setCancelButton(true);
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
            cancel.setOnAction(event -> {

                loadBoard.close();
            });



        VBox layout= new VBox(20);

        HBox url = new HBox(10);
        HBox okCancel = new HBox(10);
        HBox choosers = new HBox(10);
            okCancel.setAlignment(Pos.BASELINE_RIGHT);
            choosers.setAlignment(Pos.CENTER);



            url.getChildren().addAll(urlField,ok);
            url.setAlignment(Pos.CENTER);
            okCancel.getChildren().addAll(comboBox,browse,cancel);
            choosers.getChildren().addAll();

            layout.getChildren().addAll(label1, url, choosers,okCancel);

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
     * den kaller på draw eller draw, for visning på skjerm.
 Samtidig så setter den antall celler som er i livet i hver generation.
     */
    private void draw_Array(){
        gc.clearRect(0,0,Canvas.getWidth(), Canvas.getHeight());
        for (int row = 0; row < board.getRow(); row++) {
            for (int col = 0; col <  board.getColumn() ; col++) {
                if (board.getCellAliveState(row,col)==1){
                    draw(row  , col , colorPicker.getValue());
                    aliveCount ++;
                }
                else {
                    //draw_ned(row , col , Color.WHITE);
                }

            }

        }
        if (Grid.isSelected()) {
            gc.setLineWidth(0.3);
            gc.setStroke(Color.GRAY);
            final int boardRowLength = board.getRow();
            final int boardColumnLength = board.getColumn();

            double end_X = Canvas.getWidth();
            double end_Y = Canvas.getHeight();

            for (int y = 0; y <= boardRowLength; y++) {
                gc.strokeLine(0, 0 + (size * y),
                        end_X, 0 + (size * y));
            }

            for (int x = 0; x <= boardColumnLength; x++) {
                gc.strokeLine(0 + (size * x),
                        0, 0 + (size * x), end_Y);
            }
        }


    }


    


    private void draw( int col, int row, Color c) {
        gc.setFill(c);
        gc.fillRect((row * (size))+1 , (col  * (size))+1, ((size))-2, (size)-2);

    }


     private void updateCanvas(){

        if (!(Canvas.getWidth() == board.getRow()*size && Canvas.getHeight() == board.getColumn()*size)){
            Canvas.setWidth(board.getColumn()*size);
            Canvas.setHeight(board.getRow()*size);
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
            zoomValue = 0;
            scrollpane.setHvalue(0);
            scrollpane.setVvalue(0);
            Canvas.getTransforms().retainAll();

        }catch (Exception e){
            e.getStackTrace();
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

        metaData = new MetaData();
        boardMaker = new BoardMaker(metaData);
        fileLoader = new FileLoader(boardMaker);

        nextGenThreads = new NextGenThreads();

       



        //Clear.setDisable(true);
        StartStop.setDisable(true);
        Grid.setSelected(true);
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

                        return null;
                    }
                };
            }
        };
        scheduledService.setPeriod(Duration.millis(1000/60));
    }



   public java.awt.Color getAwkColor(javafx.scene.paint.Color fx){   
        java.awt.Color awtColor = new java.awt.Color((float) fx.getRed(),
                                             (float) fx.getGreen(),
                                             (float) fx.getBlue(),
                                             (float) fx.getOpacity());
        return awtColor;
    }



    public void saveBoard(){
        String checkGif = ".gif";


        //Pause game while saving
        scheduledService.cancel();

    //-----------------------------------------------
        Stage GifSave =new Stage();
        GridPane grid = new GridPane();
        grid.setMaxSize(42, 327);


        Label saveas = new Label("Save as:");
        Button OK = new Button("    OK    ");
        Button Cancel = new Button ("Cancel");
        Button Browse = new Button("Browse");
        ChoiceBox DurName = new ChoiceBox();
        TextField saveName = new TextField(place);
        Label Duration = new Label("Duration");
        Label Space = new Label(" ");

        saveas.setFont(new Font("Serif", 18));
        DurName.getItems().addAll("0.25","0.5", "1", "2" );
        DurName.setTooltip(new Tooltip("Select Speed (Seconds)"));
        DurName.getSelectionModel().selectFirst();


        grid.add(Space, 0,0);
        grid.add(saveas, 1,1 );
        grid.add(saveName, 2, 2, 2, 1 );
        grid.add(DurName, 2,4);
        grid.add(Duration, 2,3);
        grid.add(Browse, 4,2 );
        grid.add(OK,4, 4 );
        grid.add(Cancel, 5,4 );


        OK.setOnAction((event) -> {
                try {
                    if(!saveName.getText().isEmpty()){
                    place = saveName.getText();
                    Pattern gif = Pattern.compile("(.*/)*.+\\.(|gif|GIF)$");
                    Matcher gifMatch = gif.matcher(place);
                        if(!gifMatch.find()){
                      place += ".gif";

               }else{}
                    saveName.setText(place);
                    run = true; 
                   GifSave.close();
                }}
            catch(NullPointerException e){System.out.print("Your name is not valid"); }     
          });

        Cancel.setOnAction((event) -> {
                GifSave.close();         
            });

        Browse.setOnAction((event) -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("choosertitle");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                 place = chooser.getSelectedFile().toString() + "\\Game_of_life";

        }else{
                System.out.println("No Selection ");
            }
        saveName.setText(place);


            });   
            Scene scene = new Scene(grid, 300, 150);
            GifSave.setScene(scene);
            GifSave.showAndWait();



     //-------------------------------------------------
       try { if(run){ int value = 0;
            if ((DurName.getValue() == "0.25")){
                 value = 250;
                }else if( DurName.getValue() == "0.5") {
                 value = 500;
                }else if(DurName.getValue() == "1"){
                 value = 1000;
                }else{
                 value = 2000;
                }

            String filename = saveName.getText();
            

            
           try {
               GifWriter gifWriter = new GifWriter(board, getAwkColor(colorPicker.getValue()), RuleDropDown, filename, value, nextGenThreads);
               gifWriter.GifWriter();
           } catch (Exception ex) {
               System.out.println("It is not possible to make Gif here.");
           }
}

       }catch(NullPointerException e){
           System.out.print("you do not have a valid name");
           return;
       }
    }
   
}
