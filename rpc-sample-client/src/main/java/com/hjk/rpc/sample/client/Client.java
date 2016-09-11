package com.hjk.rpc.sample.client;

import com.hjk.rpc.sample.api.Transport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.HOUR,2);//把日期往后增加一天.整数往后推,负数往前移动
        int i=0;
        while (calendar.getTimeInMillis() > System.currentTimeMillis()){
            try {
                Thread.sleep(20);
                transport.start("hanjiankai");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        transport.start("hanjiankai");
//        Client client = ctx.getBean(Client.class);

    }
}
