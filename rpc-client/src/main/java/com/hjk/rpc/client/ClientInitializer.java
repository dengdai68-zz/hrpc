package com.hjk.rpc.client;

import com.hjk.rpc.spring.bean.ClientBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by hanjk on 16/9/8.
 */
public class ClientInitializer implements ApplicationContextAware, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ClientInitializer.class);


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
