/**
 * Created by hanjk on 16/9/8.
 */
import com.hjk.rpc.spring.bean.ClientBean;
import com.hjk.rpc.spring.bean.ServerBean;
import com.hjk.rpc.spring.bean.ZookeeperBean;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcSpringTest {
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
