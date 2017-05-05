package Game.Model.Boards;


import java.util.concurrent.CyclicBarrier;

public abstract class Board{

   public abstract int getColumn();

   public abstract void setColumn(int column);

   public abstract int getRow();

   public abstract void setRow(int row);
   
   //abstract methods
   public abstract void nextGeneration(int start, int stop, CyclicBarrier cyclicBarrier);
   public abstract void slowlyCover(int start, int stop, CyclicBarrier cyclicBarrier);
   public abstract void noDeadCellsRule(int start, int slutt, CyclicBarrier cyclicBarrier);
   protected abstract int countNeighbor(int col, int row);
   public abstract byte getCellAliveState(int row, int column);
   public abstract void setCellAliveState(int row, int column, byte aliveState);
   public abstract void makeBoard(int row, int col);
   
   public abstract void setBoard();
   public abstract void makeNextGenArray();


}
