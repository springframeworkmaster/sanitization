package com.tasks.validation.sanitization.exception;

import java.util.ArrayList;
import java.util.List;

import com.tasks.validation.sanitization.domain.Error;

public class SanitizationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final List<Error> validationErrors;

    public SanitizationException(String message) {
        super(message);
        this.validationErrors = null;
    }
    public SanitizationException(String message, Error errorDetail) {
        super(message);
        this.validationErrors = new ArrayList<>();
        this.validationErrors.add(errorDetail);
    }
    public SanitizationException(String message, List<Error> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }
    public List<Error> getValidationErrors() {
        return this.validationErrors;
    }
}