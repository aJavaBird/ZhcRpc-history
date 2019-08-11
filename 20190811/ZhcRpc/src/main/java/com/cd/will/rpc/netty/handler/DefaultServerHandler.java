package com.cd.will.rpc.netty.handler;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.cd.will.rpc.netty.model.MessageRequest;
import com.cd.will.rpc.netty.model.MessageResponse;
import com.cd.will.rpc.util.RpcUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DefaultServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(DefaultServerHandler.class);

    private String interfaceName;

    private Object serviceBean;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("server channelActive(有client连接上了)..");
        //            ctx.writeAndFlush(new UserInfo(1024, "小明-" + 1024)); // 必须有flush
    }

    public DefaultServerHandler(String interfaceName, Object serviceBean) {
        this.interfaceName = interfaceName;
        this.serviceBean = serviceBean;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("读入client消息:" + msg);
        //        MessageRequest request = MessagePack.unpack(MessagePack.pack(msg), MessageRequest.class);
        MessageRequest request = (MessageRequest) msg;
        logger.info("此处需要向 client 写 MessageResponse（可考虑写一个工具类） : ctx.writeAndFlush(msg);");
        // TODO 此处需要考虑调用 service 的异常处理
        // TODO 此处需要考虑调用 service 的异常处理
        // TODO 此处需要考虑调用 service 的异常处理
        Class<?> serviceClazz = Class.forName(interfaceName);
        Method method = serviceClazz.getDeclaredMethod(request.getMethodName(),
                RpcUtil.getClasses(request.getClassNames()));
        Object result = method.invoke(serviceBean, request.getParametersVal());

        logger.info(interfaceName + "." + request.getMethodName() + "()方法执行结果: " + result);

        ctx.writeAndFlush(new MessageResponse(request.getMessageId(), null, result, false));
        //        ctx.writeAndFlush(msg);
        //        System.out.println("向client发送消息:" + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        super.channelReadComplete(ctx);
    }

}
