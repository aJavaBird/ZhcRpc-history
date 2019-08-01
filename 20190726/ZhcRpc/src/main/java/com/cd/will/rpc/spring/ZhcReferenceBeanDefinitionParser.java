package com.cd.will.rpc.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ZhcReferenceBeanDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String interfaceName = element.getAttribute("interface");
        String registry = element.getAttribute("registry");
        String checkStr = element.getAttribute("check");
        Boolean check = false;
        if (checkStr != null) {
            try {
                check = Boolean.valueOf(checkStr);
            } catch (Exception e) {
                throw new RuntimeException("reference check error,interface=" + interfaceName + "\n" + e);
            }
        }

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(ZhcRpcReference.class);
        beanDefinition.setLazyInit(false);
        beanDefinition.getPropertyValues().addPropertyValue("interface", interfaceName);
        beanDefinition.getPropertyValues().addPropertyValue("registry", registry);
        beanDefinition.getPropertyValues().addPropertyValue("check", check);

        parserContext.getRegistry().registerBeanDefinition(interfaceName, beanDefinition);
        parserContext.getRegistry().registerBeanDefinition(element.getAttribute("id"), beanDefinition);

        return beanDefinition;
    }
}
