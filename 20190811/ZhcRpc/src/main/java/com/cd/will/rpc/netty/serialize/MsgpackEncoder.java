package com.cd.will.rpc.netty.serialize;

import org.apache.log4j.Logger;
import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 本例子参考《Netty权威指南（第2版）》第7章 
 * 负责将Object类型的POJO对象编码为byte数组，然后写入到ByteBuf中
 * */
public class MsgpackEncoder extends MessageToByteEncoder<Object> {

    private static Logger logger = Logger.getLogger(MsgpackEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {
        MessagePack msgpack = new MessagePack();
        byte[] raw;
        logger.info("msg-class: " + msg.getClass().getName());
        try {
            raw = msgpack.write(msg);
            out.writeBytes(raw);
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
