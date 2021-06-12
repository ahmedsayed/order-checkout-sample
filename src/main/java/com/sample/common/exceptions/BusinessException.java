package com.sample.common.exceptions;

public class BusinessException extends Exception {

    private String code;
    private String message;
    private String field;
    private Object rejectedValue;

    public BusinessException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(String code, String field, Object rejectedValue, String message) {
        this.code = code;
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", field='" + field + '\'' +
                ", rejectedValue=" + rejectedValue +
                '}';
    }
}
