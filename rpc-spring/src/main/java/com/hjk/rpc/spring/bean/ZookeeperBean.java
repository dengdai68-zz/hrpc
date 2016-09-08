package com.hjk.rpc.spring.bean;

/**
 * Created by hanjk on 16/9/8.
 */
public class ZookeeperBean {
    private String zkAddress;
    private int sessionTimeoutInMillis;

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

    @Override
    public String toString() {
        return "ZookeeperBean{" +
                "zkAddress='" + zkAddress + '\'' +
                ", sessionTimeoutInMillis=" + sessionTimeoutInMillis +
                '}';
    }
}
