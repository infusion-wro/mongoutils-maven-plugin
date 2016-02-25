package com.infusion.mongoutils;

import com.mongodb.BasicDBObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * When invoked, this goal connects to the mongo instance and clones the specified database from the source instance using
 * <a href="https://docs.mongodb.org/manual/reference/command/cloneCollection/">cloneCollection</a>.
 */
@Mojo(name = "cloneCollection", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class CloneCollectionMojo extends AbstractCommandMojo {
    /**
     * The namespace of the collection to clone. The namespace is a combination of the database name and the name of the collection.
     */
    @Parameter(required = true)
    private String cloneCollection;

    /**
     * The address of the server to clone from.
     */
    @Parameter(required = true)
    private String from;

    /**
     * Optional. A query that filters the documents in the source collection that cloneCollection will copy to the current database.
     */
    @Parameter(required = false)
    private String query;

    @Override
    protected BasicDBObject getCommand() throws MojoExecutionException {
        BasicDBObject command = new BasicDBObject();
        command.put("cloneCollection", cloneCollection);
        command.put("from", from);
        if (!StringUtils.isEmpty(query)) {
            command.put("query", BasicDBObject.parse(query));
        }

        return command;
    }

    @Override
    protected String getDatabaseName() throws MojoExecutionException {
        return "admin";
    }

    public CloneCollectionMojo() {
    }

    CloneCollectionMojo(String cloneCollection, String from, String query) {
        this.cloneCollection = cloneCollection;
        this.from = from;
        this.query = query;
    }
}
