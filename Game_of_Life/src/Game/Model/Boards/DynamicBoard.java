package Game.Model.Boards;

import java.util.ArrayList;
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
 * @param cyclicBarrier  The cyclic barrier.
 */
    @Override
    public void nextGeneration(int start, int stop, CyclicBarrier cyclicBarrier){
        //addTopRow(10);


        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.get(row).size(); col++) {
                int neighbors = countNeighbor(row,col);
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
     * @param cyclicBarrier  The cyclic barrier.
     */
    @Override
    public void slowlyCover(int start, int stop, CyclicBarrier cyclicBarrier){


        for (int row = start; row < stop ; row++) {
            for (int col = 0; col < board.get(row).size(); col++) {
                int neighbors = countNeighbor(row,col);
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
     * No dead cell rule. Same as next generation method, except that living cells do not die
     * decide which cell who comes to life
     * scanns game from int Start to Int stop. 
     * @param start where col starts from in Threads
     * @param stop where col ends board
     * @param cyclicBarrier  The cyclic barrier.
     */
    @Override
    public void noDeadCellsRule(int start, int stop, CyclicBarrier cyclicBarrier){
        for (int row = start; row <board.size(); row++) {
            for (int col = 0; col < board.get(row).size(); col++) {
                int neighbors = countNeighbor(row,col);
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
    
    /**
     * Sets the board to the newest value
     */
    @Override
    public void setBoard(){
        setCurrentGen();
    }

    /**
     * Generate nextGeneration
     */
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
    @Override
    protected int countNeighbor(int row, int col){
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
     * Remove one row if all sqaures on the top-row is dead 
     */
    public void checkTop(){
        final int sum1 = board
                .get(0)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum2 = board
                .get(1)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum3 = board
                .get(2)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum4 = board
                .get(3)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum5 = board
                .get(4)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int remove = sum1 + sum2 + sum3 + sum4 + sum5;
        final int add = sum1 + sum2;

        if (add != 0) {
            addTopRow(1);
        } else if (remove == 0 && board.size() > MIN_ROW) {
            removeTopRow(1);
        }
    }
    
    /**
    * Adds one column to the left if 1 square at the left side is alive
    * Remove one column if all sqaures on the left side is dead 
    */
    public void checkLeft(){
        final int rows = board.size();
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        int sum4 = 0;
        int sum5 = 0;

        for (int row = 0; row < rows; row++) {
            sum1 += board.get(row).get(0);
            sum2 += board.get(row).get(1);
            sum3 += board.get(row).get(2);
            sum4 += board.get(row).get(3);
            sum5 += board.get(row).get(4);
        }
        final int remove = sum1 + sum2 + sum3 +sum4 + sum5;
        final int add = sum1 + sum2;

        if (add != 0) {
           addLeftColumn(1);
        } else if(remove == 0 && board.get(0).size() > MIN_COL){
            removeLeftColumn(1);
        }
    }
   
     /**
    * Adds one column to the right if 1 square at the right side is alive
    * Remove one column if all sqaures on the right side is dead 
    */
    public void checkRight(){
         int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        int sum4 = 0;
        int sum5 = 0;
        final int columns = board.get(0).size();

        for (List<Byte> e : board) {
            sum1 += e.get(columns - 1);
            sum2 += e.get(columns - 2);
            sum3 += e.get(columns - 3);
            sum4 += e.get(columns - 4);
            sum5 += e.get(columns - 5);
        }
        final int remove = sum1 + sum2 + sum3 + sum4 + sum5;
        final int add = sum1 + sum2;

        if (add != 0) {
            addRightColumn(1);
        } else if (remove == 0 && board.get(0).size() > MIN_COL){
            removeRightColumn(1);
        }
    }
    
    
    
   /**
    * Adds one row to the bottom if 1 square at the bottom is alive
    * Remove one row if all sqaures on the bottom-row is dead 
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
        final int sum3 = board
                .get(rows -3)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum4 = board
                .get(rows -4)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum5 = board
                .get(rows -5)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int remove = sum1 + sum2 + sum3 + sum4 + sum5;
        final int add = sum1 + sum2;

        if (add != 0) {
            addBottomRow(1);
        } else if(remove == 0 && board.size() > MIN_ROW) {
            removeBottomRow(1);
        }
    }
    /**
     * Board get the information of the new board (nextgeneration ), and the nextgeneration sets back to null
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
     * Adds extra rows to the top. 
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
     * Adds extra columns to the right side. 
     * @param numberOfColumns number of columns you want to add at ones. 
     */
    private void addRightColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            board.stream().forEach((col) -> col.add((byte) 0));
        }
    }
    /**
     * Adds extra columns to the left side. 
     * @param numberOfColumns number of columns you want to add at ones. 
     */
    private void addLeftColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            board.stream().forEach((col) -> col.add(0, (byte) 0));
        }
    }
    /**
     * Adds extra rows to the bottom. 
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
     * Delete a row of the top. 
     * @param numberOfRows number of rows you want to delete at ones. 
     */
    private void removeTopRow(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            board.remove(0);
        }
    }
    /**
     * Delete a Column to the left side. 
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
     * Delete a column to the right side. 
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
     * Delete a row of the bottom. 
     * @param numberOfRows number of rows you want to delete at ones. 
     */
    private void removeBottomRow(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            board.remove(board.size() - 1);
        }
    }


/**
     * Checks if the current cell is alive or not. 
     * @param row row in the baord
     * @param column column in the board
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
     * Column and row is used to navigate where you are placed on the board.
     * @param row row in the board
     * @param column column in the board 
     * @param aliveState either 1 or 0 
     */

    @Override
    public void setCellAliveState(int row, int column, byte aliveState) {
        this.board.get(row).set(column, aliveState);
    }


   /**
     * Generates an emty board in the requierd size.
     * @param row
     * @param col
     */
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
    public void makeBoard(byte[][] byteBoard) {
        this.MIN_ROW = byteBoard.length;
        this.MIN_COL = byteBoard[0].length;

        board = new ArrayList<>();

        for (int rows = 0; rows < MIN_ROW; rows++) {
            board.add(new java.util.ArrayList<>());
            for (int cols = 0; cols < MIN_COL; cols++) {
                board.get(rows).add((byte)byteBoard[rows][cols]);

            }
        }

    }

}
