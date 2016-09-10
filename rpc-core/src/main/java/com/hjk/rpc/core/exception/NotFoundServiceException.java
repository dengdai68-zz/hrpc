package com.hjk.rpc.core.exception;

/**
 * Created by hanjk on 16/9/7.
 */
public class NotFoundServiceException extends RpcException {
    public NotFoundServiceException(String msg) {
        super(msg);
    }

    public NotFoundServiceException() {
        super();
    }
}
