package org.demo.dao.domain;

import java.util.UUID;

public record TeamDomain(UUID id, String name, boolean isActive) {
}
