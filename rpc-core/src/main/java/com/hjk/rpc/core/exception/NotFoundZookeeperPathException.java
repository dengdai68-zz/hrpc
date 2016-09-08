package com.hjk.rpc.core.exception;

/**
 * Created by hanjk on 16/9/7.
 */
public class NotFoundZookeeperPathException extends RuntimeException{
    public NotFoundZookeeperPathException(String msg) {
        super(msg);
    }
}
