package com.hjk.rpc.spring;

import com.hjk.rpc.registry.zookeeper.ZookeeperConf;
import com.hjk.rpc.spring.bean.ServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc服务容器上下文
 * Created by dengd on 2016/9/9.
 */
public class RpcApplicationContext implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(RpcApplicationContext.class);

    private static ApplicationContext cxt;

    private static Map<String,Object> rpcServiceMap = new HashMap();

    public static ZookeeperConf zkconf;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        cxt = applicationContext;

        //获取服务列表,初始化服务
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
