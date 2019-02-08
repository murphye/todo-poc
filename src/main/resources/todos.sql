CREATE TABLE todo(
 id UUID PRIMARY KEY,
 title VARCHAR (50) UNIQUE NOT NULL,
 "order" VARCHAR (50) UNIQUE NOT NULL,
 completed BOOLEAN NOT NULL
);

INSERT INTO todo (id, title, "order", completed) VALUES ('31569142-70e4-4820-b138-3cc0d095a2f8', 'Implement a reactive repository', 1, false);