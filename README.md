#hrpc 一款spring+zookeeper+netty整合的rpc框架使用说明


##server配置
###rpc接口定义
    package com.rpc.sample.api;
    //此接口，暴露给客户端调用
    public interface Animal {
        String sound();
    }
###rpc接口实现
    package com.rpc.sample.impl;
    import com.rpc.sample.Animal.api;
    public class Dog implements Animal{
        @Override
        public String sound() {
            return "i sound !!!!!";
        }
    }
###rpc服务暴露(spring-rpc.xml)
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:rpc="http://com.hjk.rpc/schema/rpc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
       http://com.hjk.rpc/schema/rpc http://com.hjk.rpc/schema/rpc.xsd">
        <context:property-placeholder location="classpath:rpc.properties"/>
        <!-- 注册中心zookeeper的相关配置 -->
        <rpc:zookeeper address="${zookeeper.address}" sessionTimeout="${zookeeper.sessionTimeout}" connectionTimeout="${zookeeper.connectionTimeout}"/>
        <!-- 提供服务 server 名称testServer，监听端口1236 -->
        <rpc:server name="testServer" port="1236">
            <!-- 提供服务的接口 -->
            <rpc:service class="com.rpc.sample.Animal"/>
        </rpc:server>

        <!-- 提供服务的实现类注册组件，也可以通过spring的注解方式注册组件 -->
        <bean class="com.rpc.sample.impl.Dog"/>
    </beans>
###需要的配置文件(rpc.properties)
    zookeeper.address=127.0.0.1:2181
    zookeeper.sessionTimeout=13000
    zookeeper.connectionTimeout=5000

> 至此，服务方提供服务配置完成，其中spring配置<rpc:server/>,如果容器发现有次配置，就会启动服务器监听，如果没有此配置不会启动监听；name="testServer" 当分布式部署的时候，同一个项目name要相同，同时客户端也通过此名称来调用指定项目。

###server服务启动
    package com.rpc.sample.impl.com.rpc.sample;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;
    public class Server {
        public static void main(String[] args) {
            ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-rpc.xml");
        }
    }

> 服务启动很简单，只要把spring容器启动，会自动注册服务和监听服务

##client配置
###spring引用服务(spring-rpc.xml)
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:rpc="http://com.hjk.rpc/schema/rpc"
           xmlns:context="http://www.springframework.org/schema/context"
           xsi:schemaLocation="
           http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
           http://com.hjk.rpc/schema/rpc http://com.hjk.rpc/schema/rpc.xsd">
        <!-- 注册中心zookeeper的相关配置 -->
        <context:property-placeholder location="classpath:rpc.properties"/>
        <!-- 通过spring注解来注册组件 看自己项目需要-->
        <context:component-scan base-package="com.rpc.sample.cilent"/>
        <!-- 注册中心zookeeper的相关配置 -->
        <rpc:zookeeper address="${zookeeper.address}"  sessionTimeout="${zookeeper.sessionTimeout}" connectionTimeout="${zookeeper.connectionTimeout}"/>
        <!-- 注册客户端，引用服务，testServer 要和服务端配置相同 -->
        <rpc:client server="testServer">
            <rpc:interface id="animl" class="com.rpc.sample.Animal"/>
        </rpc:client>
    </beans>
###需要的配置文件(rpc.properties)
    zookeeper.address=127.0.0.1:2181
    zookeeper.sessionTimeout=13000
    zookeeper.connectionTimeout=5000
###client 调用服务
    package com.rpc.sample.cilent;

    import com.rpc.sample.Animal;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;
    import org.springframework.stereotype.Component;
    import javax.annotation.Resource;
    @Component //注册spring组件
    public class Client {
        //通过类类型引用服务
        @Autowired
        Animal animal1;
        //通过名称引用服务
        @Resource(name = "animl")
        Animal animal2;

        public String sound1(){
            return animal1.sound();
        }

        public String sound2(){
            return animal2.sound();
        }

        public static void main(String[] args) {
            ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-rpc.xml");
            //直接通过spring容器中获取引用
            Animal animal = ctx.getBean(Animal.class);
            System.out.println("第一次:" + animal.sound());

            Client client = ctx.getBean(Client.class);
            System.out.println("第二次:" + client.sound1());
            System.out.println("第三次:" + client.sound2());
        }
    }

执行结果

    第一次:i sound !!!!!
    第二次:i sound !!!!!
    第三次:i sound !!!!!

> client客户端配置完成，配置简单，和spring完美结合

##maven依赖(server,client)
    <!-- 不管是服务端还是客户端都需要依赖一下module-->
    <dependency>
        <groupId>hrpc</groupId>
        <artifactId>rpc-core</artifactId>
    </dependency>
    <dependency>
        <groupId>hrpc</groupId>
        <artifactId>rpc-spring</artifactId>
    </dependency>


> 结语：框架配置简单，只需要通过spring配置文件来曝露服务和引用服务就行，并且配置简单。

