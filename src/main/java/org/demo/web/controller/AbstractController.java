package org.demo.web.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.demo.exception.NonRetryableException;
import org.demo.exception.RetryableException;
import org.demo.web.exceptionhandler.DefaultExceptionHandler;
import org.demo.web.exceptionhandler.ExceptionHandler;

import java.io.IOException;
import java.io.OutputStream;

import static org.demo.util.JsonMapper.fromInputStream;
import static org.demo.util.JsonMapper.toBytes;

public abstract class AbstractController implements Controller {

  private static final Logger log = LogManager.getLogger(AbstractController.class);

  private final ExceptionHandler exceptionHandler;
  private final Integer attemptCount;
  private final Integer cooldownMs;

  public AbstractController(Integer attemptCount, Integer cooldownMs) {
    exceptionHandler = new DefaultExceptionHandler();
    this.attemptCount = attemptCount;
    this.cooldownMs = cooldownMs;
  }

  public AbstractController(
    ExceptionHandler exceptionHandler,
    Integer attemptCount,
    Integer cooldownMs
  ) {
    this.exceptionHandler = exceptionHandler;
    this.attemptCount = attemptCount;
    this.cooldownMs = cooldownMs;
  }

  @Override
  public HttpHandler getHandlerMapping() {
    return exchange -> {
      var attemptCount = this.attemptCount;
      while (attemptCount > 0) {
        try {
          attemptCount--;
          switch (exchange.getRequestMethod()) {
            case "POST" -> doPost(exchange);
            case "GET" -> doGet(exchange);
            // 405 Method Not Allowed
            default -> doNothing(exchange);
          }
          break;
        } catch (NonRetryableException e) {
          exceptionHandler.handle(e, exchange);
          break;
        } catch (RetryableException e) {
          sleep();
        }
      }
    };
  }

  @Override
  public void doGet(HttpExchange exchange) throws IOException {
    doNothing(exchange);
  }

  @Override
  public void doPost(HttpExchange exchange) throws IOException {
    doNothing(exchange);
  }

  @Override
  public void doPut(HttpExchange exchange) throws IOException {
    doNothing(exchange);
  }

  @Override
  public void doOptions(HttpExchange exchange) throws IOException {
    doNothing(exchange);
  }

  protected <T> T readRequest(HttpExchange exchange, Class<T> clazz) {
    return fromInputStream(exchange.getRequestBody(), clazz);
  }

  protected <T> void writeResponse(HttpExchange exchange, T response) throws IOException {
    var responseBody = toBytes(response);
    exchange.sendResponseHeaders(200, responseBody.length);
    try (OutputStream outputStream = exchange.getResponseBody()) {
      outputStream.write(responseBody);
    }
  }

  private void sleep() {
    try {
      Thread.sleep(cooldownMs);
    } catch (InterruptedException ex) {
      log.error(ex.getMessage(), ex);
    }
  }
}
