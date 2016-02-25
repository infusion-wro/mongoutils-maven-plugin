package com.infusion.mongoutils;

import com.mongodb.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class AbstractCommandMojoTest {
    @Test
    public void shouldExecuteCommandAgainstDatabase() throws MojoFailureException, MojoExecutionException {
        // Given
        String databaseName = "testdb";
        BasicDBObject command = mock(BasicDBObject.class);
        MongoClient mongoClient = mock(MongoClient.class);
        AbstractCommandMojo mojo = spy(new TestMojo(databaseName, command));
        doReturn(mongoClient).when(mojo).getMongoClient();

        DB db = mock(DB.class);
        when(mongoClient.getDB(databaseName)).thenReturn(db);

        CommandResult result = mock(CommandResult.class);
        when(db.command(any(DBObject.class))).thenReturn(result);
        when(result.ok()).thenReturn(true);

        // When
        mojo.execute();

        // Then
        verify(db).command(command);
    }

    @Test(expected = MojoExecutionException.class)
    public void shouldThrowMojoExecutionExceptionWhenCommandThrows() throws MojoFailureException, MojoExecutionException {
        // Given
        String databaseName = "testdb";
        BasicDBObject command = mock(BasicDBObject.class);
        MongoClient mongoClient = mock(MongoClient.class);
        AbstractCommandMojo mojo = spy(new TestMojo(databaseName, command));
        doReturn(mongoClient).when(mojo).getMongoClient();

        DB db = mock(DB.class);
        when(mongoClient.getDB(databaseName)).thenReturn(db);
        when(db.command(any(DBObject.class))).thenThrow(MongoException.class);

        // When
        mojo.execute();

        // Then
        fail();
    }

    @Test(expected = MojoExecutionException.class)
    public void shouldThrowMojoExecutionExceptionWhenCommandFails() throws MojoFailureException, MojoExecutionException {
        // Given
        String databaseName = "testdb";
        BasicDBObject command = mock(BasicDBObject.class);
        MongoClient mongoClient = mock(MongoClient.class);
        AbstractCommandMojo mojo = spy(new TestMojo(databaseName, command));
        doReturn(mongoClient).when(mojo).getMongoClient();

        DB db = mock(DB.class);
        when(mongoClient.getDB(databaseName)).thenReturn(db);

        CommandResult result = mock(CommandResult.class);
        when(db.command(any(DBObject.class))).thenReturn(result);
        when(result.ok()).thenReturn(false);

        // When
        mojo.execute();

        // Then
        fail();
    }

    public static class TestMojo extends AbstractCommandMojo {
        private final String databaseName;
        private final BasicDBObject command;

        public TestMojo(String databaseName, BasicDBObject command) {
            this.databaseName = databaseName;
            this.command = command;
        }

        @Override
        protected BasicDBObject getCommand() throws MojoExecutionException {
            return command;
        }

        @Override
        protected String getDatabaseName() throws MojoExecutionException {
            return databaseName;
        }
    }
}
