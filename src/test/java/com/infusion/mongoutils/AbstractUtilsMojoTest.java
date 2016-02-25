package com.infusion.mongoutils;

import com.mongodb.MongoClient;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class AbstractUtilsMojoTest {
    @Test
    public void shouldReturnMongoClientInstanceUsingConfiguredUri() {
        // Given
        AbstractUtilsMojo.MongoClientFactory mongoClientFactory = mock(AbstractUtilsMojo.MongoClientFactory.class);
        MongoClient mongoClient = mock(MongoClient.class);
        when(mongoClientFactory.create(anyString())).thenReturn(mongoClient);
        String uri = "mongodb://localhost:27017";
        AbstractUtilsMojo mojo = new TestMojo(uri, mongoClientFactory);

        // When
        MongoClient result = mojo.getMongoClient();

        // Then
        assertThat(result, equalTo(mongoClient));
        verify(mongoClientFactory).create(uri);
    }

    @Test
    public void shouldExecute() throws MojoFailureException, MojoExecutionException {
        // Given
        String uri = "mongodb://localhost:27017";
        AbstractUtilsMojo mojo = spy(new TestMojo(uri, mock(AbstractUtilsMojo.MongoClientFactory.class)));

        // When
        mojo.execute();

        // Then
        verify(mojo).onExecute();
        verify(mojo, never()).onSkip();
    }

    @Test
    public void shouldSkipExecution() throws MojoFailureException, MojoExecutionException {
        // Given
        String uri = "mongodb://localhost:27017";
        AbstractUtilsMojo mojo = spy(new TestMojo(uri, false, mock(AbstractUtilsMojo.MongoClientFactory.class)));

        // When
        mojo.execute();

        // Then
        verify(mojo).onExecute();
        verify(mojo, never()).onSkip();
    }

    public static class TestMojo extends AbstractUtilsMojo {
        public TestMojo(String uri, MongoClientFactory mongoClientFactory) {
            super(uri, mongoClientFactory);
        }

        public TestMojo(String uri, boolean skip, MongoClientFactory mongoClientFactory) {
            super(uri, skip, mongoClientFactory);
        }

        @Override
        protected void onExecute() throws MojoExecutionException {}
    }
}
