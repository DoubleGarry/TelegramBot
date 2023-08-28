
-- changeset semenikhin:1
CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY,
                       chat_id BIGINT,
                       text TEXT,
                       task_date_time TIMESTAMP
);