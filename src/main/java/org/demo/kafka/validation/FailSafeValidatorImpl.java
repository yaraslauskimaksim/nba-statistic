package org.demo.kafka.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.demo.kafka.model.Statistic;
import org.demo.kafka.model.StatisticType;
import org.demo.kafka.validation.valitationrule.ValidationRule;

import java.util.*;
import java.util.stream.Collectors;

public class FailSafeValidatorImpl implements FailSafeValidator<Boolean, Statistic> {

  private static final Logger log = LogManager.getLogger(FailSafeValidatorImpl.class);

  private final Map<StatisticType, ValidationRule<StatisticType, Statistic>> validators;

  public FailSafeValidatorImpl() {
    this.validators = new HashMap<>();
  }

  @SafeVarargs
  public FailSafeValidatorImpl(ValidationRule<StatisticType, Statistic>... rules) {
    this.validators = Arrays.stream(rules)
      .collect(Collectors.toUnmodifiableMap(ValidationRule::getType, validator -> validator));
  }

  @Override
  public Map<Boolean, List<Statistic>> validate(List<Statistic> data) {
    return data.stream()
      .collect(Collectors.partitioningBy(this::validate));
  }

  @Override
  public Boolean validate(Statistic statistic) {
    var statisticValidator = validators.get(statistic.getType());
    if (statisticValidator == null) {
      log.debug("Statistic type {} not supported.", statistic.getType());
      return false;
    }
    if (statisticValidator.validate(statistic)) {
      return true;
    } else {
      log.debug("Statistic {} validation exception. Please, connect with data vendor", statistic);
      return false;
    }
  }
}
