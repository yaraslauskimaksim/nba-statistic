package org.demo.exception;

public class ReadJsonException extends NonRetryableException {

  public ReadJsonException(String message, Throwable cause) {
    super(message, cause);
  }
}
