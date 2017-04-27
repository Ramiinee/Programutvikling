package Game.Model.Decoders;

import Game.Model.Boards.Board;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RLEDecoder extends Decoder{

    public RLEDecoder(BufferedReader reader, Board board) {
        super(reader,board);
    }
    @Override
    public void decode() throws IOException {
        parseMetadata(reader);
        parseBoard(reader);
    }
    private void parseMetadata(BufferedReader reader) throws IOException {

        StringBuilder name = new StringBuilder();
        StringBuilder author = new StringBuilder();
        StringBuilder comment = new StringBuilder();
        Pattern RLEpatternY = Pattern.compile("([yY][\\s][=][\\s])([\\d]+)");
        Pattern RLEpatternX = Pattern.compile("([xX][\\s][=][\\s])([\\d]+)");

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.charAt(0) == '#') {
                final char tempChar = line.charAt(1);
                switch (tempChar) {
                    case 'N':
                        String tempString = line.replaceAll("(#N )", "");
                        name.append(tempString);
                        break;
                    case 'C':
                        String tempString2 = line.replaceAll("(#C )", "");
                        comment.append(tempString2);
                        comment.append("\n");
                        break;
                    case 'c':
                        String tempString3 = line.replaceAll("(#c )", "");
                        comment.append(tempString3);
                        comment.append("\n");
                        break;
                    case 'O':
                        String tempString4 = line.replaceAll("(#O )", "");
                        author.append(tempString4);
                }
            } else {
                boolean foundRows = false;
                boolean foundColumns = false;
                Matcher RLEmatcherY = RLEpatternY.matcher(line);
                Matcher RLEmatcherX = RLEpatternX.matcher(line);
                int rows = 0;
                int columns = 0;

                if (RLEmatcherY.find()) {
                    rows = Integer.parseInt(RLEmatcherY.group(2));
                    foundRows = true;
                }

                if (RLEmatcherX.find()) {
                    columns = Integer.parseInt(RLEmatcherX.group(2));
                    foundColumns = true;
                }

                if (foundRows && foundColumns) {
                    board.makeBoard(rows*5,columns*5);
                    return;

                }


            }
        }
        System.out.println(name.toString());

    }


    private void parseBoard(BufferedReader reader) throws IOException {
        String line;
        int row = 0, col = 0;
        final char lineBreak = '$';
        int charNumber = 0; // Antall forekomster av en celle

        while ((line = reader.readLine()) != null) {
            for (int j = 0; j < line.length(); j++) {
                if (Character.isDigit(line.charAt(j))) {
                    if (charNumber == 0) {
                        charNumber = Character.getNumericValue(line.charAt(j));
                    } else {
                        charNumber *= 10;
                        charNumber += Character.getNumericValue(line.charAt(j));
                    }
                } else if (line.charAt(j) == 'o' || line.charAt(j) == 'O') {
                    if (charNumber != 0) { //Iterer gjennom antall forekomster og sett celle til levende
                        while (charNumber != 0) {
                            board.setCellAliveState(row+2,col+2,(byte)1);
                            charNumber--;
                            col++;
                        }
                    } else {
                        board.setCellAliveState(row+2,col+2,(byte)1);
                        col++;
                    }
                } else if (line.charAt(j) == 'b' || line.charAt(j) == 'B') {
                    if (charNumber != 0) {
                        while (charNumber != 0) {
                            charNumber--;
                            col++;
                            System.out.print("");
                        }
                    } else {
                        col++;
                    }
                } else if (line.charAt(j) == lineBreak) {
                    if (charNumber != 0) {
                        while (charNumber != 0) {
                            charNumber--;
                            row++;
                        }
                    } else {
                        row++;
                    }
                    col = 0;
                }
            }
        }

    }
}
