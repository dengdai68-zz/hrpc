package com.hjk.rpc.registry.zookeeper;

import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.common.conf.ServerConf;
import com.hjk.rpc.registry.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by dengd on 2016/9/10.
 */
public class ZookeeperServiceInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceInitializer.class);

    public void init(List<String> services, ServerConf server) throws Exception {
        if (ZookeeperConf.zkconf == null) {
            throw new RuntimeException("注册中心未找到!");
        }
        ServiceRegistry serviceRegistry = new ZookeeperServiceRegistry(ZookeeperConf.zkconf);
        String localIp = getIp();
        //注册服务
        for (String serviceName : services) {
            ServiceObject serviceObject = new ServiceObject();
            serviceObject.setAppServer(server.getServerName());
            serviceObject.setServiceName(serviceName);
            serviceObject.setServiceAddress(localIp+":"+server.getPort());
            serviceRegistry.register(serviceObject);
            logger.info("register service:{} , {}", serviceObject.getServiceName(), serviceObject.getServiceAddress());
        }
        logger.info("service register is successful!");
    }

    private static String getIp() throws Exception {
        String ip = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (UnknownHostException e) {
            throw e;
        }
//        ip = "127.0.0.1";
        return ip;
    }



}
