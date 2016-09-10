/**
 * Created by hanjk on 16/9/8.
 */

import com.hjk.rpc.common.Constant;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hjk.rpc.common.bean.RpcRequest;
import com.hjk.rpc.common.bean.RpcResponse;
import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.common.codec.RpcDecoder;
import com.hjk.rpc.common.codec.RpcEncoder;
import com.hjk.rpc.core.registry.ServiceRegistry;
import com.hjk.rpc.registry.zookeeper.ZookeeperServiceRegistry;
import com.hjk.rpc.spring.ContainerInitializer;
import com.hjk.rpc.spring.bean.ServiceBean;
import com.hjk.rpc.spring.server.RpcServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.Charset;

public class RpcSpringTest {

    private static final Logger logger = LoggerFactory.getLogger(RpcSpringTest.class);

    @Test
    public void registrySpring(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-rpc.xml");
//        Object clientBean= ctx.getBeansOfType(ClientBean.class);
//        Object serverBean= ctx.getBeansOfType(ServerBean.class);
//        Object zookeeperBean= ctx.getBeansOfType(ZookeeperBean.class);
//        System.out.println("============clientBean" + clientBean);
//        System.out.println("============serverBean" + serverBean);
//        System.out.println("============zookeeperBean" + zookeeperBean);
//        System.out.println("============zookeeperBean" + ctx.getBean("dddd"));

    }

    @Test
    public void startNetty(){
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
            ChannelFuture future = bootstrap.bind("127.0.0.1",12124).sync();

            future.channel().closeFuture().sync();

        } catch (Exception e) {
            logger.error("server start fail!!!", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
