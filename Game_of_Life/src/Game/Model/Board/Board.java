package Game.model.Boards;



public abstract class Board{

   public abstract int getColumn();

   public abstract void setColumn(int column);

   public abstract int getRow();

   public abstract void setRow(int row);

   public abstract void nextGeneration();
   public abstract void slowlyCover();
   public abstract void noDeadCellsRule();
   protected abstract int countNeighbor(int col, int row);
   public abstract byte getCellAliveState(int row, int column);
   public abstract void setCellAliveState(int row, int column, byte aliveState);
   public abstract void resetBoard();

   public enum BoardType {
      FIXED,
      DYNAMIC
   }


}
