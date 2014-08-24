package tokenserver;

import java.lang.Thread.State;
import java.util.Calendar;

public class PingServer {

    public static final int interval = 2500;

    /**
     * @param args URLs to call
     */
    public static void main(String[] args) {

        System.out.println("PingServer (main) is starting (" + args.length + ")");
        if(args.length < 1){
            System.out.println("No URLs to work with, exiting");
            System.exit(-1);
        }
        int threadIndex = 0;
        int loopNumber = 0;

        SimpleThread[] nt = new SimpleThread[args.length*5]; // create a set of threads for each URL

        for(int ii=0; ii<args.length; ii++){

            nt[ii] = new SimpleThread(args[ii], 0, 0);
            nt[ii+(args.length)] = new SimpleThread(args[ii], 0, 0);
            nt[ii+(args.length*2)] = new SimpleThread(args[ii], 0, 0);
            nt[ii+(args.length*3)] = new SimpleThread(args[ii], 0, 0);
            nt[ii+(args.length*4)] = new SimpleThread(args[ii], 0, 0);
        }

        Calendar cal = Calendar.getInstance();

        //noinspection InfiniteLoopStatement
        while(true){
            long now = System.currentTimeMillis();

            try {
                State state = nt[threadIndex].getState();
                System.out.println("PingServer: Thread ("  + threadIndex + ") state is: " + state.toString());

                if(nt[threadIndex].getState().equals(State.TERMINATED)){
                    String who = nt[threadIndex].getName();
                    nt[threadIndex] = new SimpleThread(who, threadIndex, loopNumber);
                    nt[threadIndex].start();
                }
                if(nt[threadIndex].getState().equals(State.NEW)){
                    nt[threadIndex].number = loopNumber;
                    nt[threadIndex].start();
                }
            } catch (Exception e1) {
                System.out.println("PingServer: thread failed to start: " + threadIndex + " " + e1.getMessage());
            }

            try {
                Thread.sleep(interval/args.length);
            } catch (InterruptedException e) {
                // Auto-generated catch block
                e.printStackTrace();
            }
            long after = System.currentTimeMillis();
            System.out.println("PingServer: " + (after-now) + " ms loop nr: " + loopNumber + " " + cal.getTime());
            // allow 5 ms margin here
            if((after-now) > ((interval/args.length)+5))System.out.println("PingServer: thread had time issue");
            threadIndex++;
            if(threadIndex>=args.length*5)threadIndex=0;
            loopNumber++;
        }
    }
}
