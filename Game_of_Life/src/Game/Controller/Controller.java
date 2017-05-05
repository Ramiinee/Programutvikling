package Game.Controller;




import java.net.URL;
import java.util.ResourceBundle;
import Game.Model.Boards.Board;
import Game.Model.Boards.DynamicBoard;
import Game.Model.Boards.StaticBoard;
import Game.Model.MetaData;
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
import javafx.scene.image.Image;
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
    private final double zoom_fac = 1.05;
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
    private final ObservableList<String> ChangeRules = FXCollections.observableArrayList("Game of Life", "No deaths", "Cover");
    private final ObservableList<String> ChangeBoard = FXCollections.observableArrayList("Static", "Dynamic");
    private ScheduledService<Void> scheduledService;
    private boolean run = false;
    private String place;
    

    /**
     *Here there are listeners who respond every time a slider or colorpicker changes value.
     *      
     */
    private void listeners(){
        timer.valueProperty().addListener((ObservableValue<? extends Number> timerListener, Number oldtime, Number newtime) -> {
            Duration duration = new Duration(1000/newtime.intValue());
            scheduledService.setPeriod(duration);
        });


        colorPicker.valueProperty().addListener((ObservableValue<? extends Color> timerListener, Color oldColor, Color newColor) -> {
            
            gc.clearRect(0,0,3000,2000);
            draw_Array();
           
        });

        Grid.selectedProperty().addListener((observable) -> {
            draw_Array();
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

    /**
     * Start/stop Button
     * if the game is loaded, you can run the game by pressing the start button. 
     * if the game is running, the start button changes to a stop button
     */
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
     /**
      * If the game is loaded (is filled with black squares), the clear method Clears board.
      */
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
     /**
      * Create a new board, either clear board or random. 
      */
    public void newBoard() {
        Stage newBoard =new Stage();
        newBoard.getIcons().add(new Image("/Game/Icon.PNG"));
        newBoard.initModality(Modality.APPLICATION_MODAL);
        newBoard.setTitle("Load");
            Label label1= new Label("How do you want to load?");
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.setValue("Static");
            comboBox.setItems(ChangeBoard);

            TextField sizeField1 = new TextField();
            sizeField1.setPromptText("Enter Size");
            sizeField1.setText("50");
            TextField sizeField2 = new TextField();
            sizeField2.setPromptText("Enter Size");
            sizeField2.setText("50");
            
            sizeField1.setMaxWidth(90);
            sizeField2.setMaxWidth(90);

            Button clearBoard = new Button("Clear Board");
            Button randomBoard = new Button("Randome Board");
            Button cancel = new Button("Cancel");

            clearBoard.setOnAction(event -> {
                
                try {                    
                    int row = Integer.parseInt(sizeField1.getText());
                    int col = Integer.parseInt(sizeField1.getText());
                    setBoardMakerBoard(comboBox);
                    boardMaker.makeClearBoard(row,col);
                    loaded(loaded = true);
                    newBoard.close();
                    BoardLabel.setText("");
                } catch (Exception e) {
                    label1.setText("Not approved values");
                    
                }
                
            });

            randomBoard.setOnAction(( event) -> {
                try {
                    int row = Integer.parseInt(sizeField1.getText());
                int col = Integer.parseInt(sizeField1.getText());
                setBoardMakerBoard(comboBox);
                boardMaker.randomBoard(row,col);
                loaded(loaded = true);
                newBoard.close();
                BoardLabel.setText("");
                } catch (Exception e) {
                    label1.setText("Not approved values");
                }
                

            });


            cancel.setOnAction(event -> newBoard.close());
            cancel.setCancelButton(true);
            randomBoard.setDefaultButton(true);
               

            VBox layout= new VBox(20);
            HBox Size = new HBox(5);
            HBox okCancel = new HBox(10);

            Size.getChildren().addAll(sizeField1, sizeField2, comboBox);

            okCancel.getChildren().addAll(clearBoard,randomBoard,cancel);
            okCancel.setAlignment(Pos.BASELINE_RIGHT);

            layout.getChildren().addAll(label1, Size,okCancel);

            layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 150);
        newBoard.setScene(scene1);
        newBoard.showAndWait();

    }
    /**
     * create popup load board. 
     * Load board, either from Disk or URL. 
     */
     public void loadBoard() {
        Stage loadBoard =new Stage();
        loadBoard.getIcons().add(new Image("/Game/Icon.PNG"));
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

     /**
      * Set the Board to either Dynamic or Static. 
      * @param comboBox is used to select Dynamic or Static. 
      */
    private void setBoardMakerBoard(ComboBox comboBox){
        if (!(comboBox.getValue() == "Static")){
            board = new DynamicBoard();
            SaveBoard.setDisable(true);
        }else {
            board = new StaticBoard();
            SaveBoard.setDisable(false);
        }
        boardMaker.setBoardType(board);

    }

    /**
     * Loads a new Board to Canvas. 
     * if there is nothing to load, or that somthing went wrong, it shows a clear board. 
     * @param loaded decide if a board is loaded or not. 
     */
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
     * Draw array runs through the board and determines whether to draw the selected color or to draw white.
     * It calls on draw for display on screen.
     * At the same time, it sets the number of cells in the life of each generation.
     * Grid is a ToggleButton controlling the grid.
     */
    private void draw_Array(){
        gc.clearRect(0,0,Canvas.getWidth(), Canvas.getHeight());
        for (int row = 0; row < board.getRow(); row++) {
            for (int col = 0; col <  board.getColumn() ; col++) {
                if (board.getCellAliveState(row,col)==1){
                    draw(row  , col , colorPicker.getValue());
                    aliveCount ++;
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


    

    /**
     * Fills all living cells with a color.
     * @param col Column of the board 
     * @param row   Row of the board
     * @param c    the color thats being used to colorize the board. 
     */
    private void draw( int col, int row, Color c) {
        gc.setFill(c);
        gc.fillRect((row * (size))+1 , (col  * (size))+1, ((size))-2, (size)-2);

    }

    /**
     * Updates Canvas. To the size of the board x the size the cell is drawn.
     */
     private void updateCanvas(){

        if (!(Canvas.getWidth() == board.getRow()*size && Canvas.getHeight() == board.getColumn()*size)){
            Canvas.setWidth(board.getColumn()*size);
            Canvas.setHeight(board.getRow()*size);
        }


    }


    /**
     * Generate a blank board and then draw it.
     */
    private void showClearBoard(){
        boardMaker.makeClearBoard(200,200);
        updateCanvas();
        draw_Array();
    }


    /**
     * Reset exceeds values that the user has changed and resets to default values.
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
     * Here, objects are created and rules for how GUI will behave tightly.
     * It also launches listeners and mouse input.
     */
     @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = Canvas.getGraphicsContext2D();
        stage = Main.getPrimaryStage();
        colorPicker.setValue(Color.BLACK);

        initializeObjects();
        initializeButtons();
       
        listeners();
        initializeService();
       
        updateCanvas();
        draw_Array();
        
    }
    
    /**
     * Gui related things are set to the state that they are needed.
     */
    private void initializeButtons(){
        StartStop.setDisable(true);
        Grid.setSelected(true);
        scrollpane.setPannable(false);
        RuleDropDown.setValue("Game of Life");
        RuleDropDown.setItems(ChangeRules);
    }
    /**
     * Initializes Objects that ar needed to run the program. 
     */
    private void initializeObjects(){
        metaData = new MetaData();
        boardMaker = new BoardMaker(metaData);
        fileLoader = new FileLoader(boardMaker);

        nextGenThreads = new NextGenThreads();
        
        board = new StaticBoard();
        board.makeBoard(200,200);
        nextGenThreads.setBoard(board);
        boardMaker.setBoardType(board);
        
    }


/**
 * Initializes the Run Service. 
 * ScheduledService is cald on when run og stop i selected.
 */
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


/**
 * change scene.paint.color to Awkcolor
     * @param fx Color
     * @return awt Color
 */
   public java.awt.Color getAwkColor(javafx.scene.paint.Color fx){   
        java.awt.Color awtColor = new java.awt.Color((float) fx.getRed(),
                                             (float) fx.getGreen(),
                                             (float) fx.getBlue(),
                                             (float) fx.getOpacity());
        return awtColor;
    }


/**
 * Makes Gif from board, and saves it. 
 * Contains Modal
 */
    public void saveBoard(){

        //Pause game while saving
        scheduledService.cancel();

    //------ Modal -----------------------------------------
        Stage GifSave = new Stage();
        GifSave.getIcons().add(new Image("/Game/Icon.PNG"));
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
                
            catch(NullPointerException e){
                saveas.setText("Invalid name");
                
                }   
                
               
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
          
       }
    }
   
}
