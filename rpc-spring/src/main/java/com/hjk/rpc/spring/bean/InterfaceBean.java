package com.hjk.rpc.spring.bean;

import com.hjk.rpc.common.utils.StringUtil;
import org.springframework.beans.factory.FactoryBean;

import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.spring.client.RpcCglibProxy;

/**
 * Created by hanjk on 16/9/8.
 */
public class InterfaceBean implements FactoryBean<Object>{

    private String id;
    private String clazz;

    private String server;

    private int timeoutInMillis;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public int getTimeoutInMillis() {
        return timeoutInMillis;
    }

    public void setTimeoutInMillis(int timeoutInMillis) {
        this.timeoutInMillis = timeoutInMillis;
    }

    @Override
    public String toString() {
        return "InterfaceBean{" +
                "id='" + id + '\'' +
                ", clazz='" + clazz + '\'' +
                ", timeoutInMillis=" + timeoutInMillis +
                '}';
    }

    @Override
    public Object getObject() throws Exception {
        ServiceObject service = new ServiceObject();
        service.setServiceName(clazz);
        service.setAppServer(server);
        return RpcCglibProxy.getPoxyObject(service);
    }

    @Override
    public Class<?> getObjectType() {
        try {
            if(StringUtil.isNotEmpty(clazz)){
                return Class.forName(clazz);
            }
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
