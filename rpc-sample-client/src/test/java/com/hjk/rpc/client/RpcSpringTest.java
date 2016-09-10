package com.hjk.rpc.client; /**
 * Created by hanjk on 16/9/8.
 */

import com.hjk.rpc.common.Constant;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hjk.rpc.common.bean.RpcRequest;
import com.hjk.rpc.common.bean.RpcResponse;
import com.hjk.rpc.common.codec.RpcDecoder;
import com.hjk.rpc.common.codec.RpcEncoder;
import com.hjk.rpc.sample.api.Transport;
import com.hjk.rpc.spring.bean.ClientBean;
import com.hjk.rpc.spring.bean.ServerBean;
import com.hjk.rpc.spring.bean.ZookeeperBean;
import com.hjk.rpc.spring.client.RpcClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

public class RpcSpringTest {
    @Test
    public void registrySpring(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-rpc.xml");
        Object clientBean= ctx.getBeansOfType(ClientBean.class);
        Object serverBean= ctx.getBeansOfType(ServerBean.class);
        Object zookeeperBean= ctx.getBeansOfType(ZookeeperBean.class);
        System.out.println("============clientBean" + clientBean);
        System.out.println("============serverBean" + serverBean);
        System.out.println("============zookeeperBean" + zookeeperBean);
        Transport transport = (Transport) ctx.getBean("user");
        System.out.println("============" + transport.getName());

    }

    @Test
    public void nettySend(){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建并初始化 Netty 客户端 Bootstrap 对象
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new StringDecoder(Charset.forName(Constant.MESSAGE_CHARSET)));
                    pipeline.addLast(new StringEncoder(Charset.forName(Constant.MESSAGE_CHARSET)));
                    pipeline.addLast(new RpcClientHandler()); // 处理 RPC 响应
                }
            });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            // 连接 RPC 服务器
            ChannelFuture future = bootstrap.connect("127.0.0.1", 12124).sync();
            // 写入 RPC 请求数据并关闭连接
            Channel channel = future.channel();
            channel.writeAndFlush("nihao").sync();
            channel.closeFuture().sync();
            // 返回 RPC 响应对象
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
