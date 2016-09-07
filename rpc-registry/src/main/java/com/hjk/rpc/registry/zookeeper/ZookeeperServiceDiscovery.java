package com.hjk.rpc.registry.zookeeper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hjk.rpc.core.discovery.ServiceDiscovery;
import com.hjk.rpc.core.exception.NotFoundRegistryPathException;
import com.hjk.rpc.core.exception.NotFoundServiceException;
import com.hjk.rpc.registry.RegistryConfig;

/**
 * Created by hanjk on 16/9/7.
 */
public class ZookeeperServiceDiscovery implements ServiceDiscovery{

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceDiscovery.class);


    private LinkedHashMap<String,Vector<String>> serviceCacheMap = new LinkedHashMap();

    @Override
    public String discovery(String serviceName) {
        try {

            //查找map是否缓存
            Vector<String> services = serviceCacheMap.get(serviceName);
            if(services != null && services.size() > 0){
                logger.debug("get address{} from cache!",serviceName);
                return getRandomAddress(services);
            }
            ZooKeeper zk = new ZooKeeper(RegistryConfig.zkAddress,RegistryConfig.sessionTimeout, new ServiceWatcher(serviceCacheMap),true);
            //获取registry顶级节点
            String registryPath = RegistryConfig.zkRegistryPath;
            if (null == zk.exists(registryPath, false)) {
                logger.error("create registry top node:{}", registryPath);
                throw new NotFoundRegistryPathException("registry top node not found!");
            }
            //获取service子节点
            String servicePath = registryPath + "/" + serviceName;
            java.util.List<String> addressList = zk.getChildren(servicePath, false);
            if (addressList == null || addressList.size() == 0) {
                logger.warn("create registry top node:{}", registryPath);
                throw new NotFoundServiceException("registry top node not found!");
            }

            if(services == null){
                services = new Vector<>();
            }
            services.addAll(addressList);
            serviceCacheMap.put(serviceName,services);
            return getRandomAddress(services);

        }catch (NotFoundRegistryPathException var0){
            throw var0;
        }catch (NotFoundServiceException var1){
            throw var1;
        }catch (Exception e){
            logger.error("",e);
        }
        return null;
    }

    public String getRandomAddress(Vector<String> services){
        if(services.size() == 1){
            return services.get(0);
        }
        return services.get(ThreadLocalRandom.current().nextInt(services.size()));
    }
}
