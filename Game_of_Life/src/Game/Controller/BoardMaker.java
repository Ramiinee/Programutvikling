package Game.Controller;



import Game.Model.Boards.Board;
import Game.Model.Decoders.RLEDecoder;
import Game.Model.MetaData;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Joachim-Privat
 */
public class BoardMaker {


    private Board board;
    private final MetaData metaData;

    private final int numWorkers =  Runtime.getRuntime().availableProcessors();
    private final int[] splitedBoard;
    private final Thread[] workers;
    

    public BoardMaker(MetaData metaData) {
        this.metaData = metaData;
        workers = new Thread[numWorkers];
        splitedBoard = new int[numWorkers];
    }


    /**
     * Generate a clear board where all values are 0.
     * @param Row row in board 
     * @param Col Column in board
     */
    public void makeClearBoard(int Row, int Col){
        board.makeBoard(Row,Col);
    }

   /**
    * Place alive cells, random places on the board. 
    * @param Row Row in board 
    * @param Col column in board
    */
     public void randomBoard(int Row, int Col){
        board.makeBoard(Row,Col);
        int length = board.getRow();
        int splited = length/numWorkers;
        for (int i = 1; i <splitedBoard.length  ; i++) {
            splitedBoard[i-1] = splited * i;
        }
        splitedBoard[splitedBoard.length-1] = board.getRow();
        
        for (int i = 0; i < numWorkers; i++) {
            int start;
            int stop;
            if (i == 0){
                start = 0;
                stop = splitedBoard[i];
            }else{
                start = splitedBoard[i-1];
                stop = splitedBoard[i];
            }

            workers[i] = new Thread(RandomRun(start,stop ));

        }
       for (Thread worker : workers) {
            worker.start();
        }

        try {
             for (Thread worker : workers) {
            worker.join();
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
      private Thread RandomRun(int start, int stop){
        Random r = new Random();
        for (int row = start; row <stop ; row++) {
            for (int col = 0; col <board.getColumn() ; col++) {
                int a = r.nextInt(2);
                board.setCellAliveState(row,col,(byte)a);
            }
        }
        return new Thread() ;
    }

    /**
     * Takes reader and passes it on to decoding.
     * @param reader from Url or local file. 
     * @throws IOException if reading fails.
     */
    public void InsertRleIntoBoard( BufferedReader reader) throws IOException{
        RLEDecoder decoder = new RLEDecoder(reader, board, metaData);
        decoder.decode();
        
    }   
    public void setBoardType(Board board) {
        this.board = board;
    }
}
