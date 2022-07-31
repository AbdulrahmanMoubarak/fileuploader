package com.example.fileuploader.ticketing.exceptions;

public class TicketsLimitExceededException extends Exception{

    @Override
    public String getMessage() {
        return "Cannot generate any more tickets for this user";
    }
}
