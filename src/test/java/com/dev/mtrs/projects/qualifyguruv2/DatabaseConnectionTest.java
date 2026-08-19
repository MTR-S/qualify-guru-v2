package com.dev.mtrs.projects.qualifyguruv2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class DatabaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldConnectAndExecuteQuery() {
        // Arrange & Act: A simple ping to the database
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

        // Assert: If this equals 1, the connection is fully established
        assertThat(result).isEqualTo(1);
    }

}
