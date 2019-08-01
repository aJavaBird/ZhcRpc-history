package com.cd.will.rpc.netty;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class NettyMethodProxy implements MethodInterceptor {

    private static Logger logger = Logger.getLogger(NettyMethodProxy.class);

    public Object getInstance(Class clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        // 回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object sub, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if ("toString".equals(method.getName())) {
            return sub.getClass().getName() + "@zhcRpc";
        }
        logger.info("class=" + sub.getClass().getName() + ",method=" + method.getName() + ",params=" + objects
                + "...start");
        // TODO 此处需要调用netty
        // TODO 此处需要调用netty
        logger.info("此处需要调用netty");
        //        Object object = methodProxy.invokeSuper(sub, objects); // 代理对象和方法
        logger.info(
                "class=" + sub.getClass().getName() + ",method=" + method.getName() + ",params=" + objects + "...end");
        return null;
    }

}
