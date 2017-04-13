package Game;

import Game.StaticBoard;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoardMaker {
    public StaticBoard staticBoard;
    private byte[] byteArray;
    private byte[][] board;
    private byte[][] converted;
    private byte[][] clearBoard;
    private int testSize = 200;

    public BoardMaker(StaticBoard staticBoard) {
        this.staticBoard = staticBoard;
    }

    public void makeClearBoard(){
        clearBoard= new byte[testSize][testSize];
        staticBoard.setBoard(clearBoard);
    }
    public void randomPattern(StaticBoard staBoard){
        staticBoard = staBoard;
        board = new byte[testSize][testSize];


        Random r = new Random();

        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length ; row++) {
                int a = r.nextInt(2);

                if ( a== 0){
                    board[col][row] = 1;
                }else {
                    board[col][row] = 0;
                }

            }
        }
        staticBoard.setBoard(board);

    }

    public byte[][] boardConvertedTXT (byte[][] board,byte[][] coverted){

        for (int col = 0; col <board.length ; col++) {
            for (int row = 0; row <board[col].length ; row++) {
                board[col][row] = coverted[col][row];
                if (board[col][row] == 48){
                    board[col][row] = 0;
                }
                else {
                    board[col][row]= 1;
                }
            }
        }
        return board;
    }
    public void InsertRleIntoBoard(  String everything){
        board = staticBoard.getBoard();
        int column = staticBoard.getColumn();
        int row = staticBoard.getRow();
        System.out.println(column + " | " + row);
        try {
            String s = decode(everything);
            String start = s;
            s = s.replaceAll("b","0");
            s = s.replaceAll("o","1");

            if (s.length() != (column*row)){
                int i;
                for (i = 0; i <((column*row)-(start.length())) ; i++) {
                    s = s + "0";

                }
                System.out.println(i + " 0 lagt til");
                System.out.println(s);

            }

            byteArray = s.getBytes();


            try {
                Convert1DTo2D(row,column);
            }catch (Exception e){
                System.out.println(e + " Convertering fra 1D til 2D gikk ikke som planlagt");
            }

            for (int col = 0; col < converted.length ; col++) {
                for (int Row = 0; Row < converted[col].length ; Row++) {
                    if (converted[col][Row] == 48){
                        converted[col][Row] = 0;
                    }
                    else {
                        converted[col][Row]= 1;
                    }

                    board[col+50][Row+50] = converted[col][Row];

                }

            }

            for (int col = 0; col < converted.length; col++) {
                for (int Row = 0; Row < converted[col].length; Row++) {
                    if (converted[Row][col] == 0){
                        System.out.print("-");
                    }
                    else System.out.print(converted[Row][col]);
                }
                System.out.println();
            }


            staticBoard.setBoard(board);
        }catch (Exception e){
            System.out.println(e + " | Det går ikke å legge rle inn i array");
        }
    }

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

        System.out.println(dest.toString());
        System.out.println(dest.length());
        return dest.toString();
    }


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


    public void Convert1DTo2D(int rows, int cols){
        converted = new byte[rows][cols];

        for (int col = 0; col < converted.length; col++) {
            for (int row = 0; row < converted[col].length; row++) {
                //System.out.println("index" + ((i * arr.length) + j));
                converted[col][row] = byteArray[(col * converted[col].length) + row];
                System.out.print("  " + converted[col][row]);

            }
            System.out.println();
        }
    }
}
