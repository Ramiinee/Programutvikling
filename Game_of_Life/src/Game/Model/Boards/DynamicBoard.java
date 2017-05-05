package Game.Model.Boards;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class DynamicBoard extends Board{

    private int MIN_ROW;
    private int MIN_COL;

    public List<List<Byte>> board;
    private List<List<Byte>> nextGeneration;

    public int getRow() {
        return board.size();
    }
    public void setRow(int row) {
        this.MIN_ROW = row;
    }
    public int getColumn() {
        return board.get(0).size();
    }
    public void setColumn(int column) {
        this.MIN_COL = column;
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
        //addTopRow(10);


        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.get(row).size(); col++) {
                int neighbors = countNeighbor(col,row);
                if (board.get(row).get(col)==1 && (neighbors < 2)){
                    nextGeneration.get(row).set(col,(byte)0);
                }
                else if (board.get(row).get(col) == 1 && (neighbors > 3)) {
                    nextGeneration.get(row).set(col,(byte)0);
                }
                else if ( board.get(row).get(col) == 0 && (neighbors == 3)){
                    nextGeneration.get(row).set(col,(byte)1);
                }
                else {
                    nextGeneration.get(row).set(col, board.get(row).get(col));
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
            for (int col = 0; col < board.get(row).size(); col++) {
                int neighbors = countNeighbor(col,row);
                if (board.get(row).get(col)==1){
                    nextGeneration.get(row).set(col,(byte)1);
                }
                else if (neighbors > 3) {
                    nextGeneration.get(row).set(col,(byte)1);
                }
                else if (neighbors == 3){
                    nextGeneration.get(row).set(col,(byte)0);
                }
                else {
                    nextGeneration.get(row).set(col, board.get(row).get(col));
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
    @Override
    public void noDeadCellsRule(int start, int slutt, CyclicBarrier cyclicBarrier){
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.get(row).size(); col++) {
                int neighbors = countNeighbor(col,row);
                if ( board.get(row).get(col) == 0 && (neighbors == 3)){
                    nextGeneration.get(row).set(col,(byte)1);
                }
                else {
                    nextGeneration.get(row).set(col, board.get(row).get(col));
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
    
    @Override
    public void setBoard(){
        setCurrentGen();
    }

    @Override
    public void makeNextGenArray() {
        
        CheckSides();
        nextGeneration =  new ArrayList<>();
        for (int row = 0; row < board.size(); row++) {
            nextGeneration.add(new java.util.ArrayList<>());
            for (int col = 0; col < board.get(row).size(); col++) {
                nextGeneration.get(row).add((byte) 0);
            }
        }
    }

     /**
     * Count the closest cells to a cell. 
     * @param row rows in the board 
     * @param col columns in the board. 
     * @return returns the number of alive cells
     */
    protected int countNeighbor(int col, int row){
        int neighbors = 0;
        // Check cell on the right.
        if (row != board.size() - 1)
            if (board.get(row + 1).get(col) == 1)
                neighbors++;

        // Check cell on the bottom right.
        if (row != board.size() - 1 && col != board.get(row).size() - 1)
            if (board.get(row + 1).get(col + 1) == 1)
                neighbors++;

        // Check cell on the bottom.
        if (col != board.get(row).size() - 1)
            if (board.get(row).get(col + 1) == 1)
                neighbors++;

        // Check cell on the bottom left.
        if (row != 0 && col != board.get(row).size() - 1)
            if (board.get(row - 1).get(col + 1)== 1)
                neighbors++;

        // Check cell on the left.
        if (row != 0)
            if (board.get(row - 1).get(col) == 1)
                neighbors++;

        // Check cell on the top left.
        if (row != 0 && col != 0)
            if (board.get(row - 1).get(col - 1) == 1)
                neighbors++;

        // Check cell on the top.
        if (col != 0)
            if (board.get(row).get(col - 1) == 1)
                neighbors++;

        // Check cell on the top right.
        if (row != board.size() - 1 && col != 0)
            if (board.get(row + 1).get(col - 1) == 1)
                neighbors++;

        return neighbors;
    }
    
    /**
     * Check sides of the board, to check if some of them should expand or shrink
     */
    public void CheckSides(){
        checkTop();
        checkLeft();
        checkRight();
        checkBottom();
      
    }
/**
 * Adds one row to the top if 1 square at the top is alive
 * remove one row if all sqaures on the top-row is dead 
 */
    public void checkTop(){
        int count = 0;

        for (int topadd = 0; topadd < board.get(0).size(); topadd++) {
            for (int topdelete = 0; topdelete < board.get(0).size(); topdelete++) {
                if(board.get(0).get(topdelete) == 1){
                    count ++; 
                }
            }
            if(count == 0){
            removeTopRow(1);
            }
            if(board.get(0).get(topadd) == 1){
                addTopRow(1);
                return;
            }
        }
    }
    
    /**
 * Adds one column to the left if 1 square at the left side is alive
 * remove one column if all sqaures on the left side is dead 
 */
    public void checkLeft(){
        
        int count = 0;
           
       
        for(int leftadd = 0; leftadd < board.size(); leftadd ++){
             for(int leftdelete = 0; leftdelete < board.size(); leftdelete ++){
            if(board.get(leftdelete).get(0) == 1){
                count++;
            }
            }
             if(count == 0){
             removeLeftColumn(1);
             
             }
       
            if(board.get(leftadd).get(0) == 1){
                addLeftColumn(1);
                return;
            }
        }
    }
   
     /**
 * Adds one column to the right if 1 square at the right side is alive
 * remove one column if all sqaures on the right side is dead 
 */
    public void checkRight(){
        int sum1 = 0;
        int sum2 = 0;

   

        final int columns = board.get(0).size();

        for (List<Byte> e : board) {
            sum1 += e.get(columns - 1);
            sum2 += e.get(columns - 2);

        }
        final int remove = sum1 + sum2;
        final int add = sum1 + sum2;

        if (add != 0) {
            addRightColumn(1);
        } else if (remove == 0 && board.get(0).size() > MIN_COL){
            removeRightColumn(1);
        }
    }
    
    
    
   /**
 * Adds one row to the bottom if 1 square at the bottom is alive
 * remove one row if all sqaures on the bottom-row is dead 
 */
    
    public void checkBottom(){
         final int rows = board.size();
        final int sum1 = board
                .get(rows -1)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum2 = board
                .get(rows -2)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();

        final int remove = sum1 + sum2;
        final int add = sum1 + sum2;

        if (add != 0) {
            addBottomRow(1);
        } else if(remove == 0 && board.size() > MIN_ROW) {
            removeBottomRow(1);
        }
    }
/**
     * board get the information of the new board (nextgeneration ), and the nextgeneration sets back to null
    */
    private void setCurrentGen(){
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.get(row).size(); col++) {
                board.get(row).set(col,nextGeneration.get(row).get(col));

            }
        }
        nextGeneration.clear();
    }

    /**
     * adds extra rows to the top. 
     * @param numberOfRows number of rows you want to add at ones. 
     */
    private void addTopRow(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            board.add(0, new ArrayList<>());
            for (int col = 0; col < board.get(board.size() - 1).size(); col++) {
                board.get(0).add((byte) 0);
            }
        }
    }

    /**
     * adds extra columns to the right side. 
     * @param numberOfColumns number of columns you want to add at ones. 
     */
    private void addRightColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            board.stream().forEach((col) -> col.add((byte) 0));
        }
    }
    /**
     * adds extra columns to the left side. 
     * @param numberOfColumns number of columns you want to add at ones. 
     */
    private void addLeftColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            board.stream().forEach((col) -> col.add(0, (byte) 0));
        }
    }
    /**
     * adds extra rows to the bottom. 
     * @param numberOfRows number of rows you want to add at ones. 
     */
    private void addBottomRow(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            board.add(new ArrayList<>());
            for (int col = 0; col < board.get(0).size(); col++) {
                board.get(board.size() - 1).add((byte) 0);
            }
        }
    }
    /**
     * delete a row to the top. 
     * @param numberOfRows number of rows you want to delete at ones. 
     */
    private void removeTopRow(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            board.remove(0);
        }
    }
    /**
     * delete a Column to the left side. 
     * @param numberOfRows number of columns you want to delete at ones. 
     */
    private void removeLeftColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            for (List<Byte> row : board) {
                row.remove(0);
            }
        }
    }
    /**
     * delete a column to the right side. 
     * @param numberOfRows number of columns you want to delete at ones. 
     */
    private void removeRightColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            for (List<Byte> row : board) {
                row.remove(board.get(0).size() - 1);
            }
        }
    }
    /**
     * delete a row to the bottom. 
     * @param numberOfRows number of rows you want to delete at ones. 
     */
    private void removeBottomRow(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            board.remove(board.size() - 1);
        }
    }


/**
     * Checks if the current cell is alive or not. 
     * @param column column in the board
     * @param row row in the baord
     * @return returns the value 1 for alive or 0 for dead cell. 
     */
    @Override
    public byte getCellAliveState(int row, int column) {
        if (row > getRow() - 1 || row < 0 || column > getColumn() - 1 || column < 0) {
            return (byte)0;
        } else {
            return board.get(row).get(column);
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
        this.board.get(row).set(column, aliveState);
    }



    @Override
    public void makeBoard(int row, int col) {
        this.MIN_ROW = row;
        this.MIN_COL = col;

        board = new ArrayList<>();

        for (int rows = 0; rows < MIN_ROW; rows++) {
            board.add(new java.util.ArrayList<>());
            for (int cols = 0; cols < MIN_COL; cols++) {
                board.get(rows).add((byte)0);

            }
        }

    }

    @Override
    public byte[][] getBoard() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
