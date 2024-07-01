package org.demo.kafka.validation;

import java.util.List;

public interface FailFastValidator<T> {
  boolean validate(T data);

  List<T> validate(List<T> data);
}
