package Game;


import Game.Controller;

import Game.Waring;
import javafx.stage.FileChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Board extends Controller {
    public int kolonner;
    public int rader ;
    public byte[] byteArray;
    //public int[][] board = new int[kolonner][rader];
    public byte[][] board;
    public byte[][] clearBoard;
    private byte[][] coverted;

    private String[] strings;

    private boolean rleFile;
    private String rleFileName; // #N er navnet. Hvis filen har navn vise det i topp baren?

    private String everything;
    private Waring w = new Waring();

    public void randomPattern(){
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

    }
    public byte[][] makeClearBoard(){
        return clearBoard = new byte[60][60];
    }
    public byte[][] getBoard(){
        return board;
    }

    public int getKolonner() {
        return kolonner;
    }

    public int getRader() {
        return rader;
    }

    public  boolean FromFileToBoard() {
        rleFile = false;
        boolean ruleBoard = fileChooser();
        if (ruleBoard) {
            if (rleFile){
                System.out.println("Rle file = " + rleFile);
                InsertRleIntoBoard(); // her kommer det en funksjon som konverterter rle fil til array.
                return true;
            }
            else {
                System.out.println("Rle file = " + rleFile);
                return FileToBoardSquare(); //denne tar en .txt fil og gjør den til en 2d array.
            }
        }
        else return false;
    }
    public void InsertRleIntoBoard(){
        kolonner = 8;
        rader = 8;
        board = new byte[60][60];
        try {
            String s = decode(everything);
            String start = s;
            s = s.replaceAll("b","0");
            s = s.replaceAll("o","1");

            if (s.length() != (kolonner*rader)){
                int i;
                for (i = 0; i <((kolonner*rader)-(start.length())) ; i+=2) {
                    s = "0" +s + "0";
                }

            }

            byteArray = s.getBytes();


            try {
                Con1Dto2D(byteArray, kolonner, rader);

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
                    System.out.print(coverted[j][i]);
                }
                System.out.println();
            }



        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static String decode(String source) {

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


    public boolean FileToBoardSquare(){
        byteArray = everything.getBytes();
        System.out.println(byteArray.length + " | " + Math.sqrt(byteArray.length ));
        int sqrt = (int) Math.sqrt(byteArray.length);

        if (sqrt * sqrt == byteArray.length) {
            int a = sqrt;
            kolonner = a;
            rader = a;
            board = new byte[kolonner][rader];

            Con1Dto2D(byteArray, kolonner, rader);
            board = boardConvertedTXT();

            return true;
        } else {
            System.out.println("Ikke kvadratisk");
            return false;
        }
    }
    public boolean fileChooser(){

        File selectedFile = null;
        try {
            File recordsDir = new File(System.getProperty("user.dir"));
            recordsDir.mkdirs();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(recordsDir);
            FileChooser.ExtensionFilter fileExtensions =
                    new FileChooser.ExtensionFilter(
                            "Patern types", "*.rle", "*.txt");
            fileChooser.getExtensionFilters().add(fileExtensions);

            selectedFile = fileChooser.showOpenDialog(null);

        }catch (NullPointerException e){
            System.out.println(e);
        }


        if (selectedFile != null) {
            try{
                BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    if (line.startsWith("#")) {
                        System.out.println(line); // for testing
                    }
                    else if (line.startsWith("x")){
                        System.out.println(line); // for testing
                        line = line.replaceAll("\\s+", "");
                        String REGEX = "x=[\\d]+,y=[\\d]+,rule=B3/S23";
                        Pattern p = Pattern.compile(REGEX);
                        Matcher m = p.matcher(line);
                        if(m.find()) {
                            line = line.replaceAll(",rule=[a-zA-Z0-9]+/[a-zA-Z0-9]+", "");
                            System.out.println(line);  // for testing
                        }
                        else {
                            Waring w = new Waring();
                            w.warning("Wrong Rule","The file you selected does not have the correct rule","This program uses rule = B3/S23. It will still work, but not the way the file is made for.");
                        }
                    }
                    else {
                        sb.append(line);
                    }

                    line = br.readLine();
                }

                everything  = sb.toString();


            } catch (IOException e) {
                System.out.println(e + "2");
            }
            rleFile = isRleFile(selectedFile);

            return true;
        }
        else {
            return false;
        }

    }
    public boolean isRleFile( File selectedFile ){
        String REGEX = "[a-zA-Z0-9]+.rle";
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(selectedFile.getName());
        if(m.find()) {
            return true;
        }
        return false;
    }
    public void Con1Dto2D(  byte[] array,  int rows,  int cols ) {
        if (array.length != (rows*cols)) throw new IllegalArgumentException("Invalid array length");

        coverted = new byte[rows][cols];
        for ( int i = 0; i < rows; i++ ) System.arraycopy(array, (i*cols), coverted[i], 0, cols);

    }
    public byte[][] boardConvertedTXT (){
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

}
