package com.hjk.rpc.registry.zookeeper;

import java.io.IOException;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.common.conf.ZookeeperConf;
import com.hjk.rpc.registry.registry.ServiceRegistry;

/**
 * Created by hanjk on 16/9/7.
 */
public class ZookeeperServiceRegistry implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);

    private final ZkClient zkClient;

    private final ZookeeperConf zkconf;

    public ZookeeperServiceRegistry(ZookeeperConf zkconf) throws IOException {
        this.zkconf = zkconf;
        zkClient = new ZkClient(zkconf.getAddress(), zkconf.getSessionTimeout(), zkconf.getConnectionTimeout());

    }

    @Override
    public void register(ServiceObject service){
        service.validate();
        String servicePath = zkconf.getRegistryPath() +
                "/" + service.getAppServer() +
                "/" + service.getServiceName() ;
        zkClient.createPersistent(servicePath,true);
        //创建service address节点
        String addressPath = servicePath + "/" + service.getServiceAddress();
        zkClient.createEphemeral(addressPath);
        logger.debug("create addressPath node:{}",addressPath);
    }
}
