package com.ntnu.sune.server;

import com.ntnu.sune.resource.DumpServletA;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;

import tokenserver.Receive_Token;

// http://localhost:8089/jax/application.wadl

public class WebServer {

    public WebServer() {

        String[] hosts = {"localhost"};
        new WebServer(8085, hosts);
    }

    public WebServer(int port, String[] hosts)  {

        Server server = new Server();

        ServerConnector serverConnector0 = new ServerConnector(server);
        // serverConnector0.setHost(name);
        serverConnector0.setPort(port);
        serverConnector0.setIdleTimeout(30000L);

        server.addConnector(serverConnector0);

        // use a jersey based servlet
        final ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        final ServletHolder servletHolder = new ServletHolder(ServletContainer.class);
        // set resource classes to scan
        servletHolder.setInitParameter("com.sun.jersey.config.property.packages", "com.ntnu.sune.resource");
        // allow some Java Objects
        servletHolder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
        // show wadl
        servletHolder.setInitParameter("com.sun.jersey.config.feature.DisableWADL", "false");
        // add monitoring filter for jersey
        servletHolder.setInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters", "com.ntnu.sune.resource.MonitoringResponseFilter");

        // add servlets
        servletContextHandler.addServlet(servletHolder, "/jax/*");
        // TODO remove? servletContextHandler.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, "/");
        servletContextHandler.addServlet(new ServletHolder(new DumpServletA()), "/dump/*");
        servletContextHandler.addServlet(new ServletHolder(new Receive_Token()), "/token/Receive_Token");
        // add virtual hosts as well
        String[] hostNames = new String[hosts.length-1];
        StringBuilder names = new StringBuilder();
        for ( int x = 0 ; x < hosts.length-1 ; x++){
            hostNames[x] =  hosts[x+1];
            names.append(hostNames[x]).append(" ");
        }
        servletContextHandler.addVirtualHosts(hostNames);

        // add security handler
        // servletContextHandler.setSecurityHandler(basicAuth("demo", "hello", "private"));

        // servletContextHandler.setContextPath("/");

        // add some logging
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        NCSARequestLog requestLog = new NCSARequestLog("./logs/jetty-yyyy_mm_dd.log");
        requestLog.setRetainDays(90);
        requestLog.setAppend(true);
        requestLog.setExtended(false);
        requestLog.setLogTimeZone("GMT+1");
        requestLogHandler.setRequestLog(requestLog);
        // fix all the handlers
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{servletContextHandler, new DefaultHandler(), requestLogHandler});
        server.setHandler(handlers);

        System.out.println("Starting " + names.toString() + " on port: " + port);
        try {

        server.start();
        server.join();
        } catch (Exception e){
            System.out.println("Failed " + WebServer.class.getName() + " on port: " + port);
            System.exit(1);
        }
    }

    private static SecurityHandler basicAuth(String username, String password, String realm) {

        HashLoginService hashLoginService = new HashLoginService();
        hashLoginService.putUser(username, Credential.getCredential(password), new String[]{"user"});
        hashLoginService.setName(realm);

        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"user"});
        constraint.setAuthenticate(true);

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*");

        ConstraintSecurityHandler constraintSecurityHandler = new ConstraintSecurityHandler();
        constraintSecurityHandler.setAuthenticator(new BasicAuthenticator());
        constraintSecurityHandler.setRealmName("myrealm");
        constraintSecurityHandler.addConstraintMapping(constraintMapping);
        constraintSecurityHandler.setLoginService(hashLoginService);

        return constraintSecurityHandler;
    }

    public static void main(String[] args) {

        try {
        if(args.length > 0){
            int port = Integer.parseInt(args[0].trim());
            new WebServer(port, args);
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
