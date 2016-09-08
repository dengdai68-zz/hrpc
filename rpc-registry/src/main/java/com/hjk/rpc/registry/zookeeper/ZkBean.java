package com.hjk.rpc.registry.zookeeper;

/**
 * Created by dengd on 2016/9/9.
 */
public class ZkBean {
    private String zkAddress;
    private int sessionTimeoutInMillis;
    private String zkRegistryPath  = "rpc_registry";

    public String getZkRegistryPath() {
        return zkRegistryPath;
    }

    public void setZkRegistryPath(String zkRegistryPath) {
        this.zkRegistryPath = zkRegistryPath;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public int getSessionTimeoutInMillis() {
        return sessionTimeoutInMillis;
    }

    public void setSessionTimeoutInMillis(int sessionTimeoutInMillis) {
        this.sessionTimeoutInMillis = sessionTimeoutInMillis;
    }

    public ZkBean(String zkAddress, int sessionTimeoutInMillis) {
        this.zkAddress = zkAddress;
        this.sessionTimeoutInMillis = sessionTimeoutInMillis;
    }
}
