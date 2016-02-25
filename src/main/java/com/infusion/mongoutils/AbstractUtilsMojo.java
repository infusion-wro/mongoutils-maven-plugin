package com.infusion.mongoutils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Base class for all Mojos.
 * Provides skip functionality and a factory for creating a MongoClient instance.
 */
public abstract class AbstractUtilsMojo extends AbstractMojo {
    /**
     * The uri used to connect to the mongo instance. The format of the uri is:
     * mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
     */
    @Parameter(property = "mongoutils.uri", required = false, defaultValue = "mongodb://localhost:27017")
    private String uri;

    /**
     * Determines whether the execution of the mojo should be skipped.
     */
    @Parameter(property = "mongoutils.skip", defaultValue = "false")
    private boolean skip;

    private final MongoClientFactory mongoClientFactory;

    protected AbstractUtilsMojo() {
        this.mongoClientFactory = new MongoClientFactory();
    }

    protected AbstractUtilsMojo(String uri, MongoClientFactory mongoClientFactory) {
        this(uri, false, mongoClientFactory);
    }

    protected AbstractUtilsMojo(String uri, boolean skip, MongoClientFactory mongoClientFactory) {
        this.uri = uri;
        this.skip = skip;
        this.mongoClientFactory = mongoClientFactory;
    }

    public final void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            onSkip();
        } else {
            onExecute();
        }
    }

    protected void onSkip() {
    }

    protected MongoClient getMongoClient() {
        return mongoClientFactory.create(uri);
    }

    protected abstract void onExecute() throws MojoExecutionException;

    public static class MongoClientFactory {
        public MongoClient create(String uri) {
            return new MongoClient(new MongoClientURI(uri));
        }
    }
}
