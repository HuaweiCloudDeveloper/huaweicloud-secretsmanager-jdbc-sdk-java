package com.huawei.dew.util;

/**
 * @author q30037735
 */
public class WrappedException extends RuntimeException{
    public WrappedException() {
        super();
    }

    public WrappedException(String message) {
        super(message);
    }

    public WrappedException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrappedException(Throwable cause) {
        super(cause);
    }

    protected WrappedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
