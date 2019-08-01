package com.cd.will.rpc.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

public class ZhcRpcRegistry implements InitializingBean, DisposableBean {

    private String address;

    private String protocol;

    private int port;

    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void destroy() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO 此处需要实现 netty 初始化
        // TODO 此处需要实现 netty 初始化
        // TODO 此处需要实现 netty 初始化
    }

}
