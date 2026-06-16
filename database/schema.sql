CREATE TABLE IF NOT EXISTS users (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    role        VARCHAR(20)  NOT NULL CHECK (role IN ('TEACHER','STUDENT')),
    password_hash VARCHAR(255) NOT NULL,
    created_at  TIMESTAMPTZ  DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS courses (
    id         BIGSERIAL PRIMARY KEY,
    date       DATE         NOT NULL,
    title      VARCHAR(255),
    content    TEXT,
    updated_at TIMESTAMPTZ  DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS messages (
    id         BIGSERIAL PRIMARY KEY,
    course_id  BIGINT       NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    user_id    BIGINT       NOT NULL REFERENCES users(id),
    type       VARCHAR(20)  NOT NULL CHECK (type IN ('general','suggestion','help','like','done')),
    text       TEXT         NOT NULL,
    resolved   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ  DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS replies (
    id         BIGSERIAL PRIMARY KEY,
    message_id BIGINT       NOT NULL REFERENCES messages(id) ON DELETE CASCADE,
    user_id    BIGINT       NOT NULL REFERENCES users(id),
    text       TEXT         NOT NULL,
    created_at TIMESTAMPTZ  DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_courses_date      ON courses(date);
CREATE INDEX IF NOT EXISTS idx_messages_course   ON messages(course_id);
CREATE INDEX IF NOT EXISTS idx_replies_message   ON replies(message_id);
