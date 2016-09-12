package com.hjk.rpc.sample.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by dengd on 2016/9/9.
 */
public class Server {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-rpc.xml");
    }
}
