CREATE TABLE Game
(
    gameId        UUID PRIMARY KEY,
    duration      INTEGER NOT NULL,
    eventDateTime BIGINT  NOT NULL
);
