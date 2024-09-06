-- User 테이블 생성
CREATE TABLE user (
                      id VARCHAR(50) PRIMARY KEY,
                      username VARCHAR(50) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Compliment 테이블 생성
CREATE TABLE compliment (
                            id VARCHAR(50) PRIMARY KEY,
                            sender_id VARCHAR(50)  NOT NULL,
                            receiver_id VARCHAR(50)  NOT NULL,
                            content TEXT NOT NULL,
                            is_anonymous BOOLEAN NOT NULL DEFAULT FALSE,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (sender_id) REFERENCES users(id),
                            FOREIGN KEY (receiver_id) REFERENCES users(id)
);

-- 인덱스 생성 (성능 최적화)
CREATE INDEX idx_compliments_sender ON compliments(sender_id);
CREATE INDEX idx_compliments_receiver ON compliments(receiver_id);
CREATE INDEX idx_compliments_created_at ON compliments(created_at);
