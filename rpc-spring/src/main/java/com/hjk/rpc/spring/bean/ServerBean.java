package com.hjk.rpc.spring.bean;

import java.util.List;

/**
 * Created by hanjk on 16/9/7.
 */
public class ServerBean {

    private String server;
    private int port;
    private List<ServiceBean> services;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

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

    @Override
    public String toString() {
        return "ServerBean{" +
                "server='" + server + '\'' +
                ", port=" + port +
                ", services=" + services +
                '}';
    }


}
