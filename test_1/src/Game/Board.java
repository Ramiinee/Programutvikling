package Game;



import javafx.stage.FileChooser;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;


public class Board extends Controller {
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

    public  void copyFileBufferedIO() {
        fileChooser();

        try {
            System.out.println(byteArray.length);
            int a;
            int sqrt = (int) Math.sqrt(byteArray.length);

            if(sqrt*sqrt == byteArray.length) {

                a = (int) Math.sqrt(byteArray.length);
                kolonner = a;
                rader = a;
                board = new byte[a][a];

                Con1Dto2D(byteArray,a,a);
                try {
                    board = boardConverted();
                }catch (Exception e){
                    System.out.println(e + " feil_2");
                }
            }
            else {
                System.out.println("ingen kravdrat array");

            }

        }catch (Exception e){
            System.out.println(e + "3");

        }


    }
    public void fileChooser(){

        File recordsDir = new File(System.getProperty("user.dir"));
        recordsDir.mkdirs();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(recordsDir);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();


                while (line != null) {
                    sb.append(line);

                    line = br.readLine();
                }
                String everything = sb.toString();


                byteArray = everything.getBytes();




            } catch (IOException e) {
                System.out.println(e + "2");
            }

        }
    }
    public void Con1Dto2D(  byte[] array,  int rows,  int cols ) {
        if (array.length != (rows*cols))
            throw new IllegalArgumentException("Invalid array length");

        coverted = new byte[rows][cols];
        for ( int i = 0; i < rows; i++ )
            System.arraycopy(array, (i*cols), coverted[i], 0, cols);


    }
    public byte[][] boardConverted (){
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
