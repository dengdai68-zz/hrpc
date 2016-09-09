package com.hjk.rpc.spring.parser;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.hjk.rpc.spring.bean.ServiceBean;

/**
 * Created by hanjk on 16/9/8.
 */
public class ServiceBeanDefinitionParser extends AbstractBeanDefinitionParser {


    public ServiceBeanDefinitionParser() {
    }

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ServiceBean.class);
        String clazz = element.getAttribute("class");
        builder.addPropertyValue("clazz", clazz);
        return builder.getBeanDefinition();
    }

    protected boolean shouldGenerateId() {
        return true;
    }
}
