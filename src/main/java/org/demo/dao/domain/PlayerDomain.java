package org.demo.dao.domain;

import java.util.UUID;

public record PlayerDomain(UUID id, String name, boolean isActive) {
}
