package Game.Controller;



import Game.Model.Boards.Board;
import Game.Model.Decoders.Decoder;
import Game.Model.Decoders.RLEDecoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Joachim-Privat
 */
public class BoardMaker {


    private Board board;
    private byte[] byteArray;
    private Decoder decoder;
    private byte[][] converted;




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
        Random r = new Random();
        for (int row = 0; row <board.getRow() ; row++) {
            for (int col = 0; col <board.getColumn() ; col++) {
                int a = r.nextInt(2);
                board.setCellAliveState(row,col,(byte)a);
            }
        }

    }



    /**
     * Her får vi inn en .txt fil og konverterer den til ett brett.
     * Vi sjkeker også om brettet er kvadratisk for å få matten til å gå.
     * @param everything
     * @return
     */
    public boolean FileToBoardSquare(String everything){
        byteArray = everything.getBytes();
        System.out.println(byteArray.length + " | " + Math.sqrt(byteArray.length ));
        int sqrt = (int) Math.sqrt(byteArray.length);
        if (sqrt * sqrt == byteArray.length) {
            int a = sqrt;
            board.setColumn(a);
            board.setRow(a);

            Convert1DTo2D(board.getColumn(), board.getRow());
            boardConvertedTXT(converted);

            return true;
        } else {
            System.out.println("Ikke kvadratisk");
            return false;
        }
    }


    /**
     * Her setter vi converted til å bli board.
     * @param converted
     * @return
     * ett konvertert brett
     */
    public void boardConvertedTXT (byte[][] converted){
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
    public void Convert1DTo2D(int rows, int cols){

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
        decoder = new RLEDecoder(reader,board);
        decoder.decode();
        return true;
    }


    public void setBoardType(Board board) {
        this.board = board;
    }
}
