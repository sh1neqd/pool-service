package ru.alexbogdan.pool_service.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class AppAnswer {
    private int status;
    private String message;
    private Date timestamp;

    public AppAnswer(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}