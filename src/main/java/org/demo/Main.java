package org.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.demo.dao.DBMigration;
import org.demo.kafka.model.Statistic;
import org.demo.service.ConnectionService;
import org.demo.dao.StatisticDao;
import org.demo.kafka.KafkaListener;
import org.demo.service.StatisticService;
import org.demo.util.testdataprovider.DataGenerator;
import org.demo.web.ServerImpl;

import java.sql.Connection;
import java.util.List;

import static org.demo.web.ServerImpl.getValidators;

public class Main {
  private static final Logger log = LogManager.getLogger(Main.class);

  public static void main(String[] args) {
    log.info("Migrate db...");
    DBMigration.migrate();

    List<Statistic> statistics = DataGenerator.generateInput("dumb_data/statistics.csv", Statistic::new);
//    log.info("Produce test data...");
//    StatisticDataGenerator.produceData(statistics);

    log.info("Create db connection...");
    Connection conn = ConnectionService.connect();
    var statisticDao = new StatisticDao(conn);
    var statisticService = new StatisticService(statisticDao, getValidators());
    log.info("Create test data directly to the DB for improving start time...");
    statistics.forEach(statisticService::createOrUpdate);

    log.info("Listen messages with the test data...");
    var listener = new KafkaListener(statisticService);
    listener.start();
    // Add shutdown hook to gracefully stop the listener
    Runtime.getRuntime().addShutdownHook(new Thread(listener::stop));

    log.info("Starting server...");
    var statisticServer = new ServerImpl(8080);
    statisticServer.start();
  }
}
