package com.maxdemarzi;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.helpers.Service;
import org.neo4j.kernel.extension.KernelExtensionFactory;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.kernel.lifecycle.Lifecycle;

@Service.Implementation(KernelExtensionFactory.class)
public class SocketServerKernelExtensionFactory extends KernelExtensionFactory<SocketServerKernelExtensionFactory.Dependencies> {

    public interface Dependencies {
        GraphDatabaseService getGraphDatabaseService();
        StringLogger getStringLogger();
    }

    protected SocketServerKernelExtensionFactory() {
        super("SocketServerKernelExtensionFactory");
    }

    @Override
    public Lifecycle newKernelExtension(final Dependencies dependencies) throws Throwable {
        return new SocketServer(dependencies.getGraphDatabaseService(),dependencies.getStringLogger());
    }
}
