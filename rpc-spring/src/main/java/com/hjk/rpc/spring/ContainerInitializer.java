package com.hjk.rpc.spring;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.hjk.rpc.registry.zookeeper.ZkBean;
import com.hjk.rpc.spring.bean.ServerBean;
import com.hjk.rpc.spring.bean.ZookeeperBean;
import com.hjk.rpc.spring.server.ServerInitializer;

/**
 * Created by hanjk on 16/9/8.
 */
public class ContainerInitializer implements ApplicationContextAware, InitializingBean, ApplicationListener<ContextRefreshedEvent>, BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(ContainerInitializer.class);

    private ApplicationContext cxt;

    private BeanFactory beanFactory;

    public static ZkBean zkBean ;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("=======setApplicationContext");
        cxt = applicationContext;
    }

    /**
     * 容器初始化完成之后执行
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("=======afterPropertiesSet");

        //获取zookeeper 配置,以后要做zookeeper解耦
        Map zookeeperMap = cxt.getBeansOfType(ZookeeperBean.class);
        if (zookeeperMap != null && !zookeeperMap.isEmpty()) {
            ZookeeperBean zookeeperBean = (ZookeeperBean) zookeeperMap.values().toArray()[0];
            zkBean = new ZkBean(zookeeperBean.getZkAddress(),zookeeperBean.getSessionTimeoutInMillis());
        }

        //获取服务列表,初始化服务
        Map serverMap = cxt.getBeansOfType(ServerBean.class);
        if (serverMap != null && !serverMap.isEmpty()) {
            ServerInitializer.init((ServerBean) serverMap.values().toArray()[0]);
        }

        //获取客户端 注册代理类

    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        logger.info("=======setBeanFactory");
        this.beanFactory = beanFactory;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            if (event.getApplicationContext().getParent() == null) {//防止 mvc容器 多次刷新
//                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Transport.class);
//                ServiceObject serviceObject = new ServiceObject();
//                serviceObject.setAppServer("testServer");
//                serviceObject.setServiceName(Transport.class.getName());
//                beanDefinitionBuilder.addConstructorArgValue(RpcCglibProxy.getPoxyObject(serviceObject));
//                BeanDefinition beanDefinition  = beanDefinitionBuilder.getBeanDefinition();
//                beanDefinition.setBeanClassName(Transport.class.getName());
//                DefaultListableBeanFactory acf = (DefaultListableBeanFactory) cxt
//                        .getAutowireCapableBeanFactory();
//
//
//                acf.registerBeanDefinition("dddd", beanDefinition);
            }
        }catch (Exception e){
            logger.error("",e);

        }

        logger.info("=======onApplicationEvent");
    }
}
