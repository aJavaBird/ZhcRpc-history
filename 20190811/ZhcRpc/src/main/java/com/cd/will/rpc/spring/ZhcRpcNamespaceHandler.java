package com.cd.will.rpc.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/** 
 *  自定义标签，需要继承 NamespaceHandlerSupport，并且重写init()方法
 *  */
public class ZhcRpcNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        // 虽然 service 中有引用 registry，但是并不需要有把registry 写在 service前面
        registerBeanDefinitionParser("service", new ZhcServiceBeanDefinitionParser());
        registerBeanDefinitionParser("registry", new ZhcRegistryBeanDefinitionParser());
        //        registerBeanDefinitionParser("zhcService", new ZhcServiceBeanDefinitionParser());
        registerBeanDefinitionParser("reference", new ZhcReferenceBeanDefinitionParser());
    }

}
