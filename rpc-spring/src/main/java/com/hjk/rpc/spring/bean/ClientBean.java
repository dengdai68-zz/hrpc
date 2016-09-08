package com.hjk.rpc.spring.bean;

import java.util.List;

/**
 * Created by hanjk on 16/9/7.
 */
public class ClientBean {
    private String server;
    private List<InterfaceBean> interfaces;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public List<InterfaceBean> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<InterfaceBean> interfaces) {
        this.interfaces = interfaces;
    }

    @Override
    public String toString() {
        return "ClientBean{" +
                "server='" + server + '\'' +
                ", interfaces=" + interfaces +
                '}';
    }
}
