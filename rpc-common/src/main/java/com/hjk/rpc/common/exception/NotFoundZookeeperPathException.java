package com.hjk.rpc.common.exception;

/**
 * Created by hanjk on 16/9/7.
 */
public class NotFoundZookeeperPathException extends RuntimeException{
    public NotFoundZookeeperPathException() {
        super();
    }

    public NotFoundZookeeperPathException(String msg) {
        super(msg);
    }
}
