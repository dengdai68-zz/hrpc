package com.hjk.rpc.registry.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hjk.rpc.core.ServiceObject;
import com.hjk.rpc.core.registry.ServiceRegistry;
import com.hjk.rpc.registry.RegistryConfig;

/**
 * Created by hanjk on 16/9/7.
 */
public class ZookeeperServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);

    private ZooKeeper zk;

    public ZookeeperServiceRegistry() throws IOException {

        zk = new ZooKeeper(RegistryConfig.zkAddress,RegistryConfig.sessionTimeout,null);
    }

    @Override
    public void registry(ServiceObject serviceObject){
        try {
            serviceObject.validate();
            //创建registry顶级节点
            String registryPath = RegistryConfig.zkRegistryPath;
            keeperCreateNode(registryPath,null,CreateMode.PERSISTENT);
            //创建APPServer节点
            String appServerPath = registryPath + "/" + serviceObject.getAppServer();
            keeperCreateNode(appServerPath,null,CreateMode.PERSISTENT);
            //创建service节点
            String servicePath = appServerPath + "/" + serviceObject.getServiceName();
            keeperCreateNode(servicePath,null,CreateMode.PERSISTENT);
            //创建service address节点
            String addressPath = servicePath + "/" + serviceObject.getServiceAddress();
            keeperCreateNode(addressPath,null,CreateMode.EPHEMERAL);
            logger.debug("create addressPath node:{}",addressPath);
        } catch (KeeperException e) {
            logger.error("",e);
        } catch (InterruptedException e) {
            logger.error("",e);
        }
    }

    private void keeperCreateNode(String node,byte[] bytes,CreateMode createMode) throws
            KeeperException, InterruptedException {
        if (null == zk.exists(node,false)) {
            zk.create(node, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
            logger.debug("create node:{}",node);
        }
    }
}
