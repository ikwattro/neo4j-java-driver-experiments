package dev.ikwattro.neo4j.experiments;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImpersonationExperiment extends Neo4jIntegrationTest {

    @Test
    public void test_impersonate_as_user1() {
        String user1Q = executeQueryAs("SHOW CURRENT USER", "user1").get(0).get("user").asString();
        assertThat(user1Q).isEqualTo("user1");

        String user2Q = executeQueryAs("SHOW CURRENT USER", "user2").get(0).get("user").asString();
        assertThat(user2Q).isEqualTo("user2");
    }
}
