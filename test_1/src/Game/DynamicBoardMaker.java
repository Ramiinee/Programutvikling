package Game;

import Game.StaticBoard;
import java.util.ArrayList;
import java.util.List;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicBoardMaker {
    public DynamicBoard dynamicBoard;
    private byte[] byteArray;
    public byte[][] board = new byte[60][60];
    ArrayList<List<Byte>> arraylist = new ArrayList<>();
    public byte[][] coverted;
    ArrayList<List<Byte>> clearBoard;

    public DynamicBoardMaker(DynamicBoard dynamicBoard) {
        this.dynamicBoard = dynamicBoard;
    }

    public void makeClearBoard(){

        dynamicBoard.setBoard(clearBoard);
    }
    public void randomPattern(DynamicBoard staBoard){
        dynamicBoard = staBoard;



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
        dynamicBoard.setBoard(arraylist);

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
        arraylist = dynamicBoard.getarrayList();
        int kolonner = dynamicBoard.getKolonner();
        int rader = dynamicBoard.getRader();
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


            dynamicBoard.setBoard(arraylist);
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


    public boolean FileToBoardSquare(String everything, DynamicBoard staBoard){
        dynamicBoard = staBoard;
        byteArray = everything.getBytes();
        System.out.println(byteArray.length + " | " + Math.sqrt(byteArray.length ));
        int sqrt = (int) Math.sqrt(byteArray.length);

        if (sqrt * sqrt == byteArray.length) {
            int a = sqrt;
            dynamicBoard.setKolonner(a);
            dynamicBoard.setRader(a);

            board = new byte[dynamicBoard.getKolonner()][dynamicBoard.getRader()];

            Convert1DTo2D(dynamicBoard.getKolonner(), dynamicBoard.getRader());
            board = boardConvertedTXT(board,coverted);
            dynamicBoard.setBoard(arraylist);
            
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
