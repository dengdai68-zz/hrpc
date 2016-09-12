package com.hjk.rpc.registry.zookeeper;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.common.conf.ZookeeperConf;
import com.hjk.rpc.common.exception.NotFoundServiceException;
import com.hjk.rpc.registry.discovery.ServiceDiscovery;

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
        zkClient = new ZkClient(zkconf.getAddress(), zkconf.getSessionTimeout(), zkconf.getConnectionTimeout());
    }

    @Override
    public String discovery(final ServiceObject serviceObject) {
        try {
            String serviceKey = serviceObject.getAppServer() + "/" + serviceObject.getServiceName();
            //查找map是否缓存
            Vector<String> services = serviceCacheMap.get(serviceKey);

            if(services == null){
                services = new Vector();
            }else if(services.size() > 0){
                logger.debug("get address{} from cache!",serviceKey);
                return getRandomAddress(services);
            }

            services.addAll(findServices(serviceKey));
            serviceCacheMap.put(serviceKey,services);
            return getRandomAddress(services);
        }catch (NotFoundServiceException var1){
            throw var1;
        }catch (Exception e){
            logger.error("",e);
        }
        return null;
    }

    private List<String> findServices(final String serviceKey){
        //获取service path
        String servicePath = zkconf.getRegistryPath() + "/" + serviceKey;
        if(!zkClient.exists(servicePath)){
            logger.warn("not found zookeeper registry service, servicePath:{}",servicePath);
            throw new NotFoundServiceException("not found zookeeper registry service!");
        }

        //获取service子节点
        List<String> addressList = zkClient.getChildren(servicePath);
        if (addressList == null || addressList.size() == 0) {
            logger.warn("not found server of this service:{}", serviceKey);
            throw new NotFoundServiceException("not found server of this service!");
        }
        //订阅 服务节点变化
        zkClient.subscribeChildChanges(servicePath,new IZkChildListener(){
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                serviceCacheMap.put(serviceKey ,new Vector<String>(list));
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
