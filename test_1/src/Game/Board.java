package Game;


public class Board extends Controller{
    public int kolonner = 50;
    public int rader = 50;

    public int[][] board = new int[kolonner][rader];

    public void randomPattern(){

        for (int i = 0; i < kolonner; i++) {
            for (int j = 0; j < rader ; j++) {
                board[i][j] = (Math.random()<0.5)?0:1;
            }
        }
    }
    public int[][] getBoard(){
        return board;
    }

    public int getKolonner() {
        return kolonner;
    }

    public int getRader() {
        return rader;
    }
}
