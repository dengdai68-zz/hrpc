package com.hjk.rpc.spring.bean;

/**
 * Created by hanjk on 16/9/7.
 */
public class ServiceBean {

    private String clazz;

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "ServiceBean{" +
                "clazz='" + clazz + '\'' +
                '}';
    }
}
