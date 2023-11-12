package org.mendix.exception;

public class RecipeValidationException extends RuntimeException {

    public RecipeValidationException(String errorMessage) {
        super(errorMessage);
    }
}
