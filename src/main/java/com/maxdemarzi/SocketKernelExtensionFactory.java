package com.maxdemarzi;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.helpers.Service;
import org.neo4j.kernel.extension.KernelExtensionFactory;
import org.neo4j.kernel.lifecycle.Lifecycle;
import org.neo4j.kernel.lifecycle.LifecycleAdapter;
import org.neo4j.kernel.logging.Logging;
import org.neo4j.kernel.logging.SystemOutLogging;

@Service.Implementation(KernelExtensionFactory.class)
public class SocketKernelExtensionFactory extends KernelExtensionFactory<SocketKernelExtensionFactory.Dependencies> {

    public interface Dependencies {
        GraphDatabaseService getGraphDatabaseService();
    }

    protected SocketKernelExtensionFactory() {
        super("SocketKernelExtensionFactory");
    }

    @Override
    public Lifecycle newKernelExtension(final Dependencies dependencies) throws Throwable {
        return new LifecycleAdapter() {

            private Server server = new Server();

            @Override
            public void start() throws Throwable {
                Logging logging = new SystemOutLogging();
                logging.getConsoleLog(SocketKernelExtensionFactory.class).log("wtf***********************************************");
                System.out.println("Starting up web socket server");
                System.err.println("Starting up web socket server on err");

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

                server.start();
                server.dump(System.err);
                server.join();

            }

            @Override
            public void shutdown() throws Throwable {
                System.out.println("Shutting down web socket server");
                server.stop();
                server.destroy();
            }
        };
    }
}
