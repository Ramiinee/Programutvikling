package Testing.Package;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Controller implements Initializable{
    
    public int i; 
    public int j;
    public int a;
    public int b;
    public int[][] nextgeneration;
    Timeline timeline = new Timeline( new KeyFrame(Duration.millis(120), e -> draw_Array()));

    @FXML
    private Canvas graphics;
    
    
    // sette en random størrelse på disse? 
    public int kolonner = 100;
    public int rader = 100;
    
    public void timeline(){
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
    }


    private int[][] board = new int[kolonner][rader];
   
    public void startButton(){
        randomPattern();
        draw_Array();
        timeline();
        timeline.play();
        
    }
    private GraphicsContext gc;

    public void randomPattern(){
         for (i = 0; i < kolonner; i++) {
            for (j = 0; j < rader ; j++) {
                
        board[i][j] = (Math.random()<0.5)?0:1;
    }}}
    private void draw_Array(){
        for (i = 0; i < kolonner; i++) {
            for (j = 0; j < rader ; j++) {
                
                if (board[i][j] == 1){

                    draw( i , j, Color.BLACK);

                }
                if (board[i][j] == 0){
                    draw(i, j, Color.WHITE);
                }
            }
        }
        
       // ----------------------------
        
        
        nextgeneration = new int[100][100];
     for (a = 0; a < board.length; a++) {
       for (b = 0; b < board[a].length; b++) {
        int neighbors = 0;
            if (a != 0 && b !=0){
            if (board[a-1][b-1] == 1) 
                neighbors++;
            }
            if (b != 0){
            if (board[a][b-1] == 1) 
                neighbors++;
            }
            if (a != board[a].length -1 && b != 0 ){
            if (board[a+1][b-1] == 1) 
                neighbors++;
            }
            if (a != 0){
            if (board[a-1][b]   == 1) 
                neighbors++;
            }
            if (a != board[a].length -1){
            if (board[a+1][b]   == 1) 
                neighbors++;
            }
            if(a != 0 && b != board[b].length -1){
            if (board[a-1][b+1] == 1) 
                neighbors++;
            }
            if(b != board[b].length -1){
            if (board[a][b+1] == 1) 
                neighbors++;
            }
            if(a != board[a].length - 1 && b != board[b].length -1){
            if (board[a+1][b+1] == 1) 
                neighbors++;
            }
            
             //----------------------------        
                         
            if(neighbors <  2) {
                     nextgeneration[a][b] = 0;
                    }
            else if (neighbors >  3) {
                       nextgeneration[a][b] = 0;
                    }
            else if (neighbors == 3) {
                       nextgeneration[a][b] = 1;
                    }
            else { 
                    nextgeneration[a][b] = board[a][b];
                    }
                             //System.out.print(board[a][b] + "  ");

                }
            //System.out.println();

     }
                 //System.out.println();

            board = nextgeneration;

            }
    
    
            
                    
                    
    @FXML private void remove_Array() {
        gc = graphics.getGraphicsContext2D();
        for ( i = 0; i < board.length; i++) {
            for (j = 0; j < board.length ; j++) {
                
                if (board[i][j] == 1){

                    draw( i , j, Color.WHITE );

                }


            }

        }
    }

    private void draw( int x, int y, Color c) {
        gc.setFill(c);
        gc.fillRect(x *10, y *10, 9, 9);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = graphics.getGraphicsContext2D();

    }


}
