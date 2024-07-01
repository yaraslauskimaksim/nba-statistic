package org.demo.dao;

import org.demo.AbstractDao;
import org.demo.dao.domain.AverageStatisticDomain;
import org.demo.dao.domain.StatisticDomain;
import org.demo.dao.domain.StatisticTypeDomain;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StatisticDao extends AbstractDao {
  private static final String INSERT_OR_UPDATE_QUERY = """
    INSERT INTO statistic (gameId, playerId, teamId, type, count, minutes, actionDateTime, eventDateTime)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?) \
    ON CONFLICT (gameId, playerId, teamId, type) \
    DO UPDATE SET count = ?, minutes = ?, actionDateTime = ?, eventDateTime = ?
    """;
  private static final String GET_AVERAGE_BY_PLAYER_ID = """
    select
           pl.id           as player_id,
           pl.name,
           st.type,
           avg(st.count)   as average_count,
           avg(st.minutes) as average_minutes
    from player pl
             join statistic st on st.playerid = pl.id
    where pl.id = ?
      and eventdatetime between ? and ?
    group by pl.id, type""";
  private static final String GET_AVERAGE_BY_TEAM_ID = """
    select
           tm.id           as team_id,
           tm.name,
           st.type,
           avg(st.count)   as average_count,
           avg(st.minutes) as average_minutes
    from team tm
             join statistic st on st.teamid = tm.id
    where tm.id = ?
      and eventdatetime between ? and ?
    group by tm.id, type""";

  public StatisticDao(Connection connection) {
    super(connection);
  }

  public void createOrUpdate(StatisticDomain statisticDomain) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement(INSERT_OR_UPDATE_QUERY)) {
      stmt.setObject(1, statisticDomain.gameId());
      stmt.setObject(2, statisticDomain.playerId());
      stmt.setObject(3, statisticDomain.teamId());
      stmt.setString(4, statisticDomain.type());
      stmt.setObject(5, statisticDomain.count(), Types.INTEGER);
      stmt.setObject(6, statisticDomain.minutes(), Types.FLOAT);
      stmt.setLong(7, statisticDomain.actionDateTime());
      stmt.setLong(8, statisticDomain.eventDateTime());

      stmt.setObject(9, statisticDomain.count(), Types.INTEGER);
      stmt.setObject(10, statisticDomain.minutes(), Types.FLOAT);
      stmt.setLong(11, statisticDomain.actionDateTime());
      stmt.setLong(12, statisticDomain.eventDateTime());

      stmt.executeUpdate();
    }
  }

  public List<AverageStatisticDomain> getAverageStatisticByTeamIdAndEventDateTime(
    UUID teamId,
    Long from,
    Long to
  ) throws SQLException {
    var statistics = new ArrayList<AverageStatisticDomain>();
    try (PreparedStatement stmt = connection.prepareStatement(GET_AVERAGE_BY_TEAM_ID)) {
      stmt.setObject(1, teamId);
      stmt.setLong(2, from);
      stmt.setLong(3, to);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          statistics.add(new AverageStatisticDomain(
            (UUID) rs.getObject("team_id"),
            rs.getString("name"),
            StatisticTypeDomain.valueOf(rs.getString("type")),
            rs.getDouble("average_count"),
            rs.getDouble("average_minutes")
          ));
        }
      }
    }
    return statistics;
  }

  public List<AverageStatisticDomain> getAverageStatisticByPlayerIdAndEventDateTime(
    UUID playerId,
    Long from,
    Long to
  ) throws SQLException {
    var statistics = new ArrayList<AverageStatisticDomain>();
    try (PreparedStatement stmt = connection.prepareStatement(GET_AVERAGE_BY_PLAYER_ID)) {
      stmt.setObject(1, playerId);
      stmt.setLong(2, from);
      stmt.setLong(3, to);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          statistics.add(new AverageStatisticDomain(
            (UUID) rs.getObject("player_id"),
            rs.getString("name"),
            StatisticTypeDomain.valueOf(rs.getString("type")),
            rs.getDouble("average_count"),
            rs.getDouble("average_minutes")
          ));
        }
      }
    }
    return statistics;
  }
}
