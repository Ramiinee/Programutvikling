package Game.Controller;



import Game.Model.Boards.Board;
import Game.Model.Decoders.RLEDecoder;
import Game.Model.MetaData;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Joachim-Privat
 */
public class BoardMaker {


    private Board board;
    private MetaData metaData;
    private byte[] byteArray;
    

    public BoardMaker(MetaData metaData) {
        this.metaData = metaData;
    }



    /**
     * generate a clear board where all values are 0.
     * @param Row row in board 
     * @param Col Column in board
     */
    public void makeClearBoard(int Row, int Col){
        board.makeBoard(Row,Col);
    }

   /**
    * place alivecell on random places in board. 
    * @param Row Row in board 
    * @param Col column in board
    */
     public void randomBoard(int Row, int Col){
        board.makeBoard(Row,Col);
        int start1 = Row/4;
        int stop1 = Row/4 + start1;
        int stop2 = Row/4 + start1 +start1;
        Thread t = new Thread(test(0,start1));
        Thread t1 = new Thread(test(start1,stop1));
        Thread t2 = new Thread(test(stop1,stop2));
        Thread t3 = new Thread(test(stop2,Row));

        t.start();
        t1.start();
        t2.start();
        t3.start();

        try {
            t.join();
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
      private Thread test(int start, int stop){
        Random r = new Random();
        for (int row = start; row <stop ; row++) {
            for (int col = 0; col <board.getColumn() ; col++) {
                int a = r.nextInt(2);
                board.setCellAliveState(row,col,(byte)a);
            }
        }
        return new Thread() ;
    }


    public boolean InsertRleIntoBoard( BufferedReader reader) throws IOException{
        RLEDecoder decoder = new RLEDecoder(reader, board, metaData);
        decoder.decode();
        return true;
    }


    public void setBoardType(Board board) {
        this.board = board;
    }
}
