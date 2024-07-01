package org.demo.kafka.model;

import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Getter
public class Statistic {
  private final UUID gameId;
  private final UUID playerId;
  private final UUID teamId;
  private final StatisticType type;
  private final Integer count;
  private final Float minutes;
  private final LocalDateTime actionDateTime;
  private final LocalDateTime eventDateTime;

  public Statistic(
    UUID gameId,
    UUID playerId,
    UUID teamId,
    StatisticType type,
    Integer count,
    Float minutes,
    LocalDateTime actionDateTime,
    LocalDateTime eventDateTime
  ) {
    this.gameId = gameId;
    this.playerId = playerId;
    this.teamId = teamId;
    this.type = type;
    this.count = count;
    this.minutes = minutes;
    this.actionDateTime = actionDateTime;
    this.eventDateTime = eventDateTime;
  }

  public Statistic(String[] args) {
    this.gameId = UUID.fromString(args[0]);
    this.playerId = UUID.fromString(args[1]);
    this.teamId = UUID.fromString(args[2]);
    this.type = StatisticType.valueOf(args[3]);
    this.count = Optional.of(args[4]).filter(Predicate.not(String::isEmpty)).map(Integer::valueOf).orElse(0);
    this.minutes = Optional.of(args[5]).filter(Predicate.not(String::isEmpty)).map(Float::valueOf).orElse(0.0f);
    this.actionDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(args[6])), ZoneId.systemDefault());
    this.eventDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(args[7])), ZoneId.systemDefault());
  }
}
