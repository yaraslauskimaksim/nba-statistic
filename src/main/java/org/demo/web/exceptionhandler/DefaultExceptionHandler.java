package org.demo.web.exceptionhandler;

import com.sun.net.httpserver.HttpExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefaultExceptionHandler implements ExceptionHandler {

  private static final Logger log = LogManager.getLogger(DefaultExceptionHandler.class);

  @Override
  public void handle(Throwable throwable, HttpExchange httpExchange) {
    log.error(throwable.getMessage(), throwable);
  }
}
