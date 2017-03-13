package Game;

public abstract class Board{
   public int Column;
   public int Row;

   public int getColumn() {
      return Column;
   }

   public void setColumn(int column) {
      Column = column;
   }

   public int getRow() {
      return Row;
   }

   public void setRow(int row) {
      Row = row;
   }
}
