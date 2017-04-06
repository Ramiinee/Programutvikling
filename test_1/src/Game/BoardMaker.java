package Game;



import Game.StaticBoard;
import java.util.Random;
import java.util.regex.*;

/**
 *
 * @author Joachim-Privat
 */
public class BoardMaker {


    public StaticBoard staticBoard;
    private byte[] byteArray;
    private byte[][] board;
    private byte[][] converted;
    private byte[][] clearBoard;
    private int testSize = 121;
    /**
     *
     * @param staticBoard
     */
    public BoardMaker(StaticBoard staticBoard) {
        this.staticBoard = staticBoard;
    }

    /**
     * Her generer vi ett klart brett hvor alle verider er 0.
     */
    public void makeClearBoard(){
        clearBoard= new byte[testSize][testSize];
        staticBoard.setBoard(clearBoard);
    }

    /**
     * Her genrerer vi ett random brett
     */
    public void randomPattern(){
        board = new byte[testSize][testSize];
        Random r = new Random();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length ; j++) {
                int a = r.nextInt(2);
                if ( a== 0){
                    board[i][j] = 1;
                }else {
                    board[i][j] = 0;
                }
            }
        }
        staticBoard.setBoard(board);
    }

    /**
     * Her setter vi converted til å bli board.
     * @param board
     * @param converted
     * @return
     * ett konvertert brett
     */
    public byte[][] boardConvertedTXT (byte[][] board,byte[][] converted){
        for (int i = 0; i <board.length ; i++) {
            for (int j = 0; j <board[i].length ; j++) {
                board[i][j] = converted[i][j];
                if (board[i][j] == 48){
                    board[i][j] = 0;
                }
                else {
                    board[i][j]= 1;
                }
            }
        }
        return board;
    }

    /**
     * Her blir .rle fila omgjort til ett board som kan vises.
     * .rle fila er komposert av o,b og $, hvor o er 1 og b er 0. $ er ny linje.
     * vi gjør om den komprimerte stringen (.rle) om til 0 og 1 og gjennom decode så får vi decomprimert den.
     * @param everything
     */
    public void InsertRleIntoBoard(  String everything){
        board = staticBoard.getBoard();
        int column = staticBoard.getColumn();
        int row = staticBoard.getRow();
        System.out.println(column + " | " + row);

        try {
            String[] dd = everything.split(Pattern.quote("$"));
            System.out.println(dd.length);
            String k = "";
            for (int i = 0; i <dd.length ; i++) {
                dd[i] = decode(dd[i]);
                if (dd[i].length() < staticBoard.getRow()){
                    for (int j = 0; j <= (staticBoard.getRow() - dd[i].length()); j++) {
                        dd[i] += "b";
                    }
                }
                k += dd[i];
            }
            if (k.length() != row*column){
                k+="b";
            }
            k = k.replaceAll("b","0");
            k = k.replaceAll("o","1");
            byteArray = k.getBytes();
            System.out.println(byteArray.length);
            try {
                Convert1DTo2D(row,column);
            }catch (Exception e){
                System.out.println(e + " Convertering fra 1D til 2D gikk ikke som planlagt");
            }
            for (int i = 0; i < converted.length ; i++) {
                for (int j = 0; j < converted[i].length ; j++) {
                    if (converted[i][j] == 48){
                        converted[i][j] = 0;
                    }
                    else {
                        converted[i][j]= 1;
                    }
                    board[i+50][j+50] = converted[i][j];
                }
            }
            for (int i = 0; i < converted.length; i++) {
                for (int j = 0; j < converted[i].length; j++) {
                    if (converted[j][i] == 0){
                        System.out.print("-");
                    }
                    else System.out.print(converted[j][i]);
                }
                System.out.println();
            }
            staticBoard.setBoard(board);
        }catch (Exception e){
            System.out.println(e + " | Det går ikke å legge rle inn i array");
        }
    }

    /**
     * Her blir en komprimert string, decomprimert med satte regler om at bokstaven etter ett tall blir lagt på ant. ganger det tallet er. 3o = ooo.
     * Er det ikke noe tall forran så blir det bare lagret bokstaven selv.
     * @param source
     * @return
     */
    public String decode(String source) {

        StringBuffer dest = new StringBuffer();
        Pattern pattern = Pattern.compile("[0-99]+|[a-zA-Z]|$");
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            //System.out.println(dest);
            try {
                int number = Integer.parseInt(matcher.group());
                matcher.find();
                while (number-- != 0) {
                    dest.append(matcher.group());
                }
            }catch (Exception e){
                dest.append(matcher.group());
            }
        }
        return dest.toString();
    }

    /**
     * Her får vi inn en .txt fil og konverterer den til ett brett.
     * Vi sjkeker også om brettet er kvadratisk for å få matten til å gå.
     * @param everything
     * @param staBoard
     * @return
     */
    public boolean FileToBoardSquare(String everything, StaticBoard staBoard){
        staticBoard = staBoard;
        byteArray = everything.getBytes();
        System.out.println(byteArray.length + " | " + Math.sqrt(byteArray.length ));
        int sqrt = (int) Math.sqrt(byteArray.length);
        if (sqrt * sqrt == byteArray.length) {
            int a = sqrt;
            staticBoard.setColumn(a);
            staticBoard.setRow(a);
            board = new byte[staticBoard.getColumn()][staticBoard.getRow()];
            Convert1DTo2D(staticBoard.getColumn(), staticBoard.getRow());
            board = boardConvertedTXT(board, converted);
            staticBoard.setBoard(board);
            return true;
        } else {
            System.out.println("Ikke kvadratisk");
            return false;
        }
    }

    /**
     * Her konverterer vi ett 1D brett til ett 2D brett, med hjelp av ant. kolloner og rader.
     * @param rows
     * @param cols
     */
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
}
