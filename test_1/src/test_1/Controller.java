package Test_1;


import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Controller {
    
    public int i; 
    public int j;
    public int[][] nextgeneration = new int[i][j];
    public int a;
    public int b;
    @FXML
    private Canvas graphics;
    
    
    // sette en random størrelse på disse? 
    public int kolonner = 10;
    public int rader = 10;



    private int[][] board = new int[kolonner][rader];
   
    
    private GraphicsContext gc;

    @FXML private void draw_Array(){
        
        
        gc = graphics.getGraphicsContext2D();
        for (i = 0; i < kolonner; i++) {
            for (j = 0; j < rader ; j++) {
                
                board[i][j] = (Math.random()<0.5)?0:1; 
                
                if (board[i][j] == 1){

                    draw( i , j, Color.BLACK );

                }
            
        
        int neighbors = 0;
            if (i != 0 && j !=0){
            if (board[i-1][j-1] == 1) neighbors++;
            }
            if (j != 0){
            if (board[i][j-1] == 1) neighbors++;
            }
            if (i != board[i].length && j != 0 ){
            if (board[i+1][j-1] == 1) neighbors++;
            }
            
            if (i != 0){
            if (board[i-1][j]   == 1) neighbors++;
            }
            if (i != board[i].length){
            if (board[i+1][j]   == 1) neighbors++;
            }
            if(i != 0 && j != board[j].length){
            if (board[i-1][j+1] == 1) neighbors++;
            }
            if(j != board[j].length){
            if (board[i][j+1] == 1) neighbors++;
            }
            if(i != board[i].length && j != board[j].length){
            if (board[i+1][j+1] == 1) neighbors++;
            }
            
            for (a = -1; a <= 1; a++) {
                 for (b = -1; b <= 1; b++) {

                     neighbors += board[i+a][j+b];
                     
                     neighbors -= board[a][b];               
                    }
                }
            if((board[a][b] == 1) && (neighbors <  2)) {
                     nextgeneration[a][b] = 0;
                    }
            else if ((board[a][b] == 1) && (neighbors >  3)) {
                       nextgeneration[a][b] = 0;
                    }
            else if ((board[a][b] == 0) && (neighbors == 3)) {
                       nextgeneration[a][b] = 1;
                    }
            else {
                    nextgeneration[a][b] = board[a][b];
                    }

            }
            board = nextgeneration;
        }
            
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
        gc.fillRect(x *10, y *10, 10, 10);

    }


}
