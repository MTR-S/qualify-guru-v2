-- V1__init_schema.sql
CREATE TABLE event_publication (
    id VARCHAR(36) NOT NULL,
    listener_id VARCHAR(512) NOT NULL,
    event_type VARCHAR(512) NOT NULL,
    serialized_event VARCHAR(4000) NOT NULL,
    publication_date TIMESTAMP NOT NULL,
    completion_date TIMESTAMP,
    status VARCHAR(255) NOT NULL,
    completion_attempts INTEGER NOT NULL,
    last_resubmission_date TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE stat_optimization_metrics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_title VARCHAR(255) NOT NULL,
    compatibility_percentage INT NOT NULL,
    optimized_at TIMESTAMP NOT NULL
);

CREATE TABLE auth_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE cand_candidates (
    id BIGINT PRIMARY KEY
);

CREATE TABLE cand_saved_resumes (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    title VARCHAR(255) NOT NULL,
                                    s3_file_url VARCHAR(500) NOT NULL,
                                    saved_at TIMESTAMP NOT NULL,
                                    candidate_id BIGINT NOT NULL,
                                    CONSTRAINT fk_candidate FOREIGN KEY (candidate_id) REFERENCES cand_candidates(id)
);