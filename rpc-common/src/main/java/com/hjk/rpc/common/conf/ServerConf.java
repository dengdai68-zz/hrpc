package com.hjk.rpc.common.conf;

/**
 * Created by dengd on 2016/9/10.
 */
public class ServerConf {
    private String serverName;
    private int port;

    public ServerConf(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
    }

    public String getServerName() {
        return serverName;
    }

    public int getPort() {
        return port;
    }
}
