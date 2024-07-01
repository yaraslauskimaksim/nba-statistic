package org.demo.web.exceptionhandler;

import com.sun.net.httpserver.HttpExchange;

public interface ExceptionHandler {

  void handle(Throwable throwable, HttpExchange httpExchange);
}
