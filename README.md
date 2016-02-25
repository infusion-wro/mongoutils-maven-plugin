mongoutils-maven-plugin
=======================

This plugin lets you run the following commands against a MongoDB instance during a Maven build:
- [copydb](https://docs.mongodb.org/manual/reference/command/copydb/)
- [clone](https://docs.mongodb.org/manual/reference/command/clone/)
- [cloneCollection](https://docs.mongodb.org/manual/reference/command/cloneCollection/)

Inspired by [embedmongo-maven-plugin](https://github.com/joelittlejohn/embedmongo-maven-plugin) and can be used with that plugin to provide greater flexibility when setting up an embedded mongo instance with data.

Usage
-----

```xml
<plugin>
  <groupId>com.infusion</groupId>
  <artifactId>mongoutils-maven-plugin</artifactId>
  <version>${version}</version>
  <executions>
    <execution>
      <id>copydb</id>
      <goals>
        <goal>copydb</goal>
      </goals>
      <configuration>
        <fromdb>reporting</fromdb>
        <!-- name of the source database -->

        <todb>reporting_copy</todb>
        <!-- name of the target database -->

        <fromhost>example.net</fromhost>
        <!-- optional, hostname of the source mongod instance, omit to copy databases within the same mongod instance -->

        <slaveOk>true</slaveOk>
        <!-- optional, default is false, set to true to allow copydb to copy data from secondary members as well as the primary. fromhost must also be set. -->

        <username>reportUser</username>
        <!-- optional, name of the user on the fromhost MongoDB instance, user authenticates to the fromdb -->

        <password>abc123</password>
        <!-- optional, password on the fromhost for authentication -->

        <uri>mongodb://user:password@localhost:27017</uri>
        <!-- optional, will default to the local instance on port 27017 with no credentials -->

        <skip>false</skip>
        <!-- optional, skips this plugin entirely, use on the command line with -Dmongoutils.skip -->
      </configuration>
    </execution>
    <execution>
      <id>clone</id>
      <goals>
        <goal>clone</goal>
      </goals>
      <configuration>
        <database>reporting</database>
        <!-- name of the database to clone -->

        <clone>example.net</clone>
        <!-- hostname of the source mongod instance -->

        <uri>mongodb://user:password@localhost:27017</uri>
        <!-- optional, will default to the local instance on port 27017 with no credentials -->

        <skip>false</skip>
        <!-- optional, skips this plugin entirely, use on the command line with -Dmongoutils.skip -->
      </configuration>
    </execution>
    <execution>
      <id>cloneCollection</id>
      <goals>
        <goal>cloneCollection</goal>
      </goals>
      <configuration>
        <cloneCollection>example.net</cloneCollection>
        <!-- namespace of the collection to clone -->

        <from>example.net</from>
        <!-- hostname of the source mongod instance -->

        <query>{ active: true }</query>
        <!-- optional, query that filters the documents in the source collection -->

        <uri>mongodb://user:password@localhost:27017</uri>
        <!-- optional, will default to the local instance on port 27017 with no credentials -->

        <skip>false</skip>
        <!-- optional, skips this plugin entirely, use on the command line with -Dmongoutils.skip -->
      </configuration>
    </execution>
    <execution>
      <id>stop</id>
      <goals>
        <goal>stop</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

Notes
-----

* By default, all goals are bound to the `pre-integration-test` phase