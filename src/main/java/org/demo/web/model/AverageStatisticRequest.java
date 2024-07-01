package org.demo.web.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record AverageStatisticRequest(UUID id, Long from, Long to) {
}
