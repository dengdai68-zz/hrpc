package com.hjk.rpc.core.server;

import java.util.Map;

import javax.management.ServiceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.hjk.rpc.common.bean.RpcRequest;
import com.hjk.rpc.common.bean.RpcResponse;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

/**
 * Created by hanjk on 16/9/8.
 */
public class RpcServerHandler  extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    Map<String, Object> rpcServiceMap;

    public RpcServerHandler(Map<String, Object> rpcServiceMap) {
        this.rpcServiceMap = rpcServiceMap;
    }

    public void channelRead(ChannelHandlerContext ctx,
                                Object o) throws Exception {
        logger.debug("server received data:{}",o);
        RpcRequest request;
        RpcResponse response = new RpcResponse();
        try {
            request = JSON.parseObject(String.valueOf(o),RpcRequest.class);
            logger.info("server received client:[{}] data:{}",ctx.channel().remoteAddress(),o);
        }catch (Exception e){
            logger.error("format request object is fail",e);
            response.setResultCode(RpcResponse.FAIL);
            ctx.writeAndFlush(JSON.toJSONString(response)).addListener(ChannelFutureListener.CLOSE);
            return;
        }
        response.setRequestId(request.getRequestId());
        try {
            Object serviceBean = rpcServiceMap.get(request.getServiceName());
            if(serviceBean == null){
                throw new ServiceNotFoundException("service:"+request.getServiceName()+" not found!");
            }
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
            response.setErrorMsg(e.getMessage());
        }
        String responseStr = JSON.toJSONString(response);
        logger.debug("server return data:{}",responseStr);
        logger.info("server return client  data:{}",responseStr);
        // 写入 RPC 响应对象并自动关闭连接
        ctx.writeAndFlush(responseStr).addListener(ChannelFutureListener.CLOSE);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("RpcServerHandler caught exception", cause);
        ctx.close();
    }
}
