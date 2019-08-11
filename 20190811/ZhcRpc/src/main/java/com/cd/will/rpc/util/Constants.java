package com.cd.will.rpc.util;

public class Constants {
    public static final String ZK_PATH_FRAME = "/zhcrpc/";

    public static final String ZK_PATH_PROVIDERS = "/providers";

    public static final String RPC_APPLICATION = "zhcApplication";

    public static final String RPC_VERSION = "0.0.1-SNAPSHOT";

    public static final String RPC_GROUP = "zhcGroup";

    /** 隔多久后 netty 重连（单位秒） */
    public static final Integer SYSTEM_PROPERTY_CLIENT_RECONNECT_DELAY = 2;

    /** 隔多久后 重新 netty-client handler（单位秒） */
    public static final Integer SYSTEM_CLIENT_HANDLER_DELAY = 1;

    /** ZhcRpcReference 在连接netty时的线程个数 */
    public static final Integer MESSAGESEND_INITTASK_THREAD_NUM = 10;

}
