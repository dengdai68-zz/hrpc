package com.hjk.rpc.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hjk.rpc.common.bean.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by hanjk on 16/9/8.
 */
public class RpcServerHandler  extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        logger.debug("server received data:{}",msg);

        RpcRequest request = JSON.toJavaObject((JSON) JSON.toJSON(msg),RpcRequest.class);


        ctx.write(msg);//写回数据，
    }
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) //flush掉所有写回的数据
                .addListener(ChannelFutureListener.CLOSE); //当flush完成后关闭channel
    }
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
        cause.printStackTrace();//捕捉异常信息
        logger.error("server caught exception",cause);
        ctx.close();
    }
}
