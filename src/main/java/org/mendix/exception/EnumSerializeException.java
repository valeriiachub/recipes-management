package org.mendix.exception;

public class EnumSerializeException extends RuntimeException {

    public EnumSerializeException(String errorCode) {
        super(errorCode);
    }
}
