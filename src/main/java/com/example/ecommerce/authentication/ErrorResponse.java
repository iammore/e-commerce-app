package com.example.ecommerce.authentication;

import java.util.Date;

public class ErrorResponse {

    private String message;
    private Date date;
    private String cause;

    public ErrorResponse() {
    }

    public ErrorResponse(String message, Date date, String cause) {
        this.message = message;
        this.date = date;
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
