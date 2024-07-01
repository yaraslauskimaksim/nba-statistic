package org.demo.dao;

import org.demo.AbstractDao;
import org.demo.dao.domain.PlayerDomain;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class PlayerDao extends AbstractDao {
  private static final String INSERT_OR_UPDATE_QUERY = """
    INSERT INTO Player (id, name, isActive) \
    VALUES (?, ?, ?) \
    ON CONFLICT (id) DO UPDATE SET name = ?, isActive = ?""";
  private static final String GET_BY_ID_QUERY = "SELECT * FROM Player WHERE id = ?";

  public PlayerDao(Connection connection) {
    super(connection);
  }

  public void createOrUpdate(PlayerDomain playerDomain) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement(INSERT_OR_UPDATE_QUERY)) {
      stmt.setObject(1, playerDomain.id());
      stmt.setString(2, playerDomain.name());
      stmt.setBoolean(3, playerDomain.isActive());

      stmt.setString(4, playerDomain.name());
      stmt.setBoolean(5, playerDomain.isActive());

      stmt.executeUpdate();
    }
  }

  public Optional<PlayerDomain> getById(UUID id) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement(GET_BY_ID_QUERY)) {
      stmt.setObject(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(new PlayerDomain(
            (UUID) rs.getObject("id"),
            rs.getString("name"),
            rs.getBoolean("isActive")
          ));
        }
      }
    }
    return Optional.empty();
  }
}
