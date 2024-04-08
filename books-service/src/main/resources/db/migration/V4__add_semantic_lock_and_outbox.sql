CREATE TYPE purchasing_status AS ENUM ('NONE', 'IN_PROCESS', 'DONE');
CREATE CAST (varchar AS purchasing_status) WITH INOUT AS IMPLICIT;

ALTER TABLE books
ADD COLUMN purchasing_status purchasing_status NOT NULL DEFAULT 'NONE';

CREATE TABLE outbox (
    id BIGSERIAL PRIMARY KEY,
    data TEXT NOT NULL
);
