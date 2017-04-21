package Game.Controller;



import Game.model.Boards.Board;
import Game.model.Boards.StaticBoard;
import Game.model.Decoders.Decoder;
import Game.model.Decoders.RLEDecoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;
import java.util.regex.*;

/**
 *
 * @author Joachim-Privat
 */
public class BoardMaker {


    private Board staticBoard;
    private Board dynamicBoard;
    private byte[] byteArray;
    private Decoder decoder;
    private byte[][] converted;
    private byte[][] clearBoard;


    /**
     *
     * @param staticBoard
     */
    public BoardMaker(Board staticBoard, Board dynamicBoard) {
        this.staticBoard = staticBoard;
        this.dynamicBoard = dynamicBoard;
    }

    /**
     * Her generer vi ett klart brett hvor alle verider er 0.
     */
    public void makeClearBoard(){
        staticBoard.resetBoard();
    }

    /**
     * Her genrerer vi ett random brett
     */
    public void randomPattern(){
        Random r = new Random();
        for (int row = 0; row <staticBoard.getRow() ; row++) {
            for (int col = 0; col <staticBoard.getColumn() ; col++) {
                int a = r.nextInt(2);
                staticBoard.setCellAliveState(row,col,(byte)a);
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
            staticBoard.setColumn(a);
            staticBoard.setRow(a);

            Convert1DTo2D(staticBoard.getColumn(), staticBoard.getRow());
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
        for (int i = 0; i <staticBoard.getRow() ; i++) {
            for (int j = 0; j <staticBoard.getColumn() ; j++) {

                if (converted[i][j] == 48){
                    converted[i][j] = 0;
                }
                else {
                    converted[i][j]= 1;
                }
                staticBoard.setCellAliveState(i,j,converted[i][j]);
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
        decoder = new RLEDecoder(reader,staticBoard);
        decoder.decode();
        return true;
    }


    /**
     * Her konverterer vi ett 1D brett til ett 2D brett, med hjelp av ant. kolloner og rader.
     * @param rows
     * @param cols
     */



}
