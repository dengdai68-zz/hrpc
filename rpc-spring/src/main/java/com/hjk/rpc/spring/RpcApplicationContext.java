package com.hjk.rpc.spring;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.hjk.rpc.core.server.ServerInitializer;
import com.hjk.rpc.spring.bean.ServerBean;
import com.hjk.rpc.spring.bean.ServiceBean;
import com.hjk.rpc.spring.bean.ZookeeperBean;

/**
 * rpc服务容器上下文
 * Created by dengd on 2016/9/9.
 */
public class RpcApplicationContext implements ApplicationContextAware,InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RpcApplicationContext.class);

    private static ApplicationContext cxt;

    private static Map<String,Object> rpcServiceMap = new HashMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        cxt = applicationContext;

        //获取 客戶端引用服务列表
        Map serviceMap = cxt.getBeansOfType(ServiceBean.class);
        if (serviceMap != null && !serviceMap.isEmpty()) {
            for(Object bean : serviceMap.values()){
                String clazz = ((ServiceBean)bean).getClazz();
                try {
                    Object service = cxt.getBean(Class.forName(clazz));
                    rpcServiceMap.put(clazz,service);
                } catch (ClassNotFoundException e) {
                    logger.warn("not found rpc service:[{}] component!",clazz);
                }
            }
        }
    }
    /**
     * 容器初始化完成之后执行
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        //获取zookeeper 配置
        Map zookeeperMap = RpcApplicationContext.getApplicationContext().getBeansOfType(ZookeeperBean.class);
        if (zookeeperMap != null && !zookeeperMap.isEmpty()) {
            ((ZookeeperBean) zookeeperMap.values().toArray()[0]).injectionConf();

        }else{
            return;
        }
        //获取服务列表,初始化服务
        Map serverMap = RpcApplicationContext.getApplicationContext().getBeansOfType(ServerBean.class);
        if (serverMap != null && !serverMap.isEmpty()) {
            ((ServerBean) serverMap.values().toArray()[0]).injectionConf();
            new ServerInitializer().init(rpcServiceMap);

        }
    }

    public static Object getRpcBean(String beanName){
        return rpcServiceMap.get(beanName);
    }
    public static Map<String,Object> getRpcServiceMap(){
        return rpcServiceMap;
    }

    public static ApplicationContext getApplicationContext(){
        return cxt;
    }
}
