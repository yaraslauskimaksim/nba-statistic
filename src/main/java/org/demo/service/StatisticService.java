package org.demo.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.demo.dao.StatisticDao;
import org.demo.dao.domain.AverageStatisticDomain;
import org.demo.dao.domain.StatisticTypeDomain;
import org.demo.kafka.model.Statistic;
import org.demo.kafka.validation.FailSafeValidator;
import org.demo.service.convertor.StatisticToStatisticDomainConvertor;
import org.demo.web.model.AverageStatisticDto;
import org.demo.web.model.StatisticByTypeDto;
import org.demo.web.model.StatisticTypeDto;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class StatisticService {
  private static final Logger log = LogManager.getLogger(StatisticService.class);

  private final StatisticDao statisticDao;
  private final FailSafeValidator<Boolean, Statistic> validators;

  public void createOrUpdate(Statistic statistic) {
    try {
      if (validators.validate(statistic)) {
        statisticDao.createOrUpdate(new StatisticToStatisticDomainConvertor().convert(statistic));
      }
    } catch (SQLException e) {
      log.debug(e);
    }
  }

  public AverageStatisticDto getAverageStatisticByTeamIdAndEventDateTime(
    UUID teamId,
    Long from,
    Long to
  ) {
    try {
      List<AverageStatisticDomain> statistic =
        statisticDao.getAverageStatisticByTeamIdAndEventDateTime(teamId, from, to);
      return getAverageStatisticByIdAndEventDateTime(teamId, statistic);
    } catch (SQLException e) {
      log.debug(e);
      return null;
    }
  }

  public AverageStatisticDto getAverageStatisticByPlayerIdAndEventDateTime(
    UUID playerId,
    Long from,
    Long to
  ) {
    try {
      List<AverageStatisticDomain> statistic =
        statisticDao.getAverageStatisticByPlayerIdAndEventDateTime(playerId, from, to);
      return getAverageStatisticByIdAndEventDateTime(playerId, statistic);
    } catch (SQLException e) {
      log.debug(e);
      return null;
    }
  }

  private AverageStatisticDto getAverageStatisticByIdAndEventDateTime(UUID id, List<AverageStatisticDomain> statistic) {
    var builder = AverageStatisticDto.builder()
      .id(id);
    statistic.forEach(statisticDomain -> builder.name(statisticDomain.name())
      .statistic(new StatisticByTypeDto(
        StatisticTypeDto.valueOf(statisticDomain.type().name()),
        statisticDomain.type() == StatisticTypeDomain.MINUTES_PLAYED ?
          statisticDomain.averageMinutes() : statisticDomain.averageCount()
      )));
    return builder.build();
  }
}
