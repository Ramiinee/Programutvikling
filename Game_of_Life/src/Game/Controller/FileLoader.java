package Game.Controller;



import javafx.stage.FileChooser;
import java.io.*;
import java.net.URL;


public class FileLoader {

    private BoardMaker boardMaker;

    public FileLoader(BoardMaker boardMaker) {
        this.boardMaker = boardMaker;
    }
    /**
     * Generates a reader from url imput.
     * The reader gets passed on to decoding.
     * @param url from userinput
     * @return true if everything went ok, false if something went wrong.
     */
    public boolean ReadFromUrl(String url) {
        try {
            URL oracle = new URL(url);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));
            boardMaker.InsertRleIntoBoard(reader); 
            reader.close();
            return true;
        }catch (IOException e){
            System.out.println("Not an valid Url");
            return false;
        }

    }
    /**
     * Opens opp a Filechooser and makes a reader.
     * The reader gets passed on to decoding.
     * @return true if everything went ok, false if something went wrong.
     */
   
    public boolean ReadFromFile(){
        File selectedFile;
        BufferedReader reader;
        try {
            File recordsDir = new File(System.getProperty("user.dir"));
            recordsDir.mkdirs();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(recordsDir);
            FileChooser.ExtensionFilter fileExtensions =
                    new FileChooser.ExtensionFilter(
                        "Patern type", "*.rle");//, "*.txt"
            fileChooser.getExtensionFilters().add(fileExtensions);
            selectedFile = fileChooser.showOpenDialog(null);
        }catch (NullPointerException e) {
            System.out.println(e + " | Velger fil");
            return false;
        }
        if (selectedFile != null) {
            try {
                reader = new BufferedReader(new FileReader(selectedFile));
                boardMaker.InsertRleIntoBoard(reader); // her kommer det en funksjon som konverterter rle fil til array.
                reader.close();
                return true;
            } catch (IOException e) {
                System.out.println(e + " | Reader failed");
                return false;
            }
        }
        return false;
    }


}
