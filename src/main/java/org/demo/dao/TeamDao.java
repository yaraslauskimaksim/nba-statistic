package org.demo.dao;

import org.demo.AbstractDao;
import org.demo.dao.domain.TeamDomain;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class TeamDao extends AbstractDao {
  private static final String INSERT_OR_UPDATE_QUERY = """
    INSERT INTO Team (id, name, isActive) \
    VALUES (?, ?, ?) \
    ON CONFLICT (id) DO UPDATE SET name = ?, isActive = ?""";
  private static final String GET_BY_ID_QUERY = "SELECT * FROM Team WHERE id = ?";

  public TeamDao(Connection connection) {
    super(connection);
  }

  public void createOrUpdate(TeamDomain teamDomain) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement(INSERT_OR_UPDATE_QUERY)) {
      stmt.setObject(1, teamDomain.id());
      stmt.setString(2, teamDomain.name());
      stmt.setBoolean(3, teamDomain.isActive());

      stmt.setString(4, teamDomain.name());
      stmt.setBoolean(5, teamDomain.isActive());

      stmt.executeUpdate();
    }
  }

  public Optional<TeamDomain> getById(UUID id) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement(GET_BY_ID_QUERY)) {
      stmt.setObject(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(new TeamDomain(
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
