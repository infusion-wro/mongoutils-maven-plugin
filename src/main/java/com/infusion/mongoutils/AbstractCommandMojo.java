package com.infusion.mongoutils;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * When invoked, this goal connects to the mongo instance and executes a mongo command.
 */
public abstract class AbstractCommandMojo extends AbstractUtilsMojo {
    /**
     * Returns command that should be executed.
     *
     * @return Command for execution
     * @throws MojoExecutionException when errors occur while creating command object
     */
    protected abstract BasicDBObject getCommand() throws MojoExecutionException;

    /**
     * Returns the name of the database on which the command should be executed.
     *
     * @return Database name to use for command
     * @throws MojoExecutionException when errors occur while obtaining database name
     */
    protected abstract String getDatabaseName() throws MojoExecutionException;

    @Override
    protected void onExecute() throws MojoExecutionException {
        executeCommand(getDatabaseName(), getCommand());
    }

    protected CommandResult executeCommand(String databaseName, BasicDBObject command) throws MojoExecutionException {
        MongoClient mongo = getMongoClient();
        CommandResult result;
        try {
            result = mongo.getDB(databaseName).command(command);
        } catch (MongoException e) {
            throw new MojoExecutionException(String.format("Unable to execute command %s", command.toJson()), e);
        }

        if (!result.ok()) {
            getLog().error(String.format("Unable to execute command: %s %s", result.getErrorMessage(), command.toJson()));
            throw new MojoExecutionException(String.format("Unable to execute command: %s %s", result.getErrorMessage(), command.toJson()), result.getException());
        }

        return result;
    }
}
