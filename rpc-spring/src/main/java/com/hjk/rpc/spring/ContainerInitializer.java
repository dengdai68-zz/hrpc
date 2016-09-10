package com.hjk.rpc.spring;

import com.hjk.rpc.registry.zookeeper.ZookeeperConf;
import com.hjk.rpc.spring.bean.ServerBean;
import com.hjk.rpc.spring.bean.ZookeeperBean;
import com.hjk.rpc.spring.server.ServerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

/**
 * spring 容器获取 zookeeper配置 和初始化server服务
 * Created by hanjk on 16/9/8.
 */
public class ContainerInitializer implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ContainerInitializer.class);
    /**
     * 容器初始化完成之后执行
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //获取zookeeper 配置,以后要做zookeeper解耦
        Map zookeeperMap = RpcApplicationContext.getApplicationContext().getBeansOfType(ZookeeperBean.class);
        if (zookeeperMap != null && !zookeeperMap.isEmpty()) {
            ZookeeperBean zookeeperBean = (ZookeeperBean) zookeeperMap.values().toArray()[0];
            RpcApplicationContext.zkconf = new ZookeeperConf(zookeeperBean.getZkAddress(),zookeeperBean.getSessionTimeoutInMillis());
        }
        //获取服务列表,初始化服务
        Map serverMap = RpcApplicationContext.getApplicationContext().getBeansOfType(ServerBean.class);
        if (serverMap != null && !serverMap.isEmpty()) {
            ServerInitializer.init((ServerBean) serverMap.values().toArray()[0]);
        }
    }
}
