/**
 * Created by hanjk on 16/9/8.
 */

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcSpringTest {

    private static final Logger logger = LoggerFactory.getLogger(RpcSpringTest.class);

    @Test
    public void registrySpring(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-rpc.xml");
//        Object clientBean= ctx.getBeansOfType(ClientBean.class);
//        Object serverBean= ctx.getBeansOfType(ServerBean.class);
//        Object zookeeperBean= ctx.getBeansOfType(ZookeeperBean.class);
//        System.out.println("============clientBean" + clientBean);
//        System.out.println("============serverBean" + serverBean);
//        System.out.println("============zookeeperBean" + zookeeperBean);
//        System.out.println("============zookeeperBean" + ctx.getBean("dddd"));

    }

}
