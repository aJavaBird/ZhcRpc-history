package com.cd.will.rpc.netty;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

import com.cd.will.rpc.netty.handler.DefaultClientHandler;
import com.cd.will.rpc.netty.model.MessageRequest;
import com.cd.will.rpc.netty.model.MessageResponse;
import com.cd.will.rpc.spring.ZhcRpcRegistry;
import com.cd.will.rpc.util.Constants;
import com.cd.will.rpc.util.RpcUtil;
import com.cd.will.rpc.util.ZookeeperUtil;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class NettyMethodProxy implements MethodInterceptor {

    private static Logger logger = Logger.getLogger(NettyMethodProxy.class);

    private ZhcRpcRegistry registryBean;

    private String registry;

    public NettyMethodProxy() {

    }

    public NettyMethodProxy(ZhcRpcRegistry registryBean) {
        this.registryBean = registryBean;
    }

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
        logger.info("class=" + sub.getClass().getInterfaces()[0].getName() + ",method=" + method.getName() + ",params="
                + objects
                + "...start");
        logger.info("interface=" + sub.getClass().getInterfaces()[0].getName());
        // TODO 考虑 timeout 等，ZhcRpcReference.referenceInfoMap
        // TODO 此处调用netty 需要验证
        // TODO 此处调用netty 需要验证
        //        if (registryBean == null) {
        //            registryBean = SpringUtils.getBean(registry);
        //        }
        MessageRequest request = new MessageRequest(RpcUtil.getMessageId(), sub.getClass().getInterfaces()[0].getName(),
                method.getName(),
                RpcUtil.getClassNames(method.getParameterTypes()), objects);
        DefaultClientHandler handler = getClientHandler(sub);
        Object result = null;
        if (handler != null) {
            MessageResponse response = handler.sendRequest(request);
            result = response.getResult();
        }else{
            throw new RuntimeException("DefaultClientHandler 为空:" + sub.getClass().getInterfaces()[0].getName());
        }
        
        //        Object object = methodProxy.invokeSuper(sub, objects); // 代理对象和方法
        logger.info(
                "class=" + sub.getClass().getInterfaces()[0].getName() + ",method=" + method.getName() + ",params="
                        + objects + "...end");
        return result;
    }

    private DefaultClientHandler getClientHandler(Object sub) {
        DefaultClientHandler handler = null;
        try {
            ZooKeeper zk = ZookeeperUtil.getZooKeeperCli(registryBean.getAddress(), registryBean.getTimeout());
            String parentPath = new StringBuilder(Constants.ZK_PATH_FRAME)
                    .append(sub.getClass().getInterfaces()[0].getName())
                    .append(Constants.ZK_PATH_PROVIDERS).toString();
            List<String> nodes = zk.getChildren(parentPath, false);
            if (nodes != null && !nodes.isEmpty()) {
                String node = RpcUtil.getRandomElement(nodes);
                InetSocketAddress inetSocketAddr = RpcUtil.getInetSocketAddress(node);
                handler = MessageSendExecutor.getInstance().getClientHandler(registryBean.getAddress(),
                        inetSocketAddr.getHostString() + ":" + inetSocketAddr.getPort());
            } else {
                logger.error("zk路径子节点为空: " + parentPath);
                this.wait(1000 * Constants.SYSTEM_CLIENT_HANDLER_DELAY);
                logger.info("尝试重新获取ClientHandler: " + parentPath);
                return getClientHandler(sub);
            }
        } catch (Exception e) {
            logger.error("getClientHandler error:", e);
            e.printStackTrace();
        }

        return handler;
    }

}
