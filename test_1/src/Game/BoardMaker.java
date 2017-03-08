package Game;

import Game.StaticBoard;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoardMaker {
    public StaticBoard staticBoard;
    private byte[] byteArray;
    private byte[][] board;
    private byte[][] coverted;
    private byte[][] clearBoard;

    public BoardMaker(StaticBoard staticBoard) {
        this.staticBoard = staticBoard;
    }

    public void makeClearBoard(){
        clearBoard= new byte[60][60];
        staticBoard.setBoard(clearBoard);
    }
    public void randomPattern(StaticBoard staBoard){
        staticBoard = staBoard;
        board = new byte[60][60];


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

    public byte[][] boardConvertedTXT (byte[][] board,byte[][] coverted){

        for (int i = 0; i <board.length ; i++) {
            for (int j = 0; j <board[i].length ; j++) {
                board[i][j] = coverted[i][j];
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
    public void InsertRleIntoBoard(  String everything){
        board = staticBoard.getBoard();
        int kolonner = staticBoard.getKolonner();
        int rader = staticBoard.getRader();
        System.out.println(kolonner + " | " + rader);
        try {
            String s = decode(everything);
            String start = s;
            s = s.replaceAll("b","0");
            s = s.replaceAll("o","1");

            if (s.length() != (kolonner*rader)){
                int i;
                for (i = 0; i <((kolonner*rader)-(start.length())) ; i++) {
                    s = s + "0";

                }
                System.out.println(i + " 0 lagt til");
                System.out.println(s);

            }

            byteArray = s.getBytes();


            try {
                Convert1DTo2D(rader,kolonner);
            }catch (Exception e){
                System.out.println(e + " 44");
            }

            for (int i = 0; i <coverted.length ; i++) {
                for (int j = 0; j <coverted[i].length ; j++) {
                    if (coverted[i][j] == 48){
                        coverted[i][j] = 0;
                    }
                    else {
                        coverted[i][j]= 1;
                    }

                    board[i+10][j+10] = coverted[i][j];

                }

            }

            for (int i = 0; i < coverted.length; i++) {
                for (int j = 0; j < coverted[i].length; j++) {
                    if (coverted[j][i] == 0){
                        System.out.print("-");
                    }
                    else System.out.print(coverted[j][i]);
                }
                System.out.println();
            }


            staticBoard.setBoard(board);
        }catch (Exception e){
            System.out.println(e + " InsertRleIntoBoard");
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
            staticBoard.setKolonner(a);
            staticBoard.setRader(a);

            board = new byte[staticBoard.getKolonner()][staticBoard.getRader()];

            Convert1DTo2D(staticBoard.getKolonner(), staticBoard.getRader());
            board = boardConvertedTXT(board,coverted);
            staticBoard.setBoard(board);
            return true;
        } else {
            System.out.println("Ikke kvadratisk");
            return false;
        }
    }


    public void Convert1DTo2D(int rows, int cols){
        coverted = new byte[rows][cols];

        for (int i = 0; i < coverted.length; i++) {
            for (int j = 0; j < coverted[i].length; j++) {
                //System.out.println("index" + ((i * arr.length) + j));
                coverted[i][j] = byteArray[(i * coverted[i].length) + j];
                System.out.print("  " + coverted[i][j]);

            }
            System.out.println();
        }
    }
}
