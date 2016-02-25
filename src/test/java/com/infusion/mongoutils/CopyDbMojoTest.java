package com.infusion.mongoutils;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.util.Util;
import org.apache.maven.plugin.MojoExecutionException;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class CopyDbMojoTest {
    @Test
    public void shouldExecuteAgainstAdminDatabase() throws MojoExecutionException {
        // Given
        CopyDbMojo mojo = new CopyDbMojo(null, null, null, false, null, null);

        // When
        String result = mojo.getDatabaseName();

        // Then
        assertThat(result, equalTo("admin"));
    }

    @Test
    public void shouldExecuteCopyDbCommand() throws MojoExecutionException {
        // Given
        CopyDbMojo mojo = new CopyDbMojo("source", "target", "example.net", true, null, null);

        // When
        BasicDBObject result = mojo.getCommand();

        // Then
        assertThat(result, allOf(
                Matchers.<String, Object>hasEntry("copydb", 1),
                Matchers.<String, Object>hasEntry("fromdb", "source"),
                Matchers.<String, Object>hasEntry("todb", "target"),
                Matchers.<String, Object>hasEntry("fromhost", "example.net"),
                Matchers.<String, Object>hasEntry("slaveOk", true)));
    }

    @Test
    public void shouldIncludeUsernameKeyAndNonceIfCredentialsProvided() throws MojoExecutionException {
        // Given
        String key = Util.hexMD5(("testnoncetestuser" + Util.hexMD5("testuser:mongo:testpassword".getBytes())).getBytes());
        CopyDbMojo mojo = spy(new CopyDbMojo("source", "target", "example.net", true, "testuser", "testpassword"));
        CommandResult nonceResult = mock(CommandResult.class);
        when(nonceResult.getString("nonce")).thenReturn("testnonce");
        doReturn(nonceResult).when(mojo).executeCommand(anyString(), any(BasicDBObject.class));

        // When
        BasicDBObject result = mojo.getCommand();

        // Then
        assertThat(result, allOf(
                Matchers.<String, Object>hasEntry("username", "testuser"),
                Matchers.<String, Object>hasEntry("nonce", "testnonce"),
                Matchers.<String, Object>hasEntry("key", key)));

        ArgumentCaptor<BasicDBObject> commandCaptor = ArgumentCaptor.forClass(BasicDBObject.class);
        verify(mojo).executeCommand(eq("admin"), commandCaptor.capture());
        assertThat(commandCaptor.getValue(), allOf(
                Matchers.<String, Object>hasEntry("copydbgetnonce", 1),
                Matchers.<String, Object>hasEntry("fromhost", "example.net")));

    }
}
