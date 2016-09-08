package com.hjk.rpc.client;

import com.alibaba.fastjson.JSON;
import com.hjk.rpc.common.bean.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * RPC 客户端（用于发送 RPC 请求）
 *
 * @author huangyong
 * @since 1.0.0
 */
public class RpcClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);

    protected RpcResponse response;


    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        logger.debug("client received data:{}",msg);

        response = JSON.parseObject(JSON.toJSONString(msg),RpcResponse.class);

//        ctx.write(response);//写回数据，
    }
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) //flush掉所有写回的数据
                .addListener(ChannelFutureListener.CLOSE); //当flush完成后关闭channel
    }
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
        cause.printStackTrace();//捕捉异常信息
        logger.error("client caught exception",cause);
        ctx.close();
    }

}
