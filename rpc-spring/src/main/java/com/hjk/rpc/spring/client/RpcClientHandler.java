package com.hjk.rpc.spring.client;

import com.alibaba.fastjson.JSON;
import com.hjk.rpc.common.bean.RpcResponse;
import io.netty.channel.SimpleChannelInboundHandler;
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
public class RpcClientHandler extends SimpleChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);

    protected RpcResponse response;



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                Object o) throws Exception {
        logger.debug("client received data:{}",o);
    }

}
