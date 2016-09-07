package com.hjk.rpc.core.registry;

import com.hjk.rpc.core.ServiceObject;

/**
 * 服务注册接口
 * Created by hanjk on 16/9/7.
 */
public interface ServiceRegistry {

    /**
     * 注册服务
     */
    void registry(ServiceObject serviceObject);
}
