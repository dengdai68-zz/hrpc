package com.hjk.rpc.sample.client;

import com.hjk.rpc.sample.api.Transport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dengd on 2016/9/10.
 */
@Component
public class Client {

    @Resource(name = "user")
    Transport transport;

    public String getName(){
        return transport.getName();
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-rpc.xml");
        String[] beanNames = ctx.getBeanNamesForType(Transport.class);
        String[] beanNamess = ctx.getBeanDefinitionNames();
        Transport transport = (Transport) ctx.getBean("user");
        System.out.println("transport.getName:" + transport.getName());
        transport.start("hanjiankai");
        Client client = ctx.getBean(Client.class);

    }
}
