package dev.ikwattro.neo4j.experiments;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseAliasChangeExperiment extends Neo4jIntegrationTest {

    @Test
    public void testChangingDatabaseAlias() {
        executeSystemQuery("CREATE DATABASE hello");
        executeSystemQuery("CREATE DATABASE goodbye");
        executeQueryOnDatabase("UNWIND range(1, 10) AS i CREATE (n:Person)", Map.of(), "hello");
        executeQueryOnDatabase("UNWIND range(1, 10) AS i CREATE (n:Movie)", Map.of(), "goodbye");
        executeSystemQuery("CREATE ALIAS prod IF NOT EXISTS FOR DATABASE hello");
        // Testing changing alias and effect in session
        try (Session session = driver.session(SessionConfig.forDatabase("prod"))) {
            assertEquals(10, session.run("MATCH (n:Person) RETURN n", Map.of()).list().size());
            executeSystemQuery("ALTER ALIAS prod SET DATABASE TARGET goodbye");
            assertEquals(0, session.run("MATCH (n:Person) RETURN n", Map.of()).list().size());
            assertEquals(10, session.run("MATCH (n:Movie) RETURN n", Map.of()).list().size());
        }
    }
}
