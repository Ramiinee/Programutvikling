package Game.Controller;


import Game.Model.Boards.Board;
import javafx.scene.control.ComboBox;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class NextGenThreads {
    //objects 
    Board board;
    CyclicBarrier barrier;
    
    //Datatypes
    private final int numWorkers =  Runtime.getRuntime().availableProcessors();
    private final int[] splitedBoard;
    private final Thread[] workers;

    
    

    /**
     * Prepare the required to run workers
     */
    public NextGenThreads() {
        workers = new Thread[numWorkers];
        splitedBoard = new int[numWorkers];
        barrier = new CyclicBarrier(1);

    }

   
    public void setBoard(Board board) {
        this.board = board;

    }
     
    /**
     * The function being called on by scheduledService.
     * Runs the required preporations before @
     * @param RuleDropDown from controller. Determines which rule to use
     * @throws InterruptedException
     */
    public void GenerationWorkers(ComboBox RuleDropDown ) throws InterruptedException {
        board.makeNextGenArray();
        createWorkers(RuleDropDown);
        runWorkers();
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.err.println("Thread interrupted!");

        }
        board.setBoard();

    }
    
    /**
     * Splits the board into the amount of available prosessors.
     */
    public void split(){
        int length = board.getRow();
        int splited = length/numWorkers;
        for (int i = 1; i <splitedBoard.length  ; i++) {
            splitedBoard[i-1] = splited * i;
        }
        splitedBoard[splitedBoard.length-1] = board.getRow();
    }
    
    /**
     * Generate the workes needed and sets the required messurse for a new generation to be generated.
     * @param RuleDropDown from controller. Determines which rule to use
     */
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

            workers[i] = new Thread(new NextGenerationRun(barrier, board, start,stop,RuleDropDown ));

        }
    }
      
    /**
     * Starts the workers and joins them.
     * @throws InterruptedException 
     */
    private void runWorkers() throws InterruptedException {

        for (Thread worker : workers) {
            worker.start();
        }
        for (Thread worker : workers) {
            worker.join();
        }

    }


   
    
private static class NextGenerationRun implements Runnable {
        CyclicBarrier cyclicBarrier;
        Board board;
        int start;
        int stop;
        ComboBox RuleDropDown;
        
       
        /**
         * Creates a {@link Runnable}
         * 
         * @param cyclicBarrier The cyclic barrier.
         * @param board The {@link Board} to use.
         * @param start The start position of the board segment.
         * @param stop The end position of the board segment.
         * @param RuleDropDown from {@link Controller} . Determines which rule to use .
         */
        public NextGenerationRun(CyclicBarrier cyclicBarrier, Board board, int start, int stop, ComboBox RuleDropDown ) {

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
