package com.hjk.rpc.spring.bean;

import java.util.List;

/**
 * Created by hanjk on 16/9/7.
 */
public class ServerBean {

    private String name;
    private int port;
    private List<ServiceBean> services;

    public List<ServiceBean> getServices() {
        return services;
    }

    public void setServices(List<ServiceBean> services) {
        this.services = services;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ServerBean{" +
                "name='" + name + '\'' +
                ", port=" + port +
                ", services=" + services +
                '}';
    }
}
