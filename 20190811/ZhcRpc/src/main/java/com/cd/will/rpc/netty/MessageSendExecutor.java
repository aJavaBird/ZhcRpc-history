package com.cd.will.rpc.netty;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.cd.will.rpc.netty.handler.DefaultClientHandler;
import com.cd.will.rpc.netty.model.MessageClientWait;
import com.cd.will.rpc.util.Constants;

public class MessageSendExecutor {

    private static Logger logger = Logger.getLogger(MessageSendExecutor.class);

    private Map<String, DefaultClientHandler> handlerMap = new HashMap<>();

    private Map<String, MessageClientWait> handlerWaitMap = new HashMap<>();

    private ExecutorService executor = Executors.newFixedThreadPool(Constants.MESSAGESEND_INITTASK_THREAD_NUM);

    private static class MessageSendExecutorHolder {
        private static final MessageSendExecutor INSTANCE = new MessageSendExecutor();
    }

    public static MessageSendExecutor getInstance() {
        return MessageSendExecutorHolder.INSTANCE;
    }

    public ExecutorService getExecutorService() {
        return executor;
    }

    /** 将DefaultClientHandler 放入map中，后续rpc调用是，从此处获取channel */
    public void putHanderToMap(String zkAddr, String nettyAddr, DefaultClientHandler handler) {
        if (zkAddr != null && !"".equals(zkAddr) && nettyAddr != null && !"".equals(nettyAddr)
                && handler != null) {
            handlerMap.put(zkAddr + "/" + nettyAddr, handler);
            //            if (handlerWaitMap.get(zkAddr + "/" + nettyAddr) != null) {
            //                handlerWaitMap.get(zkAddr + "/" + nettyAddr).notify();
            //                handlerWaitMap.get(zkAddr + "/" + nettyAddr).setIsWait(false);
            //            }
        }
    }

    /** 获取DefaultClientHandler，随机获取 */
    public DefaultClientHandler getClientHandler(String zkAddr, String nettyAddr) {
        String address = zkAddr + "/" + nettyAddr;
        DefaultClientHandler handler = handlerMap.get(address);
        if (handler == null) {
            logger.error("client-handler未初始化: " + address);
            if (handlerWaitMap.get(address) != null) {
                try {
                    handlerWaitMap.get(address).waitAMinute(1000 * Constants.SYSTEM_CLIENT_HANDLER_DELAY);
                } catch (InterruptedException e) {
                    logger.error("");
                    e.printStackTrace();
                }
            } else {
                MessageClientWait clientWait = new MessageClientWait(address, true);
                handlerWaitMap.put(address, clientWait);
                try {
                    clientWait.waitAMinute(1000 * Constants.SYSTEM_CLIENT_HANDLER_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.error("尝试重新获取client-handler: " + address);
            return getClientHandler(zkAddr, nettyAddr);
        }
        return handler;
    }

    /** 查看 ClientHandler 是否为空 */
    public Boolean isClientHandlerNull(String zkAddr, String nettyAddr) {
        DefaultClientHandler handler = handlerMap.get(zkAddr + "/" + nettyAddr);
        if (handler == null) {
            return true;
        }
        return false;
    }
}
