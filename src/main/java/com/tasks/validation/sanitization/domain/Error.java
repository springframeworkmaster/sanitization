package com.tasks.validation.sanitization.domain;


import java.io.Serializable;


public class Error implements Serializable {
    private static final long serialVersionUID = -2814734929482548090L;

    private String field;

    private String message;

    public Error(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return this.field;
    }

    public String getMessage() {
        return this.message;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
