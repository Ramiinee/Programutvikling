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
    private byte[][] converted;

    public BoardMaker(MetaData metaData) {
        this.metaData = metaData;
    }


    /**
     * Her generer vi ett klart brett hvor alle verider er 0.
     */
    public void makeClearBoard(int Row, int Col){

        board.makeBoard(Row,Col);
    }

    /**
     * Her genrerer vi ett random brett
     */
    public void randomPattern(int Row, int Col){
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



    @Deprecated
    public boolean FileToBoardSquare(String everything){
        byteArray = everything.getBytes();
        System.out.println(byteArray.length + " | " + Math.sqrt(byteArray.length ));
        int sqrt = (int) Math.sqrt(byteArray.length);
        if (sqrt * sqrt == byteArray.length) {
            board.setColumn(sqrt);
            board.setRow(sqrt);

            Convert1DTo2D(board.getColumn(), board.getRow());
            boardConvertedTXT(converted);

            return true;
        } else {
            System.out.println("Ikke kvadratisk");
            return false;
        }
    }


    @Deprecated
    private void boardConvertedTXT(byte[][] converted){
        for (int i = 0; i <board.getRow() ; i++) {
            for (int j = 0; j <board.getColumn() ; j++) {

                if (converted[i][j] == 48){
                    converted[i][j] = 0;
                }
                else {
                    converted[i][j]= 1;
                }
                board.setCellAliveState(i,j,converted[i][j]);
            }
        }
    }
    @Deprecated
    private void Convert1DTo2D(int rows, int cols){

        converted = new byte[rows][cols];
        for (int i = 0; i < converted.length; i++) {
            for (int j = 0; j < converted[i].length; j++) {
                //System.out.println("index" + ((i * arr.length) + j));
                converted[i][j] = byteArray[(i * converted[i].length) + j];
                if (converted[i][j] == 48){
                    System.out.print("  -");
                }else {
                    System.out.print("  " + converted[i][j]);
                }
            }
            System.out.println();
        }
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
