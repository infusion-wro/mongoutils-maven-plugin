package com.infusion.mongoutils;

import com.mongodb.BasicDBObject;
import org.apache.maven.plugin.MojoExecutionException;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CloneMojoTest {
    @Test
    public void shouldExecuteAgainstTargetDatabase() throws MojoExecutionException {
        // Given
        CloneMojo mojo = new CloneMojo("testdb", null);

        // When
        String result = mojo.getDatabaseName();

        // Then
        assertThat(result, equalTo("testdb"));
    }

    @Test
    public void shouldExecuteCloneCommand() throws MojoExecutionException {
        // Given
        CloneMojo mojo = new CloneMojo("testdb", "example.net");

        // When
        BasicDBObject result = mojo.getCommand();

        // Then
        assertThat(result, Matchers.<String, Object>hasEntry("clone", "example.net"));
    }
}
