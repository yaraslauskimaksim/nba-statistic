package org.demo.dao;

import org.flywaydb.core.Flyway;

import static org.demo.dao.Config.*;

public class DBMigration {

  public static void migrate() {
    Flyway flyway = Flyway.configure()
      .dataSource(URL, USER, PASSWORD)
      .locations(LOCATION)
      .load();

    // Start the migration
    flyway.migrate();
  }
}
