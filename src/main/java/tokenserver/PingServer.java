package tokenserver;

import java.io.IOException;
import java.lang.Thread.State;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class PingServer {

    public static final int interval = 2500;
    private static final Logger LOGGER = Logger.getLogger(PingServer.class.getName());

    /**
     * @param args URLs to call
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("PingServer (main) is starting (" + args.length + ")");
        if(args.length < 1){
            System.out.println("No URLs to work with, exiting");
            System.exit(-1);
        }
        int threadIndex = 0;
        int loopNumber = 0;

        FileHandler fh = new FileHandler("PingServer_logfile%g.log", 50000000, 100, true);
        fh.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fh);
        Thread.sleep(1000);
        LOGGER.setLevel(Level.ALL);
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
                LOGGER.log(Level.INFO, "PingServer: Thread ("  + threadIndex + ") state is: " + state.toString());

                if(nt[threadIndex].getState().equals(State.TERMINATED)){
                    String who = nt[threadIndex].getName();
                    nt[threadIndex] = new SimpleThread(who, threadIndex, loopNumber);
                    nt[threadIndex].start();
                }
                if(nt[threadIndex].getState().equals(State.NEW)){
                    nt[threadIndex].number = loopNumber;
                    nt[threadIndex].start();
                }
                if(nt[threadIndex].getState().equals(State.WAITING)){
                    LOGGER.log(Level.INFO, "PingServer: Waiting (" + threadIndex + ")");
                }

            } catch (Exception e1) {
                LOGGER.log(Level.INFO, "PingServer: thread failed to start: " + threadIndex + " " + e1.getMessage());
            }

            try {
                Thread.sleep(interval/args.length);
            } catch (InterruptedException e) {
                LOGGER.log(Level.INFO, "PingServer: Sleep failed");
            }
            long after = System.currentTimeMillis();
            LOGGER.log(Level.INFO, "PingServer: " + (after-now) + " ms loop nr: " + loopNumber + " " + cal.getTime());
            // allow 5 ms margin here
            if((after-now) > ((interval/args.length)+5))LOGGER.log(Level.INFO, "PingServer: thread had time issue");
            threadIndex++;
            if(threadIndex>=args.length*5)threadIndex=0;
            loopNumber++;
        }
    }
}
