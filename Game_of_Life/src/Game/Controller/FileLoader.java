package Game.Controller;


import Game.model.Boards.Board;
import Game.model.Boards.StaticBoard;
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


    public BoardMaker boardMaker;
    private boolean rleFile = false;



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
            e.printStackTrace();
            return false;
        }

    }
    /**
     * Her henter jeg inn fila og direkte fra mappa programmet ligger i. Her er det satt en begrensning om at du bare kan velge .rle og .txt filer.
     * Videre så leser vi over fila og henter det vi er interisert i.
     * Til slutt så blir det lagt til ekstra rader der det er nødvendig.
     * @return
     */
    public boolean ReadFromFile(){
        File selectedFile = null;
        BufferedReader reader = null;
        try {
            File recordsDir = new File(System.getProperty("user.dir"));
            recordsDir.mkdirs();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(recordsDir);
            FileChooser.ExtensionFilter fileExtensions =
                    new FileChooser.ExtensionFilter(
                            "Patern types", "*.rle");//, "*.txt"
            fileChooser.getExtensionFilters().add(fileExtensions);
            selectedFile = fileChooser.showOpenDialog(null);
        }catch (NullPointerException e) {
            System.out.println(e + " | Velger fil");
        }
        if (selectedFile != null) {
            rleFile = isRleFile(selectedFile);
            try {
                reader = new BufferedReader(new FileReader(selectedFile));


            if (reader != null) {
                if (rleFile) {
                    //System.out.println("Rle file = " + rleFile);
                    boolean inserted = boardMaker.InsertRleIntoBoard(reader); // her kommer det en funksjon som konverterter rle fil til array.
                    reader.close();
                    return inserted;
                } else {
                    /* Funker ikke nå. Kommer senere.
                    System.out.println("Rle file = " + rleFile);
                    boolean inserted = boardMaker.FileToBoardSquare(everything); //denne tar en .txt fil og gjør den til en 2d array.
                    reader.close();
                    return inserted;
                    */
                }
            }
            } catch (IOException e) {
                System.out.println(e + " | bufferReader");
            }
        }
        return false;
    }


    /**
     * Her sjekker vi om filtypen er .rle, hvis den er det bir det kjørt spesifikke funksjoner lengre bak.
     * @param selectedFile
     * @return
     */
    public boolean isRleFile( File selectedFile ){
        String REGEX = "[a-zA-Z0-9]+.rle";
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(selectedFile.getName());
        if(m.find()) {
            return true;
        }
        return false;
    }




}
