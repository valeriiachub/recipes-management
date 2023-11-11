package org.mendix.exception;

public class EnumSerializeException extends RuntimeException {

    private final String errorCode;

    public EnumSerializeException(String errorCode) {
        this.errorCode = errorCode;
    }
}
