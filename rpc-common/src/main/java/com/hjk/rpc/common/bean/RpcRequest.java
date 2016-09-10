package com.hjk.rpc.common.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;

/**
 * 请求
 * Created by hanjk on 16/9/8.
 */
public class RpcRequest {

    private String requestId;
    private String serviceName;
    private String methodName;
    private String[] parameterTypes;
    private Object[] parameters;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }


    public String[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId='" + requestId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }

    @JSONField(serialize = false)
    public Class<?>[] getParameterTypesClass() throws ClassNotFoundException {
        Class<?>[] clazzs = new Class<?>[parameterTypes.length];
        for (int i=0;i<parameterTypes.length;i++){
            clazzs[i] = Class.forName(parameterTypes[i]);
        }
        return clazzs;
    }

    public void setParameterTypes(String[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
    public void setParameterTypesClass(Class<?>[] parameterTypes) {
        String[] classs = new String[parameterTypes.length];
        for (int i=0;i<parameterTypes.length;i++){
            classs[i] = parameterTypes[i].getName();
        }
        this.parameterTypes =  classs;
    }
}
