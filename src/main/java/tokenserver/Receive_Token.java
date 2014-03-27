package tokenserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

public class Receive_Token extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8155865214784696975L;
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
    boolean debug = true;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**Process the HTTP doGet request.
     */
    public void doGet(HttpServletRequest request, 
                      HttpServletResponse response) throws ServletException, IOException {response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();   
        // Identify client system
        String destName = request.getRemoteAddr();
        String destAddr = request.getRemoteHost();
//         int destPort = request.getRemotePort();
        String myque = request.getQueryString();
        if(debug)System.out.println("Receive_Token Accepted GET connection from " + destName + " (" + destAddr + ") QueryString: " + myque); // + " on port " + destPort);
        
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
        out.println("<html><head>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\">");
        out.println("<title>Receive_Token in Token_Server</title></head>");
        out.println("<body>");
        
        long free_mem = Runtime.getRuntime().freeMemory();
        out.println("Receive_Token GET " + free_mem + " " + Calendar.getInstance().getTime());
        out.println("</body></html>");
        out.close();          
    }

    /**Process the HTTP doPost request.
     */
    public void doPost(HttpServletRequest request, 
                       HttpServletResponse response) throws ServletException, IOException {response.setContentType(CONTENT_TYPE);
                       
        long entryStamp = System.currentTimeMillis();
        long now = new Date().getTime();
        long free_mem = Runtime.getRuntime().freeMemory();
        PrintWriter out = response.getWriter();
        String destName = request.getRemoteAddr();
//        String destAddr = request.getRemoteHost();
//         int destPort = request.getRemotePort();
        String myque = request.getQueryString();
        if(debug)System.out.println("Receive_Token Accepted POST connection from " + destName 
        + " QueryString: " + myque 
        + " localtime: " + now + " ms"); 
        
        // + " (" + destAddr + ")"); //  + " on port " + destPort); Tomcat 4 does not like RemotePort
                        
        ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
        long connectStamp = System.currentTimeMillis();
        
        String myArr[] = null;

        try {

            myArr = (String[]) ois.readObject();
            
        } catch (ClassNotFoundException e) {
            // 
            if(debug)System.out.println("Receive_Token Got ClassNotFoundException");
        }
        long readStamp = System.currentTimeMillis();
        ois.close();
        long closeStamp = System.currentTimeMillis();
        out.close();
        int len = myArr.length;
        
//        System.out.println("Got: " + len + " objects");
        
//        for( int x=0; x<len; x++)
//                {
//                        String result = myArr[x].equals(name) ? " match" : " nope" ;
//                        System.out.println("[" + x + "]: " + myArr[x] );
//                }
        // do something
        if(len > 2){
        
        String[] newarr = myArr[0].split(" ");
        
//        for(int z=0; z < newarr.length ;z++)System.out.println("[" + z + "]: " + newarr[z]);
        
         long created = Long.parseLong(newarr[2]);
         int counter = Integer.parseInt(newarr[4]);
         long sendt = Long.parseLong(newarr[6]);
        
        System.out.println("Roundtrip nr: " + counter 
        + " took: " + (now - sendt) + " ms"  
        + " freemem: " + free_mem + " bytes"
            + " connect: " + (connectStamp-entryStamp) + " ms"
            + " read: " + (readStamp-connectStamp) + " ms"
            + " close: " + (closeStamp-readStamp) + " ms"
        + " sendt: "   + new Date(sendt)    + " " + sendt
        + " created: " + new Date(created)  + " " + created 
        );
        Token_Store.getInstance(false).setNodeList(created, myArr); // save a copy of the Token in the Token_Store}
        
        }
    }
}
