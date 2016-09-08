package com.hjk.rpc.common.bean;

import com.hjk.rpc.common.utils.StringUtil;

/**
 * Created by hanjk on 16/9/7.
 */
public class ServiceObject {

    private String appServer;
    private String serviceName;
    private String serviceAddress;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getAppServer() {
        return appServer;
    }

    public void setAppServer(String appServer) {
        this.appServer = appServer;
    }

    public void validate(){
        if (StringUtil.isEmpty(appServer))
            throw new IllegalArgumentException("appServer is not null");
        if (StringUtil.isEmpty(serviceName))
            throw new IllegalArgumentException("serviceName is not null");
        if (StringUtil.isEmpty(serviceAddress))
            throw new IllegalArgumentException("serviceAddress is not null");
    }
}
