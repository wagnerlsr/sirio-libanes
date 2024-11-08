package br.com.sirio.handlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleUserServiceException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<Object>(new ErrorMessage(new Date(), ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(new ErrorMessage(new Date(), ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}