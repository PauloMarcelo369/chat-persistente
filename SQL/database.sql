CREATE TABLE users (
    username VARCHAR(255) PRIMARY KEY
);

CREATE TABLE messages (
    sender VARCHAR(255),
    receiver VARCHAR(255),
    content TEXT,
    timestamp TIMESTAMP
);