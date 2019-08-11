package com.cd.will.rpc.netty.execute;

import java.net.InetAddress;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.cd.will.rpc.netty.handler.DefaultServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class MessageServerTask implements Callable<Boolean> {

    private static Logger logger = Logger.getLogger(MessageServerTask.class);

    private String hostIp;

    private Integer port;

    private String interfaceName;

    private Object serviceBean;

    public MessageServerTask(String hostIp, Integer port, String interfaceName, Object serviceBean) {
        this.hostIp = hostIp;
        this.port = port;
        this.interfaceName = interfaceName;
        this.serviceBean = serviceBean;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public Object getServiceBean() {
        return serviceBean;
    }

    @Override
    public Boolean call() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serboot = new ServerBootstrap().group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                        ch.pipeline().addLast(new LengthFieldPrepender(2));
                        //                        ch.pipeline().addLast(new MsgpackDecoder());
                        //                        ch.pipeline().addLast(new MsgpackEncoder());
                        ch.pipeline().addLast(new ObjectEncoder());
                        ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        ch.pipeline().addLast(new DefaultServerHandler(interfaceName, serviceBean));
                    }
                });
        InetAddress addr = InetAddress.getLocalHost();
        String inetHost = addr.getHostAddress();
        logger.info("绑定server: " + inetHost + ":" + port);
        // 绑定端口，同步等待成功
        ChannelFuture future = serboot.bind(inetHost, port);
        // 等待服务端监听端口关闭
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    //                    final ExecutorService executor = Executors.newFixedThreadPool(numberOfEchoThreadsPool);
                    //                    ExecutorCompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(executor);
                    //                    completionService.submit(new ApiEchoResolver(host, echoApiPort));
                    //                    System.out.printf("[author tangjie] Netty RPC Server start success!\nip:%s\nport:%d\nprotocol:%s\nstart-time:%s\njmx-invoke-metrics:%s\n\n", host, port, serializeProtocol, ModuleMetricsHandler.getStartTime(), (RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT ? "open" : "close"));
                    //                    channelFuture.channel().closeFuture().sync().addListener(new ChannelFutureListener() {
                    //                        @Override
                    //                        public void operationComplete(ChannelFuture future) throws Exception {
                    //                            executor.shutdownNow();
                    //                        }
                    //                    });
                }
            }
        });
        // TODO Netty 完善
        // TODO Netty 完善
        // TODO Netty 完善
        return Boolean.TRUE;
    }

}
