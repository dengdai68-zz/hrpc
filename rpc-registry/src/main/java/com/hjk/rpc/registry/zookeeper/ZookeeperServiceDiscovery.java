package com.hjk.rpc.registry.zookeeper;

import com.hjk.rpc.core.discovery.ServiceDiscovery;
import com.hjk.rpc.core.exception.NotFoundServiceException;
import com.hjk.rpc.core.exception.NotFoundZookeeperPathException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by hanjk on 16/9/7.
 */
public class ZookeeperServiceDiscovery implements ServiceDiscovery{

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceDiscovery.class);

    private ConcurrentHashMap<String,Vector<String>> serviceCacheMap = new ConcurrentHashMap();

    private ZookeeperConf zkBean;

    public ZookeeperServiceDiscovery(ZookeeperConf zkBean) {
        this.zkBean = zkBean;
    }

    @Override
    public String discovery(String appServer,String serviceName) {
        try {
            String serviceKey = appServer + "/" + serviceName;
            //查找map是否缓存
            Vector<String> services = serviceCacheMap.get(serviceKey);
            if(services != null && services.size() > 0){
                logger.debug("get address{} from cache!",serviceKey);
                return getRandomAddress(services);
            }
            if(services == null){
                services = new Vector<>();
            }
            services.addAll(findServices(appServer,serviceName));
            serviceCacheMap.put(serviceKey,services);
            return getRandomAddress(services);
        }catch (NotFoundZookeeperPathException var0){
            throw var0;
        }catch (NotFoundServiceException var1){
            throw var1;
        }catch (Exception e){
            logger.error("",e);
        }
        return null;
    }

    private List<String> findServices(String appServer, String serviceName) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk = new ZooKeeper(zkBean.getZkAddress(),zkBean.getSessionTimeoutInMillis(), new ServiceWatcher(serviceCacheMap),true);
        //获取registry顶级节点
        String registryPath = zkBean.getZkRegistryPath();
        if (null == zk.exists(registryPath, false)) {
            logger.error("not found registry top node:{}", registryPath);
            throw new NotFoundZookeeperPathException("registry top node not found!");
        }
        //获取系统节点
        String appServerPath = registryPath + "/" + appServer;
        if (null == zk.exists(appServerPath, false)) {
            logger.error("not found appServer node:{}", appServerPath);
            throw new NotFoundZookeeperPathException("registry top node not found!");
        }
        //获取service子节点
        String servicePath = appServerPath + "/" + serviceName;
        List<String> addressList = zk.getChildren(servicePath, false);
        if (addressList == null || addressList.size() == 0) {
            logger.warn("not found server:{}", servicePath);
            throw new NotFoundServiceException("registry top node not found!");
        }
        zk.close();
        return addressList;
    }

    public String getRandomAddress(Vector<String> services){
        if(services.size() == 1){
            return services.get(0);
        }
        return services.get(ThreadLocalRandom.current().nextInt(services.size()));
    }


    private static volatile ServiceDiscovery INSTANCE;

    private static class SingletonHolder {
        private static final ServiceDiscovery INSTANCE = new ZookeeperServiceDiscovery(null);
    }

    public static final ServiceDiscovery getInstance(ZookeeperConf zkBean) {
        if(INSTANCE == null){
            synchronized(ZookeeperServiceDiscovery.class){
                if(INSTANCE == null){
                    INSTANCE = new ZookeeperServiceDiscovery(zkBean);
                }
            }
        }
        return INSTANCE;
    }

}
