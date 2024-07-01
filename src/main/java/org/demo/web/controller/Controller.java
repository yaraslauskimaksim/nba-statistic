package org.demo.web.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public interface Controller {

  HttpHandler getHandlerMapping();

  void doGet(HttpExchange exchange) throws IOException;

  void doPost(HttpExchange exchange) throws IOException;

  void doPut(HttpExchange exchange) throws IOException;

  void doOptions(HttpExchange exchange) throws IOException;

  default void doNothing(HttpExchange exchange) throws IOException {
    exchange.sendResponseHeaders(405, -1);
  }
}
