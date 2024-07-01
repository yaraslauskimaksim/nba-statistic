package org.demo.web.controller;

import com.sun.net.httpserver.HttpExchange;
import org.demo.service.StatisticService;
import org.demo.web.model.AverageStatisticRequest;

import java.io.IOException;

public class AverageStatisticByPlayerController extends AbstractController {

  private final StatisticService statisticService;

  public AverageStatisticByPlayerController(
    StatisticService statisticService,
    Integer attemptCount,
    Integer cooldownMs
  ) {
    super(attemptCount, cooldownMs);
    this.statisticService = statisticService;
  }

  @Override
  public void doPost(HttpExchange exchange) throws IOException {
    var averageStatisticRequest = readRequest(exchange, AverageStatisticRequest.class);
    var response = statisticService.getAverageStatisticByPlayerIdAndEventDateTime(
      averageStatisticRequest.id(),
      averageStatisticRequest.from(),
      averageStatisticRequest.to()
    );
    writeResponse(exchange, response);
  }
}
