package com.hjk.rpc.spring.parser;

import com.hjk.rpc.spring.bean.ZookeeperBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Created by hanjk on 16/9/8.
 */
public class ZookeeperBeanDefinitionParser extends AbstractBeanDefinitionParser {


    public ZookeeperBeanDefinitionParser() {
    }

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ZookeeperBean.class);
        String zkAddress = element.getAttribute("zkAddress");
        String sessionTimeoutInMillis = element.getAttribute("sessionTimeoutInMillis");
        builder.addPropertyValue("zkAddress", zkAddress);
        builder.addPropertyValue("sessionTimeoutInMillis", sessionTimeoutInMillis);

        return builder.getBeanDefinition();
    }

    protected boolean shouldGenerateId() {
        return true;
    }
}
