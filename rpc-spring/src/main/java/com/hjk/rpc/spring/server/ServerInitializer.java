package com.hjk.rpc.spring.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hjk.rpc.common.bean.RpcRequest;
import com.hjk.rpc.common.bean.RpcResponse;
import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.common.codec.RpcDecoder;
import com.hjk.rpc.common.codec.RpcEncoder;
import com.hjk.rpc.core.registry.ServiceRegistry;
import com.hjk.rpc.registry.zookeeper.ZookeeperServiceRegistry;
import com.hjk.rpc.spring.ContainerInitializer;
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
 * Created by hanjk on 16/9/9.
 */
public class ServerInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ServerInitializer.class);

    public static void init(ServerBean server) throws Exception {
        //开始注册服务
        logger.info("begin registry service!");
        if (ContainerInitializer.zkBean == null) {
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

            ServiceRegistry serviceRegistry = new ZookeeperServiceRegistry(ContainerInitializer.zkBean);

            String localIp = getIp();
            //注册服务
            for (ServiceBean service : server.getServices()) {
                ServiceObject serviceObject = new ServiceObject();
                serviceObject.setAppServer(server.getServer());
                serviceObject.setServiceName(service.getClazz());
                serviceObject.setServiceAddress(localIp+":"+server.getPort());
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
