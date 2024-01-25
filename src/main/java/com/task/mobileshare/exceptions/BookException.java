package com.task.mobileshare.exceptions;

public class BookException extends RuntimeException {

    private final String text;

    public BookException(String text) {
        super();
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
