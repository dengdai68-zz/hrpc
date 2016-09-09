package com.hjk.rpc.spring.factory;

import com.hjk.rpc.spring.bean.InterfaceBean;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by hanjk on 16/9/9.
 */
public class InterfaceBeanFactory implements FactoryBean<InterfaceBean> {

    @Override
    public InterfaceBean getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return InterfaceBean.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
