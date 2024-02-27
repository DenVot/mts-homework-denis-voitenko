CREATE SCHEMA IF NOT EXISTS books_service;

CREATE TABLE IF NOT EXISTS books_service.authors (
    id BIGSERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS books_service.books (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGINT REFERENCES authors(id) NOT NULL,
    title TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS books_service.tags (
    id BIGSERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS books_service.books_tags (
    book_id BIGINT REFERENCES books(id) ON DELETE CASCADE NOT NULL,
    tag_id BIGINT REFERENCES tags(id) ON DELETE CASCADE  NOT NULL,
    PRIMARY KEY (book_id, tag_id)
);
