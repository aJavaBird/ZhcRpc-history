package com.cd.will.rpc.spring;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cd.will.rpc.netty.MessageSendExecutor;
import com.cd.will.rpc.netty.NettyMethodProxy;
import com.cd.will.rpc.netty.execute.MessageSendInitTask;

import net.sf.cglib.proxy.Enhancer;

/*
 * InitializingBean 实现 afterPropertiesSet()，表示在资源加载完以后，初始化bean之前执行的方法
 * DisposableBean 实现 destroy()，在一个bean被销毁的时候，spring容器会帮你自动执行这个方法
 * FactoryBean 实现 getObject()、getObjectType()等方法，在spring 注册bean时，注册的不是 ZhcRpcReference，而是自己定义的类型，可以用于实现代理
 * */
public class ZhcRpcReference implements ApplicationContextAware, FactoryBean, InitializingBean/*, DisposableBean*/ {

    private String interfaceName;

    private String registry;

    private Boolean check;

    private int timeout;

    private ApplicationContext applicationContext;

    // 注册中心
    private ZhcRpcRegistry registryBean;

    private Boolean isWaitRegistryBean = false;

    private Object referenceBean;

    public final static Map<String, Map<String, Object>> referenceInfoMap = new HashMap<>();

    private static Logger logger = Logger.getLogger(ZhcRpcReference.class);

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

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    //    @Override
    //    public void destroy() throws Exception {
    //        // TODO Auto-generated method stub
    //
    //    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        registryBean = (ZhcRpcRegistry) applicationContext.getBean(registry);
        logger.info("registryBean 注入...");
        if (isWaitRegistryBean) {
            isWaitRegistryBean = false;
            registry.notifyAll();
        }
    }

    @Override
    public Object getObject() throws Exception {
        // 通过CGLIB动态代理获取代理对象的过程
        Enhancer enhancer = new Enhancer();
        Class interfaceClass = Class.forName(interfaceName);
        // 设置enhancer对象的父类
        enhancer.setSuperclass(interfaceClass);
        if (registryBean == null) {
            logger.info("registry wait...");
            isWaitRegistryBean = true;
            registry.wait();
        }
        // 设置enhancer的回调对象
        enhancer.setCallback(new NettyMethodProxy(registryBean));
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("timeout", timeout);
        referenceInfoMap.put(interfaceName, infoMap);
        // 创建代理对象
        /*Object proxy = interfaceClass.cast(enhancer.create());
        return proxy;*/
        return enhancer.create();

    }

    @Override
    public Class getObjectType() {
        try {
            return this.getClass().getClassLoader().loadClass(interfaceName);
        } catch (ClassNotFoundException e) {
            System.err.println("spring analyze fail!");
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (registryBean == null) {
            logger.info("registry wait...");
            isWaitRegistryBean = true;
            registry.wait();
        }
        MessageSendExecutor.getInstance().getExecutorService()
                .submit(new MessageSendInitTask(registryBean, interfaceName));

    }

}
