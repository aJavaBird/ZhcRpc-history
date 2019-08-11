package com.cd.will.rpc.netty.serialize;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 本例子参考《Netty权威指南（第2版）》第7章 
 * 首先从数据报msg 中获取要解码的 byte数组，然后调用 MessagePack 的read 方法将其反序列化为对象，
 * 再把对象加入到解码队列 out 中，这样完成了MessagePack的解码过程
 *  */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        final byte[] array = new byte[msg.readableBytes()];
        msg.getBytes(msg.readerIndex(), array, 0, msg.readableBytes());
        MessagePack msgpack = new MessagePack();
        try {
            out.add(msgpack.read(array));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
