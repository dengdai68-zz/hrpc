package com.hjk.rpc.core.client;

import com.alibaba.fastjson.JSON;
import com.hjk.rpc.common.bean.RpcResponse;
import com.hjk.rpc.common.exception.MessageFormatException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理服务端返回信息handler
 */
public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);

    protected RpcResponse response;

    public void channelRead(ChannelHandlerContext ctx,
                                Object o) throws Exception {
        logger.debug("client received return data:{}",o);
        try {
            response = JSON.parseObject(String.valueOf(o),RpcResponse.class);
        }catch (Exception e){
            throw new MessageFormatException("format request object is fail!");
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("RpcClientHandler caught exception", cause);
        ctx.close();
    }

}
