package com.hjk.rpc.core.discovery;

import com.sun.tools.javac.util.List;

/**
 * 发现服务
 * Created by hanjk on 16/9/7.
 */
public interface ServiceDiscovery {

    /**
     *
     * 根据服务名称,获取服务ip列表
     */
    String discovery(String serviceName);

}
