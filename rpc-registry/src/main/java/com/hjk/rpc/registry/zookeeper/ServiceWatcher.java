package com.hjk.rpc.registry.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hanjk on 16/9/7.
 */
public class ServiceWatcher implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceDiscovery.class);

    private ConcurrentHashMap<String,Vector<String>> serviceMap;

    public ServiceWatcher(ConcurrentHashMap<String, Vector<String>> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        logger.info("监控到数据变化 watchedEvent:{}",watchedEvent);
        //TODO

    }
}
