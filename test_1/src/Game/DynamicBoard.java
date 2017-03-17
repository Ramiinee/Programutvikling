package Game;

import java.util.ArrayList;
import java.util.List;

public class DynamicBoard extends Board{

    public int Rows;
    public int Columns;
    

    ArrayList<List<Byte>> arraylist = new ArrayList<>();
    
    
    public int getRader() {
        return Rows;
    }

    public void setRader(int rader) {
        Rows = rader;
    }

    public int getKolonner() {
        return Columns;
    }

    public void setKolonner(int kolonner) {
        Columns = kolonner;
    }

    public ArrayList<List<Byte>> getarrayList() {
        return arraylist;
    }

    public void setBoard(ArrayList<List<Byte>> arraylist) {
        this.arraylist = arraylist;
    }
}

