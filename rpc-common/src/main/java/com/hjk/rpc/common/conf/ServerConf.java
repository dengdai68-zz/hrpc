package com.hjk.rpc.common.conf;

/**
 * Created by dengd on 2016/9/10.
 */
public class ServerConf {
    private static ServerConf serverConf;

    private String serverName;
    private int port;

    public ServerConf(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
        serverConf = this;
    }

    public String getServerName() {
        return serverName;
    }

    public int getPort() {
        return port;
    }

    public static ServerConf getServerConf() {
        return serverConf;
    }
}
