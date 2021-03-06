package com.sample.orders.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sample.common.config.constants.JpaConstants;
import com.sample.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class RestApiError {

   private HttpStatus status;
   
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JpaConstants.DATA_TIME_FORMAT)
   private LocalDateTime timestamp;
   
   private String message;
   private String debugMessage;
   private BusinessException businessException;

   private RestApiError() {
       this.timestamp = LocalDateTime.now();
   }

   public RestApiError(HttpStatus status) {
       this();
       this.status = status;
   }

   public RestApiError(HttpStatus status, Throwable ex) {
       this();
       this.status = status;
       this.message = "Unexpected error";
       this.debugMessage = ex.getLocalizedMessage();
   }

   public RestApiError(HttpStatus status, String message, Throwable ex) {
       this();
       this.status = status;
       this.message = message;
       this.debugMessage = ex.getLocalizedMessage();
   }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public BusinessException getBusinessException() {
        return businessException;
    }

    public void setBusinessException(BusinessException businessException) {
        this.businessException = businessException;
    }
}
