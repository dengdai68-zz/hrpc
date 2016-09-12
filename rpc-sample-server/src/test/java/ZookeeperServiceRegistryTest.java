import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.registry.zookeeper.ZookeeperServiceDiscovery;
import com.hjk.rpc.registry.zookeeper.ZookeeperServiceRegistry;

/**
 * Created by hanjk on 16/9/7.
 */
public class ZookeeperServiceRegistryTest {
    @Test
    public void registry() throws Exception {
        ZookeeperServiceRegistry registry = new ZookeeperServiceRegistry(null);
        ServiceObject serviceObject = new ServiceObject();
        serviceObject.setServiceName("testService");
        serviceObject.setServiceAddress("testServiceAddress");
        serviceObject.setAppServer("testAppServer");
        registry.register(serviceObject);
        Thread.sleep(400*1000);
    }
    @Test
    public void discovery() throws IOException {
        ZookeeperServiceDiscovery discovery = new ZookeeperServiceDiscovery(null);
        String serviceAddress = discovery.discovery(null);
        System.out.println(serviceAddress);
    }
    @Test
    public void zookeeperTest(){
        try {
            // 创建一个与服务器的连接
            ZooKeeper zk = new ZooKeeper("127.0.0.1:2181",
                    3000, new Watcher() {
                // 监控所有被触发的事件
                public void process(WatchedEvent event) {
                    System.out.println("已经触发了" + event.getType() + "事件！");
                }
            });
            // 创建一个目录节点
            zk.create("/testRootPath", "testRootData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
            // 创建一个子目录节点
            zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            System.out.println(new String(zk.getData("/testRootPath",false,null)));
            // 取出子目录节点列表
            System.out.println(zk.getChildren("/testRootPath",true));
            // 修改子目录节点数据
            zk.setData("/testRootPath/testChildPathOne","modifyChildDataOne".getBytes(),-1);
            System.out.println("目录节点状态：["+zk.exists("/testRootPath",true)+"]");
            // 创建另外一个子目录节点
            zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo",true,null)));
            // 删除子目录节点
            zk.delete("/testRootPath/testChildPathTwo",-1);
            zk.delete("/testRootPath/testChildPathOne",-1);
            // 删除父目录节点
            zk.delete("/testRootPath",-1);
            // 关闭连接
            zk.close();
        }catch (Throwable e){

        }

    }

}