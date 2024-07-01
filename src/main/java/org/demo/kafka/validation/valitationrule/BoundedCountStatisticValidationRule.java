package org.demo.kafka.validation.valitationrule;

import org.demo.kafka.model.Statistic;
import org.demo.kafka.model.StatisticType;

public record BoundedCountStatisticValidationRule(
  StatisticType statisticType,
  Integer min,
  Integer max
) implements ValidationRule<StatisticType, Statistic> {

  @Override
  public StatisticType getType() {
    return statisticType;
  }

  @Override
  public boolean validate(Statistic statistic) {
    return min != null && max != null & statistic.getCount() >= min && statistic.getCount() <= max;
  }
}
