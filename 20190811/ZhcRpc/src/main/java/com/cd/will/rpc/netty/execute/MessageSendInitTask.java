package com.cd.will.rpc.netty.execute;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

import com.cd.will.rpc.netty.MessageSendExecutor;
import com.cd.will.rpc.netty.handler.DefaultClientHandler;
import com.cd.will.rpc.spring.ZhcRpcRegistry;
import com.cd.will.rpc.util.Constants;
import com.cd.will.rpc.util.RpcUtil;
import com.cd.will.rpc.util.ZookeeperUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class MessageSendInitTask implements Callable<Boolean> {

    private static Logger logger = Logger.getLogger(MessageSendInitTask.class);

    String interfaceName;

    ZhcRpcRegistry registryBean;

    EventLoopGroup eventLoopGroup;

    public MessageSendInitTask(ZhcRpcRegistry registryBean, String interfaceName) {
        this.registryBean = registryBean;
        this.interfaceName = interfaceName;
        eventLoopGroup = new NioEventLoopGroup();
        // TODO 此处需要加入  序列化类型参数: protocol
        // TODO 此处需要加入  序列化类型参数: protocol
        // TODO 此处需要加入  序列化类型参数: protocol
    }

    public Boolean call() {
        List<String> nodes = new ArrayList<>();
        try {
            ZooKeeper zk = ZookeeperUtil.getZooKeeperCli(registryBean.getAddress(), registryBean.getTimeout());
            String parentPath = new StringBuilder(Constants.ZK_PATH_FRAME).append(interfaceName)
                    .append(Constants.ZK_PATH_PROVIDERS).toString();
            nodes = zk.getChildren(parentPath, false);
            if (nodes.isEmpty()) {
                logger.error("zookeeper 未获取到节点: addr=" + registryBean.getAddress() + "," + interfaceName);
                this.wait(Constants.SYSTEM_PROPERTY_CLIENT_RECONNECT_DELAY * 100);
                return call();
            }
        } catch (Exception e) {
            logger.error("zookeeper 未获取到节点: addr=" + registryBean.getAddress() + "," + interfaceName);
            e.printStackTrace();
            try {
                this.wait(Constants.SYSTEM_PROPERTY_CLIENT_RECONNECT_DELAY * 100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return call();
        }

        for (String node : nodes) {
            logger.info(interfaceName + " find server " + node);
            String nodeInfo[] = node.split("%3a");
            String host = nodeInfo[0];
            int port = Integer.valueOf(nodeInfo[1]);
            InetSocketAddress inetSocketAddr = RpcUtil.getInetSocketAddress(node);
            nettyConnect(inetSocketAddr);
        }
        return Boolean.TRUE;
    }

    /** 链接netty服务器 */
    public Boolean nettyConnect(InetSocketAddress inetSocketAddr) {
        String host = inetSocketAddr.getHostString();
        int port = inetSocketAddr.getPort();
        if (!MessageSendExecutor.getInstance().isClientHandlerNull(registryBean.getAddress(), host + ":" + port)) {
            // netty 服务器已连接，则不再次进行连接
            return Boolean.TRUE;
        }
        Bootstrap boot = new Bootstrap().group(eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                        ch.pipeline().addLast(new LengthFieldPrepender(2));
                        //                        ch.pipeline().addLast(new MsgpackDecoder());
                        //                        ch.pipeline().addLast(new MsgpackEncoder());
                        ch.pipeline().addLast(new ObjectEncoder());
                        ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        ch.pipeline().addLast(new DefaultClientHandler());
                    }
                }).remoteAddress(inetSocketAddr);
        logger.info("准备连接到netty服务器:" + host + ":" + port);
        ChannelFuture future = boot.connect();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    DefaultClientHandler handler = channelFuture.channel().pipeline().get(DefaultClientHandler.class);
                    MessageSendExecutor.getInstance().putHanderToMap(registryBean.getAddress(),
                            host + ":" + port, handler);
                } else {
                    EventLoop loop = (EventLoop) eventLoopGroup.schedule(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("zhcrpc server is down,start to reconnecting to: "
                                    + host + ':' + port);
                            nettyConnect(inetSocketAddr);
                        }
                    }, Constants.SYSTEM_PROPERTY_CLIENT_RECONNECT_DELAY, TimeUnit.SECONDS);
                }
            }
        });
        return Boolean.TRUE;
    }
}
