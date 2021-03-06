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
    
    public ComboBox RuleDropDown;
    public String filename;
    private NextGenThreads nextGenThreads;
    public int value;


public GifWriter(Board board,Color c,ComboBox RuleDropDown,String filename, int value, NextGenThreads nextGenThreads ){
    this.board = board;
    this.c = c;
    this.RuleDropDown = RuleDropDown;
    this.filename = filename; 
    this.value = value; 
    this.nextGenThreads = nextGenThreads;
    
    
  }

    /**
     * Method that makes a gif. 
     * checks if the cell is alive. if the cell is alive, the square get filled with a color. 
     * makes a board out of the colored squares, and insert into a gif. 
     * @return 
     * @throws Exception 
     */
    public lieng.GIFWriter GifWriter() throws Exception {
        int i = 10;
		
		// data related to the GIF image file
		String path = filename;
                
		int width =  board.getColumn();
		int height = board.getRow();
		int timePerMilliSecond = value; 
		
		// create the GIFWriter object
		lieng.GIFWriter gwriter = new lieng.GIFWriter(width,height,path,timePerMilliSecond);
		
                while(i != 0){
                 int cellSize = (int) calculateSize(height, width, board.getRow(), board.getColumn());

                 int x1 = 0;
                 int x2 = cellSize-1;
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

                    //System.out.println("rule");
                    nextGenThreads.split();
                    nextGenThreads.GenerationWorkers(RuleDropDown);
                    i--;
                }
                
              
		
		
		System.out.println("done!");
                
                return gwriter;
		
	}
    /**
     * calculate the available height and width for the GIF
     * @param availableHeight available height
     * @param availableWidth available width
     * @param rows rows from board
     * @param columns columns from board
     * @return return smaller of sizewith and sizeheight
     */
     private double calculateSize(double availableHeight, double availableWidth,
            int rows, int columns) {
                 double sizeHeight = availableHeight / rows;
                 double sizeWidth = availableWidth / columns;
                 return Math.min(sizeWidth, sizeHeight);
    }
}
