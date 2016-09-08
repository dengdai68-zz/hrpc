package com.hjk.rpc.spring.bean;

/**
 * Created by hanjk on 16/9/8.
 */
public class InterfaceBean {
    private String id;
    private String clazz;
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
}
