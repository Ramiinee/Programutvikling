package Game.Controller;



import Game.Model.Boards.Board;
import java.awt.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;

/**
 *
 * @author Joachim-Privat
 */
public class GifWriter {
    private Canvas Canvas;
    public Slider size;
    public Board board;
    private Color c;
    public NextGenThreads nextGenThreads;
    public ComboBox RuleDropDown;


public GifWriter(Board board,Slider size,Canvas Canvas,Color c, NextGenThreads nextGenThreads, ComboBox RuleDropDown  ){
    this.board = board;
    this.size = size;
    this.Canvas = Canvas;
    this.c = c;
    this.nextGenThreads = nextGenThreads;
    this.RuleDropDown = RuleDropDown;
    
  }

    
    public boolean GifWriter() throws Exception {
        int i = 4;
		
		// data related to the GIF image file
		String path = "testgif.gif";
                
		int width =  board.getColumn();
		int height = board.getRow();
		int timePerMilliSecond = 1000; // 1 second
		
		// create the GIFWriter object
		lieng.GIFWriter gwriter = new lieng.GIFWriter(width,height,path,timePerMilliSecond);
		
                while(i != 0){
                 int cellSize = (int) calculateSize(height, width, board.getRow(), board.getColumn());

                 int x1 = 0;
                 int x2 = cellSize;
                 int y1 = 0;
                 int y2 = cellSize;
                
                       
                 
                for(int col = 0; col < board.getColumn() -1; col++){
                    for(int row = 0; row < board.getRow() -1; row++){
                        if(board.getCellAliveState(col, row) == 1){
                            gwriter.fillRect(x1, x2, y1, y2, c);
                        }
                        x1 += cellSize;
                        x2 += cellSize;
                    }
                    x1 = 0;
                    x2 = cellSize;
                    y1 += cellSize;
                    y2 += cellSize;
                }

		// insert the painted image into the animation sequence
		gwriter.insertAndProceed();
              
		board.nextGeneration(0, board.getRow());
                --i;
                 }
		
		
		System.out.println("done!");
                
                return true;
		
	}
     private double calculateSize(double availableHeight, double availableWidth,
            int rows, int columns) {
        double sizeHeight = availableHeight / rows;
        double sizeWidth = availableWidth / columns;
        return Math.min(sizeWidth, sizeHeight);
    }
}
