package com.infusion.mongoutils;

import com.mongodb.BasicDBObject;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * When invoked, this goal connects to the mongo instance and clones the specified database from the source instance using
 * <a href="https://docs.mongodb.org/manual/reference/command/clone/">clone</a>.
 */
@Mojo(name = "clone", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class CloneMojo extends AbstractCommandMojo {
    /**
     * The database to clone.
     */
    @Parameter(required = true)
    private String database;

    /**
     * The source instance host in format: host[:port].
     */
    @Parameter(required = true)
    private String clone;

    @Override
    protected BasicDBObject getCommand() throws MojoExecutionException {
        return new BasicDBObject("clone", clone);
    }

    @Override
    protected String getDatabaseName() throws MojoExecutionException {
        return database;
    }

    public CloneMojo() {}

    CloneMojo(String database, String clone) {
        this.database = database;
        this.clone = clone;
    }
}
