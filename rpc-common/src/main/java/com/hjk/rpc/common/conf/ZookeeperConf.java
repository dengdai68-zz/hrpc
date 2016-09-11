package com.hjk.rpc.common.conf;

/**
 * Created by dengd on 2016/9/9.
 */
public class ZookeeperConf {

    private static ZookeeperConf zkconf = null;
    private String registryPath  = "/rpc_registry";
    private String address;
    private int sessionTimeout;
    private int connectionTimeout;

    public String getRegistryPath() {
        return registryPath;
    }

    public void setRegistryPath(String registryPath) {
        this.registryPath = registryPath;
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

    public ZookeeperConf(String address, int sessionTimeout, int connectionTimeout) {
        this.address = address;
        this.sessionTimeout = sessionTimeout;
        this.connectionTimeout = connectionTimeout;
        ZookeeperConf.zkconf = this;
    }

    public static ZookeeperConf getZkconf(){
        return zkconf;
    }
}
