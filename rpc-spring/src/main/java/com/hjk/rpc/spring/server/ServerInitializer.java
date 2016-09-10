package com.hjk.rpc.spring.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import com.hjk.rpc.common.Constant;
import com.hjk.rpc.registry.zookeeper.ZookeeperConf;
import com.hjk.rpc.spring.RpcApplicationContext;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.core.registry.ServiceRegistry;
import com.hjk.rpc.registry.zookeeper.ZookeeperServiceRegistry;
import com.hjk.rpc.spring.bean.ServerBean;
import com.hjk.rpc.spring.bean.ServiceBean;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty服务启动，注册zookeeper
 * Created by hanjk on 16/9/9.
 */
public class ServerInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ServerInitializer.class);

    public static void init(ServerBean server) throws Exception {
        //开始注册服务
        logger.info("begin ServerInitializer!");
        if (RpcApplicationContext.zkconf == null) {
            throw new RuntimeException("注册中心未找到!");
        }
        ZookeeperConf zkBean = RpcApplicationContext.zkconf;
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
                    pipeline.addLast(new StringDecoder(Charset.forName(Constant.MESSAGE_CHARSET)));
                    pipeline.addLast(new StringEncoder(Charset.forName(Constant.MESSAGE_CHARSET)));
                    pipeline.addLast(new RpcServerHandler());
                }
            });
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 启动 netty 服务器
            ChannelFuture future = bootstrap.bind(server.getPort()).sync();
            logger.info("netty started on port {}", server.getPort());
            ServiceRegistry serviceRegistry = new ZookeeperServiceRegistry(zkBean);
            String localIp = getIp();
            //注册服务
            for (ServiceBean service : server.getServices()) {
                ServiceObject serviceObject = new ServiceObject();
                serviceObject.setAppServer(server.getServer());
                serviceObject.setServiceName(service.getClazz());
                serviceObject.setServiceAddress(localIp+":"+server.getPort());
                serviceRegistry.register(serviceObject);
                logger.info("register service:{} , {}", serviceObject.getServiceName(), serviceObject.getServiceAddress());
            }
            logger.info("service register is successful!");
            logger.info("server to start successfully!!!");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("server to start failure!!!", e);
            throw e;
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    private static String getIp() throws Exception {
        String ip = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (UnknownHostException e) {
            throw e;
        }
        ip = "127.0.0.1";
        return ip;
    }
}
