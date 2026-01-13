package com.appointments.manager.exception;

public class BusinessException extends RuntimeException {
    private String message;

    public BusinessException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BusinessException [message=" + message + "]";
    }
}
