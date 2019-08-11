package com.cd.will.rpc.spring;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.cd.will.rpc.event.ServerStartEvent;
import com.cd.will.rpc.netty.MessageSendExecutor;
import com.cd.will.rpc.netty.execute.MessageServerTask;
import com.cd.will.rpc.util.Constants;
import com.cd.will.rpc.util.ZookeeperUtil;

public class ZhcRpcService implements ApplicationContextAware, ApplicationListener {

    private String interfaceName;

    private String ref;

    private int timeout;

    private ApplicationContext applicationContext;

    private Object serviceBean;

    // 注册中心
    private ZhcRpcRegistry registryBean;

    private String registry;

    private static Logger logger = Logger.getLogger(ZhcRpcService.class);

    private static String hostIp = "127.0.0.1";

    static {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostIp = addr.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("本机IP获取出差: {}", e);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        registryBean = (ZhcRpcRegistry) applicationContext.getBean(registry);
        serviceBean = applicationContext.getBean(ref);
        try {
            ZooKeeper zk = ZookeeperUtil.getZooKeeperCli(registryBean.getAddress(), registryBean.getTimeout());
            ZookeeperUtil.createNodeIfNotExists(Constants.ZK_PATH_FRAME + interfaceName + Constants.ZK_PATH_PROVIDERS,
                    zk);
            String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
            StringBuilder methondBuilder = new StringBuilder();
            Method[] methods = Class.forName(interfaceName).getMethods();
            if (methods.length > 0) {
                methondBuilder.append(methods[0].getName());
                for (int i = 1; i < methods.length; i++) {
                    methondBuilder.append("%2c").append(methods[i].getName());
                }
            }

            //            zk.create(
            //                    new StringBuilder(Constants.ZK_PATH_FRAME).append(interfaceName).append(Constants.ZK_PATH_PROVIDERS)
            //                            .append("/").append(hostIp).append("%3a").append("5167").append("%2f")
            //                            .append(interfaceName).append("%3fapplication%3d")
            //                            .append(Constants.RPC_APPLICATION).append("%26zhcrpc%3d").append(Constants.RPC_VERSION)
            //                            .append("%26group%3d").append(Constants.RPC_GROUP).append("%26methods%3d")
            //                            .append(methondBuilder)
            //                            .append("%26pid%3d").append(pid).append("%26timeout%3d").append(timeout)
            //                            .toString(),
            //                    null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            // 不考虑搞得像dubbo那么复杂
            Boolean ifFirstCreate = ZookeeperUtil.createNodeIfNotExists(
                    new StringBuilder(Constants.ZK_PATH_FRAME).append(interfaceName).append(Constants.ZK_PATH_PROVIDERS)
                            .append("/").append(hostIp).append("%3a").append(registryBean.getPort()).toString(),
                    CreateMode.EPHEMERAL, zk);
            if (ifFirstCreate) { // 此节点首次创建，则启动服务
                MessageSendExecutor.getInstance().getExecutorService()
                        .submit(new MessageServerTask(hostIp, registryBean.getPort(), interfaceName, serviceBean));
            }
        } catch (IOException e) {
            logger.error("连接zk出错: {}", e);
        } catch (InterruptedException e) {
            logger.error("连接zk出错: {}", e);
        } catch (KeeperException e) {
            logger.error("写入zk出错: {}", e);
        } catch (SecurityException e) {
            logger.error("获取接口方法出错: " + interfaceName + ",{}", e);
        } catch (ClassNotFoundException e) {
            logger.error("获取接口方法出错: " + interfaceName + ",{}", e);
        }
        // TODO 注册netty事件
        // TODO 注册netty事件
        // TODO 注册netty事件
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

    public ZhcRpcRegistry getRegistryBean() {
        return registryBean;
    }

    public void setRegistryBean(ZhcRpcRegistry registryBean) {
        this.registryBean = registryBean;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
