package Game;

import Game.StaticBoard;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileConverter {
    public BoardMaker boardMaker;
    public StaticBoard staticBoard;
    private boolean rleFile;
    private String everything;

    public  boolean FromFileToBoard(StaticBoard staBoard, BoardMaker boaker) {
        staticBoard = staBoard;
        boardMaker = boaker;
        rleFile = false;
        boolean ruleBoard = fileChooser();
        if (ruleBoard) {
            if (rleFile){
                System.out.println("Rle file = " + rleFile);
                boardMaker.InsertRleIntoBoard(everything); // her kommer det en funksjon som konverterter rle fil til array.
                return true;
            }
            else {
                System.out.println("Rle file = " + rleFile);
                return boardMaker.FileToBoardSquare(everything, staticBoard); //denne tar en .txt fil og gjør den til en 2d array.
            }
        }
        else return false;
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
                        String StringToX = line;
                        String StringToY = line;
                        StringToX = StringToX.replaceAll("x=","");
                        StringToX = StringToX.replaceAll(",y=[0-99]+","");
                        StringToY = StringToY.replaceAll("x=[0-99]+,y=","");

                        staticBoard.setKolonner(Integer.parseInt(StringToX));
                        staticBoard.setRader(Integer.parseInt(StringToY));
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

}
