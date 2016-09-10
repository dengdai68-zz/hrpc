package com.hjk.rpc.common.utils;

import java.util.UUID;

/**
 * Created by dengd on 2016/9/9.
 */
public class UUIDUtil {
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }
}
