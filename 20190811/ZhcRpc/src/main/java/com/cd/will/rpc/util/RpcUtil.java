package com.cd.will.rpc.util;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RpcUtil {

    /** 获取 小于max的 随机数 */
    public static int getRandomInt(int max) {
        return ThreadLocalRandom.current().nextInt(max);
    }

    /** 随机从列表中获取一个元素 */
    public static <E> E getRandomElement(Collection<E> set) {
        int rn = getRandomInt(set.size());
        int i = 0;
        for (E e : set) {
            if (i == rn) {
                return e;
            }
            i++;
        }
        return null;
    }

    public static InetSocketAddress getInetSocketAddress(String nettyNodeStr) {
        String nodeInfo[] = nettyNodeStr.split("%3a");
        String host = nodeInfo[0];
        int port = Integer.valueOf(nodeInfo[1]);
        return new InetSocketAddress(host, port);
    }

    /** 获取messageId */
    public static String getMessageId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /** Class 数组转换为 ClassName数组 */
    public static String[] getClassNames(Class<?>[] clazzs) {
        String[] classNames = new String[clazzs.length];
        for (int i = 0; i < clazzs.length; i++) {
            classNames[i] = clazzs[i].getName();
        }
        return classNames;
    }

    /** ClassName数组 转换为  Class数组 */
    public static Class<?>[] getClasses(String[] classNames) throws ClassNotFoundException {
        Class<?>[] clazzs = new Class<?>[classNames.length];
        for (int i = 0; i < classNames.length; i++) {
            clazzs[i] = Class.forName(classNames[i]);
        }
        return clazzs;
    }

}
