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

import tokenserver.ReceiveToken;

import java.util.logging.Level;
import java.util.logging.Logger;

// http://localhost:8085/jax/application.wadl

public class WebServer {

    private static final Logger LOGGER = Logger.getLogger(WebServer.class.getName());

    public WebServer() {
        String[] hosts = {"localhost"};
        new WebServer(8085, hosts);
    }

    public WebServer(int port, String[] hosts)  {

        LOGGER.log(Level.INFO, "Starting WebServer");
        Server server = new Server();

        for ( int x = 0 ; x < hosts.length-1 ; x++){
        ServerConnector serverConnector0 = new ServerConnector(server);
        serverConnector0.setHost(hosts[x+1]);
        serverConnector0.setPort(port);
        serverConnector0.setIdleTimeout(30000L);
        server.addConnector(serverConnector0);
        }

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
        servletContextHandler.addServlet(new ServletHolder(new ReceiveToken()), "/token/ReceiveToken");
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
        requestLog.setExtended(true);
        requestLog.setLogTimeZone("GMT+1");
        requestLogHandler.setRequestLog(requestLog);
        // fix all the handlers
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{servletContextHandler, new DefaultHandler(), requestLogHandler});
        server.setHandler(handlers);

        LOGGER.log(Level.INFO, "WebServer started at " + names.toString() + " using port: " + port + " version: 24 Aug 2014");
        try {

        server.start();
        server.join();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Failed on port: " + port, e);
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
        constraintSecurityHandler.setRealmName("myRealm");
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
            LOGGER.log(Level.SEVERE, "Failed...", e);
            e.printStackTrace();
        }
    }
}
