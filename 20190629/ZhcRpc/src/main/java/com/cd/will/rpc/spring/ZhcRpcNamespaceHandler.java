package com.cd.will.rpc.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/** 
 *  自定义标签，需要继承 NamespaceHandlerSupport，并且重写init()方法
 *  */
public class ZhcRpcNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("service", new ZhcServiceBeanDefinitionParser());
        //        registerBeanDefinitionParser("zhcService", new ZhcServiceBeanDefinitionParser());
    }

}
