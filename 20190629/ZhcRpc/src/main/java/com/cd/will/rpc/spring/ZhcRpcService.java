package com.cd.will.rpc.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.cd.will.rpc.event.ServerStartEvent;

public class ZhcRpcService implements ApplicationContextAware, ApplicationListener {

    private String interfaceName;

    private String ref;

    private ApplicationContext applicationContext;

    private Object serviceBean;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        serviceBean = applicationContext.getBean(ref);
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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Object getServiceBean() {
        return serviceBean;
    }

    public <T> T getServiceBean(Class<T> clazz) {
        //        if(serviceBean instanceof T.class){
        //            
        //        }
        return (T) serviceBean;
    }

    public void setServiceBean(Object serviceBean) {
        this.serviceBean = serviceBean;
    }

}
