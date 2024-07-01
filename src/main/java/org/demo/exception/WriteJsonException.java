package org.demo.exception;

public class WriteJsonException extends NonRetryableException {

  public WriteJsonException(String message, Throwable cause) {
    super(message, cause);
  }
}
