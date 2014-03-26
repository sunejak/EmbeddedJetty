package tokenserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class Invoke_HTTP_Get {

    long max_time = 5000;

    String action(String url, int test, int cnt) {

        int pretxt = System.identityHashCode(this);
        Calendar last_known = Calendar.getInstance();
        HttpURLConnection connection = null;
        int code = -1;
        URL invoke = null;
        InetAddress address = null;
        String m = null;
        String txt = null;
        long entryStamp = System.currentTimeMillis();
        long readStamp = 0;

        try {
            invoke = new URL(url);
            address = InetAddress.getByName(invoke.getHost());
            connection = (HttpURLConnection) invoke.openConnection();
            connection.setDoOutput(true);
            BufferedReader in =  new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
            String response;
            while ((response = in.readLine()) != null ) {
                if(test > 5)System.out.println("Invoke_HTTP_Get: " + response);
                if(response.startsWith("Receive_Token"))txt = response;
            }
            in.close();
            connection.disconnect();
            readStamp = System.currentTimeMillis();
            if((readStamp - entryStamp) > max_time){  m = "Invoke_HTTP_Get_" + cnt + ": (" + pretxt + "): Timeissue_with: " + " Connect: " + (readStamp-entryStamp) ;
                if(test > 3)System.out.println(m);
            }

            code = connection.getResponseCode();
            String msg = connection.getResponseMessage();

            m = "Invoke_HTTP_Get_" + cnt + ": (" + pretxt + "): Took: " + (readStamp-entryStamp) + " ms Response_code_from: " + url + " (" + address + ") "
                    + code + " " + msg + " "  + last_known.getTime() + " Mem: " + txt.substring(18);
            if((readStamp - entryStamp) > max_time)m = m + " Time_issue";
            if(test > 3)System.out.println(m);

        } catch (MalformedURLException e) {
            readStamp = System.currentTimeMillis();
            m = "Invoke_HTTP_Get_" + cnt + ": (" + pretxt + "): Took: " + (readStamp-entryStamp) + " ms Response_code_from: " + url + " (" + address + ") "
                    + -1 + " MalformedURLException " + last_known.getTime() + " Mem: 0 " + last_known.getTime();
            if(test > 3)System.out.println(m);
            //			                                e.printStackTrace();
        } catch (IOException e) {
            readStamp = System.currentTimeMillis();
            m = "Invoke_HTTP_Get_" + cnt + ": (" + pretxt + "): Took: " + (readStamp-entryStamp) + " ms Response_code_from: " + url + " (" + address + ") "
                    + -1 + " IOException "  + " "  + last_known.getTime() + " Mem: 0 " + last_known.getTime();
            if(test > 3)System.out.println(m);
            //			                                e.printStackTrace();
        }
        return m;
    }

    public static void main(String[] args) {

        Invoke_HTTP_Get ihp = new Invoke_HTTP_Get();
        long start = System.currentTimeMillis();
        if(args.length < 1){
            System.out.println("Intended usage java -cp xx tokenserver.Invoke_HTTP_Get http://reidun.no-ip.com/tokenserver/Receive_Token ");
            System.exit(1);
        }
        System.out.println("Invoke_HTTP_Get(main): invoking " + args[0]);
        int m = 0;
        String n = ihp.action(args[0], 3, m++);
        long stop = System.currentTimeMillis();
        System.out.println("Invoke_HTTP_Get(main): sendt to " + args[0] + " " + n + " took: " + (stop - start) + "ms");
    };

}
