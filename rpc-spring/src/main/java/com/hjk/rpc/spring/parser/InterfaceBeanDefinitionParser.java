package com.hjk.rpc.spring.parser;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.hjk.rpc.spring.bean.InterfaceBean;

/**
 * Created by hanjk on 16/9/8.
 */
public class InterfaceBeanDefinitionParser extends AbstractBeanDefinitionParser {


    public InterfaceBeanDefinitionParser() {
    }

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(InterfaceBean.class);
        String id = element.getAttribute("id");
        String clazz = element.getAttribute("class");
        String timeoutInMillis = element.getAttribute("timeoutInMillis");
        builder.addPropertyValue("id", id);
        builder.addPropertyValue("clazz", clazz);
        builder.addPropertyValue("timeoutInMillis", timeoutInMillis);

        return builder.getBeanDefinition();
    }

    protected boolean shouldGenerateId() {
        return true;
    }
}
