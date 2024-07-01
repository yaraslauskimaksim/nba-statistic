package org.demo.dao.domain;

import java.util.UUID;

public record StatisticDomain(UUID gameId, UUID playerId, UUID teamId, String type, Integer count, Float minutes,
                              Long actionDateTime, Long eventDateTime) {
}
