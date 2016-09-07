package com.hjk.rpc.registry.zookeeper;

import com.hjk.rpc.core.ServiceObject;
import com.hjk.rpc.core.registry.ServiceRegistry;
import com.hjk.rpc.registry.RegistryConfig;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

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
            //创建registry顶级节点
            String registryPath = RegistryConfig.zkRegistryPath;
            keeperCreateNode(registryPath,null);
            logger.debug("create registry top node:{}",registryPath);
            //创建service节点
            String servicePath = registryPath + "/" + serviceObject.getServiceName();
            keeperCreateNode(servicePath,null);
            logger.debug("create service node:{}",servicePath);
            //创建service address节点
            String addressPath = servicePath + "/" + serviceObject.getServiceAddress();
            keeperCreateNode(addressPath,null);
            logger.debug("create addressPath node:{}",addressPath);
        } catch (KeeperException e) {
            logger.error("",e);
        } catch (InterruptedException e) {
            logger.error("",e);
        }
    }

    private void keeperCreateNode(String node,byte[] bytes) throws
            KeeperException, InterruptedException {
        if (null == zk.exists(node,false)) {
            zk.create(node, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.debug("create node:{}",node);
        }
    }
}