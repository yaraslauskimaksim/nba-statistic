package org.demo.dao;

import org.demo.AbstractDao;
import org.demo.dao.domain.GameDomain;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class GameDao extends AbstractDao {
  private static final String INSERT_OR_UPDATE_QUERY = """
    INSERT INTO Game (gameId, duration, eventDateTime) \
    VALUES (?, ?, ?) \
    ON CONFLICT (gameId) DO UPDATE SET duration = ?, eventDateTime = ?""";
  private static final String GET_BY_ID_QUERY = "SELECT * FROM Game WHERE gameId = ?";

  public GameDao(Connection connection) {
    super(connection);
  }

  public void createOrUpdate(GameDomain gameDomain) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement(INSERT_OR_UPDATE_QUERY)) {
      stmt.setObject(1, gameDomain.gameId());
      stmt.setInt(2, gameDomain.duration());
      stmt.setLong(3, gameDomain.eventDateTime());

      stmt.setInt(4, gameDomain.duration());
      stmt.setLong(5, gameDomain.eventDateTime());

      stmt.executeUpdate();
    }
  }

  public Optional<GameDomain> getById(UUID gameId) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement(GET_BY_ID_QUERY)) {
      stmt.setObject(1, gameId);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(new GameDomain(
            (UUID) rs.getObject("gameId"),
            rs.getInt("duration"),
            rs.getLong("eventDateTime")
          ));
        }
      }
    }
    return Optional.empty();
  }
}
