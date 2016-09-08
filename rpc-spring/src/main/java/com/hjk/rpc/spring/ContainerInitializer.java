package com.hjk.rpc.spring;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.hjk.rpc.common.bean.RpcRequest;
import com.hjk.rpc.common.bean.RpcResponse;
import com.hjk.rpc.common.codec.RpcDecoder;
import com.hjk.rpc.common.codec.RpcEncoder;
import com.hjk.rpc.registry.zookeeper.ZkBean;
import com.hjk.rpc.spring.server.RpcServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.core.registry.ServiceRegistry;
import com.hjk.rpc.registry.zookeeper.ZookeeperServiceRegistry;
import com.hjk.rpc.spring.bean.ServerBean;
import com.hjk.rpc.spring.bean.ServiceBean;
import com.hjk.rpc.spring.bean.ZookeeperBean;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by hanjk on 16/9/8.
 */
public class ContainerInitializer implements ApplicationContextAware, InitializingBean, ApplicationListener<ContextRefreshedEvent>, BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(ContainerInitializer.class);

    private ApplicationContext cxt;
    private ServerBean server;

    private ZookeeperBean zk;

    private ServiceRegistry serviceRegistry;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("=======setApplicationContext");
        cxt = applicationContext;
        //获取zookeeper 配置,以后要做zookeeper解耦
        Map zookeeperMap = applicationContext.getBeansOfType(ZookeeperBean.class);
        if (zookeeperMap != null && !zookeeperMap.isEmpty()) {
            zk = (ZookeeperBean) zookeeperMap.values().toArray()[0];
        }

        //获取服务列表
        Map serverMap = applicationContext.getBeansOfType(ServerBean.class);
        if (serverMap != null && !serverMap.isEmpty()) {
            server = (ServerBean) serverMap.values().toArray()[0];
        }
        //获取客户端列表
    }

    /**
     * 容器初始化完成之后执行
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("=======afterPropertiesSet");
        //开始注册服务
        logger.info("begin registry service!");
        if (zk == null) {
            new RuntimeException("注册中心未找到!");
        }

        if (server == null)
            return;

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new RpcDecoder(RpcRequest.class)); // 解码  请求
                    pipeline.addLast(new RpcEncoder(RpcResponse.class)); // 编码  响应
                    pipeline.addLast(new RpcServerHandler());
                }
            });
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 启动 netty 服务器
            ChannelFuture future = bootstrap.bind(server.getPort()).sync();

            ZkBean zkBean = new ZkBean(zk.getZkAddress(),zk.getSessionTimeoutInMillis());
            if (serviceRegistry == null) {
                serviceRegistry = new ZookeeperServiceRegistry(zkBean);
            }
            String localIp = getIp();
            //注册服务
            for (ServiceBean service : server.getServices()) {
                ServiceObject serviceObject = new ServiceObject();
                serviceObject.setAppServer(server.getServer());
                serviceObject.setServiceName(service.getClazz());
                serviceObject.setServiceAddress(localIp);
                serviceRegistry.registry(serviceObject);
                logger.info("register service:{} , {}", serviceObject.getServiceName(), serviceObject.getServiceAddress());
            }

            logger.info("server started on port {}", server.getPort());

            future.channel().closeFuture().sync();

        } catch (Exception e) {
            logger.error("server start fail!!!", e);
            throw e;
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    public static String getIp() throws Exception {
        String ip = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (UnknownHostException e) {
            throw e;
        }

        return ip;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        logger.info("=======setBeanFactory");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {//防止 mvc容器 多次刷新

        }
        logger.info("=======onApplicationEvent");
    }
}
