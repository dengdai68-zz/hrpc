package com.hjk.rpc.spring.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hjk.rpc.common.bean.RpcRequest;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by hanjk on 16/9/8.
 */
public class RpcServerHandler  extends SimpleChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                Object o) throws Exception {
        logger.debug("server received data:{}",o);

    }
}
