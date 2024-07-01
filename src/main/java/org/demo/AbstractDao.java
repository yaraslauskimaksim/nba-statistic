package org.demo;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;

@RequiredArgsConstructor
public abstract class AbstractDao {
  protected final Connection connection;
}
