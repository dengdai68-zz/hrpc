package com.hjk.rpc.sample.server.impl;

import com.hjk.rpc.sample.api.Transport;

/**
 * Created by hanjk on 16/9/9.
 */
public class Car implements Transport {
    @Override
    public String getName() {
        return "car";
    }
}
