package com.runnersteam.mail.config;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RunnersMailExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleInternalError(Exception exception) {
    log.error("", exception);
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
        new ErrorResponse(exception.getMessage())
    );
  }
}
