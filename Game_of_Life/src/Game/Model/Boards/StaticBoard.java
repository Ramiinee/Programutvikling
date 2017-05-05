package Game.Model.Boards;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Joachim-Privat
 */
public class StaticBoard extends Board{

    private int Column;
    private int Row;
    public byte[][] board;
    private byte[][] nextGeneration;



    @Override
    public int getColumn() {
        return Column;
    }

    @Override
    public void setColumn(int column) {
        this.Column = column;
    }

    @Override
    public int getRow() {
        return board.length;
    }
    
    @Override
    public void setRow(int row) {
        this.Row = row;

    }
    
     @Override
    public void makeNextGenArray() {
        nextGeneration = new byte[board.length][board[0].length];
    }

    
    
   

   

/**
 * The rules of Game of life. 
 * decide whats alive or not in the next generation
 * scanns game from int Start to Int stop. 
 * @param start where therow starts from 
 * @param stop where the row ends.  
 * @param cyclicBarrier 
 */
    @Override
    public void nextGeneration(int start, int stop, CyclicBarrier cyclicBarrier){

        for (int row = start; row < stop ; row++) {
            for (int col = 0; col < board[row].length; col++) {
                int neighbors = countNeighbor(row, col);
                if ((board[row][col] == 1) && (neighbors < 2)) {
                    nextGeneration[row][col] = 0;
                } else if ((board[row][col] == 1) && (neighbors > 3)) {
                    nextGeneration[row][col] = 0;
                } else if (board[row][col] == 0 && (neighbors == 3)) {
                    nextGeneration[row][col] = 1;
                } else {
                    nextGeneration[row][col] = board[row][col];
                }

            }
        }
        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Main Thread interrupted!");
            e.printStackTrace();

        }



    }
    
    /**
     * Slowly cover Rules. 
     * decide whats alive or not in the next generation
     * scanns game from int Start to Int stop. 
     * @param start where row starts
     * @param stop where row ends
     * @param cyclicBarrier 
     */
    @Override
    public void slowlyCover(int start, int stop, CyclicBarrier cyclicBarrier){

        for (int row = start; row < stop ; row++) {
            for (int col = 0; col < board[row].length; col++) {
                int neighbors = countNeighbor(col,row);
                if((board[col][row] == 1)) {
                    nextGeneration[col][row] = 1;
                }
                else if ((neighbors >  3)) {
                    nextGeneration[col][row] = 1;
                }
                else if ((neighbors == 3)) {
                    nextGeneration[col][row] = 0;
                }
                else {
                    nextGeneration[col][row] = board[col][row];
                }

            }

        }
        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Main Thread interrupted!");
            e.printStackTrace();

        }
    }
    /**
     * No dead cell rule. det same as nextGeneration method, except that alive cells doesnt die. 
     * decide which cell who comes to life
     * scanns game from int Start to Int stop. 
     * @param start where col starts from  in board
     * @param stop where col ends board
     * @param cyclicBarrier 
     */
    
    public void noDeadCellsRule(int start, int stop, CyclicBarrier cyclicBarrier){

        for (int col = start; col < stop; col++) {
            for (int row = 0; row < board[col].length; row++) {
                int neighbors = countNeighbor(col,row);

                if (board[col][row] == 0 && (neighbors == 3)) {
                    nextGeneration[col][row] = 1;
                }
                else {
                    nextGeneration[col][row] = board[col][row];
                }

            }
        }


        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Main Thread interrupted!");
            e.printStackTrace();

        }


    }
   
    /**
     * Count the closest cells to a cell. 
     * @param row rows in the board 
     * @param col columns in the board. 
     * @return returns the number of alive cells
     */
    @Override
    protected int countNeighbor(int row, int col){
        int neighbors = 0;
        // Check cell on the right.
        if (row != board.length - 1)
            if (board[row + 1][col] == 1)
                neighbors++;

        // Check cell on the bottom right.
        if (row != board.length - 1 && col != board[row].length - 1)
            if (board[row + 1][col + 1] == 1)
                neighbors++;

        // Check cell on the bottom.
        if (col != board[row].length - 1)
            if (board[row][col + 1] == 1)
                neighbors++;

        // Check cell on the bottom left.
        if (row != 0 && col != board[row].length - 1)
            if (board[row - 1][col + 1]== 1)
                neighbors++;

        // Check cell on the left.
        if (row != 0)
            if (board[row - 1][col] == 1)
                neighbors++;

        // Check cell on the top left.
        if (row != 0 && col != 0)
            if (board[row - 1][col - 1] == 1)
                neighbors++;

        // Check cell on the top.
        if (col != 0)
            if (board[row][col - 1] == 1)
                neighbors++;

        // Check cell on the top right.
        if (row != board.length - 1 && col != 0)
            if (board[row +1][col - 1] == 1)
                neighbors++;

        return neighbors;

    }
    
    //---test-----
    public String toString(){
        String check = "0";
        for (int row = 0; row < board.length ; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if(board[col][row] == 1){
                    check += 1;
                }else{
                    check += 0;
                }
                    
                }  
            }
        return check;
    }
    
    
    public byte[][] setBoard(byte[][] board) {
        this.board = new byte[board.length][board[0].length];
         for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length; row++) {
        this.board[col][row] = board[col][row];
            }
            
         }
         return this.board;
    }
    
    
    
    //------------


    /**
     * Checks if the current cell is alive or not. 
     * @param col column in the board
     * @param row row in the baord
     * @return returns the value 1 for alive or 0 for dead cell. 
     */
    
    @Override
    public byte getCellAliveState(int col, int row) {
        if (col > getRow()-1 || col < 0 || row > getColumn()-1 || row < 0) {
            System.out.println("outside array");
            return 0;
        } else {
            return board[col][row];
        }
    }
    /**
     * Set the cell to either 1 (alive ) or 0 ( dead) in the board. 
     * column and row is used to navigate where you are placed on the board.
     * @param row row in the board
     * @param column column in the board 
     * @param aliveState either 1 or 0 
     */
    
    @Override
    public void setCellAliveState(int row, int column, byte aliveState) {
        if (row > getRow()-1 || row < 0 || column > getColumn()-1 || column < 0) {

        } else {
            if (aliveState == 0 || aliveState == 1) {
                board[row][column] = aliveState;
            } else {
                throw new RuntimeException("Invalid number in cell state: " + aliveState);
            }
        }
    }


    @Override
    public void makeBoard(int row, int col) {
        this.Row = row;
        this.Column = col;
        board = new byte[Row][Column];
    }

    /**
     * board get the information of the new board (nextgeneration ), and the nextgeneration sets back to null
    */
    @Override
    public void setBoard() {
        board = nextGeneration;
        nextGeneration = null;
    }

    @Override
    public byte[][] getBoard() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
