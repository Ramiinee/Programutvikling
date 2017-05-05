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
    //objects 
    Board board;
    CyclicBarrier barrier;
    
    //Datatypes
    private final int numWorkers =  Runtime.getRuntime().availableProcessors();
    private int[] splitedBoard;
    private Thread[] workers;

    
    //Threads for next generation with defined workers, cyclic barrier and splitedBoard
    public NextGenThreads() {
        workers = new Thread[numWorkers];
        splitedBoard = new int[numWorkers];
        barrier = new CyclicBarrier(1);

    }
     public void setBoard(Board board) {
        this.board = board;

    }
     
     /*
      * 
      */
     
    public void nextGen(ComboBox RuleDropDown ) throws InterruptedException {

        board.makeNextGenArray();

        createWorkers(RuleDropDown);

        runWorkers();
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Main Thread interrupted!");
            e.printStackTrace();

        }
        board.setBoard();

    }
    
    public void split(){
        int length = board.getRow();
        int splited = length/numWorkers;
        for (int i = 1; i <splitedBoard.length  ; i++) {
            splitedBoard[i-1] = splited * i;
        }
        splitedBoard[splitedBoard.length-1] = board.getRow();
    }
    
    //creates thread workers that do the tasks given
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
      
    /*
     * thread worker that loops and waits for the availability of a task to execute,
     * once a task is assigned the worker comes out of the waiting loop and do the task given
     * after the worker has runned, it waits for a new task
     */
    private void runWorkers() throws InterruptedException {

        for (Thread worker : workers) {
            worker.start();
        }
        for (Thread worker : workers) {
            worker.join();
        }

    }


   
      /*
       *implements the interface Runnable which is meant to contain the code executed in the thread
       */
private static class NextGenera implements Runnable {
        CyclicBarrier cyclicBarrier;
        Board board;
        int start;
        int stop;
        ComboBox RuleDropDown;
        
        //constructor
        public NextGenera(CyclicBarrier cyclicBarrier, Board board, int start, int stop, ComboBox RuleDropDown ) {

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
