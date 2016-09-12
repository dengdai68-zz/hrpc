package com.hjk.rpc.spring.bean;

import com.hjk.rpc.common.conf.ZookeeperConf;

/**
 * Created by hanjk on 16/9/8.
 */
public class ZookeeperBean {
    private String address;
    private int sessionTimeout;
    private int connectionTimeout;

    public void injectionConf(){
        new ZookeeperConf(this.address,this.sessionTimeout,this.connectionTimeout);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    public String toString() {
        return "ZookeeperBean{" +
                "address='" + address + '\'' +
                ", sessionTimeout=" + sessionTimeout +
                ", connectionTimeout=" + connectionTimeout +
                '}';
    }
}
