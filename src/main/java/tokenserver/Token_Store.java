package tokenserver;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

public class Token_Store {
    // These fields help us see whether one or
    // more Singletons were created. There is usually
    // no need for them in a Singleton.
    private int instanceNumber;
    private Date creationDate;
    private java.util.Hashtable node_list;
    private java.util.Hashtable timestamp;
    private String[] errors;
    private int errors_p;

    // Counts instantiations. Should always be  0 or 1
    private static int numberOfInstances;
    public static final int interval = 30000;
	public static final String version = "30 Oct 2011"; 

    // You could add = new SingletonX() at the end of the
    // next line for simple non-lazy initialisation,
    // instead of doing lazy initialisation in getInstance()
    private static Token_Store instance;

    /**
    * Note that the constructor is private, to prevent
    * outside objects from constructing more instances.
    */
    private Token_Store(boolean main) {
    	
        instanceNumber = ++numberOfInstances;
        Calendar now = Calendar.getInstance();
        creationDate = now.getTime();
        node_list = new java.util.Hashtable();
        timestamp = new java.util.Hashtable();
        long total_mem = Runtime.getRuntime().totalMemory();
        errors = new String[10];
        errors_p = 0;
        
        System.out.println("Token_Store is created (" + instanceNumber + ") " + creationDate + " (" + total_mem + ")bytes " + main);
        
        if(main){
        	// create a task that keeps sending tokens.
        	Timer timer = new Timer();
        	now.set(Calendar.SECOND, 0);
        	TimerTask n = new Token_Store.SendToken();
        	try {
        		Thread.sleep(500);
        	} catch (InterruptedException e) {
        		System.out.println("Failed in SendToken");
        	}
        	timer.scheduleAtFixedRate(n, now.getTime(), interval); // send tokens at 30 second interval
        }
        else {
            // create a task that keeps checking tokens.
            Timer timer = new Timer();
            now.set(Calendar.SECOND, 0);
            TimerTask n = new Token_Store.CheckToken();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
        		System.out.println("Failed in CheckToken");
            }
            timer.scheduleAtFixedRate(n, now.getTime(), 10*interval); // check tokens at 300 second interval
        }  
    }

    /**
  * The getInstance() method is static, giving a global
  * point of access through the class name. There are
  * other ways to implement the Singleton.
  */
    public static Token_Store getInstance(boolean main) {
        if (instance == null) {
            System.out.println("Token_Store does not exist (" + main + ") creating version: " + version);
            instance = new Token_Store(main);
        }
        return instance;
    }

    /**
    * As an experiment, have the Singleton hold a reference
    * to a servlet to prevent the servlet class from being discarded.
    *
    * Alternately, you might try to hold a reference in the
    * opposite direction, from another class to the
    * Singleton: Either hold a reference to class SingletonX
    * (in which case the class cannot be garbage-collected),
    * or else to a super-interface of SingletonX, loading
    * class SingletonX dynamically (in which case class SingletonX might be
    * garbage-collected).
    */

    public String[] getNodeList(Long key) {
        String[] n = (String[])node_list.get(key);
        return n;
    }
    
    public int getCounterFromList(Long key) {
        String[] n = (String[])node_list.get(key);
        String[] newarr = n[0].split(" ");     
        return Integer.parseInt(newarr[4]);
    }

    public void setNodeList(Long created, String[] n) {
        node_list.put(created, n);
    }

    public Long[] getAllNodeKeys() {

        Enumeration en = node_list.keys();
        Long[] tmp = new Long[node_list.size()];
        int z = 0;
        while (en.hasMoreElements()) {
            tmp[z++] = (Long)en.nextElement();
        }
        return tmp;
    }
    
	public void setNodeTime(Long key, Long endStamp) {
		timestamp.put(key, endStamp);
	}

	public long getNodeTime(Long key) {
		Long m = (Long)timestamp.get(key);
		
		if(m != null)return m.longValue(); else return -1L;
	}
	
	public void clearAllNodeKeys() {
		
		Object o = new Object();	
		synchronized(o){
			node_list.clear();
			timestamp.clear();
		}
	}
	
	public void setError(String err){
		
		if(errors_p >= errors.length)errors_p = 0;
		errors[errors_p++] = new String(err);
	}
	
	public String[] getErrors() {
		return errors;
	}

    public static void main(String[] args) {

//        String debug_in = args.length > 0 ? args[0] : "false";
        
        System.out.println("Token_Store (main) is starting (" + args.length + ")");
        if(args.length < 2){
        	System.out.println("No URLs to work with, exiting");
        	System.exit(-1);
        }
//        boolean debug = debug_in.equalsIgnoreCase("true");
        Token_Store mtts = Token_Store.getInstance(true);

        Long now = new Long(new Date().getTime());
        
        String[] arr_a = new String[args.length + 2];
        
        arr_a[0] = "Token created: " + now + " counter: 0 dispatched: " + now;
        arr_a[1] = "3" ;
              
        for (int x=2; x < (args.length + 2) ; x++)arr_a[x] = args[x-2];
        System.out.println("First node to access is: " + arr_a[3]);
       
//        String[] arr_a =       	
//        { 
//        		"Token created: " + now + " counter: 0 dispatched: " + now, 
//        		"3", 
//        		"http://sune-ws.no-ip.org/tokenserver/Receive_Token",
//        		"http://reidun.no-ip.com/tokenserver/Echo_Token",
//        		"http://bjarne.pats.no:8090/tokenserver/Echo_Token",
//        		"http://129.241.200.85:8180/tokenserver/Echo_Token"
//         };  	
        
//        if(debug){
//        	
//        	arr_a = new String[3];
//        	arr_a[0] = "Token created: " + now + " counter: 0 dispatched: " + now;
//        	arr_a[1] = "1";
//        	arr_a[2] = "http://localhost:8080/TokenServer/Receive_Token";
//        	arr_a[3] = "http://localhost:8080/TokenServer/Echo_Token";	
//        }
        
        mtts.setNodeList(now, arr_a);
        System.out.println("Token_Store (" + version + ")(main) is done");
    }

    private class CheckToken extends TimerTask {

        public void run() {
            Calendar rightNow = Calendar.getInstance();
            int pretxt = System.identityHashCode(this);
            System.out.println("CheckToken: Time's up! (" + pretxt + ") " + rightNow.getTime().toString());

            Enumeration en = node_list.keys();
            Long[] tmp = new Long[node_list.size()];
            int zz = 0;
            while (en.hasMoreElements()) {
                tmp[zz++] = (Long)en.nextElement();
            }  
            if (tmp.length > 0)
                for (int x = 0; x < tmp.length; x++) {
                	
                	long last_stamp = getNodeTime(tmp[x]);
                	String txt = "";
                	
                	long delta = System.currentTimeMillis() - last_stamp;
                	
                	if (delta > ( 30*interval )){
                	
                	if (last_stamp > 0L){ 
                		txt = "Failed";
                		setNodeTime(tmp[x], new Long(-1L)); // to show Failed once
                	}
                	
                	else txt = "Dismissed";
                	
                	System.out.println("CheckToken: (" + pretxt 
                    		+ ") Token_Store contains: [" + x + "] " + tmp[x] 
                    		+ " : " + getCounterFromList(tmp[x])
                    		+ " : " + delta + " ms " + txt
                    		);
                	}
                }
        	}
        }

    private class SendToken extends TimerTask {

        public void run() {
            Calendar rightNow = Calendar.getInstance();
            int pretxt = System.identityHashCode(this);
            String txt = " strange time";
            if (rightNow.get(Calendar.SECOND) == 0) txt = " ok time";
            if (rightNow.get(Calendar.SECOND) == 30)txt = " ok time";
            System.out.println("SendToken: Time's up! (" + pretxt + ") " + rightNow.getTime().toString() + txt);

            Enumeration en = node_list.keys();
            Long[] tmp = new Long[node_list.size()];
            int zz = 0;
            while (en.hasMoreElements()) {
                tmp[zz++] = (Long)en.nextElement();
            }
            if (tmp.length > 0)
                for (int x = 0; x < tmp.length; x++) {

                    System.out.println("SendToken: (" + pretxt + ") Token_Store contains: [" + x + "] " + tmp[x]);
                    String[] tmparr = (String[])node_list.get(tmp[x]);
                    
                    String[] copied = new String[tmparr.length];
                    for (int x1 = 0; x1<tmparr.length;x1++)copied[x1] = new String(tmparr[x1]);

                    Long now = new Long(new Date().getTime());

                    String[] harr = copied[0].split(" ");
                    int counter = Integer.parseInt(harr[4]);
                    counter++;

                    copied[0] = harr[0] + " "; // Token
                    copied[0] += harr[1] + " "; // created:
                    copied[0] += harr[2] + " ";
                    copied[0] += harr[3] + " "; // counter:
                    copied[0] += counter + " ";
                    copied[0] += harr[5] + " "; // dispatched:
                    copied[0] += now.toString();

                    String myurl = copied[3];
                    int mycnt = counter;

                    Long created = new Long(harr[2]);
                    System.out.println("Remind_Task: Entry [" + x + "] : " + copied[0]);
                    node_list.put(created, copied);

                    new WorkThread(myurl, copied, mycnt);
                }
            System.out.println("SendToken is done (" + pretxt + ")");
        }
    }

    private class WorkThread extends Thread {

        private String myurl;
        private String[] tmparr;
        private int cnt;

        WorkThread(String myurl, String[] tmparr, int cnt) {
            this.myurl = myurl;
            this.tmparr = tmparr;
            this.cnt = cnt;
            this.start();
        }

        public void run() {
            long start = System.currentTimeMillis();
            long free_mem = Runtime.getRuntime().freeMemory();

            int i = new Invoke_HTTP_Post().action(myurl, tmparr);
            long timeItTook = System.currentTimeMillis() - start;
            String status = timeItTook > interval ? " and a token got skipped" : "";
            System.out.println("WorkThread: Round: " + cnt + " " + new Date() + 
                               " " + free_mem + " bytes Got: " + i + 
                               " when token was sendt to: " + myurl + 
                               " and it took: " + timeItTook + " ms " + status);
        }
    }
}
