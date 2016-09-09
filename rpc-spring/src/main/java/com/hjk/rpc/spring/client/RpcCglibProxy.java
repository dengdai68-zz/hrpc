package com.hjk.rpc.spring.client;

import java.lang.reflect.Method;
import java.util.UUID;

import com.hjk.rpc.core.exception.NotFoundServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hjk.rpc.common.bean.RpcRequest;
import com.hjk.rpc.common.bean.RpcResponse;
import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.common.utils.StringUtil;
import com.hjk.rpc.core.discovery.ServiceDiscovery;
import com.hjk.rpc.registry.zookeeper.ZookeeperServiceDiscovery;
import com.hjk.rpc.spring.ContainerInitializer;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Created by hanjk on 16/9/8.
 */
public class RpcCglibProxy{

    private static final Logger logger = LoggerFactory.getLogger(RpcCglibProxy.class);

    public static Object getPoxyObject(ServiceObject serviceObject) throws
            ClassNotFoundException {

        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(Class.forName(serviceObject.getServiceName()));

        enhancer.setCallback(new MethodInterceptor() {

            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                if(method.getDeclaringClass() == Object.class){
                    return method.invoke(this, objects);
                }else{
                    logger.info("RpcCglibProxy ==== method:{} ,args:{}",method.getName(),objects);

                    //封装 request
                    RpcRequest request = new RpcRequest();
                    request.setServiceName(method.getDeclaringClass().getName());
                    request.setMethodName(method.getName());
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setParameterTypes(method.getParameterTypes());
                    request.setParameters(objects);

                    //发送请求
                    ServiceDiscovery serviceDiscovery = ZookeeperServiceDiscovery.getInstance(ContainerInitializer.zkBean);
                    String serverAddress ;
                    try {
                        serverAddress = serviceDiscovery.discovery(serviceObject.getAppServer(),serviceObject.getServiceName());
                    }catch (NotFoundServiceException var){
                        return null;
                    }

                    if(StringUtil.isEmpty(serverAddress)){
                        return null;
                    }
                    String[] address = serverAddress.split(":");
                    RpcClient client = new RpcClient(address[0],Integer.parseInt(address[1]));

                    RpcResponse response = client.send(request);
                    return response;
                }

            }

        });
        return enhancer.create();

    }
}
