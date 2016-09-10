package com.hjk.rpc.spring.server;

import com.alibaba.fastjson.JSON;
import com.hjk.rpc.common.bean.RpcRequest;
import com.hjk.rpc.common.bean.RpcResponse;
import com.hjk.rpc.spring.RpcApplicationContext;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

/**
 * Created by hanjk on 16/9/8.
 */
public class RpcServerHandler  extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);


    public void channelRead(ChannelHandlerContext ctx,
                                Object o) throws Exception {
        logger.debug("server received data:{}",o);
        RpcRequest request;
        RpcResponse response = new RpcResponse();
        try {
            request = JSON.parseObject(String.valueOf(o),RpcRequest.class);
            logger.info("server received client:[{}] requestId:{} data:{}",ctx.channel().remoteAddress(),request.getRequestId(),o);
        }catch (Exception e){
            logger.error("format request object is fail",e);
            response.setResultCode(RpcResponse.FAIL);
            ctx.writeAndFlush(JSON.toJSONString(response)).addListener(ChannelFutureListener.CLOSE);
            return;
        }
        response.setRequestId(request.getRequestId());
        try {
            Object serviceBean = RpcApplicationContext.getRpcBean(request.getServiceName());
            Class<?> serviceClass = serviceBean.getClass();
            String methodName = request.getMethodName();
            Class<?>[] parameterTypes = request.getParameterTypesClass();
            Object[] parameters = request.getParameters();

            FastClass serviceFastClass = FastClass.create(serviceClass);
            FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
            Object result = serviceFastMethod.invoke(serviceBean, parameters);
            response.setResult(result);
            response.setResultCode(RpcResponse.SUCCESS);
        } catch (Throwable e) {
            logger.error("server invoke is error!",e);
            response.setResultCode(RpcResponse.FAIL);
        }
        String responseStr = JSON.toJSONString(response);
        logger.debug("server return data:{}",responseStr);
        logger.info("server return client requestId:{} data:{}",response.getRequestId(),responseStr);
        // 写入 RPC 响应对象并自动关闭连接
        ctx.writeAndFlush(responseStr).addListener(ChannelFutureListener.CLOSE);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("RpcServerHandler caught exception", cause);
        ctx.close();
    }
}
