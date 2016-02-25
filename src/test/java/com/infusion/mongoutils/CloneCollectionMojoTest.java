package com.infusion.mongoutils;

import com.mongodb.BasicDBObject;
import org.apache.maven.plugin.MojoExecutionException;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.AllOf.allOf;

public class CloneCollectionMojoTest {
    @Test
    public void shouldExecuteAgainstAdminDatabase() throws MojoExecutionException {
        // Given
        CloneCollectionMojo mojo = new CloneCollectionMojo(null, null, null);

        // When
        String result = mojo.getDatabaseName();

        // Then
        assertThat(result, equalTo("admin"));
    }

    @Test
    public void shouldExecuteCloneCommand() throws MojoExecutionException {
        // Given
        CloneCollectionMojo mojo = new CloneCollectionMojo("testdb.testcol", "example.net", null);

        // When
        BasicDBObject result = mojo.getCommand();

        // Then
        assertThat(result, allOf(
                Matchers.<String, Object>hasEntry("cloneCollection", "testdb.testcol"),
                Matchers.<String, Object>hasEntry("from", "example.net")));
    }

    @Test
    public void shouldParseQuery() throws MojoExecutionException {
        // Given
        CloneCollectionMojo mojo = new CloneCollectionMojo("testdb.testcol", "example.net", "{active:true}");

        // When
        BasicDBObject result = mojo.getCommand();

        // Then
        assertThat((Map<String, Object>)result.get("query"), hasEntry("active", (Object)true));
    }
}
