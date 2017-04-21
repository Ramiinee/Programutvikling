package Game.model.Decoders;



import Game.model.Boards.Board;
import java.io.BufferedReader;
import java.io.IOException;


public abstract class Decoder {

    
    protected Board staticBoard;
    protected final BufferedReader reader;

   
    public Decoder(BufferedReader reader, Board staticBoard) {
        this.reader = reader;
        this.staticBoard = staticBoard;
    }

    public abstract void decode() throws IOException;

    

}

