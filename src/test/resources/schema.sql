-- Spring Modulith Outbox Table
CREATE TABLE IF NOT EXISTS event_publication (
                                                 id VARCHAR(36) NOT NULL,
    listener_id VARCHAR(512) NOT NULL,
    event_type VARCHAR(512) NOT NULL,
    serialized_event VARCHAR(4000) NOT NULL,
    publication_date TIMESTAMP(6) NOT NULL,
    completion_date TIMESTAMP(6),
    status VARCHAR(255) NOT NULL,
    completion_attempts INTEGER NOT NULL,
    last_resubmission_date TIMESTAMP(6),
    PRIMARY KEY (id)
    );

-- Statistics Module Table
CREATE TABLE IF NOT EXISTS stat_optimization_metrics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_title VARCHAR(255) NOT NULL,
    compatibility_percentage INT NOT NULL,
    optimized_at TIMESTAMP(6) NOT NULL
    );