package com.hjk.rpc.registry.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.core.registry.ServiceRegistry;

/**
 * Created by hanjk on 16/9/7.
 */
public class ZookeeperServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);

    private ZooKeeper zk;

    public ZookeeperServiceRegistry(){
    }

    public ZookeeperServiceRegistry(ZkBean zkbean) throws IOException {
        zk = new ZooKeeper(zkbean.getZkAddress(), zkbean.getSessionTimeoutInMillis(), new Watcher() {

            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }

    @Override
    public void registry(ServiceObject serviceObject){
        try {
            serviceObject.validate();
            //创建registry顶级节点
            String registryPath = "/rpc_registry";
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
