package org.demo.kafka.validation.valitationrule;

import org.demo.kafka.model.Statistic;
import org.demo.kafka.model.StatisticType;

public record BoundedFloatStatisticValidationRule (
  StatisticType statisticType,
  Float min,
  Float max
) implements ValidationRule<StatisticType, Statistic> {

  @Override
  public StatisticType getType() {
    return statisticType;
  }

  @Override
  public boolean validate(Statistic statistic) {
    return min != null && max != null & statistic.getMinutes() >= min && statistic.getMinutes() <= max;
  }
}
