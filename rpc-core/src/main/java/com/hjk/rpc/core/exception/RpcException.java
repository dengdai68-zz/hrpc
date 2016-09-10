package com.hjk.rpc.core.exception;

/**
 * rpc通用异常
 * Created by dengd on 2016/9/9.
 */
public class RpcException extends RuntimeException {

    public RpcException() {
    }

    public RpcException(String message) {
        super(message);
    }
}
