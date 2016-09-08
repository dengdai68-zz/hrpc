package com.hjk.rpc.common.bean;

/**
 * Created by hanjk on 16/9/8.
 */
public class RpcResponse {

    public static final String SUCCESS="0000";
    public static final String FAIL = "9999";

    private String requestId;
    private Object result;
    private String resultCode;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
