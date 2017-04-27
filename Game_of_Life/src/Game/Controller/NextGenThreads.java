package Game.Controller;


import Game.Model.Boards.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class NextGenThreads {
    Board board;
    Thread serviceOneThread;
    Thread nextGenThread;
    CyclicBarrier barrier;
    private final int numWorkers =Runtime.getRuntime().availableProcessors();
    private int[][] segments;
    private List<Thread> threads;

    public NextGenThreads(Board boardType) {
        this.board = boardType;
        barrier = new CyclicBarrier(3);
        segments = new int[numWorkers][4];
        threads = new ArrayList<>();
    }

    public void nextGen(){


        int rowpart1;


        if ((board.getRow()%2) ==1 ){
            rowpart1 = (board.getRow()/2) + 1;

        }else {
            rowpart1 = board.getRow()/2;

        }

        //System.out.println(rowpart1 + " | " + rowpart1);

        serviceOneThread = new Thread((new NextGenera(barrier,board, 0, rowpart1)));
        nextGenThread= new Thread(new NextGenera(barrier,board,rowpart1, board.getRow()));

        serviceOneThread.start();
        nextGenThread.start();

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Main Thread interrupted!");
            e.printStackTrace();

        }



    }
    public void stop(){
        serviceOneThread.stop();
        nextGenThread.stop();
    }

    private static class NextGenera implements Runnable {
        CyclicBarrier cyclicBarrier;
        Board boardType;
        int start;
        int slutt;

        public NextGenera(CyclicBarrier cyclicBarrier,  Board boardType, int start, int slutt) {
            this.cyclicBarrier = cyclicBarrier;
            this.boardType = boardType;
            this.start = start;
            this.slutt = slutt;
        }

        @Override
        public void run() {
            //boardType.nextGeneration(start,slutt,cyclicBarrier);

        }


    }




}
