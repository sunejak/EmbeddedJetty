package tokenserver;

import java.lang.Thread.State;
import java.util.Calendar;

public class Ping_Server {

    public static final int interval = 2500;

    /**
     * @param args URLs to call
     */
    public static void main(String[] args) {

        System.out.println("Ping_Server (main) is starting (" + args.length + ")");
        if(args.length < 1){
            System.out.println("No URLs to work with, exiting");
            System.exit(-1);
        }
        int cnt = 0;
        int m = 0;

        SimpleThread[] nt = new SimpleThread[args.length*5]; // create a set of threads for each URL

        for(int ii=0; ii<args.length; ii++){

            nt[ii] = new SimpleThread(args[ii], 0);
            nt[ii+(args.length*1)] = new SimpleThread(args[ii], 0);
            nt[ii+(args.length*2)] = new SimpleThread(args[ii], 0);
            nt[ii+(args.length*3)] = new SimpleThread(args[ii], 0);
            nt[ii+(args.length*4)] = new SimpleThread(args[ii], 0);
        }

        Calendar cal = Calendar.getInstance();

        while(true){
            long now = System.currentTimeMillis();

            try {
                State state = nt[cnt].getState();
                System.out.println("Ping_Server: Thread ("  + cnt + ") state is: " + state.toString());

                if(nt[cnt].getState().equals(State.TERMINATED)){
                    String who = nt[cnt].getName();
//					System.out.println("Ping_Server: Thread is TERMINATED,  start over: " + nt[cnt].getState());
                    nt[cnt] = new SimpleThread(who, m);
                    nt[cnt].start();
                }
                if(nt[cnt].getState().equals(State.NEW)){
//					System.out.println("Ping_Server: Thread is NEW,  start over: " + nt[cnt].getState());
                    nt[cnt].numb = m;
                    nt[cnt].start();

                }
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                System.out.println("Ping_Server: failed to start: " + cnt + " " + e1.getMessage());
            }

            try {
                Thread.sleep(interval/args.length);
            } catch (InterruptedException e) {
                // Auto-generated catch block
                e.printStackTrace();
            }
            long after = System.currentTimeMillis();
            System.out.println("Ping_Server: " + (after-now) + " ms loop nr: " + m + " " + cal.getTime());
            cnt++; if(cnt>=args.length*5)cnt=0;
            m++;
        }
    }
}
