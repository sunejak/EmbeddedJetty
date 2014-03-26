package tokenserver;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class Invoke_HTTP_Post {

    Invoke_HTTP_Post() { };

    int action(String url, String[] arr) {
    
        long entryStamp = System.currentTimeMillis();
        int pretxt = System.identityHashCode(this);
        Calendar last_known = Calendar.getInstance();
        HttpURLConnection connection = null;
        int code = -1;
        URL invoke = null;
        InetAddress address = null;
        
        try {
        	invoke = new URL(url);
        	
        	address = InetAddress.getByName(invoke.getHost());
        	
             connection = (HttpURLConnection) invoke.openConnection();
             connection.setRequestMethod("POST");
        //              connection.setUseCaches(false);
             connection.setDoOutput(true);
        //              connection.setDoInput(true);
             connection.connect();
             
        ObjectOutputStream ois = new ObjectOutputStream(connection.getOutputStream());
        long connectStamp = System.currentTimeMillis();
        
        if(arr != null) {
        	ois.writeObject(arr);
        	ois.flush();
        	ois.close();
      	}
        long writeStamp = System.currentTimeMillis();
        
        // Read response
        code = connection.getResponseCode(); 
        
        long readStamp = System.currentTimeMillis();
        
        if((readStamp - entryStamp) > 5000){ String m = "Invoke_HTTP_Post (" + pretxt + "): Timeissue with: " + arr[0] 
                    + " Connect: " + (connectStamp-entryStamp) 
                    + " ms  Write: " + (writeStamp-connectStamp) 
                    + " ms  Read: " + (readStamp-writeStamp) + " ms" ;
        			System.out.println(m);
        }
        
        String msg = connection.getResponseMessage(); 
        
        String[] newarr = arr[0].split(" ");
        long sendt = Long.parseLong(newarr[6]);
        last_known.setTimeInMillis(sendt);
     
        if(code != 200){ String m = "Invoke_HTTP_Post (" + pretxt + "): Response code from: " 
        		+ url + " (" + address + ") is " + code + " " + msg + " " + arr[0] + " " + last_known.getTime(); 
        		Token_Store.getInstance(false).setError(m);
        		System.out.println(m);
        }
        
                } catch (MalformedURLException e) {
                    String m = "Invoke_HTTP_Post (" + pretxt + "): MalformedURLException " + e.getMessage() + " to: " 
                    		+ url+ " (" + address + ") " + arr[0] + " " + last_known.getTime(); 
                    Token_Store.getInstance(false).setError(m);
                    System.out.println(m); 
//                                e.printStackTrace();
                } catch (IOException e) {
                    String m = "Invoke_HTTP_Post (" + pretxt + "): IOException " + e.getMessage() + " to: " 
                    		+ url + " (" + address + ") " + arr[0] + " " + last_known.getTime(); 
                    Token_Store.getInstance(false).setError(m);
                    System.out.println(m);
//                                e.printStackTrace();
                } 
        return code;
    }
    
    public static void main(String[] args) {
    
    Invoke_HTTP_Post ihp = new Invoke_HTTP_Post();
    
    String[] arr_a = new String[args.length + 2];

    long start = System.currentTimeMillis();
    
    arr_a[0] = "Token created: 000000 counter: 0 dispatched: " + start ;
    arr_a[1] = "3" ;
    
    for ( int x = 0; x < args.length ; x++ )
    	{
    		System.out.println("Invoke_HTTP_Post (main): entry [" + x + "] " + args[x]);
    		arr_a[x+2] = args[x];
    	}
    	
  	if(args.length < 1){
  		System.out.println("Intended usage java -cp xx tokenserver.Invoke_HTTP_Post http://reidun.no-ip.com/tokenserver/Receive_Token http://reidun.no-ip.com/tokenserver/Echo_Token");
  		System.exit(1);
  	}
  	String myurl = args[1];
    System.out.println("Invoke_HTTP_Post (main): invoking " + myurl);
    int n = ihp.action(myurl, arr_a);
    long stop = System.currentTimeMillis();
    System.out.println("Invoke_HTTP_Post (main): sendt to " + myurl + " " + n + " took: " + (stop - start) + "ms");
    };

}
