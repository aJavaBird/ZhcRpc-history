package com.cd.will.rpc.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

import com.cd.will.rpc.netty.NettyMethodProxy;

import net.sf.cglib.proxy.Enhancer;

/*
 * InitializingBean 实现 afterPropertiesSet()，表示在资源加载完以后，初始化bean之前执行的方法
 * DisposableBean 实现 destroy()，在一个bean被销毁的时候，spring容器会帮你自动执行这个方法
 * FactoryBean 实现 getObject()、getObjectType()等方法，在spring 注册bean时，注册的不是 ZhcRpcReference，而是自己定义的类型，可以用于实现代理
 * */
public class ZhcRpcReference implements FactoryBean/*, InitializingBean, DisposableBean*/ {

    private String interfaceName;

    private String registry;

    private Boolean check;

    private ApplicationContext applicationContext;

    // 注册中心
    private ZhcRpcRegistry registryBean;

    private Object referenceBean;

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

    //    @Override
    //    public void destroy() throws Exception {
    //        // TODO Auto-generated method stub
    //
    //    }
    //
    //    @Override
    //    public void afterPropertiesSet() throws Exception {
    //        // TODO Auto-generated method stub
    //
    //    }

    @Override
    public Object getObject() throws Exception {
        // 通过CGLIB动态代理获取代理对象的过程
        Enhancer enhancer = new Enhancer();
        Class interfaceClass = Class.forName(interfaceName);
        // 设置enhancer对象的父类
        enhancer.setSuperclass(interfaceClass);
        // 设置enhancer的回调对象
        enhancer.setCallback(new NettyMethodProxy());
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

}
