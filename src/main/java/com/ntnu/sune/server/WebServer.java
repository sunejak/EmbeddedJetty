package com.ntnu.sune.server;

import com.ntnu.sune.resource.DumpServletA;
import com.ntnu.sune.resource.DumpServletB;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import org.eclipse.jetty.server.Server;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

// http://localhost:8089/jax/application.wadl

public class WebServer {

    private int portNumber = 8089;

    public WebServer(int num) {
        portNumber = num;
    }

    public WebServer()  {
        Server server = new Server(portNumber);
        final ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        final ServletHolder servletHolder = new ServletHolder(ServletContainer.class);

        servletHolder.setInitParameter("com.sun.jersey.config.property.packages","com.ntnu.sune.resource");
        servletHolder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
        servletHolder.setInitParameter("com.sun.jersey.config.feature.DisableWADL", "false");
        servletHolder.setInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters", "com.ntnu.sune.resource.MonitoringResponseFilter");

        servletContextHandler.setContextPath("/");
        server.setHandler(servletContextHandler);

        servletContextHandler.addServlet(servletHolder, "/jax/*");
        servletContextHandler.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, "/");
        servletContextHandler.addServlet(new ServletHolder(new DumpServletA()), "/dump/*");
        servletContextHandler.addServlet(new ServletHolder(new DumpServletB(100)), "/test/*");

        System.out.println("Starting " + WebServer.class.getName() + " on port: " + portNumber);
        try {
        server.start();
        server.join();
        } catch (Exception e){
            System.out.println("Failed " + WebServer.class.getName() + " on port: " + portNumber);
            System.exit(1);
        }
    }
 
    public static void main(String[] args) throws Exception {

        if(args.length > 1){
            new WebServer(Integer.parseInt(args[0]));
        } else {
            new WebServer();
        }
    }
}
