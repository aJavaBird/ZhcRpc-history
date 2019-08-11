package com.cd.will.test.util;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

import com.cd.will.rpc.util.ZookeeperUtil;

/**
 * Zookeeper Test
 */
public class ZookeeperTest2 {

    /** zookeeper地址 */
    static final String CONNECT_ADDR = "127.0.0.1:2181";

    /** session超时时间 */
    static final int SESSION_OUTTIME = 2000;// ms

    /** 信号量，阻塞程序执行，用于等待zookeeper连接成功，发送成功信号 */
    static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

    private static Logger logger = Logger.getLogger(ZookeeperTest2.class);

    public static void main(String[] args) throws Exception {

        ZooKeeper zk = ZookeeperUtil.getZooKeeperCli(CONNECT_ADDR, SESSION_OUTTIME);

        logger.info("test start...");

        // 删除节点
        //        ZookeeperUtil.deleteNode("/zhcrpc", zk);
        ZookeeperUtil.createNodeIfNotExists("/zhcrpc/com.cd.will.test.service.IShowInfoService/providers", zk);
        ZookeeperUtil.createNodeIfNotExists("/zhcrpc/com.cd.will.test.service.IShowInfoService/consumers", zk);

        logger.info("test end...");

        zk.close();

    }


}

