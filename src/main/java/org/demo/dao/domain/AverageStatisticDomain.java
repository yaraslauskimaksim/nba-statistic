package org.demo.dao.domain;

import java.util.UUID;

public record AverageStatisticDomain(UUID id, String name, StatisticTypeDomain type, Number averageCount,
                                     Number averageMinutes) {
}
