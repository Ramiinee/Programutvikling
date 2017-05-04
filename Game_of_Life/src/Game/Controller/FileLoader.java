package Game.Controller;



import javafx.stage.FileChooser;
import java.io.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Joachim
 */
public class FileLoader {

    private BoardMaker boardMaker;

    public FileLoader(BoardMaker boardMaker) {
        this.boardMaker = boardMaker;
    }

    public boolean ReadFromUrl(String url) {
        try {
            URL oracle = new URL(url);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));
            boardMaker.InsertRleIntoBoard(reader); // her kommer det en funksjon som konverterter rle fil til array.
            reader.close();
            return true;
        }catch (IOException e){
            System.out.println("Not an valid Url");
            return false;
        }

    }
    /**
     * Her henter jeg inn fila og direkte fra mappa programmet ligger i. Her er det satt en begrensning om at du bare kan velge .rle og .txt filer.
     * Videre så leser vi over fila og henter det vi er interisert i.
     * Til slutt så blir det lagt til ekstra rader der det er nødvendig.
     * @return if board is loaded
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
                boolean inserted = boardMaker.InsertRleIntoBoard(reader); // her kommer det en funksjon som konverterter rle fil til array.
                reader.close();
                return inserted;
            } catch (IOException e) {
                System.out.println(e + " | Reader failed");
                return false;
            }
        }
        return false;
    }


    @Deprecated
    private boolean isRleFile(File selectedFile){
        String REGEX = "[a-zA-Z0-9]+.rle";
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(selectedFile.getName());
        return m.find();
    }




}
