package com.cd.will.rpc.netty;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cd.will.rpc.netty.handler.DefaultClientHandler;
import com.cd.will.rpc.netty.model.MessageRequest;
import com.cd.will.rpc.netty.model.MessageResponse;

/** 消息临时队列 */
public class Queue {

    private static Logger logger = Logger.getLogger(DefaultClientHandler.class);

    /** 所有正在执行的request */
    private final static Map<String, MessageRequest> requestMap = new HashMap<>();

    /** 所有返回了的response */
    private final static Map<String, MessageResponse> responseMap = new HashMap<>();

    /** 等待消息返回 */
    public static void waitResponse(MessageRequest request) {
        requestMap.put(request.getMessageId(), request);
        try {
            synchronized (request) {
                request.wait();
            }
        } catch (InterruptedException e) {
            logger.error("waitResponse error: ", e);
            e.printStackTrace();
        }
    }

    /** 消息返回后，唤醒消息等待 */
    public static void notifyResponse(MessageResponse response) {
        responseMap.put(response.getMessageId(), response);
        MessageRequest request = requestMap.get(response.getMessageId());
        synchronized (request) {
            request.notify();
        }
        requestMap.remove(response.getMessageId());
    }

    /** 获得返回结果 */
    public static MessageResponse getResponse(String messageId) {
        MessageResponse response = responseMap.get(messageId);
        responseMap.remove(messageId);
        return response;
    }

}
