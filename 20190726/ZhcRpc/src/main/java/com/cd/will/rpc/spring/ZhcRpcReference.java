package com.cd.will.rpc.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.cd.will.rpc.event.ServerStartEvent;

public class ZhcRpcReference implements ApplicationContextAware, ApplicationListener {

    private String interfaceName;

    private String registry;

    private Boolean check;

    private ApplicationContext applicationContext;

    // 注册中心
    private ZhcRpcRegistry registryBean;

    private Object referenceBean;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        registryBean = (ZhcRpcRegistry) applicationContext.getBean(registry);
        //TODO
        //TODO 需要处理代理
        //TODO
        //        referenceBean = applicationContext.getBean(ref);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.applicationContext.publishEvent(new ServerStartEvent(new Object()));// ServerStartEvent 此处不是必须的，但是后续可能会用到
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public String getInterface() {
        return getInterfaceName();
    }

    public void setInterface(String interfaceName) {
        setInterfaceName(interfaceName);
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public ZhcRpcRegistry getRegistryBean() {
        return registryBean;
    }

    public void setRegistryBean(ZhcRpcRegistry registryBean) {
        this.registryBean = registryBean;
    }

    public Object getReferenceBean() {
        return referenceBean;
    }

    public void setReferenceBean(Object referenceBean) {
        this.referenceBean = referenceBean;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

}
