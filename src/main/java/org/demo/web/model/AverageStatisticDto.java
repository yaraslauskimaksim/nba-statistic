package org.demo.web.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public final class AverageStatisticDto {
  private final UUID id;
  private final String name;
  @Singular
  private final List<StatisticByTypeDto> statistics;
}
