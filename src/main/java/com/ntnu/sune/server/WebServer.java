package com.ntnu.sune.server;

import com.ntnu.sune.resource.DumpServletA;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import tokenserver.Receive_Token;

// http://localhost:8089/jax/application.wadl

public class WebServer {

    public WebServer() {

        new WebServer("localhost" , 8089);
    }

    public WebServer(String name, int port)  {

        System.out.println("Initialing:  " + name + " on port: " + port);
        Server server = new Server();
        ServerConnector http = new ServerConnector(server);
        http.setHost(name);
        http.setPort(port);
        http.setIdleTimeout(30000L);
        server.addConnector(http);

        final ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        final ServletHolder servletHolder = new ServletHolder(ServletContainer.class);
        // set resource classes to scan
        servletHolder.setInitParameter("com.sun.jersey.config.property.packages", "com.ntnu.sune.resource");
        servletHolder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
        servletHolder.setInitParameter("com.sun.jersey.config.feature.DisableWADL", "false");
        // add monitoring filter
        servletHolder.setInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters", "com.ntnu.sune.resource.MonitoringResponseFilter");

        servletContextHandler.setContextPath("/");
        server.setHandler(servletContextHandler);

        servletContextHandler.addServlet(servletHolder, "/jax/*");
        servletContextHandler.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, "/");
        servletContextHandler.addServlet(new ServletHolder(new DumpServletA()), "/dump/*");
        servletContextHandler.addServlet(new ServletHolder(new Receive_Token()), "/token/Receive_Token");

        System.out.println("Starting " + WebServer.class.getName() + " on port: " + port);
        try {
        server.start();
        server.join();
        } catch (Exception e){
            System.out.println("Failed " + WebServer.class.getName() + " on port: " + port);
            System.exit(1);
        }
    }
 
    public static void main(String[] args) {

        try {
        if(args.length > 0){
            String name = args[0];
            int port = Integer.parseInt(args[1].trim());
            new WebServer(name, port);
        } else {
            new WebServer();
        }
        }
        catch (Exception e){
            System.out.println("Failed...");
            e.printStackTrace();
        }
    }
}
