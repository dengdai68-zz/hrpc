package com.hjk.rpc.core.server;

import com.hjk.rpc.common.Constant;
import com.hjk.rpc.common.conf.ServerConf;
import com.hjk.rpc.registry.zookeeper.ZookeeperServiceInitializer;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * netty服务启动，注册zookeeper
 * Created by hanjk on 16/9/9.
 */
public class ServerInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ServerInitializer.class);

    private ServerConf server;

    public ServerInitializer(ServerConf server) {
        this.server = server;
    }

    public void init(List<String> services, final Map<String, Object> rpcServiceMap) throws Exception {
        //开始注册服务
        logger.info("begin ServerInitializer!");
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
                    pipeline.addLast(new RpcServerHandler(rpcServiceMap));
                }
            });
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 启动 netty 服务器
            ChannelFuture future = bootstrap.bind(server.getPort()).sync();
            logger.info("netty started on port {}", server.getPort());
            new ZookeeperServiceInitializer().init(services,server);
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
}
