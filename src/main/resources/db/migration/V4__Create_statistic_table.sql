CREATE TABLE Statistic
(
    gameId         UUID        NOT NULL,
    playerId       UUID        NOT NULL,
    teamId         UUID        NOT NULL,
    type           VARCHAR(50) NOT NULL,
    count          INTEGER,
    minutes        FLOAT,
    actionDateTime BIGINT      NOT NULL,
    eventDateTime  BIGINT      NOT NULL,
    PRIMARY KEY (gameId, playerId, teamId, type),
    FOREIGN KEY (gameId) REFERENCES Game (gameId),
    FOREIGN KEY (playerId) REFERENCES Player (id),
    FOREIGN KEY (teamId) REFERENCES Team (id)
);
