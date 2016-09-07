package com.hjk.rpc.registry;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hanjk on 16/9/7.
 */
public class RegistryConfig {

    private static final Logger logger = LoggerFactory.getLogger(RegistryConfig.class);

    public static String zkRegistryPath = "/rpc_registry";

    public static String zkAddress = "127.0.0.1:2181";

    public static int sessionTimeout=3000;

    static {
        try {
            Properties prop = new Properties();
            InputStream in = RegistryConfig.class.getResourceAsStream("classpath:rpc.properties");
            prop.load(in);
            zkAddress = prop.getProperty("zkAddress");
            sessionTimeout = Integer.parseInt(prop.getProperty("zkSessionTimeout","3000"));
            in.close();
        } catch (Throwable e) {
            logger.error("RegistryConfig static block is fail",e);
        }
    }
}
