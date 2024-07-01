package org.demo.dao.domain;

import java.util.UUID;

public record GameDomain(UUID gameId, int duration, long eventDateTime) {
}
