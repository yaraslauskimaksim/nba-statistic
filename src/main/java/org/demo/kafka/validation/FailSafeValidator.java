package org.demo.kafka.validation;

import java.util.List;
import java.util.Map;

public interface FailSafeValidator<K, V> {
  K validate(V data);

  Map<K, List<V>> validate(List<V> data);
}
