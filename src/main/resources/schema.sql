CREATE TABLE IF NOT EXISTS personas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS inscripciones (
    persona_id BIGINT NOT NULL,
    bootcamp_id BIGINT NOT NULL,
    PRIMARY KEY (persona_id, bootcamp_id)
);