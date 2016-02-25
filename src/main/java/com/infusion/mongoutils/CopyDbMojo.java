package com.infusion.mongoutils;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * When invoked, this goal connects to the mongo instance and copies the specified database from the source instance using
 * <a href="https://docs.mongodb.org/manual/reference/command/copydb/">copydb</a>.
 */
@Mojo(name = "copydb", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class CopyDbMojo extends AbstractCommandMojo {
    /**
     * Name of the source database.
     */
    @Parameter(required = true)
    private String fromdb;

    /**
     * Name of the target database.
     */
    @Parameter(required = true)
    private String todb;

    /**
     * Optional. The source instance host in format: host[:port].
     */
    @Parameter(required = false)
    private String fromhost;

    /**
     * Optional. Set slaveOK to true to allow copydb to copy data from secondary members as well as the primary. fromhost must also be set.
     */
    @Parameter(required = false, defaultValue = "false")
    private boolean slaveOk;

    /**
     * Optional. The name of the user on the fromhost MongoDB instance. The user authenticates to the fromdb.
     */
    @Parameter(required = false)
    private String username;

    /**
     * Optional. The password on the fromhost for authentication. The method does not transmit the password in plaintext.
     */
    @Parameter(required = false)
    private String password;

    @Override
    protected String getDatabaseName() throws MojoExecutionException {
        return "admin";
    }

    @Override
    protected BasicDBObject getCommand() throws MojoExecutionException {
        final BasicDBObject command = new BasicDBObject();
        command.put("copydb", 1);
        command.put("fromdb", fromdb);
        command.put("todb", todb);
        command.put("slaveOk", slaveOk);

        if (!StringUtils.isEmpty(fromhost)) {
            command.put("fromhost", fromhost);
        }

        if (!StringUtils.isEmpty(username)) {
            BasicDBObject nonceCommand = new BasicDBObject();
            nonceCommand.put("copydbgetnonce", 1);
            nonceCommand.put("fromhost", fromhost);
            final CommandResult nonceResult = executeCommand("admin", nonceCommand);
            final String nonce = nonceResult.getString("nonce");
            final byte[] innerHex = (username + ":mongo:" + password).getBytes();
            final byte[] outerHex = (nonce + username + Util.hexMD5(innerHex)).getBytes();

            command.put("username", username);
            command.put("nonce", nonce);
            command.put("key", Util.hexMD5(outerHex));
        }

        return command;
    }

    public CopyDbMojo() {}

    CopyDbMojo(String fromdb, String todb, String fromhost, boolean slaveOk, String username, String password) {
        this.fromdb = fromdb;
        this.todb = todb;
        this.fromhost = fromhost;
        this.slaveOk = slaveOk;
        this.username = username;
        this.password = password;
    }
}
