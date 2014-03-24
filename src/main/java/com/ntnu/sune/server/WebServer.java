package com.ntnu.sune.server;

import com.ntnu.sune.resource.DumpServletA;
import com.ntnu.sune.resource.DumpServletB;
import com.ntnu.sune.resource.ServerStatus;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
 
public class WebServer {

    int portNumber = 8089;

    public WebServer(int num) {
        portNumber = num;
    }

    public WebServer() throws  Exception {
        Server server = new Server(portNumber);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, "/");
        context.addServlet(new ServletHolder(new DumpServletA()), "/dump/*");
        context.addServlet(new ServletHolder(new DumpServletB(100)), "/test/*");
        server.start();
        server.join();
    }
 
    public static void main(String[] args) throws Exception {

        if(args.length > 1){
            new WebServer(Integer.parseInt(args[0]));
        } else {
            new WebServer();
        }
    }
}
