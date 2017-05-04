package Game.Controller;


import Game.Model.Boards.Board;
import javafx.scene.control.ComboBox;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class NextGenThreads {
    Board board;
    CyclicBarrier barrier;
    private final int numWorkers = 4;
    private int[] splitedBoard;
    private Thread[] workers;

    public NextGenThreads() {



        workers = new Thread[numWorkers];
        splitedBoard = new int[numWorkers];
        barrier = new CyclicBarrier(1);

    }


    public void createWorkers(ComboBox RuleDropDown) {
        for (int i = 0; i < numWorkers; i++) {
            int start;
            int stop;
            if (i == 0){
                start = 0;
                stop = splitedBoard[i];
            }else{
                start = splitedBoard[i-1];
                stop = splitedBoard[i];
            }

            workers[i] = new Thread(new NextGenera(barrier, board, start,stop,RuleDropDown ));

        }



    }
    public void split(){
        int length = board.getRow();
        int splited = length/numWorkers;
        for (int i = 1; i <splitedBoard.length  ; i++) {
            splitedBoard[i-1] = splited * i;
        }
        splitedBoard[splitedBoard.length-1] = board.getRow();

        System.out.println(Arrays.toString(splitedBoard));

    }
    private void runWorkers() throws InterruptedException {

        for (int i = 0; i <workers.length ; i++) {
            workers[i].start();
        }
        for (int i = 0; i <workers.length ; i++) {

            workers[i].join();

        }


    }

    public void nextGen(ComboBox RuleDropDown ) throws InterruptedException {

        board.makeNextGenArray();

        createWorkers(RuleDropDown);

        runWorkers();
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        board.setBoard();

    }
    public void stop(){

    }

    public void setBoard(Board board) {
        this.board = board;

    }

    private static class NextGenera implements Runnable {
        CyclicBarrier cyclicBarrier;
        Board board;
        int start;
        int stop;
        ComboBox RuleDropDown;

        public NextGenera(CyclicBarrier cyclicBarrier,Board board, int start, int stop,ComboBox RuleDropDown ) {

            this.cyclicBarrier = cyclicBarrier;
            this.board = board;
            this.start = start;
            this.stop = stop;
            this.RuleDropDown = RuleDropDown;

        }

        @Override
        public void run() {
            if(RuleDropDown.getValue() == "Game of Life"){
                board.nextGeneration(start,stop,cyclicBarrier);
            }
            else if(RuleDropDown.getValue() == "No deaths"){
                board.noDeadCellsRule(start,stop,cyclicBarrier);


            }
            else if(RuleDropDown.getValue() == "Cover"){
                board.slowlyCover(start,stop,cyclicBarrier);
            }


        }



    }




}
