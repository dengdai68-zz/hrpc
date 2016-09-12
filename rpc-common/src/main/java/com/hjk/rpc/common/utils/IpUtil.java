package com.hjk.rpc.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by hanjk on 16/9/12.
 */
public class IpUtil {
    public static String getLocalIp(){
        String ip = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
        return ip;
    }
}
