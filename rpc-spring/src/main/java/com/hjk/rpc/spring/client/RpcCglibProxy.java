package com.hjk.rpc.spring.client;

import java.lang.reflect.Method;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hjk.rpc.common.bean.RpcRequest;
import com.hjk.rpc.common.bean.RpcResponse;
import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.core.discovery.ServiceDiscovery;
import com.hjk.rpc.registry.zookeeper.ZookeeperServiceDiscovery;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Created by hanjk on 16/9/8.
 */
public class RpcCglibProxy implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RpcCglibProxy.class);

    private ServiceObject serviceObject;

    public Object getInstance(Class clazz,ServiceObject serviceObject) {
        this.serviceObject = serviceObject;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        // 回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        logger.info("RpcCglibProxy ==== method:{} ,args:{}",method.getName(),objects);

        //封装 request
        RpcRequest request = new RpcRequest();
        request.setServiceName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setRequestId(UUID.randomUUID().toString());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(objects);

        //发送请求
        ServiceDiscovery serviceDiscovery = ZookeeperServiceDiscovery.getInstance();
        String serverAddress = serviceDiscovery.discovery(serviceObject.getAppServer(),serviceObject.getServiceName());
        String[] address = serverAddress.split(":");
        RpcClient client = new RpcClient(address[0],Integer.parseInt(address[1]));

        RpcResponse response = client.send(request);
        return response;
    }
}
