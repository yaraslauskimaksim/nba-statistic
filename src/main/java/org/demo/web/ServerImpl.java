package org.demo.web;

import com.sun.net.httpserver.HttpServer;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.demo.dao.StatisticDao;
import org.demo.kafka.model.Statistic;
import org.demo.kafka.model.StatisticType;
import org.demo.kafka.validation.FailSafeValidator;
import org.demo.kafka.validation.FailSafeValidatorImpl;
import org.demo.kafka.validation.valitationrule.BoundedCountStatisticValidationRule;
import org.demo.kafka.validation.valitationrule.BoundedFloatStatisticValidationRule;
import org.demo.kafka.validation.valitationrule.PositiveIntegerStatisticValidationRule;
import org.demo.service.ConnectionService;
import org.demo.service.StatisticService;
import org.demo.web.controller.AverageStatisticByPlayerController;
import org.demo.web.controller.AverageStatisticByTeamController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;

@RequiredArgsConstructor
public class ServerImpl {
  private static final Logger log = LogManager.getLogger(ServerImpl.class);

  private final int port;
  private HttpServer server;

  public void start() {
    try {
      server = HttpServer.create(new InetSocketAddress(port), 0);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Connection conn = ConnectionService.connect();
    var statisticDao = new StatisticDao(conn);
    var statisticService = new StatisticService(statisticDao, getValidators());

    var averageStatisticByPlayerController = new AverageStatisticByPlayerController(statisticService, 2, 1000);
    var averageStatisticByTeamController = new AverageStatisticByTeamController(statisticService, 2, 1000);

    // Define the /post endpoint
    server.createContext("/statistic/get_average_statistic_by_player_id", averageStatisticByPlayerController.getHandlerMapping());
    server.createContext("/statistic/get_average_statistic_by_team_id", averageStatisticByTeamController.getHandlerMapping());

    // Start the server
    server.setExecutor(null);
    server.start();
    log.info("Server started on port 8080...");
  }

  public void stop() {
    server.stop(0);
    server = null;
  }

  public static FailSafeValidator<Boolean, Statistic> getValidators() {
    return new FailSafeValidatorImpl(
      new PositiveIntegerStatisticValidationRule(StatisticType.POINT),
      new PositiveIntegerStatisticValidationRule(StatisticType.REBOUND),
      new PositiveIntegerStatisticValidationRule(StatisticType.ASSIST),
      new PositiveIntegerStatisticValidationRule(StatisticType.STEAL),
      new PositiveIntegerStatisticValidationRule(StatisticType.BLOCK),
      new BoundedCountStatisticValidationRule(StatisticType.FOUL, 0, 6),
      new PositiveIntegerStatisticValidationRule(StatisticType.TURNOVER),
      new BoundedFloatStatisticValidationRule(StatisticType.MINUTES_PLAYED, 0f, 48f)
    );
  }
}
