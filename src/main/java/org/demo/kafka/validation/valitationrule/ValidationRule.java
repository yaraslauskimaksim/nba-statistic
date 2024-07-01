package org.demo.kafka.validation.valitationrule;

public interface ValidationRule<T, R> {
  T getType();
  boolean validate(R statistic);
}
