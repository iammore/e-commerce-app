package com.example.ecommerce.authentication;

import java.util.Date;

public class SucessResponse {
    private String message;
    private Date date;


    public SucessResponse(String message, Date date) {
        this.message = message;
        this.date = date;
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
}
