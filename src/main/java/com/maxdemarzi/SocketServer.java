package com.maxdemarzi;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.kernel.lifecycle.Lifecycle;

public class SocketServer implements Lifecycle {
    private Server server;
    private final StringLogger logger;
    private final GraphDatabaseService db;

    public SocketServer(GraphDatabaseService db, StringLogger logger) {
        this.logger = logger;
        this.db = db;
    }

    @Override
    public void init() throws Throwable {
        System.out.println("Why wont this work?");
        logger.info("In SocketServer Init");
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(7472);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Add a websocket to a specific path spec
        ServletHolder holderEvents = new ServletHolder("ws-events", EventServlet.class);
        context.addServlet(holderEvents, "/events/*");
    }

    @Override
    public void start() throws Throwable {
        logger.info("In SocketServer Start");
        server.start();
        server.dump(System.err);
        server.join();
    }

    @Override
    public void stop() throws Throwable {
        logger.info("In SocketServer Stop");
        server.stop();
    }

    @Override
    public void shutdown() throws Throwable {
        logger.info("In SocketServer shutdown");
        server.destroy();
    }
}
