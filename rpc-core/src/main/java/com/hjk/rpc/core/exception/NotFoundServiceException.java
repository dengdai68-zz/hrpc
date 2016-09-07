package com.hjk.rpc.core.exception;

/**
 * Created by hanjk on 16/9/7.
 */
public class NotFoundServiceException extends RuntimeException {
    public NotFoundServiceException(String msg) {
        super(msg);
    }
}
