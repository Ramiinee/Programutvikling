package Game.model.Decoders;



import Game.model.Boards.Board;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The {@link Decoder} class
 */
public abstract class Decoder {

    protected byte[][] board;
    protected Board staticBoard;
    protected final BufferedReader reader;

    /**
     * Creates a {@link Decoder} class with the specified parameters
     * @param reader The {@link BufferedReader} to open
     */
    public Decoder(BufferedReader reader, Board staticBoard) {
        this.reader = reader;
        this.staticBoard = staticBoard;
    }

    /**
     * The abstract decode method that every decoder must implement
     * @throws IOException If an unspecified I/O error occurs
     */
    public abstract void decode() throws IOException;

    

}

