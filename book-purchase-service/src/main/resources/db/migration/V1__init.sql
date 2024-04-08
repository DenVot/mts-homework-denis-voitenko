CREATE TABLE wallet (
    id SERIAL PRIMARY KEY,
    balance INTEGER NOT NULL CHECK (balance >= 0)
);

CREATE TABLE outbox (
    id BIGSERIAL PRIMARY KEY,
    data TEXT NOT NULL
);
