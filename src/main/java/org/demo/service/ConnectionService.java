package org.demo.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.demo.dao.Config.*;

public class ConnectionService {
  private static final Logger log = LogManager.getLogger(ConnectionService.class);

  public static Connection connect() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(URL, USER, PASSWORD);
      if (conn != null) {
        log.debug("Connected to the PostgreSQL server successfully.");
      } else {
        log.debug("Failed to make connection!");
      }
    } catch (SQLException e) {
      log.debug(e.getMessage(), e);
    }

    return conn;
  }
}
