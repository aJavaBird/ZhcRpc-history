package com.cd.will.rpc.netty.handler;

import org.apache.log4j.Logger;

import com.cd.will.rpc.netty.Queue;
import com.cd.will.rpc.netty.model.MessageRequest;
import com.cd.will.rpc.netty.model.MessageResponse;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DefaultClientHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(DefaultClientHandler.class);

    private volatile Channel channel;

    public Channel getChannel() {
        return channel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("client channelActive(client 连接成功)..");
        //        ctx.writeAndFlush(new UserInfo(i, "小明-" + i)); // 必须有flush
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // msg 是一个序列化之后的对象
        logger.info("读入 server 消息:" + msg);
        //        MessageResponse response = MessagePack.unpack(MessagePack.pack(msg), MessageResponse.class);

        Queue.notifyResponse((MessageResponse) msg);
        logger.info("格式化 server 消息为 MessageResponse:" + msg);
    }

    /** 向server端发送消息 */
    public MessageResponse sendRequest(MessageRequest request) {
        logger.info("sending request: " + request.getMessageId());
        channel.writeAndFlush(request);
        Queue.waitResponse(request);
        logger.info("get response: " + request.getMessageId());
        return Queue.getResponse(request.getMessageId());
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
