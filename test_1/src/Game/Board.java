package Game;


import javafx.stage.FileChooser;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Iterator;
import java.util.Random;


public class Board extends Controller{
    public int kolonner;
    public int rader ;
    public byte[] byteArray;
    //public int[][] board = new int[kolonner][rader];
    public byte[][] board;
    private byte[][] coverted;

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
    public byte[][] getBoard(){
        return board;
    }

    public int getKolonner() {
        return kolonner;
    }

    public int getRader() {
        return rader;
    }

    public  void copyFileBufferedIO()
    {
        Path infile = Paths.get(chooseTextFile());

        try {
            byteArray = copyFileOneGo(infile);

        }catch (IOException ioe){}
        try {
            System.out.println(byteArray.length);
            int a;
            if ((Math.sqrt(byteArray.length)%2 == 0)){

                a = (int) Math.sqrt(byteArray.length);
                kolonner = a;
                rader = a;
                board = new byte[kolonner][rader];

                    Con1Dto2D(byteArray,a,a);


                try {
                    board = boardConverted();
                }catch (Exception e){
                    System.out.println(e + " feil_2");
                }
            }
            else {
                System.out.println("ingen kravdrat array");
                stage.close();
            }



        }catch (Exception e){
            System.out.println(e);

        }

    }
    public byte[][] boardConverted (){
        for (int i = 0; i <board.length ; i++) {
            for (int j = 0; j <board[i].length ; j++) {
               board[i][j] = coverted[i][j];
            }
        }
        return board;
    }
    public void Con1Dto2D(  byte[] array,  int rows,  int cols ) {
        if (array.length != (rows*cols))
            throw new IllegalArgumentException("Invalid array length");

        coverted = new byte[rows][cols];
        for ( int i = 0; i < rows; i++ )
            System.arraycopy(array, (i*cols), coverted[i], 0, cols);


    }

    private String chooseTextFile() {

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text files", "txt");
        chooser.addChoosableFileFilter(filter);




        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile().getAbsoluteFile().toString();
        else
            return "";

    }

    public  byte[] copyFileOneGo(Path infile) throws IOException
    {
        byte[] Array = Files.readAllBytes(infile);

        for (int i = 0; i < Array.length; i++) {
            if (Array[i] == 49) Array[i] = 1;
            else Array[i] = 0;

        }

        return Array;
    }
}
