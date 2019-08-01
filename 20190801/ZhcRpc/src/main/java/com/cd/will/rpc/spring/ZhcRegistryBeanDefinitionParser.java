package com.cd.will.rpc.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ZhcRegistryBeanDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String address = element.getAttribute("address");
        String protocol = element.getAttribute("protocol");
        String portStr = element.getAttribute("port");
        String id = element.getAttribute("id");

        int port = -1;
        if (portStr != null) {
            try {
                port = Integer.valueOf(portStr);
            } catch (Exception e) {
                throw new RuntimeException(
                        "Registry init error,id=" + id + ",port=" + portStr + ",address=" + address + "\n" + e);
            }
        }

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(ZhcRpcRegistry.class);
        beanDefinition.setLazyInit(false);
        beanDefinition.getPropertyValues().addPropertyValue("address", address);
        beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);
        beanDefinition.getPropertyValues().addPropertyValue("port", port);

        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);

        return beanDefinition;
    }
}
