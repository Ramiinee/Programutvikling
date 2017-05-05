package Game.Model.Decoders;

import Game.Model.Boards.Board;
import Game.Model.MetaData;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RLEDecoder {


    private Board board;
    private  BufferedReader reader;
    private byte[][] byteBoard;
    private MetaData metaData;
    private Pattern YPattern = Pattern.compile("([yY][\\s][=][\\s])([\\d]+)");
    private Pattern XPattern = Pattern.compile("([xX][\\s][=][\\s])([\\d]+)");
    private  int row = 0, col = 0;

    //contructor
    public RLEDecoder(BufferedReader reader, Board board, MetaData metaData) {
       this.board = board;
       this.reader = reader;
       this.metaData = metaData;
    }

    public void decode() throws IOException {
        MetaDataReader(reader);
        BoardReader(reader);
        //board.makeBoard(byteBoard);
    }
     
    /*
    * Reads from the .rle file and sets author, name and comment, there it should. 
    * Gets the size of the board and makes a new board, than sets the required values for the board.
    */
    private void MetaDataReader(BufferedReader reader) throws IOException {
        StringBuilder name = new StringBuilder();
        StringBuilder author = new StringBuilder();
        StringBuilder comment = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            if (line.charAt(0) == '#') {
                char tempChar = line.charAt(1);
                if (tempChar == 'N') {
                    String nameLine = line.replaceAll("(#N )", "");
                    metaData.setName(name.append(nameLine).toString());
                }else if (tempChar == 'C' || tempChar == 'c' ) {
                    String commentLine = line.replaceAll("(#C )", "");
                    metaData.setComment(comment.append(commentLine + "\n").toString());
                }else if (tempChar == 'O'){
                    String authorLine = line.replaceAll("(#O )", "");
                    metaData.setAuthor(author.append(authorLine).toString());
                }
            } else {
                Matcher Y = YPattern.matcher(line);
                Matcher X = XPattern.matcher(line);
                int rows = 0;
                int columns = 0;

                if (X.find() && (Y.find())) {
                    rows = Integer.parseInt(Y.group(2));
                    columns = Integer.parseInt(X.group(2));
                    //byteBoard = new byte[rows][columns];
                    board.makeBoard(rows+2,columns+2);
                    return;
                }
            }
            line = reader.readLine();
        }
    }
    
    
    /*
     *   Reads text from a character-input stream,
     *   buffering characters so as to provide for the efficient reading of characters. 
    */
    
    private void BoardReader(BufferedReader reader) throws IOException {
        int NumberOfCells = 0;
        char lineBreak = '$';

        String line = reader.readLine();
        while (line != null) {
            for (int j = 0; j < line.length(); j++) {
                if(Character.isDigit(line.charAt(j))) {
                    if (NumberOfCells == 0) {
                        NumberOfCells = Character.getNumericValue(line.charAt(j));
                    } else {
                        NumberOfCells *= 10;
                        NumberOfCells += Character.getNumericValue(line.charAt(j));
                    }
                } else if (line.charAt(j) == 'o' || line.charAt(j) == 'O') {
                    if (NumberOfCells != 0) {
                        while (NumberOfCells != 0) {
                            //byteBoard[row][col] = 1;
                            board.setCellAliveState(row,col,(byte)1);
                            NumberOfCells--;
                            col++;
                        }
                    } else {
                        board.setCellAliveState(row,col,(byte)1);
                        //|byteBoard[row][col] = 1;
                        col++;
                    }
                } else if (line.charAt(j) == 'b' || line.charAt(j) == 'B') {
                    if(NumberOfCells != 0) {
                        while (NumberOfCells != 0) {
                            NumberOfCells--;
                            col++;
                        }
                    }else {
                        col++;
                    }
                }else if (line.charAt(j) == lineBreak) {
                    if(NumberOfCells != 0) {
                        while (NumberOfCells != 0) {
                            row++;
                            NumberOfCells--;
                        }
                    }else {
                        row++;
                    }
                    col = 0;
                }
            }
            line = reader.readLine();
        }
    }
}
