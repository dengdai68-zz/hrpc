package com.hjk.rpc.spring.parser;

import com.hjk.rpc.spring.bean.ClientBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.Iterator;
import java.util.List;

/**
 * Created by hanjk on 16/9/8.
 */
public class ClientBeanDefinitionParser extends AbstractBeanDefinitionParser {


    public ClientBeanDefinitionParser() {
    }

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ClientBean.class);

        String server = element.getAttribute("server");
        builder.addPropertyValue("server", server);

        List childElements = DomUtils.getChildElementsByTagName(element, "interface");
        if(childElements != null && childElements.size() > 0) {
            this.parseChildren(childElements,builder, parserContext,server);
        }

        return builder.getBeanDefinition();
    }

    private void parseChildren(List<Element> childElements, BeanDefinitionBuilder builder, ParserContext parserContext, String server) {
        InterfaceBeanDefinitionParser parser = new InterfaceBeanDefinitionParser();
        Iterator var5 = childElements.iterator();

        while(var5.hasNext()) {
            Element element = (Element)var5.next();
            element.setAttribute("server",server);
            parser.parse(element, parserContext);
        }
    }

    protected boolean shouldGenerateId() {
        return true;
    }
}
