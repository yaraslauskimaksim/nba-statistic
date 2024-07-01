package org.demo.dao.domain;

import java.util.UUID;

public record StatisticByTypeDomain(UUID id, String name, StatisticTypeDomain type, Number average) {
}
