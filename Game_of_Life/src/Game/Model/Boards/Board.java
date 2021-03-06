package Game.Model.Boards;


import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author jo-be_000
 */
public abstract class Board{

    /**
     * Gets Column
     * @return Column
     */
    public abstract int getColumn();

    /**
     * Sets Column
     * @param column
     */
    public abstract void setColumn(int column);

    /**
     * Gets Row
     * @return
     */
    public abstract int getRow();

    /**
     * sets Row
     * @param row
     */
    public abstract void setRow(int row);
   
    /**
     * The rules of Game of life. 
     * decide whats alive or not in the next generation
     * scanns game from int Start to Int stop. 
     * @param start The start position of the board segment.
     * @param stop The end position of the board segment.
     * @param cyclicBarrier  The cyclic barrier.
     */
    public abstract void nextGeneration(int start, int stop, CyclicBarrier cyclicBarrier);

    /**
     * Slowly cover Rules. 
     * decide whats alive or not in the next generation
     * scanns game from int Start to Int stop. 
     * @param start The start position of the board segment.
     * @param stop The end position of the board segment.
     * @param cyclicBarrier  The cyclic barrier.
     */
    public abstract void slowlyCover(int start, int stop, CyclicBarrier cyclicBarrier);

    /**
     * No dead cell rule. det same as nextGeneration method, except that alive cells doesnt die. 
     * decide which cell who comes to life
     * scanns game from int Start to Int stop.
     * @param start The start position of the board segment.
     * @param stop The end position of the board segment.
     * @param cyclicBarrier  The cyclic barrier.
     */
    public abstract void noDeadCellsRule(int start, int stop, CyclicBarrier cyclicBarrier);

    /**
     * Takes a row and col value, and determines the amount of neighbours.
     * @param col Column
     * @param row Row
     * @return the amount of neighbours. 
     */
    protected abstract int countNeighbor(int col, int row);

    /**
     * Gets the cells alive state.
     * @param row Row
     * @param column Column
     * @return the state of the cell.
     */
    public abstract byte getCellAliveState(int row, int column);

   /**
     * Sets the cells alive state.
     * @param row Row
     * @param column Column
     */
    public abstract void setCellAliveState(int row, int column, byte aliveState);

    /**
     * Generates an emty board in the requierd size.
     * @param row
     * @param col
     */
    public abstract void makeBoard(int row, int col);
    /**
     * Generates an emty board in the requierd size.
     * @param byteboard.
     */
    public abstract void makeBoard(byte[][] byteBoard);
   
    
    /**
     * Sets the board to the newest value
     */
    public abstract void setBoard();

    /**
     * Generate nextGeneration
     */
    public abstract void makeNextGenArray();


}
