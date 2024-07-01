package org.demo.kafka.validation.valitationrule;

import org.demo.kafka.model.Statistic;
import org.demo.kafka.model.StatisticType;

public record PositiveIntegerStatisticValidationRule(
  StatisticType statisticType
) implements ValidationRule<StatisticType, Statistic> {

  @Override
  public StatisticType getType() {
    return statisticType;
  }

  @Override
  public boolean validate(Statistic statistic) {
    return statistic.getCount() != null && statistic.getCount() > 0;
  }
}
