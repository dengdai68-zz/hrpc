package com.hjk.rpc.registry.zookeeper;

import com.hjk.rpc.common.conf.ZookeeperConf;
import com.hjk.rpc.common.exception.NotFoundServiceException;
import com.hjk.rpc.common.exception.NotFoundZookeeperPathException;
import com.hjk.rpc.registry.discovery.ServiceDiscovery;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
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
public class ZookeeperServiceDiscovery implements ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceDiscovery.class);

    private ConcurrentHashMap<String,Vector<String>> serviceCacheMap = new ConcurrentHashMap();

    private final ZkClient zkClient;

    private final ZookeeperConf zkconf;

    public ZookeeperServiceDiscovery(ZookeeperConf zkconf) {
        this.zkconf = zkconf;
        //TODO 增加 WatchedEvent
        zkClient = new ZkClient(zkconf.getAddress(), zkconf.getSessionTimeout(), zkconf.getConnectionTimeout());
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

    private List<String> findServices(final String appServer, final String serviceName){
        //获取service path
        String servicePath = zkconf.getRegistryPath() + "/" + appServer + "/" + serviceName;
        if(!zkClient.exists(servicePath)){
            logger.warn("not found zookeeper registry service, servicePath:{}",servicePath);
            throw new NotFoundServiceException("not found zookeeper registry service!");
        }

        //获取service子节点
        List<String> addressList = zkClient.getChildren(servicePath);
        if (addressList == null || addressList.size() == 0) {
            logger.warn("not found server of this service:{}", serviceName);
            throw new NotFoundServiceException("not found server of this service!");
        }
        //订阅 服务节点变化
        zkClient.subscribeChildChanges(servicePath,new IZkChildListener(){
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                ;
                serviceCacheMap.put(appServer + s.substring(s.lastIndexOf("/")),new Vector<String>(list));
            }
        });
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

    public static final ServiceDiscovery getInstance() {
        if(INSTANCE == null){
            synchronized(ZookeeperServiceDiscovery.class){
                if(INSTANCE == null){
                    INSTANCE = new ZookeeperServiceDiscovery(ZookeeperConf.getZkconf());
                }
            }
        }
        return INSTANCE;
    }
}
