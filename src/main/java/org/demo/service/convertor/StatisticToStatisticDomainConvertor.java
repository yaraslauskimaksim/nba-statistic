package org.demo.service.convertor;

import org.demo.dao.domain.StatisticDomain;
import org.demo.kafka.model.Statistic;

import java.time.ZoneOffset;

public class StatisticToStatisticDomainConvertor implements Convertor<Statistic, StatisticDomain> {

  @Override
  public StatisticDomain convert(Statistic from) {
    return new StatisticDomain(
      from.getGameId(),
      from.getPlayerId(),
      from.getTeamId(),
      from.getType().name(),
      from.getCount(),
      from.getMinutes(),
      from.getActionDateTime().toEpochSecond(ZoneOffset.UTC),
      from.getActionDateTime().toEpochSecond(ZoneOffset.UTC)
    );
  }
}
