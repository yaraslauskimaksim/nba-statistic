package org.demo.web.controller;

import com.sun.net.httpserver.HttpExchange;
import org.demo.service.StatisticService;
import org.demo.web.model.AverageStatisticRequest;

import java.io.IOException;

public class AverageStatisticByTeamController extends AbstractController {

  private final StatisticService statisticService;

  public AverageStatisticByTeamController(
    StatisticService statisticServiceInteger,
    Integer attemptCount,
    Integer cooldownMs
  ) {
    super(attemptCount, cooldownMs);
    this.statisticService = statisticServiceInteger;
  }

  @Override
  public void doPost(HttpExchange exchange) throws IOException {
    var averageStatisticRequest = readRequest(exchange, AverageStatisticRequest.class);
    var response = statisticService.getAverageStatisticByTeamIdAndEventDateTime(
      averageStatisticRequest.id(),
      averageStatisticRequest.from(),
      averageStatisticRequest.to()
    );
    writeResponse(exchange, response);
  }
}
