package com.hjk.rpc.core.server;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hjk.rpc.common.Constant;
import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.common.conf.ServerConf;
import com.hjk.rpc.common.conf.ZookeeperConf;
import com.hjk.rpc.common.utils.IpUtil;
import com.hjk.rpc.registry.registry.ServiceRegistry;
import com.hjk.rpc.registry.zookeeper.ZookeeperServiceRegistry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * netty服务启动，注册zookeeper
 * Created by hanjk on 16/9/9.
 */
public class ServerInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ServerInitializer.class);

    public void init(final Map<String, Object> rpcServiceMap) throws Exception {
        ServerConf serverConf = ServerConf.getServerConf();
        //开始注册服务
        logger.info("begin ServerInitializer!");
        if (serverConf == null)
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
                    pipeline.addLast(new RpcServerHandler(rpcServiceMap));
                }
            });
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 启动 netty 服务器
            ChannelFuture future = bootstrap.bind(serverConf.getPort()).sync();
            logger.info("netty started on port {}", serverConf.getPort());
            serviceRegister(rpcServiceMap.keySet());
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

    private void serviceRegister(Set<String> services) throws IOException {
        ZookeeperConf zkconf = ZookeeperConf.getZkconf();
        ServerConf server = ServerConf.getServerConf();
        if (ZookeeperConf.getZkconf() == null) {
            throw new RuntimeException("注册中心未找到!");
        }
        ServiceRegistry serviceRegistry = new ZookeeperServiceRegistry(zkconf);
        String localIp = IpUtil.getLocalIp();
        //注册服务
        for (String serviceName : services) {
            ServiceObject serviceObject = new ServiceObject();
            serviceObject.setAppServer(server.getServerName());
            serviceObject.setServiceName(serviceName);
            serviceObject.setServiceAddress(localIp+":"+server.getPort());
            serviceRegistry.register(serviceObject);
            logger.info("register service:{} , {}", serviceObject.getServiceName(), serviceObject.getServiceAddress());
        }
        logger.info("service register is successful!");
    }
}
