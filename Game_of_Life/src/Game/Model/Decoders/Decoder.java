package Game.Model.Decoders;



import Game.Model.Boards.Board;

import java.io.BufferedReader;
import java.io.IOException;


public abstract class Decoder {


    protected Board board;
    protected final BufferedReader reader;


    public Decoder(BufferedReader reader, Board board) {
        this.reader = reader;
        this.board = board;
    }


    public abstract void decode() throws IOException;


}

